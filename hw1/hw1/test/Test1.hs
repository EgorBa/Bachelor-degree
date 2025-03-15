module Test1 where

import Test.Tasty (TestTree)
import Test.Tasty.Hspec (Spec, describe, it, shouldBe, shouldSatisfy, testSpec)

import Task1

hspecTest1 :: IO TestTree
hspecTest1 = testSpec "Task1" spec

spec :: Spec
spec = do
  describe "nextDay" $
    it "returns next day" $ do
      show (nextDay Sunday)    `shouldBe` show Monday
      show (nextDay Monday)    `shouldBe` show Tuesday
      show (nextDay Tuesday)   `shouldBe` show Wednesday
      show (nextDay Wednesday) `shouldBe` show Thursday
      show (nextDay Thursday)  `shouldBe` show Friday
      show (nextDay Friday)    `shouldBe` show Saturday
      show (nextDay Saturday)  `shouldBe` show Sunday

  describe "afterDays" $
    it "returns day after given count of days and current day" $ do
      show (afterDays 0 Monday)     `shouldBe` show Monday
      show (afterDays 7 Monday)     `shouldBe` show Monday
      show (afterDays 14 Monday)    `shouldBe` show Monday
      show (afterDays 5 Wednesday)  `shouldBe` show Monday

  describe "isWeekend" $
    it "returns true if given day is weekend" $ do
      isWeekend Monday    `shouldBe` False
      isWeekend Tuesday   `shouldBe` False
      isWeekend Wednesday `shouldBe` False
      isWeekend Thursday  `shouldBe` False
      isWeekend Friday    `shouldBe` False
      isWeekend Saturday  `shouldBe` True
      isWeekend Sunday    `shouldBe` True

  describe "daysToParty" $
    it "returns count of days to Friday" $ do
      daysToParty Monday    `shouldBe` 4
      daysToParty Tuesday   `shouldBe` 3
      daysToParty Wednesday `shouldBe` 2
      daysToParty Thursday  `shouldBe` 1
      daysToParty Friday    `shouldBe` 0
      daysToParty Saturday  `shouldBe` 6
      daysToParty Sunday    `shouldBe` 5