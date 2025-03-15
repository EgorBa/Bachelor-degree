module Test3 where

import Test.Tasty (TestTree)
import Test.Tasty.Hspec (Spec, describe, it, shouldBe, shouldSatisfy, testSpec)
import Task3
import Data.Functor.Identity
import Data.Functor.Compose
import Data.Monoid

hspecTest3 :: IO TestTree
hspecTest3 = testSpec "Task3" spec

one :: NonEmpty Int
oneId :: NonEmpty (Identity Int)
oneIdId :: NonEmpty (Identity (Identity Int))
oneTwo :: NonEmpty Int
onePlus :: NonEmpty (Int -> Int)
two :: NonEmpty Int
three :: NonEmpty Int
one = 1 :| []
oneId = Identity 1 :| []
oneIdId = Identity (Identity 1) :| []
oneTwo = 2 :| []
onePlus = (+1) :| []
two = 1 :| [2]
three = 1 :| [2,3]

spec :: Spec
spec = do
  describe "foldr" $
    it "returns result of foldr" $ do
      foldr (+) 0 one   `shouldBe` 1
      foldr (+) 0 two   `shouldBe` 3
      foldr (+) 0 three `shouldBe` 6

  describe "foldMap" $
    it "returns result of foldMap" $ do
      foldMap Sum one    `shouldBe` Sum {getSum = 1}
      foldMap Sum two    `shouldBe` Sum {getSum = 3}
      foldMap Sum three  `shouldBe` Sum {getSum = 6}

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

  describe "Monad laws" $
    it "returns result of retun ans (>>=)" $ do
      show (return 1 >>= (:| []))                   `shouldBe` show ((:| []) 1)
      show (return 2 >>= (:| []))                   `shouldBe` show ((:| []) 2)
      show (one >>= return)                         `shouldBe` show one
      show (two >>= return)                         `shouldBe` show two
      show ((two >>= (:| [])) >>= (\x -> x :| [x])) `shouldBe` show (two >>= (\y -> (:| []) y >>= (\x -> x :| [x])))
      show ((two >>= (:| [])) >>= (\x -> x :| [x])) `shouldBe` show (two >>= (\y -> (:| []) y >>= (\x -> x :| [x])))