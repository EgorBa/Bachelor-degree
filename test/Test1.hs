module Test1 where

import Test.Tasty (TestTree)
import Test.Tasty.Hspec (Spec, describe, it, shouldBe, shouldSatisfy, testSpec)

import Task1

hspecTest1 :: IO TestTree
hspecTest1 = testSpec "Task1" spec

point00 :: Point
point00 = Point
  { x = 0
  , y = 0
  }

point10 :: Point
point10 = Point
  { x = 1
  , y = 0
  }

point01 :: Point
point01 = Point
  { x = 0
  , y = 1
  }

point11 :: Point
point11 = Point
  { x = 1
  , y = 1
  }

spec :: Spec
spec = do
  describe "perimeter" $
    it "returns perimeter" $ do
      perimeter [point00, point01, point11, point10] `shouldBe` 4.0
      perimeter [point00, point01]                   `shouldBe` 2.0
      perimeter [point00]                            `shouldBe` 0.0
  describe "doubleArea" $
    it "returns double area" $ do
      doubleArea [point00, point10, point11, point01] `shouldBe` 2
      doubleArea [point00, point11, point01]          `shouldBe` 1
      doubleArea [point00, point10, point11]          `shouldBe` 1
      doubleArea [point00]                            `shouldBe` 0  