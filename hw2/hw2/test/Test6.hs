module Test6 where

import Test.Tasty (TestTree)
import Test.Tasty.Hspec (Spec, describe, it, shouldBe, shouldSatisfy, testSpec)

import Task6
import Data.Functor.Identity
import Control.Applicative

hspecTest6 :: IO TestTree
hspecTest6 = testSpec "Task6" spec

one :: Parser Int Int
two :: Parser Int Int
onePlus :: Parser Int (Int -> Int)
oneId :: Parser Int (Identity Int)
one = Parser (\x -> Just(1, x))
onePlus = Parser (\x -> Just((+1), x))
two = Parser (\x -> Just(2, x))
oneId = Parser (\x -> Just(Identity 1, x))
func x = Parser (\y -> Just (x, y))

spec :: Spec
spec = do
  describe "Functor laws" $
    it "returns result of fmap" $ do
      show (runParser (fmap id one) [1])           `shouldBe` show (runParser (id one) [1])
      show (runParser (fmap id two) [1])           `shouldBe` show (runParser (id two) [1])
      show (runParser (fmap ((+1).(+1)) one) [1])  `shouldBe` show (runParser ((fmap (+1) . fmap (+1)) one) [1])
      show (runParser (fmap ((+1).(+1)) two) [1])  `shouldBe` show (runParser ((fmap (+1) . fmap (+1)) two) [1])

  describe "Applicative laws" $
    it "returns result of pure and (<*>)" $ do
      show (runParser (pure id <*> one) [1])                          `shouldBe` show (runParser one [1])
      show (runParser (pure id <*> two) [1])                          `shouldBe` show (runParser two [1])
      show (runParser (pure (.) <*> onePlus <*> onePlus <*> one) [1]) `shouldBe` show (runParser (onePlus <*> (onePlus <*> one)) [1])
      show (runParser (pure (.) <*> onePlus <*> onePlus <*> two) [1]) `shouldBe` show (runParser (onePlus <*> (onePlus <*> two)) [1])
      show (runParser (onePlus <*> pure 1) [1])                       `shouldBe` show (runParser two [1])
      show (runParser (onePlus <*> pure 1) [1])                       `shouldBe` show (runParser (pure ($ 1) <*> onePlus) [1])

  describe "Monad laws" $
    it "returns result of retun ans (>>=)" $ do
      show (runParser (return 1 >>= func) [1])        `shouldBe` show (runParser (func 1) [1])
      show (runParser (return 2 >>= func) [1])        `shouldBe` show (runParser (func 2) [1])
      show (runParser (one >>= return) [1])           `shouldBe` show (runParser one [1])
      show (runParser (two >>= return) [1])           `shouldBe` show (runParser two [1])
      show (runParser ((one >>= func) >>= func) [1])  `shouldBe` show (runParser (one >>= (\y -> func y >>= func)) [1])
      show (runParser ((two >>= func) >>= func) [1])  `shouldBe` show (runParser (two >>= (\y -> func y >>= func)) [1])

  describe "Alternative laws" $
      it "returns result of empty and (<|>)" $ do
        show (runParser (one <|> empty) [1])            `shouldBe` show (runParser one [1])
        show (runParser (two <|> empty) [1])            `shouldBe` show (runParser two [1])
        show (runParser (empty <|> one) [1])            `shouldBe` show (runParser one [1])
        show (runParser (empty <|> two) [1])            `shouldBe` show (runParser two [1])
        show (runParser ((empty <|> two) <|> one) [1])  `shouldBe` show (runParser (empty <|> (two <|> one)) [1])