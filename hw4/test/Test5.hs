module Test5 where

import Test.Tasty (TestTree)
import Test.Tasty.Hspec (Spec, describe, it, shouldBe, shouldSatisfy, testSpec)

import Task4
import Task5

hspecTest5 :: IO TestTree
hspecTest5 = testSpec "Task5" spec

log2Text :: String
log2Text = "function f (a) {\n"
  ++ "\tvar x1 = 0;\n"
  ++ "\tvar x2 = 1;\n"
  ++ "\twhile ((a) > (x2)) {\n"
  ++ "\t\tx2 = (x2) + (x2);\n"
  ++ "\t\tx1 = (x1) + (1);\n"
  ++ "\t}\n"
  ++ "\treturn (x1);\n"
  ++ "}\n"

appendHaskellText :: String
appendHaskellText = "function f (a) {\n"
  ++ "\tvar x1 = \"\";\n"
  ++ "\tx1 = (a) ++ (\" Haskell!\");\n"
  ++ "\treturn (x1);\n"
  ++ "}\n"

multiplicityTwoText :: String
multiplicityTwoText = "function f (a) {\n"
  ++ "\tvar x1 = 0;\n"
  ++ "\tvar x2 = False;\n"
  ++ "\tx1 = (a) % (2);\n"
  ++ "\tx2 = (x1) == (0);\n"
  ++ "\treturn (x2);\n"
  ++ "}\n"

spec :: Spec
spec = do
  describe "log2" $
    it "count whole logarithm" $ do
      showCode log2 `shouldBe` log2Text
  describe "appendHaskell" $
    it "append \" Haskell!\" to string" $ do
      showCode appendHaskell `shouldBe` appendHaskellText
  describe "multiplicityTwo" $
    it "check multiplicity two" $ do
      showCode multiplicityTwo `shouldBe` multiplicityTwoText