module Test2 where

import Test.Tasty (TestTree)
import Test.Tasty.Hspec (Spec, describe, it, shouldBe, shouldSatisfy, testSpec)

import Task2

hspecTest2 :: IO TestTree
hspecTest2 = testSpec "Task2" spec

spec :: Spec
spec = do
  describe "integrate" $
    it "count integral" $ do
      integrate 2.0 4.0 (+4)              `shouldSatisfy` (\x -> x >= 12 && x <= 16 )
      integrate 2.0 4.0 (*2)              `shouldSatisfy` (\x -> x >= 8 && x <= 16 )
      integrate 2.0 4.0 (\x -> x * 2 + 4) `shouldSatisfy` (\x -> x >= 16 && x <= 24 )
  describe "integrateConcurrently" $
    it "count integral" $ do
      integrateConcurrently 2.0 4.0 (+4)              `shouldSatisfy` (\x -> x >= 12 && x <= 16 )
      integrateConcurrently 2.0 4.0 (*2)              `shouldSatisfy` (\x -> x >= 8 && x <= 16 )
      integrateConcurrently 2.0 4.0 (\x -> x * 2 + 4) `shouldSatisfy` (\x -> x >= 16 && x <= 24 )