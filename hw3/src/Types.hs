module Types
  ( Command(..)
  , FileSystem(..)
  , isDirectory
  , isFile
  , printDirectory
  , printFile
  , isExit
  , getDirSize
  , getName
  ) where

import Data.Time.Clock (UTCTime)
import System.Directory (Permissions)
import System.FilePath.Windows (splitDirectories)

-- | Type of commands
data Command
  = HELP -- ^ Show guide
  | CD FilePath -- ^ Go to folder or root
  | DIR -- ^ Show content of current directory
  | MKDIR FilePath -- ^ Create folder
  | TOUCH FilePath -- ^ Create file
  | CAT FilePath -- ^ Show content of file
  | RM FilePath -- ^ Remove file
  | RMDIR FilePath -- ^ Remove folder
  | FIND String -- ^ Show absolute path to file
  | WRITE FilePath [String] -- ^ Write to file words
  | LS FilePath -- ^ Show content of folder
  | INFO FilePath -- ^ Show information about file or folder
  | EXIT -- ^ Save FileSystem
  deriving (Show)

-- | Type of content in FileSystem
data FileSystem
  = File -- ^ File of FileSystem
    { path        :: FilePath -- ^ Path of file
    , permissions :: Permissions -- ^ Permissions of file
    , fileType    :: String -- ^ Type of file
    , time        :: UTCTime -- ^ Last time of modified file
    , size        :: Integer -- ^ Size of file
    , name        :: String -- ^ Name of file
    , content     :: String -- ^ Content of file
    , wasChanged  :: Bool -- ^ Flag that show was changed file
    }
  | Directory -- ^ Directory of FileSystem
    { size         :: Integer -- ^ Size of folder
    , path         :: FilePath -- ^ Path of folder
    , countOfFiles :: Int -- ^ Count of files in folder
    , permissions  :: Permissions -- ^ Permissions of folder
    , name         :: String -- ^ Name of folder
    , files        :: [FileSystem] -- ^ Content of folder
    }
  deriving (Show)

instance Eq FileSystem where
  fs1 == fs2
    | isDirectory fs1 && isDirectory fs2 = name fs1 == name fs2 
                                           && size fs1 == size fs2
                                           && countOfFiles fs1 == countOfFiles fs2
                                           && path fs1 == path fs2
                                           && files fs1 == files fs2
    | isFile fs1 && isFile fs2           = name fs1 == name fs2
                                           && size fs1 == size fs2
                                           && fileType fs1 == fileType fs2
                                           && path fs1 == path fs2
                                           && content fs1 == content fs2
    | otherwise                          = False

-- | Returns True if FileSystem is Directory
isDirectory 
  :: FileSystem -- ^ Given FileSystem
  -> Bool -- ^ True if FileSystem is Directory
isDirectory Directory {} = True
isDirectory _            = False

-- | Returns True if FileSystem is File
isFile 
  :: FileSystem -- ^ Given FileSystem
  -> Bool -- ^ True if FileSystem is File
isFile File {} = True
isFile _       = False

-- | Show info about file
printFile 
  :: FileSystem -- ^ Given FileSystem
  -> IO() -- ^ Info about file
printFile file
  | isFile file = do
    putStrLn ("file path : " ++ path file)
    putStrLn ("permissions : " ++ show (permissions file))
    putStrLn ("file type : " ++ fileType file)
    putStrLn ("time : " ++ show (time file))
    putStrLn ("size : " ++ show (size file))
  | otherwise = return ()

-- | Show info about folder
printDirectory 
  :: FileSystem -- ^ Given FileSystem
  -> IO() -- ^ Info about folder
printDirectory dir
  | isDirectory dir = do
    putStrLn ("dir path : " ++ path dir)
    putStrLn ("permissions : " ++ show (permissions dir))
    putStrLn ("size : " ++ show (size dir))
    putStrLn ("countOfFiles : " ++ show (countOfFiles dir))
  | otherwise = return ()

-- | Returns True if Command is EXIT
isExit 
  :: Command -- ^ Given command
  -> Bool -- ^ True if Command is EXIT
isExit EXIT = True
isExit _    = False

-- | Returns size of folder
getDirSize 
  :: [FileSystem] -- ^ Content of folder
  -> Integer -- ^ Size of folder
getDirSize = foldr ((+) . size) 0

-- | Returns name from FilePath
getName 
  :: FilePath -- ^ Given FilePath
  -> String -- ^ Name from FilePath
getName = last . splitDirectories