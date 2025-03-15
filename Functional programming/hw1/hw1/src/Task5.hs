module Task5
  ( splitOn
  , joinWith
  ) where

import Data.List.NonEmpty hiding (init)

-- | Returns splitted given String by given Char as NonEmpty List of Strings
splitOn 
  :: Char -- ^ Given Char
  -> String -- ^ Given String
  -> NonEmpty String -- ^ NonEmpty List of splitted given String
splitOn chr = foldr (splitOn' chr) ("" :| []) where
  splitOn' c x (h:|hs)
      | x == c    = "" :| (h : hs)
      | otherwise = (x : h) :| hs

-- | Returns String is assembled from List of Strings and a separator between them
joinWith 
  :: Char -- ^ Given separator
   -> NonEmpty String -- ^ Given NonEmpty List of Strings
   -> String -- ^ String is assembled from List of Strings and a separator between them
joinWith chr str = init $ foldr (joinWith' chr) [] str where
  joinWith' c str1 str2 = str1 ++ [c] ++ str2