module Task5
  ( splitOn
  , joinWith
  ) where

import Data.List.NonEmpty

splitOn :: Char -> String -> NonEmpty String
splitOn chr str = foldr (func chr) ("":|[]) str
func :: Char -> Char -> NonEmpty String -> NonEmpty String
func chr x (h:|hs)
    | x == chr  = ("":|([h] ++ hs))
    | otherwise = ((x:h):|hs)

joinWith :: Char -> NonEmpty String -> String
joinWith chr str = foldr (f chr) [] str
f :: Char -> String -> String -> String
f _ str1 [] = str1
f chr str1 str2 = str1 ++ [chr] ++ str2