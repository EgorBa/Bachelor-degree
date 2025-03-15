module Test4 where

import Test.Tasty (TestTree)
import Test.Tasty.Hspec (Spec, describe, it, shouldBe, shouldSatisfy, testSpec)
import Data.Monoid
import Test.Hspec.QuickCheck
import Task34
import Data.List.NonEmpty hiding (fromList, toList, sort)
import Data.List (sort)

hspecTest4 :: IO TestTree
hspecTest4 = testSpec "Task4" spec

empty :: Tree Int
one :: Tree Int
oneTwo :: Tree Int
two :: Tree Int
empty = Leaf
one = Tree (1:|[]) Leaf Leaf
oneTwo = Tree (1:|[1]) Leaf Leaf
two = Tree (1:|[]) Leaf $ Tree (2:|[]) Leaf Leaf

spec :: Spec
spec = do
  describe "foldr" $
    it "funtion foldr" $ do
      foldr (+) 1 two    `shouldBe` 4
      foldr (+) 1 empty  `shouldBe` 1
      foldr (*) 2 two    `shouldBe` 4
      foldr (+) 1 oneTwo `shouldBe` 3
      product oneTwo     `shouldBe` 1

  describe "foldMap" $
    it "funtion foldMap" $ do
      foldMap Sum two    `shouldBe` Sum {getSum = 3}
      foldMap Sum one    `shouldBe` Sum {getSum = 1}
      foldMap Sum oneTwo `shouldBe` Sum {getSum = 2}

  describe "property" $
    prop "toList ans fromList [Int]" $ \list -> do
      toList (fromList (list :: [Int])) `shouldBe` sort list

  describe "property" $
    prop "toList ans fromList [Char]" $ \list -> do
      toList (fromList (list :: [Char])) `shouldBe` sort list