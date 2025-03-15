module Test67 where

import Test.Tasty (TestTree)
import Test.Tasty.Hspec (Spec, describe, it, shouldBe, shouldSatisfy, testSpec)

import Task6
import Task7
import Control.Lens ((^..), (^?), (&), (.~))

hspecTest67 :: IO TestTree
hspecTest67 = testSpec "Task6, Task7" spec

dir1 :: FS
dir1 = Dir
  { _name = "dir1"
  , _contents = [file1, dir2]
  }

dir2 :: FS
dir2 = Dir
  { _name = "dir2"
  , _contents = [file2, dir3]
  }

dir3 :: FS
dir3 = Dir
  { _name = "dir3"
  , _contents = []
  }

file1 :: FS
file1 = File { _name = "file1" }

file2 :: FS
file2 = file1 & name .~ "file2"

spec :: Spec
spec = do
  describe "cd" $
    it "go to child directiry" $ do
      dir1 ^.. cd "file1"             `shouldBe` []
      dir1 ^? cd "file1"              `shouldBe` Nothing
      dir1 ^.. cd "dir2"              `shouldBe` [dir2]
      dir1 ^.. cd "dir2" . cd "dir3"  `shouldBe` [dir3]
      dir1 ^? cd "dir2"               `shouldBe` Just dir2
      dir1 ^.. cd "dir2" . cd "file2" `shouldBe` []
      dir1 ^? cd "dir2" . cd "file2"  `shouldBe` Nothing
  describe "ls" $
    it "show content of directiry" $ do
      dir1 ^.. ls                          `shouldBe` ["file1", "dir2"]
      dir1 ^.. cd "dir2" . ls              `shouldBe` ["file2", "dir3"]
      dir2 ^.. ls                          `shouldBe` ["file2", "dir3"]
      dir1 ^.. cd "dir2" . cd "file2" . ls `shouldBe` []
      dir1 ^? cd "dir2" . cd "file2" .ls   `shouldBe` Nothing
  describe "file" $
    it "show name of file in directiry" $ do
      dir1 ^.. file "file1"             `shouldBe` ["file1"]
      dir2 ^.. file "file2"             `shouldBe` ["file2"]
      dir1 ^.. file "file"              `shouldBe` []
      dir1 ^.. cd "dir2" . file "file2" `shouldBe` ["file2"]
      dir1 ^? cd "dir2" . file "file"   `shouldBe` Nothing
      dir1 ^? file "file1"              `shouldBe` Just "file1"
      dir1 ^? cd "dir2" . file "file2"  `shouldBe` Just "file2"