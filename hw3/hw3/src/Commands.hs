module Commands
  ( runCommand
  , showLS
  , tryFind
  )
where

import Control.Applicative ((<|>))
import Data.List (intercalate)
import Data.Maybe ()
import System.Directory (emptyPermissions, searchable, executable, writable, readable, Permissions)
import System.FilePath.Windows (pathSeparator, splitDirectories, takeFileName, takeExtension)
import Data.Time.Clock (UTCTime, getCurrentTime)
import Types

-- | Returns FilePath and FileSystem after execute command
runCommand 
  :: Command -- ^ Given command
  -> FilePath -- ^ Given FilePath
  -> FileSystem -- ^ Given FileSystem
  -> IO (FilePath, FileSystem) -- ^ FilePath and FileSystem after execute command
runCommand HELP filePath fileSystem = do
  putStr "help - show guide\n"          
  putStr "cd <folder> - go to folder\n"
  putStr "cd .. - go to root of file system folder\n"
  putStr "dir - show content of current directory\n"
  putStr "ls <folder> - show content of folder\n"
  putStr "mkdir <folder> - create folder\n"
  putStr "touch <file> - create file\n"
  putStr "rmdir <folder> - remove folder\n"
  putStr "rm <file> - remove file\n"
  putStr "info <file|folder> - show information about file or folder\n"
  putStr "cat <file> - show content of file\n"
  putStr "find <name of file> - show absolute path to file\n"
  putStr "write <file> <words> - write to file words that are separated by a space in quotation marks\n"
  putStr "exit - save your system on device\n"
  return (filePath, fileSystem)
runCommand (CD fp) filePath fileSystem = do
  if fp == ".."
    then return (path fileSystem, fileSystem)
    else execCommand id fp filePath fileSystem cd'
      where
        cd' fs = do
          if isDirectory fs
            then return (path fs, fileSystem)
            else do
              putStr "you should use only directories\n"
              return (filePath, fileSystem)
runCommand (FIND fileName) filePath fileSystem = do
  execCommand id "" filePath fileSystem find'
    where
      find' fs = do
        case tryFind [fs] fileName of
          Nothing        -> putStr "this file doesn't exists\n"
          Just foundFile -> putStr (path foundFile ++ "\n")
        return (filePath, fileSystem)
runCommand (CAT fp) filePath fileSystem = do
  execCommand id fp filePath fileSystem cat'
    where
      cat' fs = do
        if isFile fs
          then putStr (content fs ++ "\n")
          else putStr "you should use only files\n"
        return (filePath, fileSystem)   
runCommand (WRITE fp info) filePath fileSystem = do
  execCommand init fp filePath fileSystem write'
    where
      write' fs = do
        if isDirectory fs
          then do
            if contains fp fs
              then do
                fileTime <- getCurrentTime
                return (filePath, update fs fileSystem (getFullPath fs fp, fileTime, info) updFile countSize countSize)
              else do
                putStr "this file doesn't exist, you can create it with \"touch <file>\"\n"
                return (filePath, fileSystem)
          else do
            putStr "you should write only in files\n"
            return (filePath, fileSystem)
runCommand (LS fp) filePath fileSystem = do
  execCommand id fp filePath fileSystem ls'
    where
      ls' fs = do
        if isDirectory fs
          then putStr $ showLS fs ++ "\n"
          else putStr "you should use only directories\n"
        return (filePath, fileSystem)
runCommand DIR filePath fileSystem = runCommand (LS "") filePath fileSystem
runCommand (INFO fp) filePath fileSystem = do
  execCommand id fp filePath fileSystem info'
    where
      info' fs = do
        if isFile fs
          then printFile fs
          else printDirectory fs
        return (filePath, fileSystem)
runCommand (MKDIR fp) filePath fileSystem = do
  execCommand init fp filePath fileSystem mkdir'
    where
      mkdir' fs = do
        if isDirectory fs
          then do
            if not (contains fp fs)
              then return (filePath, update fs fileSystem (newDirByPath (getFullPath fs fp))  addFile countPlus id)
              else do
                putStr "folder have content with the same name\n"
                return (filePath, fileSystem)
          else do
            putStr "you should create folder in folder\n"
            return (filePath, fileSystem)
runCommand (TOUCH fp) filePath fileSystem = do
  execCommand init fp filePath fileSystem touch'
    where
      touch' fs = do
        if isDirectory fs
          then do
            if not (contains fp fs)
              then do
                currentTime <- getCurrentTime
                return (filePath, update fs fileSystem (newFileByPath (getFullPath fs fp) currentTime) addFile countPlus id)
              else do
                putStr "folder have content with the same name\n"
                return (filePath, fileSystem)
          else do
            putStr "you should create file in folder\n"
            return (filePath, fileSystem)
