module Test7 where

import Test.Tasty (TestTree)
import Test.Tasty.Hspec (Spec, describe, it, shouldBe, shouldSatisfy, testSpec)
import Task7

hspecTest7 :: IO TestTree
hspecTest7 = testSpec "Task7" spec

spec :: Spec
spec = do
  describe "Semigroup for NonEmpty" $
    it "returns NonEmpty" $ do
      show ((1 :| [3]) <> (1 :| [3])) `shouldBe` show (1 :| [3,1,3])
      show ((1 :| []) <> (1 :| []))   `shouldBe` show (1 :| [1])
      show ((1 :| [3]) <> (1 :| []))  `shouldBe` show (1 :| [3,1])
      show ((1 :| []) <> (1 :| [3]))  `shouldBe` show (1 :| [1,3])

  describe "Semigroup for ThisOrThat" $
    it "returns TisOrThat" $ do
      show (This 5 <> That 5)    `shouldBe` show (Both 5 5)
      show (This 5 <> Both 4 5)  `shouldBe` show (Both 5 5)
      show (Both 5 5 <> That 4)  `shouldBe` show (Both 5 5)
      show (Both 5 5 <> This 4)  `shouldBe` show (Both 5 5)
      show (That 5 <> Both 5 4)  `shouldBe` show (Both 5 5)
      show (That 5 <> This 5)    `shouldBe` show (Both 5 5)

  describe "Monoid and Semigroup for Name" $
    it "returns Name" $ do
      show ((Name "lol") <> (Name "kek"))        `shouldBe` show (Name "lol.kek")
      show ((Name "lol") <> (Name ""))           `shouldBe` show (Name "lol.")
      show ((Name "") <> (Name "lol"))           `shouldBe` show (Name ".lol")
      show ((Name "lol") `mappend` (Name "kek")) `shouldBe` show (Name "lol.kek")
      show (mempty `mappend` (Name "lol"))       `shouldBe` show (Name "lol")
      show ((Name "lol") `mappend` mempty)       `shouldBe` show (Name "lol")

  describe "Monoid and Semigroup for Endo" $
    it "returns Endo" $ do
      getEndo mempty 5                                `shouldBe` 5
      getEndo ((Endo (+ 5)) <> mempty) 5              `shouldBe` 10
      getEndo ((Endo (+ 5)) `mappend` mempty) 5       `shouldBe` 10
      getEndo (mempty `mappend` (Endo (+ 5))) 5       `shouldBe` 10
      getEndo ((Endo (+ 5)) <> (Endo (+ 5))) 5        `shouldBe` 15
      getEndo ((Endo (+ 5)) `mappend` (Endo (+ 5))) 5 `shouldBe` 15