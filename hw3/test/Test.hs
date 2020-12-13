module Test where

import Test.Tasty (TestTree)
import Test.Tasty.Hspec (Spec, describe, it, shouldBe, testSpec)

import Data.Time.Calendar (fromGregorian)
import System.Directory (emptyPermissions, searchable, executable, writable, readable, Permissions)
import Data.Time.Clock (UTCTime, secondsToDiffTime)
import Data.Time
import Commands
import Types

hspecTest :: IO TestTree
hspecTest = testSpec "Commands" spec

basePermissions :: Permissions
basePermissions = emptyPermissions {readable = True, writable = True, executable = False, searchable = True}

baseTime :: UTCTime
baseTime = UTCTime (fromGregorian 2000 6 23) (secondsToDiffTime 0)

file :: FileSystem
file = File 
  { path = "C:\\dir\\file.txt"
  , permissions = basePermissions
  , fileType = ".txt"
  , time = baseTime
  , size = 0
  , name = "file.txt"
  , content = ""
  , wasChanged = True
  }

fileDir1 :: FileSystem
fileDir1 = file { path = "C:\\dir\\dir1\\file.txt" }

file1 :: FileSystem
file1 = file 
  { path = "C:\\dir\\test.txt"
  , size = 4
  , name = "test.txt"
  , content = "test"
  , wasChanged = False
  }

file1' :: FileSystem
file1' = file1 
  { size = 9
  , content = "good test"
  , wasChanged = True
  }

file2 :: FileSystem
file2 = file 
  { path = "C:\\dir\\dir1\\test1.txt"
  , size = 5
  , name = "test1.txt"
  , content = "test1"
  , wasChanged = False
  }

file2' :: FileSystem
file2' = file2 
  { size = 9
  , content = "good test"
  , wasChanged = True
  }

dir2 :: FileSystem
dir2 = rootDir 
  { size = 0
  , path = "C:\\dir\\dir1\\dir2"
  , countOfFiles = 0
  , name = "dir2"
  , files = []
  }

dir1 :: FileSystem
dir1 = rootDir 
  { size = 5
  , path = "C:\\dir\\dir1"
  , name = "dir1"
  , files = [file2, dir2]
  }

dir1Write :: FileSystem
dir1Write = dir1 
  { size = 9
  , files = [file2', dir2]
  }

dir :: FileSystem
dir = dir1 
  { path = "C:\\dir\\dir"
  , size = 0
  , countOfFiles = 0
  , name = "dir"
  , files = []
  }

dirDir1 :: FileSystem
dirDir1 = dir { path = "C:\\dir\\dir1\\dir" }

rootDir :: FileSystem
rootDir = Directory 
  { size = 9
  , path = "C:\\dir"
  , countOfFiles = 2
  , permissions = basePermissions
  , name = "dir"
  , files = [file1, dir1]
  }

rootDirWrite :: FileSystem
rootDirWrite = rootDir 
  { size = 14
  , files = [file1', dir1]
  }

rootDirWrite' :: FileSystem
rootDirWrite' = rootDir 
  { size = 13
  , files = [file1, dir1Write]
  }

rootDirModified :: FileSystem
rootDirModified = rootDir 
  { countOfFiles = 3
  , files = [file, file1, dir1]
  }

rootDirModified' :: FileSystem
rootDirModified' = rootDirModified { files = [dir, file1, dir1] }

dir1Modified :: FileSystem
dir1Modified = dir1 
  { countOfFiles = 3
  , files = [fileDir1, file2, dir2]
  }

dir1Modified' :: FileSystem
dir1Modified' = dir1Modified { files = [dirDir1, file2, dir2] }

rootDir1Modified :: FileSystem
rootDir1Modified = rootDir { files = [file1, dir1Modified] }

rootDir1Modified' :: FileSystem
rootDir1Modified' = rootDir { files = [file1, dir1Modified'] }

rootPath :: FilePath
rootPath = "C:\\dir"

dir1Path :: FilePath
dir1Path = "C:\\dir\\dir1"

dir2Path :: FilePath
dir2Path = "C:\\dir\\dir1\\dir2"

