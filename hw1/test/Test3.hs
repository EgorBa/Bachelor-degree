module Test3 where

import Test.Tasty (TestTree)
import Test.Tasty.Hspec (Spec, describe, it, shouldBe, shouldSatisfy, testSpec)
import Task34
import Data.List (sort)
import Data.List.NonEmpty hiding (toList, fromList, tail, head, sort)

hspecTest3 :: IO TestTree
hspecTest3 = testSpec "Task3" spec

empty = List
one = Tree (1:|[]) List List
oneTwo = Tree (1:|[1]) List List
two = Tree (1:|[]) List $ Tree (2:|[]) List List

spec :: Spec
spec = do
  describe "isEmpty" $
    it "returns teue if tree is empty" $ do
      isEmpty empty  `shouldBe` True
      isEmpty one    `shouldBe` False
      isEmpty oneTwo `shouldBe` False

  describe "count" $
    it "returns count of values" $ do
      count empty  `shouldBe` 0
      count one    `shouldBe` 1
      count oneTwo `shouldBe` 2

  describe "find" $
    it "returns true is tree contains this value" $ do
      find 0 empty  `shouldBe` False
      find 1 empty  `shouldBe` False
      find 0 one    `shouldBe` False
      find 1 one    `shouldBe` True
      find 1 oneTwo `shouldBe` True

  describe "add" $
    it "returns given tree with added value" $ do
      show (add 1 empty) `shouldBe` show one
      show (add 1 one)   `shouldBe` show oneTwo
      show (add 2 one)   `shouldBe` show two

  describe "fromList" $
    it "returns tree from given list of values" $ do
      toList (fromList [1])   `shouldBe` toList one
      toList (fromList [1,1]) `shouldBe` toList oneTwo
      toList (fromList [1,2]) `shouldBe` toList two
      toList (fromList [1,2]) `shouldBe` toList (fromList [2,1])

  describe "toList" $
    it "returns list of values from given tree" $ do
      toList one    `shouldBe` [1]
      toList two    `shouldBe` [1,2]
      toList oneTwo `shouldBe` [1,1]

  describe "del" $
    it "returns tree without given value" $ do
      show (del 1 one)    `shouldBe` "List"
      show (del 1 oneTwo) `shouldBe` show one
      show (del 2 one)    `shouldBe` show one
      show (del 2 two)    `shouldBe` show one

