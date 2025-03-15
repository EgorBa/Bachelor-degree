module Test8 where

import Test.Tasty (TestTree)
import Test.Tasty.Hspec (Spec, describe, it, shouldBe, shouldSatisfy, testSpec)

import Task8
import Task6

hspecTest8 :: IO TestTree
hspecTest8 = testSpec "Task8" spec

ok :: Maybe ((), [Char])
ok = Just ((),[])

neok :: Maybe ((), [Char])
neok = Nothing

spec :: Spec
spec = do
  describe "correctBracketSequenceParser" $
    it "check bracket sequence" $ do
      show (runParser correctBracketSequenceParser "()")                 `shouldBe` show ok
      show (runParser correctBracketSequenceParser "()()()()")           `shouldBe` show ok
      show (runParser correctBracketSequenceParser "(((())))")           `shouldBe` show ok
      show (runParser correctBracketSequenceParser "()(())()(())")       `shouldBe` show ok
      show (runParser correctBracketSequenceParser "(())()()((((()))))") `shouldBe` show ok
      show (runParser correctBracketSequenceParser "(")                  `shouldBe` show neok
      show (runParser correctBracketSequenceParser ")()()()")            `shouldBe` show neok
      show (runParser correctBracketSequenceParser "(((())")             `shouldBe` show neok
      show (runParser correctBracketSequenceParser "()()()(()")          `shouldBe` show neok

  describe "numberParser" $
    it "returns number" $ do
      show (runParser numberParser "15")       `shouldBe` show (Just (15,[]) :: Maybe (Int, [Char]))
      show (runParser numberParser "+1212")    `shouldBe` show (Just (1212,[]) :: Maybe (Int, [Char]))
      show (runParser numberParser "-200")     `shouldBe` show (Just (-200,[]) :: Maybe (Int, [Char]))
      show (runParser numberParser "1111")     `shouldBe` show (Just (1111,[]) :: Maybe (Int, [Char]))
      show (runParser numberParser "+-11")     `shouldBe` show (Nothing :: Maybe (Int, [Char]))
      show (runParser numberParser "--21")     `shouldBe` show (Nothing :: Maybe (Int, [Char]))
      show (runParser numberParser "++45")     `shouldBe` show (Nothing :: Maybe (Int, [Char]))
      show (runParser numberParser " 1111 ")   `shouldBe` show (Nothing :: Maybe (Int, [Char]))
      show (runParser numberParser "egor ")    `shouldBe` show (Nothing :: Maybe (Int, [Char]))
