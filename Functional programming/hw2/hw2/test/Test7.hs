module Test7 where

import Test.Tasty (TestTree)
import Test.Tasty.Hspec (Spec, describe, it, shouldBe, shouldSatisfy, testSpec)

import Task7
import Task6
import Data.Char

hspecTest7 :: IO TestTree
hspecTest7 = testSpec "Task7" spec

spec :: Spec
spec = do
  describe "ok" $
    it "returns parser that never crashes and does not absorb the input" $ do
      show (runParser ok [1])   `shouldBe` show (Just ((),[1]))
      show (runParser ok ['a']) `shouldBe` show (Just ((),['a']))

  describe "eof" $
    it "checks that the parser has reached the end of the data stream" $ do
      show (runParser eof ['a']) `shouldBe` show (Nothing :: Maybe (Char, [Char]))
      show (runParser eof "")    `shouldBe` show (Just ((),""))

  describe "satisfy" $
    it "returns parser that returns the element, absorbing it from the stream" $ do
      show (runParser (satisfy isDigit) ['1']) `shouldBe` show (Just ('1',[]) :: Maybe (Char, [Char]))
      show (runParser (satisfy (== 1)) [1])    `shouldBe` show (Just (1,[]) :: Maybe (Int, [Int]))
      show (runParser (satisfy (== 2)) [1])    `shouldBe` show (Nothing :: Maybe (Int, [Int]))

  describe "element" $
      it "returns parser that parse one element" $ do
        show (runParser (element '1') ['1'])      `shouldBe` show (Just ('1',[]) :: Maybe (Char, [Char]))
        show (runParser (element '1') ['1', '2']) `shouldBe` show (Just ('1',['2']) :: Maybe (Char, [Char]))
        show (runParser (element '2') ['1'])      `shouldBe` show (Nothing :: Maybe (Char, [Char]))

  describe "stream" $
      it "returns parser that parse stream of elements" $ do
        show (runParser (stream "1") ['1'])      `shouldBe` show (Just ("1",[]) :: Maybe (String, String))
        show (runParser (stream "1") "112")      `shouldBe` show (Just ("1","12") :: Maybe (String, String))
        show (runParser (stream "12") "1212")    `shouldBe` show (Just ("12","12") :: Maybe (String, String))
        show (runParser (stream "212") "1212")   `shouldBe` show (Nothing :: Maybe (String, String))