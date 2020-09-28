module Test_4 where

import Test.Tasty (TestTree)
import Test.Tasty.Hspec (Spec, describe, it, shouldBe, shouldSatisfy, testSpec)
import Data.Monoid
import Task_4
import Data.List.NonEmpty

hspecTest4 :: IO TestTree
hspecTest4 = testSpec "Task4" spec

empty :: Tree Int
one :: Tree Int
oneTwo :: Tree Int
two :: Tree Int
empty = List
one = Tree (1:|[]) List List
oneTwo = Tree (1:|[1]) List List
two = Tree (1:|[]) List $ Tree (2:|[]) List List

spec :: Spec
spec = do
  describe "foldr" $
    it "funtion foldr" $ do
      foldr (+) 1 two    `shouldBe` 4
      foldr (+) 1 empty  `shouldBe` 1
      foldr (*) 1 two    `shouldBe` 2
      foldr (+) 1 oneTwo `shouldBe` 3
      foldr (*) 1 oneTwo `shouldBe` 1

  describe "foldMap" $
    it "funtion foldMap" $ do
      foldMap Sum two    `shouldBe` Sum {getSum = 3}
      foldMap Sum one    `shouldBe` Sum {getSum = 1}
      foldMap Sum oneTwo `shouldBe` Sum {getSum = 2}