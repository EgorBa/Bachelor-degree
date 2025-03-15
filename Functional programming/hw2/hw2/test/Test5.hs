module Test5 where

import Test.Tasty (TestTree)
import Test.Tasty.Hspec (Spec, describe, it, shouldBe, shouldSatisfy, testSpec)

import Task5

hspecTest5 :: IO TestTree
hspecTest5 = testSpec "Task5" spec

spec :: Spec
spec = do
  describe "moving" $
    it "returns simple moving average" $ do
      moving 4 [1, 5, 3, 8, 7, 9, 6] `shouldBe` [1.0, 3.0, 3.0, 4.25, 5.75, 6.75, 7.5]
      moving 2 [1, 5, 3, 8, 7, 9, 6] `shouldBe` [1.0, 3.0, 4.0, 5.5, 7.5, 8.0, 7.5]
      moving 1 [1, 5, 3, 8, 7, 9, 6] `shouldBe` [1, 5, 3, 8, 7, 9, 6]
      moving 0 [1, 5, 3, 8, 7, 9, 6] `shouldBe` []
      moving 1 []                    `shouldBe` []