runCommand (RMDIR fp) filePath fileSystem = runCommand (RM fp) filePath fileSystem        
runCommand (RM fp) filePath fileSystem = do
  execCommand init fp filePath fileSystem rm'
    where
      rm' fs = do
        if isDirectory fs
          then do
            if contains fp fs
              then return (filePath, update fs fileSystem (getFullPath fs fp) rmFile countMinus id)
              else do
                putStr "this file doesn't exists\n"
                return (filePath, fileSystem)
          else do
            putStr "you should remove file/folder in folder\n"
            return (filePath, fileSystem)
runCommand _ filePath fileSystem = return (filePath, fileSystem)

-- | Returns FilePath and FileSystem after execute command
execCommand 
  :: ([FilePath] -> [FilePath]) -- ^ List function
  -> FilePath -- ^ FilePath from command
  -> FilePath -- ^ Current FilePath
  -> FileSystem -- ^ Current FileSystem
  -> (FileSystem -> IO(FilePath, FileSystem)) -- ^ Command's function
  -> IO(FilePath, FileSystem) -- ^ FilePath and FileSystem after execute command
execCommand listFunc newFilePath filePath fileSystem commandFunc = do
  ans <- goToPath (listFunc (splitDirectories (getSpecialPath newFilePath filePath fileSystem))) fileSystem
  case ans of
    Nothing -> do
      putStr "not this directory or file\n"
      return (filePath, fileSystem)
    Just fs -> commandFunc fs

-- | Returns FilePath relative to the root of FileSystem for FilePath from command
getSpecialPath 
  :: FilePath -- ^ FilePath from command
  -> FilePath -- ^ Current FilePath
  -> FileSystem  -- ^ Current FileSystem
  -> FilePath -- ^ FilePath relative to the root of FileSystem for FilePath from command
getSpecialPath newFilePath filePath fileSystem
  | null (preparePath filePath (path fileSystem)) = newFilePath
  | otherwise                                     = preparePath filePath (path fileSystem) ++ [pathSeparator] ++ newFilePath

-- | Returns FilePath relative to the root of FileSystem
preparePath 
  :: FilePath -- ^ Current FilePath
  -> FilePath -- ^ FilePath of current FileSystem
  -> FilePath -- ^ FilePath relative to the root of FileSystem
preparePath [] [] = []
preparePath s []  = tail s
preparePath (a:as) (b:bs)
  | a == b = preparePath as bs
  | otherwise = as
preparePath _ _ = []

-- | Returns content of directory
showLS 
  :: FileSystem -- ^ Given directory
  -> String -- ^ Content of directory
showLS fileSystem = intercalate "\n" (getNames (files fileSystem))
  where
    getNames []       = []
    getNames (a : as) = name a : getNames as

-- | Try to go by given FilePath
goToPath 
  :: [FilePath] -- ^ Given list of names of FilePath
  -> FileSystem -- ^ Given FileSystem
  -> IO (Maybe FileSystem) -- ^ Found FileSystem is FilePath is OK
goToPath [] fs = return $ Just fs
goToPath (a : as) fs
  | isDirectory fs = case nextDir (files fs) a of
    (Just newFS) -> goToPath as newFS
    Nothing      -> return Nothing
  | otherwise = return Nothing

-- | Try to go to next directory by name
nextDir 
  :: [FileSystem] -- ^ Content of FileSystem
  -> FilePath -- ^ Given name of FilePath
  -> Maybe FileSystem -- ^ Next FileSystem
nextDir [] _ = Nothing
nextDir (x : xs) a
  | name x == a = Just x
  | otherwise   = nextDir xs a

-- | Returns full FilePath
getFullPath 
  :: FileSystem -- ^ Given FileSystem
  -> FilePath -- ^ Given FilePath
  -> FilePath -- ^ Full FilePath
getFullPath fs fp = path fs ++ [pathSeparator] ++ last (splitDirectories fp)

-- | Update FileSystem
update 
  :: FileSystem -- ^ FileSystem where will be changes
  -> FileSystem -- ^ Current FileSystem
  -> a -- ^ Parameter that need to change FileSystem
  -> ([FileSystem] -> a -> [FileSystem]) -- ^ Function to change FileSystem by parameter
  -> (FileSystem -> FileSystem) -- ^ Addition change of FileSystem
  -> (FileSystem -> FileSystem) -- ^ Addition change of FileSystem
  -> FileSystem -- ^ Updated FileSystem
