module Test2 where

import Test.Tasty (TestTree)
import Test.Tasty.Hspec (Spec, describe, it, shouldBe, shouldSatisfy, testSpec)
import Task2
import Test.Hspec.QuickCheck
import Prelude hiding (sum)

hspecTest2 :: IO TestTree
hspecTest2 = testSpec "Task2" spec

zero = Z
one = S Z
two = S $ S Z
five = S $ S $ S $ S $ S Z

spec :: Spec
spec = do
  describe "sum" $
    it "returns sum of numbers" $ do
      show (sum one zero) `shouldBe` show one
      show (sum zero one) `shouldBe` show one
      show (sum one one)  `shouldBe` show two

  describe "multiply" $
    it "returns multiply of numbers" $ do
      show (multiply one zero) `shouldBe` show zero
      show (multiply zero one) `shouldBe` show zero
      show (multiply one one)  `shouldBe` show one
      show (multiply two one)  `shouldBe` show two
      show (multiply one two)  `shouldBe` show two

  describe "sub" $
    it "returns difference of numbers" $ do
      show (sub one zero) `shouldBe` show one
      show (sub two one)  `shouldBe` show one
      show (sub zero one) `shouldBe` show zero
      show (sub one one)  `shouldBe` show zero

  describe "eq" $
    it "returns true if numbers are equals" $ do
      eq one zero `shouldBe` False
      eq two one  `shouldBe` False
      eq zero one `shouldBe` False
      eq one one  `shouldBe` True

  describe "more" $
    it "returns true if the first number more than the second number" $ do
      more one zero `shouldBe` True
      more two one  `shouldBe` True
      more zero one `shouldBe` False
      more one one  `shouldBe` False

  describe "less" $
    it "returns true if the first number less than the second number" $ do
      less one zero `shouldBe` False
      less two one  `shouldBe` False
      less zero one `shouldBe` True
      less one one  `shouldBe` False

  describe "even'" $
    it "returns true if number is even" $ do
      even' zero `shouldBe` True
      even' one  `shouldBe` False
      even' two  `shouldBe` True

  describe "div'" $
    it "returns integer division of numbers" $ do
      show (div' zero one)  `shouldBe` show zero
      show (div' one one)   `shouldBe` show one
      show (div' two one)   `shouldBe` show two
      show (div' five two)  `shouldBe` show two

  describe "mod'" $
    it "returns remainder of division of numbers" $ do
      show (mod' zero one)  `shouldBe` show zero
      show (mod' one one)   `shouldBe` show zero
      show (mod' two one)   `shouldBe` show zero
      show (mod' five two)  `shouldBe` show one

  describe "fromNatToInt" $
    it "returns Int from Nat" $ do
      fromNatToInt zero `shouldBe` 0
      fromNatToInt one  `shouldBe` 1
      fromNatToInt two  `shouldBe` 2
      fromNatToInt five `shouldBe` 5

  describe "fromIntToNat" $
    it "returns Nat from Int" $ do
      show (fromIntToNat 0) `shouldBe` show zero
      show (fromIntToNat 1) `shouldBe` show one
      show (fromIntToNat 2) `shouldBe` show two
      show (fromIntToNat 5) `shouldBe` show five

  describe "property" $
    prop "fromNatToInt ans fromIntToNat" $ \number -> do
      fromNatToInt (fromIntToNat number) `shouldBe` max 0 number

  describe "property" $
    prop "sum" $ \s1 s2 -> do
      fromNatToInt (sum (fromIntToNat s1) (fromIntToNat s2)) `shouldBe` max 0 s1 + max 0 s2

  describe "property" $
    prop "multiply" $ \s1 s2 -> do
      fromNatToInt (multiply (fromIntToNat s1) (fromIntToNat s2)) `shouldBe` max 0 s1 * max 0 s2

  describe "property" $
    prop "sub" $ \s1 s2 -> do
      fromNatToInt (sub (fromIntToNat s1) (fromIntToNat s2)) `shouldBe` max 0 (max 0 s1 - max 0 s2)

  describe "property" $
    prop "eq" $ \s1 s2 -> do
      eq (fromIntToNat s1) (fromIntToNat s2) `shouldBe` max 0 s1 == max 0 s2

  describe "property" $
    prop "more" $ \s1 s2 -> do
      more (fromIntToNat s1) (fromIntToNat s2) `shouldBe` max 0 s1 > max 0 s2

  describe "property" $
    prop "less" $ \s1 s2 -> do
      less (fromIntToNat s1) (fromIntToNat s2) `shouldBe` max 0 s1 < max 0 s2

  describe "property" $
    prop "div'" $ \s1 s2 -> do
      fromNatToInt (div' (fromIntToNat s1) (sum (fromIntToNat s2) one)) `shouldBe` div (max 0 s1) (max 0 s2 + 1)

  describe "property" $
    prop "mod'" $ \s1 s2 -> do
      fromNatToInt (mod' (fromIntToNat s1) (sum (fromIntToNat s2) one)) `shouldBe` mod (max 0 s1) (max 0 s2 + 1)

  describe "property" $
    prop "even'" $ \s -> do
      even' (fromIntToNat s) `shouldBe` even (max 0 s)