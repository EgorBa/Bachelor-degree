{-# LANGUAGE InstanceSigs #-}

module Task34
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
import Prelude hiding (length)

data Tree a = List | Tree (NonEmpty a) (Tree a) (Tree a)
    deriving Show

instance Foldable Tree where
    foldr :: (a -> b -> b) ->  b -> Tree a -> b
    foldr _ k List = k
    foldr f k (Tree a x y) = foldr f (foldr f (foldr f k y) a) x
    foldMap :: Monoid m => (a -> m) -> Tree a -> m
    foldMap _ List = mempty
    foldMap f (Tree a x y) = foldMap f x `mappend` foldMap f a `mappend` foldMap f y

isEmpty :: Tree a -> Bool
isEmpty List = True
isEmpty _ = False

count :: Tree a -> Int
count List = 0
count (Tree a x y) = length a + count x + count y

find :: Ord a => a -> Tree a -> Bool
find _ List = False
find k (Tree (a:|_) x y)
    | k == a    = True
    | k > a     = find k y
    | otherwise = find k x

add :: Ord a => a -> Tree a -> Tree a
add k List = Tree (k :| []) List List
add k (Tree (a:|as) x y)
    | k == a    = Tree (k:|(a : as)) x y
    | k > a     = Tree (a:|as) x (add k y)
    | otherwise = Tree (a:|as) (add k x) y

del :: Ord a => a -> Tree a -> Tree a
del _ List = List
del k (Tree aa@(a:|as) x y)
    | k == a && length aa == 1      = fromList (toList x ++ toList y)
    | k == a                        = Tree (head as :| tail as) x y
    | k > a                         = Tree (a:|as) x (del k y)
    | otherwise                     = Tree (a:|as) (del k x) y

toList :: Tree a -> [a]
toList = foldr (:) []

fromList :: Ord a => [a] -> Tree a
fromList = foldr add List