module StartFileSystem
  ( startFileSystem,
  )
where

import Data.Maybe()
import System.Directory
  (doesDirectoryExist, doesFileExist, getFileSize, getModificationTime, getPermissions, 
  listDirectory, createDirectory, setModificationTime, removeFile)
import Control.Monad.IO.Class (liftIO)
import Control.Monad (when, unless)
import Control.Monad.Trans.Reader (ReaderT, runReaderT, ask)
import Data.IORef (IORef, readIORef, writeIORef, newIORef)
import Control.Exception (catch, try)
import System.FilePath.Windows (takeExtension, takeFileName, pathSeparator)
import Parser (opts)
import Commands (runCommand)
import Options.Applicative (prefs, showHelpOnEmpty, execParserPure, getParseResult)
import Types

-- | Launch work with FileSystem with given FilePath
startFileSystem
  :: FilePath -- ^ Given FilePath
  -> IO () -- ^ Work in IO
startFileSystem startPath = do
  fs <- readFileSystem startPath
  case fs of
    Nothing  -> return ()
    Just fs' -> do
      ref <- newIORef (startPath, fs')
      runReaderT readCommands ref

-- | Returns read FileSystem if everything is OK ot Nothing
readFileSystem
  :: FilePath -- ^ Given FilePath
  -> IO (Maybe FileSystem) -- ^ FileSystem if everything is OK
readFileSystem rootPath = do
  eitherRoot <- try (parseFileSystem rootPath) :: IO (Either IOError FileSystem)
  case eitherRoot of
    Left error' -> do
      print error'
      return Nothing
    Right root -> do
      return (Just root)

-- | Returns read FileSystem if everything is OK or Error
parseFileSystem
  :: FilePath -- ^ Given FilePath
  -> IO FileSystem -- ^ FileSystem if everything is OK
parseFileSystem pathFS = do
  isDirectoryExist <- doesDirectoryExist pathFS
  isFileExist      <- doesFileExist pathFS
  perms            <- getPermissions pathFS
  if isDirectoryExist
    then do
      listOfFilePaths <- listDirectory pathFS
      dirFiles        <- scanDir listOfFilePaths pathFS
      return $ Directory (getDirSize dirFiles) pathFS (length dirFiles) perms (getName pathFS) dirFiles
    else
      if isFileExist
        then do
          fileTime        <- getModificationTime pathFS
          fileSize        <- getFileSize pathFS
          fileContent     <- readFile pathFS
          return $ File pathFS perms (takeExtension pathFS) fileTime fileSize (takeFileName pathFS) fileContent False
        else error "this path doesn't exists\n"

-- | Returns read FileSystems of directory
scanDir
  :: [FilePath] -- ^ Content of directory
  -> FilePath -- ^ FilePath of given directory
  -> IO [FileSystem] -- ^ FileSystems of directory
scanDir [] _        = return []
scanDir (a : as) fp = do
  eitherCurFileSystem <- try (parseFileSystem (fp ++ [pathSeparator] ++ a)) :: IO (Either IOError FileSystem)
  tailOfCurFileSystem <- scanDir as fp
  case eitherCurFileSystem of
    Left error' -> do
      print error'
      return tailOfCurFileSystem
    Right curFileSystem -> do
      return (curFileSystem : tailOfCurFileSystem)

-- | Read Users commands
readCommands
  :: ReaderT (IORef (FilePath, FileSystem)) IO () -- ^ Current FilePath and FileSystem
readCommands = do
  context <- ask
  (curDir, fs) <- liftIO $ readIORef context
  liftIO $ print (curDir ++ ">")
  input <- liftIO getLine
  case getParseResult $ execParserPure (prefs showHelpOnEmpty) opts (words input) of
    Just result -> do
      if isExit result
        then liftIO $ catch (saveFileSystem fs) handler
        else do
          ans <- liftIO $ runCommand result curDir fs
          liftIO $ writeIORef context ans
          readCommands
    Nothing -> do
      liftIO $ putStrLn "Unknown command. To see how to use FileSystem print : \"help\"\n"
      readCommands

-- | Handles a save error FileSystem
handler
  :: IOError -- ^ Save error
  -> IO() -- ^ Help message
handler _ = putStrLn "Someone change your real FileSystem and your cahnges won't save\n"

-- | Save FileSystem
saveFileSystem
  :: FileSystem -- ^ Given FileSystem
  -> IO() -- ^ Result of save
saveFileSystem fs = saveFiles (files fs)

-- | Save files of FileSystem
saveFiles
  :: [FileSystem] -- ^ Files of FileSystem
  -> IO() -- ^ Result of save
saveFiles [] = return ()
saveFiles (a:as)
  | isDirectory a = do
    workWithDir a
    saveFiles (as ++ files a)
  | isFile a = do
    workWithFile a
    saveFiles as
  | otherwise = return ()

-- | Handles file for save
workWithFile
  :: FileSystem -- ^ Given file
  -> IO() -- ^ Result of handler
workWithFile file = do
  when (wasChanged file) $ do
    removeFile (path file)
    writeFile (path file) (content file)
    setModificationTime (path file) (time file)

-- | Handles directory for save
workWithDir
  :: FileSystem -- ^ Given directory
  -> IO() -- ^ Result of handler
workWithDir dir = do
  isDir <- doesDirectoryExist (path dir)
  unless isDir $ createDirectory (path dir)