module Task3
  ( Tree(..)
  , isEmpty
  , count
  , find
  , add
  , fromList
  , del
  , toList
  ) where

import Data.List.NonEmpty hiding (toList, fromList, tail, head)
import Prelude

data Tree a = List | Tree (NonEmpty a) (Tree a) (Tree a)
    deriving Show

isEmpty :: Tree a -> Bool
isEmpty List = True
isEmpty _ = False

count :: Tree a -> Int
count List = 0
count (Tree a x y) = (Data.List.NonEmpty.length a) + (count x) + (count y)

find :: Ord a => a -> Tree a -> Bool
find _ List = False
find k (Tree (a:|_) x y)
    | k == a    = True
    | k > a     = find k y
    | otherwise = find k x

add :: Ord a => a -> Tree a -> Tree a
add k List = (Tree (k:|[]) List List)
add k (Tree (a:|as) x y)
    | k == a    = Tree (k:|([a]++as)) x y
    | k > a     = Tree (a:|as) x (add k y)
    | otherwise = Tree (a:|as) (add k x) y

fromList :: Ord a => [a] -> Tree a
fromList x = fromList1 x List where
    fromList1 (a:as) t = fromList1 as (add a t)
    fromList1 [] t = t

del :: Ord a => a -> Tree a -> Tree a
del _ List = List
del k (Tree (a:|as) x y)
    | k == a && (Prelude.length as) == 0    = fromList ((toList x) ++ (toList y))
    | k == a                        = Tree ((head as):|(tail as)) x y
    | k > a                         = Tree (a:|as) x (del k y)
    | otherwise                     = Tree (a:|as) (del k x) y

toList :: Tree a -> [a]
toList (Tree (a:|as) x y) = (toList x) ++ [a] ++ as ++ (toList y)
toList List = []