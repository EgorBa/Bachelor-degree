{-# LANGUAGE TypeFamilies   #-}

module Task5
  ( CodeShow (..)
  , showCode
  ) where

import Data.Functor.Const (Const (..))
import Task4 (HalyavaScript (..))

-- | Type for showing content of functions
newtype CodeShow a = CodeShow { code :: Int -> String }

instance HalyavaScript CodeShow where
  type Value CodeShow = Const String

  var        = CodeShow . const . show
  toVar      = CodeShow . const . show
  eRead      = CodeShow . const . getConst
  (@=) v1 v2 = CodeShow $ \x -> getConst v1 ++ " = " ++ code v2 x ++ ";"
  (#) v1 v2  = CodeShow $ \x -> code v1 x ++ "\n" ++ code v2 x

  sWithVar value body = CodeShow $ \x ->
    "var x"
    ++ show x
    ++ " = "
    ++ code value x
    ++ ";\n"
    ++ code (body $ Const ("x" ++ show x)) (x + 1)

  sFun1 f = CodeShow $ \x ->
    "function f (a) {\n"
    ++ addTabs (addReturn x $ f $ CodeShow $ const "a")
    ++ "}\n"

  sIf cond b1 b2 = CodeShow $ \x ->
    "if ("
    ++ code cond x
    ++ ") {\n"
    ++ addTabs (code b1 x)
    ++ "} else {\n"
    ++ addTabs (code b2 x)
    ++ "}"

  sWhile cond body = CodeShow $ \x ->
    "while ("
    ++ code cond x
    ++ ") {\n"
    ++ addTabs (code body x)
    ++ "}"

  (@==) = showOp "=="
  (@!=) = showOp "/="
  (@>)  = showOp ">"
  (@<)  = showOp "<"
  (@>=) = showOp ">="
  (@<=) = showOp "<="
  (@%)  = showOp "%"
  (@+)  = showOp "+"
  (@-)  = showOp "-"
  (@*)  = showOp "*"
  (@++) = showOp "++"

-- | Shows content of CodeShow
showCode 
  :: CodeShow a -- ^ Given CodeShow
  -> String -- ^ Content of CodeShow
showCode a = code a 0

-- | Create new CodeShow with operation
showOp 
  :: String -- ^ Given operation
  -> CodeShow a -- ^ First CodeShow
  -> CodeShow b -- ^ Second CodeShow
  -> CodeShow c -- ^ New CodeShow
showOp op a b = CodeShow $ \x -> "(" ++ code a x ++ ") " ++ op ++ " (" ++ code b x ++ ")"

-- | Add tabs to str
addTabs 
  :: String -- ^ Given str
  -> String -- ^ New str
addTabs str = unlines $ map ("\t" ++) (lines str)

-- | Add "return" to CodeShow
addReturn 
  :: Int -- ^ Field of CodeShow
  -> CodeShow a -- ^ Given CodeShow
  -> String -- ^ Content of CodeShow with add "return"
addReturn n a = unlines $ init codeLines ++ ["return (" ++ last codeLines ++ ");"]
  where
    codeLines = lines $ code a (n + 1)
    
-- Example how to convert HalyavaScript code to JS : showCode \your function\