spec :: Spec
spec = do
  describe "cd" $
    it "go to folder or root" $ do
      (fp, fs) <- runCommand (CD "dir1") rootPath rootDir
      fp `shouldBe` dir1Path
      fs `shouldBe` rootDir
      (fp', fs') <- runCommand (CD "..") fp fs
      fp' `shouldBe` rootPath
      fs' `shouldBe` rootDir
      (fp, fs) <- runCommand (CD "dir1\\dir2") rootPath rootDir
      fp `shouldBe` dir2Path
      fs `shouldBe` rootDir
      (fp', fs') <- runCommand (CD "..") fp fs
      fp' `shouldBe` rootPath
      fs' `shouldBe` rootDir
  describe "touch" $
    it "create file" $ do
      (fp, fs) <- runCommand (TOUCH "file.txt") rootPath rootDir
      fp `shouldBe` rootPath
      fs `shouldBe` rootDirModified
      (fp, fs) <- runCommand (TOUCH "dir1\\file.txt") rootPath rootDir
      fp `shouldBe` rootPath
      fs `shouldBe` rootDir1Modified
      (fp, fs) <- runCommand (TOUCH "dir1\\file.txt") rootPath rootDir1Modified
      fp `shouldBe` rootPath
      fs `shouldBe` rootDir1Modified
  describe "rm" $
    it "remove file" $ do
      (fp, fs) <- runCommand (RM "file.txt") rootPath rootDirModified
      fp `shouldBe` rootPath
      fs `shouldBe` rootDir
      (fp, fs) <- runCommand (RM "dir1\\file.txt") rootPath rootDir1Modified
      fp `shouldBe` rootPath
      fs `shouldBe` rootDir
  describe "mkdir" $
    it "create folder" $ do
      (fp, fs) <- runCommand (MKDIR "dir") rootPath rootDir
      fp `shouldBe` rootPath
      fs `shouldBe` rootDirModified'
      (fp, fs) <- runCommand (MKDIR "dir1\\dir") rootPath rootDir
      fp `shouldBe` rootPath
      fs `shouldBe` rootDir1Modified'
      (fp, fs) <- runCommand (MKDIR "dir1\\dir") rootPath rootDir1Modified'
      fp `shouldBe` rootPath
      fs `shouldBe` rootDir1Modified'
  describe "rmdir" $
    it "remove folder" $ do
      (fp, fs) <- runCommand (RMDIR "dir") rootPath rootDirModified'
      fp `shouldBe` rootPath
      fs `shouldBe` rootDir
      (fp, fs) <- runCommand (RMDIR "dir1\\dir") rootPath rootDir1Modified'
      fp `shouldBe` rootPath
      fs `shouldBe` rootDir
  describe "write" $
    it "write to file" $ do
      (fp, fs) <- runCommand (WRITE "test.txt" ["good", "test"] ) rootPath rootDir
      fp `shouldBe` rootPath
      fs `shouldBe` rootDirWrite
      (fp, fs) <- runCommand (WRITE "dir1\\test1.txt" ["good", "test"]) rootPath rootDir
      fp `shouldBe` rootPath
      fs `shouldBe` rootDirWrite'
  describe "ls and dir" $
    it "show content of given or current folder" $ do
      (fp, fs) <- runCommand DIR rootPath rootDir
      fp `shouldBe` rootPath
      fs `shouldBe` rootDir
      showLS rootDir `shouldBe` "test.txt\ndir1"
      (fp', fs') <- runCommand (LS "dir1") rootPath rootDir
      fp' `shouldBe` rootPath
      fs' `shouldBe` rootDir
      showLS dir1 `shouldBe` "test1.txt\ndir2"
  describe "cat" $
    it "show content of file" $ do
      (fp, fs) <- runCommand (CAT "test.txt") rootPath rootDir
      fp `shouldBe` rootPath
      fs `shouldBe` rootDir
      content file1 `shouldBe` "test"
      (fp, fs) <- runCommand (CAT "dir1\\test1.txt") rootPath rootDir
      fp `shouldBe` rootPath
      fs `shouldBe` rootDir
      content file2 `shouldBe` "test1"
  describe "find" $
    it "show path of file in folder if exists" $ do
      (fp, fs) <- runCommand (FIND "test.txt") rootPath rootDir
      fp `shouldBe` rootPath
      fs `shouldBe` rootDir
      tryFind [rootDir] "test.txt" `shouldBe` Just file1
      (fp, fs) <- runCommand (FIND "test1.txt") rootPath rootDir
      fp `shouldBe` rootPath
      fs `shouldBe` rootDir
      tryFind [rootDir] "test1.txt" `shouldBe` Just file2
      (fp, fs) <- runCommand (FIND "test1.txt") dir1Path rootDir
      fp `shouldBe` dir1Path
      fs `shouldBe` rootDir
      tryFind [dir1] "test1.txt" `shouldBe` Just file2
  describe "help" $
    it "show FileSystem ruls" $ do
      (fp, fs) <- runCommand HELP rootPath rootDir
      fp `shouldBe` rootPath
      fs `shouldBe` rootDir
  describe "info" $
    it "show info about folder or file" $ do
      (fp, fs) <- runCommand (INFO rootPath) rootPath rootDir
      fp `shouldBe` rootPath
      fs `shouldBe` rootDir
      (fp, fs) <- runCommand (INFO (path file1)) rootPath rootDir
      fp `shouldBe` rootPath
      fs `shouldBe` rootDir
