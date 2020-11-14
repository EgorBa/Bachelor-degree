module Test1 where

import Test.Tasty (TestTree)
import Test.Tasty.Hspec (Spec, describe, it, shouldBe, shouldSatisfy, testSpec)

import Task1

hspecTest1 :: IO TestTree
hspecTest1 = testSpec "Task1" spec

spec :: Spec
spec = do
  describe "stringSum" $
    it "returns sum of string" $ do
      stringSum "1 2 3"       `shouldBe` Just 6
      stringSum "  1  2   3 " `shouldBe` Just 6
      stringSum "  1   -1"    `shouldBe` Just 0
      stringSum " 1  a -1"    `shouldBe` Nothing
      stringSum " 1r   r-1"   `shouldBe` Nothing