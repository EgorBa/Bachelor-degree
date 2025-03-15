module Test1 where

import Test.Tasty (TestTree)
import Test.Tasty.Hspec (Spec, describe, it, shouldBe, shouldSatisfy, testSpec)
import Test.Hspec.QuickCheck

import Task1

hspecTest1 :: IO TestTree
hspecTest1 = testSpec "Task1" spec

getStringWithError :: Int -> Int -> String
getStringWithError s count = getString s count ++ "err"

getStringByNumber :: Int -> Int -> String
getStringByNumber = getString

getString :: Int -> Int -> String
getString s count
  | count == 0 = ""
  | otherwise  = show s ++ ' ' : getString s (count-1)

spec :: Spec
spec = do
  describe "stringSum" $
    it "returns sum of string" $ do
      stringSum "1 2 3"       `shouldBe` Just 6
      stringSum "  1  2   3 " `shouldBe` Just 6
      stringSum "  1   -1"    `shouldBe` Just 0
      stringSum " 1  a -1"    `shouldBe` Nothing
      stringSum " 1r   r-1"   `shouldBe` Nothing
  describe "property" $
    prop "stringSum" $ \number count -> do
      stringSum (getStringByNumber number (abs count + 1))  `shouldBe` Just (number * (abs count + 1))
      stringSum (getStringWithError number (abs count + 1)) `shouldBe` Nothing
