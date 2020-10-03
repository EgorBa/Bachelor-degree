module Task5
  ( splitOn
  , joinWith
  ) where

import Data.List.NonEmpty hiding (init)

splitOn :: Char -> String -> NonEmpty String
splitOn chr = foldr (func chr) ("" :| [])
func :: Char -> Char -> NonEmpty String -> NonEmpty String
func chr x (h:|hs)
    | x == chr  = "" :| (h : hs)
    | otherwise = (x : h) :| hs

joinWith :: Char -> NonEmpty String -> String
joinWith chr str = init $ foldr (f chr) [] str
f :: Char -> String -> String -> String
f chr str1 str2 = str1 ++ [chr] ++ str2