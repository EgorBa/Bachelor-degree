module Test4 where

import Test.Tasty (TestTree)
import Test.Tasty.Hspec (Spec, describe, it, shouldBe, shouldSatisfy, testSpec)

import Task_4

hspecTest4 :: IO TestTree
hspecTest4 = testSpec "Task4" spec

var = Add (Divide (Const 4) (Const 2)) (Multiply (Const 4) (Const 2))
var1 = Add (Divide (Const 4) (Const 0)) (Multiply (Const 4) (Const 2))
var2 = Add (Divide (Const 4) (Const 2)) (Pow (Const 4) (Const (-2)))
var3 = Sub (Divide (Const 4) (Const 2)) (Pow (Const (-4)) (Const 2))

spec :: Spec
spec = do
  describe "eval" $
    it "returns safety evaluate of expression" $ do
      eval var  `shouldBe` Right 10
      eval var1 `shouldBe` Left DivisionByZero
      eval var2 `shouldBe` Left NegativePow
      eval var3 `shouldBe` Right (-14)