module Test5 where

import Test.Tasty (TestTree)
import Test.Hspec.QuickCheck
import Test.Tasty.Hspec (Spec, describe, it, shouldBe, shouldSatisfy, testSpec)
import Task5
import Data.List.NonEmpty

hspecTest5 :: IO TestTree
hspecTest5 = testSpec "Task5" spec

spec :: Spec
spec = do
  describe "splitOn" $
    it "returns lines separated by given symbol" $ do
      splitOn '/' "path/to/file"  `shouldBe` ("path":|["to", "file"])
      splitOn '/' "to/file"       `shouldBe` ("to":|["file"])
      splitOn '/' "/file"         `shouldBe` ("":|["file"])
      splitOn '/' "file"          `shouldBe` ("file":|[])
      splitOn 'g' "g"             `shouldBe` ("":|[""])

  describe "joinWith" $
    it "returns line join with given symbol" $ do
      joinWith '/' ("path":|["to", "file"])  `shouldBe` "path/to/file"
      joinWith '/' ("to":|["file"])          `shouldBe` "to/file"
      joinWith '/' ("file":|[])              `shouldBe` "file"
      joinWith 'g' ("":|[""])                `shouldBe` "g"

  describe "property" $
    prop "joinWith and splitOn" $ \chr str -> do
      joinWith chr (splitOn chr str) `shouldBe` str