update fs fileSystem param commandFunc countFS countFS' = update' fileSystem $ splitDirectories $ preparePath (path fs) (path fileSystem)
  where
    update' fs' [] = countFS $ fs' {files = commandFunc (files fs') param}
    update' fs' (a : as) = case nextDir (files fs') a of
      (Just newFS) -> countFS' $ fs' {files = updateFiles newFS (update' newFS as) (files fs')}
      Nothing      -> fs'

-- | Updates files if FileSystem
updateFiles 
  :: FileSystem -- ^ Old FileSystem
  -> FileSystem -- ^ New FileSystem
  -> [FileSystem] -- ^ Files of FileSystem
  -> [FileSystem] -- ^ Updated files of FileSystem
updateFiles _ _ [] = []
updateFiles fs newFS (f:fx)
  | fs == f   = newFS : updateFiles fs newFS fx
  | otherwise = f : updateFiles fs newFS fx

-- | Add file to files
addFile 
  :: [FileSystem] -- ^ Given files
  -> FileSystem  -- ^ Given file
  -> [FileSystem] -- ^ Updated files
addFile allFiles file = file : allFiles

-- | Remove file in files
rmFile 
  :: [FileSystem] -- ^ Given files
  -> FilePath -- ^ Given FilePath of file that will be removed
  -> [FileSystem] -- ^ Updated files
rmFile [] _ = []
rmFile (a:as) p
  | path a == p = rmFile as p
  | otherwise   = a : rmFile as p

-- | Update file in files
updFile 
  :: [FileSystem] -- ^ Given files
  -> (FilePath, UTCTime, [String]) -- ^ Given info of file that will be updated
  -> [FileSystem] -- ^ Updated files
updFile [] _ = []
updFile (a:as) info@(filePath, fileTime, fileContent)
  | path a == filePath = a 
    { content = unwords fileContent 
    , time = fileTime
    , size = toInteger (length (unwords fileContent)) 
    , wasChanged = True
    } : updFile as info
  | otherwise          = a : updFile as info

-- | Add one to count of files of FileSystem
countPlus 
  :: FileSystem -- ^ Given FileSystem
  -> FileSystem -- ^ Updated FileSystem
countPlus fs
  | isDirectory fs = fs {countOfFiles = countOfFiles fs + 1}
  | otherwise      = fs

-- | Minus one to count of files of FileSystem  
countMinus 
  :: FileSystem -- ^ Given FileSystem
  -> FileSystem -- ^ Updated FileSystem
countMinus fs
  | isDirectory fs = fs {countOfFiles = countOfFiles fs - 1}
  | otherwise      = fs

-- | Update size of FileSystem  
countSize 
  :: FileSystem -- ^ Given FileSystem
  -> FileSystem -- ^ Updated FileSystem
countSize fs
  | isDirectory fs = fs {size = getDirSize (files fs)}
  | otherwise      = fs

-- | Returns base Permissions for new file and directory
basePermissions 
  :: Permissions -- | Base Permissions for new file and directory
basePermissions = emptyPermissions 
  { readable = True 
  , writable = True 
  , executable = False
  , searchable = True
  }

-- | Create new directory
newDirByPath 
  :: FilePath -- ^ Given FilePath
  -> FileSystem -- ^ New directory
newDirByPath fp = Directory 0 fp 0 basePermissions (getName fp) []

-- | Create new file
newFileByPath 
  :: FilePath -- ^ Given FilePath
  -> UTCTime -- ^ Given time
  -> FileSystem -- ^ New file
newFileByPath fp t = File fp basePermissions (takeExtension fp) t 0 (takeFileName fp) "" True

-- | Try to find FileSystem by name of file
tryFind 
  :: [FileSystem] -- ^ Files of FileSystem
  -> String -- ^ Name of file
  -> Maybe FileSystem -- ^ FileSystem if it was found
tryFind [] _ = Nothing
tryFind (a : as) nm
  | isFile a && name a == nm = Just a <|> tryFind as nm
  | isFile a                 = tryFind as nm
  | otherwise                = tryFind (as ++ files a) nm

-- | Check contains FilePath in FileSyatem
contains
  :: FilePath -- ^ Given FilePath 
  -> FileSystem -- ^ Given FileSystem
  -> Bool -- ^ True if in given FileSystem contain given FilePath
contains nameFP fs = contains' (files fs) (getName nameFP)
  where
    contains' [] _ = False
    contains' (a:as) name'
      | name' == name a = True
      | otherwise       = contains' as name'