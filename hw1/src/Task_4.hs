{-# LANGUAGE InstanceSigs #-}

module Task_4
  ( Tree(..)
  ) where

import Data.List.NonEmpty

data Tree a = List | Tree (NonEmpty a) (Tree a) (Tree a)
    deriving Show

instance Foldable Tree where
    foldr :: (a -> b -> b) ->  b -> Tree a -> b
    foldr _ k List = k
    foldr f k (Tree a x y) = foldr f (foldr f (foldr f k y) a) x
    foldMap :: Monoid m => (a -> m) -> Tree a -> m
    foldMap _ List = mempty
    foldMap f (Tree a x y) = foldMap f x `mappend` foldMap f a `mappend` foldMap f y