module Test6 where

import Test.Tasty (TestTree)
import Test.Tasty.Hspec (Spec, describe, it, shouldBe, shouldSatisfy, testSpec)
import Task6
import Data.Monoid

hspecTest6 :: IO TestTree
hspecTest6 = testSpec "Task6" spec

spec :: Spec
spec = do
  describe "maybeConcat" $
    it "returns list of values of Maybe" $ do
      maybeConcat [Just [1,2,3], Nothing, Just [4,5]]  `shouldBe` [1,2,3,4,5]
      maybeConcat [Nothing, Nothing, Just [4,5]]       `shouldBe` [4,5]
      maybeConcat [Nothing, Just [4,5], Nothing]       `shouldBe` [4,5]
      maybeConcat [Just [4,5], Nothing, Nothing]       `shouldBe` [4,5]
      length (maybeConcat [Nothing, Nothing, Nothing]) `shouldBe` 0

  describe "eitherConcat" $
    it "returns either of concat left and right parts" $ do
      eitherConcat [Left (Sum 3), Right [1,2,3], Left (Sum 5), Right [4,5]]  `shouldBe` (Sum {getSum = 8}, [1,2,3,4,5])
      eitherConcat [Left (Sum 3), Right [1,2,3]]                             `shouldBe` (Sum {getSum = 3}, [1,2,3])
      eitherConcat [Left (Sum 5), Right [4,5]]                               `shouldBe` (Sum {getSum = 5}, [4,5])
      eitherConcat [Left [4,5], Right (Sum 5)]                               `shouldBe` ([4,5], Sum {getSum = 5})