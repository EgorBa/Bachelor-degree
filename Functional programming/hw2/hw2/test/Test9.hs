module Test9 where

import Test.Tasty (TestTree)
import Test.Tasty.Hspec (Spec, describe, it, shouldBe, shouldSatisfy, testSpec)

import Task9
import Task6
import Data.Char

hspecTest9 :: IO TestTree
hspecTest9 = testSpec "Task9" spec

spec :: Spec
spec = do
  describe "listlistParser" $
    it "returns list of lists of numbers" $ do
      show (runParser listlistParser "2, 1,+10  ")                   `shouldBe` show (Just ([[1, 10]], "") :: Maybe ([[Int]], String))
      show (runParser listlistParser "2, 1,+10  , 3,5,-7, 2")        `shouldBe` show (Just ([[1, 10], [5, -7, 2]], "") :: Maybe ([[Int]], String))
      show (runParser listlistParser "  2, 1,  +10  , 3,5,-7, 2   ") `shouldBe` show (Just ([[1, 10], [5, -7, 2]], "") :: Maybe ([[Int]], String))
      show (runParser listlistParser " -2, 1,  +10  , 3,5,-7, 2   ") `shouldBe` show (Nothing :: Maybe ([[Int]], String))
      show (runParser listlistParser "2, 1,+10  , 3,5,-7, 2rt")      `shouldBe` show (Nothing :: Maybe ([[Int]], String))
      show (runParser listlistParser "2, 1,+10  , 3,5,-7  ")         `shouldBe` show (Nothing :: Maybe ([[Int]], String))