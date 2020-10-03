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

-- | Type of Binary Tree with empty leaf and nonempty node
data Tree a 
  = Leaf -- ^ Leaf without anything
  | Tree (NonEmpty a) (Tree a) (Tree a) -- ^ Tree with nonempty list of values and two children
  deriving Show

instance Foldable Tree where
  -- | Returns result of folding Tree by the function with start value
  foldr 
    :: (a -> b -> b) -- ^ Folding function
    ->  b -- ^ Start value for folding function
    -> Tree a -- ^ Given Tree
    -> b -- ^ Result of folding Tree by the function with start value
  foldr _ k Leaf = k
  foldr f k (Tree a x y) = foldr f (foldr f (foldr f k y) a) x
  -- | Returns Monoid as a result of applying the function to the Trees' values
  foldMap 
    :: Monoid m -- ^ Function should returns Monoid
    => (a -> m) -- ^ Mapping function
    -> Tree a -- ^ Given Tree
    -> m -- ^ Resulting monoid as a result of applying the function to the Trees' values
  foldMap _ Leaf = mempty
  foldMap f (Tree a x y) = foldMap f x `mappend` foldMap f a `mappend` foldMap f y

-- | Returns True if given Tree is empty
isEmpty 
  :: Tree a -- ^ Given tree
  -> Bool -- ^ True if given Tree is empty, else False
isEmpty Leaf = True
isEmpty _ = False

-- | Returns count of values in given Tree
count 
  :: Tree a -- ^ Given tree
  -> Int -- ^ Count of values in given tree
count Leaf = 0
count (Tree a x y) = length a + count x + count y

-- | Returns True if given Tree contains given number
find 
  :: Ord a -- ^ Values can be compared
  => a -- ^ Given value
  -> Tree a -- ^ Given Tree
  -> Bool -- ^ True if given Tree contains given number, ele False
find _ Leaf = False
find k (Tree (a:|_) x y)
    | k == a    = True
    | k > a     = find k y
    | otherwise = find k x

-- | Returns given Tree with added given value
add 
  :: Ord a -- ^ Values can be compared
  => a -- ^ Given value
  -> Tree a -- ^ Given Tree
  -> Tree a -- ^ Given Tree with added given value
add k Leaf = Tree (k :| []) Leaf Leaf
add k (Tree (a:|as) x y)
    | k == a    = Tree (k:|(a : as)) x y
    | k > a     = Tree (a:|as) x (add k y)
    | otherwise = Tree (a:|as) (add k x) y

-- | Returns given Tree without given value
-- if tree contains this value, else just given Tree
del 
  :: Ord a -- ^ Values can be compared
  => a -- | Given value
  -> Tree a -- | Given Tree
  -> Tree a -- | Given Tree without given value
del _ Leaf = Leaf
del k (Tree aa@(a:|as) x y)
    | k == a && length aa == 1      = fromList (toList x ++ toList y)
    | k == a                        = Tree (head as :| tail as) x y
    | k > a                         = Tree (a:|as) x (del k y)
    | otherwise                     = Tree (a:|as) (del k x) y

-- | Returns List from values from given tree
toList 
  :: Tree a -- ^ Given tree
  -> [a] -- ^ List from values from given tree
toList = foldr (:) []

-- | Returns Tree from values of given List
fromList 
  :: Ord a -- ^ Values can be compared
  => [a] -- ^ Given List
  -> Tree a -- ^ Tree from values of given List
fromList = foldr add Leaf