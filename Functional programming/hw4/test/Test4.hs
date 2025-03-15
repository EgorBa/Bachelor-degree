module Test4 where

import Test.Tasty (TestTree)
import Test.Tasty.Hspec (Spec, describe, it, shouldBe, shouldSatisfy, testSpec)
import Test.Hspec.QuickCheck

import Task4

hspecTest4 :: IO TestTree
hspecTest4 = testSpec "Task4" spec

multiplicityTwo' :: Int -> Bool
multiplicityTwo' a = do
  if ((abs a) `mod` 2 == 0)
    then True
    else False

log2' :: Int -> Int -> Int
log2' a b
  | a < 0     = log2' (abs a) b
  | a > b     = 1 + log2' a (2 * b)
  | otherwise = 0

spec :: Spec
spec = do
  describe "log2" $
    it "count whole logarithm" $ do
      runLog2 8 `shouldBe` 3
      runLog2 9 `shouldBe` 4
      runLog2 0 `shouldBe` 0
      runLog2 1 `shouldBe` 0
      runLog2 2 `shouldBe` 1
  describe "appendHaskell" $
    it "append \" Haskell!\" to string" $ do
      runAppendHaskell "Love" `shouldBe` "Love Haskell!"
      runAppendHaskell ""     `shouldBe` " Haskell!"
      runAppendHaskell "Hope" `shouldBe` "Hope Haskell!"
  describe "multiplicityTwo" $
    it "check multiplicity two" $ do
      runMultiplicityTwo 8 `shouldBe` True
      runMultiplicityTwo 0 `shouldBe` True
      runMultiplicityTwo 3 `shouldBe` False
      runMultiplicityTwo 5 `shouldBe` False
  describe "property" $
    prop "count whole logarithm" $ \a -> do
      runLog2 (abs (a :: Int)) `shouldBe` log2' a 1
  describe "property" $
    prop "append \" Haskell!\" to string" $ \a -> do
      runAppendHaskell (a :: String) `shouldBe` a ++ " Haskell!" 
  describe "property" $
    prop "heck multiplicity two" $ \a -> do
      runMultiplicityTwo (abs (a :: Int)) `shouldBe` multiplicityTwo' a