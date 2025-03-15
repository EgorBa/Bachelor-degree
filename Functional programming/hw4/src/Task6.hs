module Task6
  ( FS(..)
  , contents
  , getDirectory'
  , name
  , _Dir
  , _File
  ) where

import Control.Lens (Lens', Prism', lens, prism)
import System.Directory (doesDirectoryExist, doesFileExist, listDirectory)
import System.FilePath.Posix (pathSeparator, splitDirectories, takeFileName)
import Control.Exception (try)

-- | Type of content in FileSystem
data FS 
  = Dir -- ^ Directory of FileSystem
    { _name     :: FilePath  -- ^ Name of directory
    , _contents :: [FS] -- ^ Content of directory
    } 
  | File -- ^ File of FileSystem
    { _name     :: FilePath  -- ^ Name of file
    } deriving (Show, Eq)

-- | Returns read FileSystem if everything is OK or Error
getDirectory'
  :: FilePath -- ^ Given FilePath
  -> IO FS -- ^ FileSystem if everything is OK
getDirectory' pathFS = do
  isDirectoryExist <- doesDirectoryExist pathFS
  isFileExist      <- doesFileExist pathFS
  if isDirectoryExist
    then do
      listOfFilePaths <- listDirectory pathFS
      dirFiles        <- scanDir listOfFilePaths pathFS
      return $ Dir (getDirName pathFS) dirFiles
    else
      if isFileExist
        then do
          return $ File (takeFileName pathFS)
        else error "this path doesn't exists\n"

-- | Returns read FileSystems of directory
scanDir
  :: [FilePath] -- ^ Content of directory
  -> FilePath -- ^ FilePath of given directory
  -> IO [FS] -- ^ FileSystems of directory
scanDir [] _        = return []
scanDir (a : as) fp = do
  eitherCurFileSystem <- try (getDirectory' (fp ++ [pathSeparator] ++ a)) :: IO (Either IOError FS)
  tailOfCurFileSystem <- scanDir as fp
  case eitherCurFileSystem of
    Left error' -> do
      print error'
      return tailOfCurFileSystem
    Right curFileSystem -> do
      return (curFileSystem : tailOfCurFileSystem)

-- | Returns True if FileSystem is Dir else False
isDir 
  :: FS -- ^ Given FileSystem
  -> Bool -- ^ True if FileSystem is Dir else False
isDir (Dir _ _) = True
isDir _         = False

-- | Returns name of Dir
getDirName 
  :: FilePath -- ^ FilePath of Dir
  -> FilePath -- ^ Name of Dir
getDirName = last . splitDirectories

-- | Returns name of FileSystem
name :: Lens' FS FilePath
name = lens _name (\fs v -> fs { _name = v })

-- | Returns contents for Dir or [] for File
contents :: Lens' FS [FS]
contents = lens (\fs -> if isDir fs then _contents fs else []) (\fs v -> if isDir fs then fs { _contents = v } else fs)

-- | Prism for Dir
_Dir :: Prism' FS FS
_Dir = prism id (\fs -> if isDir fs then Right fs else Left fs)

-- | Prism for File
_File :: Prism' FS FS
_File = prism id (\fs -> if not (isDir fs) then Right fs else Left fs)

