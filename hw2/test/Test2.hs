module Test2 where

import Test.Tasty (TestTree)
import Test.Tasty.Hspec (Spec, describe, it, shouldBe, shouldSatisfy, testSpec)
import Data.Monoid
import Data.Functor.Compose
import Task2
import Data.Functor.Identity

hspecTest2 :: IO TestTree
hspecTest2 = testSpec "Task2" spec

one :: Tree Int
oneId :: Tree (Identity Int)
oneIdId :: Tree (Identity (Identity Int))
oneTwo :: Tree Int
onePlus :: Tree (Int -> Int)
two :: Tree Int
three :: Tree Int
onePlus = Leaf (+1)
one = Leaf 1
oneId = Leaf (Identity 1)
oneIdId = Leaf (Identity (Identity 1))
oneTwo = Leaf 2
two = Branch (Leaf 1) (Leaf 1)
three = Branch (Leaf 1) (Branch (Leaf 1) (Leaf 1))

spec :: Spec
spec = do
  describe "foldr" $
    it "returns result of foldr" $ do
      foldr (+) 0 one   `shouldBe` 1
      foldr (+) 0 two   `shouldBe` 2
      foldr (+) 0 three `shouldBe` 3

  describe "foldMap" $
    it "returns result of foldMap" $ do
      foldMap Sum one    `shouldBe` Sum {getSum = 1}
      foldMap Sum two    `shouldBe` Sum {getSum = 2}
      foldMap Sum three  `shouldBe` Sum {getSum = 3}

  describe "Functor laws" $
    it "returns result of fmap" $ do
      show (fmap id one)            `shouldBe` show (id one)
      show (fmap id two)            `shouldBe` show (id two)
      show (fmap ((+1).(+1)) one)   `shouldBe` show ((fmap (+1) . fmap (+1)) one)
      show (fmap ((+1).(+1)) two)   `shouldBe` show ((fmap (+1) . fmap (+1)) two)

  describe "Applicative laws" $
    it "returns result of pure and (<*>)" $ do
      show (pure id <*> one)                          `shouldBe` show one
      show (pure id <*> two)                          `shouldBe` show two
      show (pure (.) <*> onePlus <*> onePlus <*> one) `shouldBe` show (onePlus <*> (onePlus <*> one))
      show (pure (.) <*> onePlus <*> onePlus <*> two) `shouldBe` show (onePlus <*> (onePlus <*> two))
      show (onePlus <*> pure 1)                       `shouldBe` show oneTwo
      show (onePlus <*> pure 1)                       `shouldBe` show (pure ($ 1) <*> onePlus)

  describe "Traversable laws" $
    it "returns result of traverse and sequenceA" $ do
      show (traverse Identity one)                               `shouldBe` show (Identity one)
      show ((id . traverse Sum) one)                             `shouldBe` show (traverse (id . Sum) one)
      show (traverse (Compose . fmap Identity . Identity) oneId) `shouldBe`  show ((Compose . fmap (traverse Identity) . (traverse Identity)) oneId)
      show ((id . sequenceA) oneId)                              `shouldBe` show ((sequenceA . fmap id) oneId)
      show ((sequenceA . fmap Identity) oneId)                   `shouldBe`  show (Identity oneId)
      show ((sequenceA . fmap Compose) oneIdId)                  `shouldBe`  show ((Compose . fmap sequenceA . sequenceA) oneIdId)
