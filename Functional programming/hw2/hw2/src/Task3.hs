{-# LANGUAGE InstanceSigs #-}
module Task3
  ( NonEmpty(..)
  ) where

-- | NonEmpty list
data NonEmpty a = a :| [a]
  deriving Show

instance Foldable NonEmpty where
  -- | Returns result of folding Tree by the function with start value
  foldr
    :: (a -> b -> b) -- ^ Folding function
    ->  b -- ^ Start value for folding function
    -> NonEmpty a -- ^ Given list
    -> b -- ^ Result of folding list by the function with start value
  foldr f k (a :| as) = f a (foldr f k as)

instance Functor NonEmpty where
  -- | Returns NonEmpty list from given list with applied function
  fmap 
    :: (a -> b) -- ^ Given function
    -> NonEmpty a -- ^ Given list with values
    -> NonEmpty b -- ^ New NonEmpty list
  fmap f (a :| as) = f a :| fmap f as

instance Applicative NonEmpty where
  -- | Returns NonEmpty list with one value
  pure 
    :: a -- ^ Given value
    -> NonEmpty a -- ^ NonEmpty list with value
  pure a = a :| []
  -- | Returns NonEmpty list from given list with applied function in list
  (<*>) 
    :: NonEmpty (a -> b) -- ^ Given list with functions
    -> NonEmpty a -- ^ Given list with values
    -> NonEmpty b -- ^ New NonEmpty list
  (<*>) (f :| fs) (x :| xs) = head list :| tail list where
    list = [f1 x1 | f1 <- f:fs, x1 <-  x:xs]

instance Traversable NonEmpty where
  -- | Returns new NonEmpty list in Applicative f by the function with given NonEmpty list
  traverse 
    :: Applicative f -- ^ f should be Applicative
    => (a -> f b) -- ^ Given function
    -> NonEmpty a -- ^ Given NonEmpty list
    -> f (NonEmpty b) -- ^ New NonEmpty list in Applicative f
  traverse f (x :| xs) = (:|) <$> f x <*> traverse f xs

instance Monad NonEmpty where
  -- | Returns result of binding
  (>>=) 
    :: NonEmpty a -- ^ Given NonEmpty list
    -> (a -> NonEmpty b) -- ^ Given function
    -> NonEmpty b -- ^ New NonEmpty list
  (>>=) (a :| []) f = f a
  (>>=) (a :| (b : bs)) f = nonEmptySum (f a) ((b :| bs) >>= f) where
    nonEmptySum (x :| xs) (y :| ys) = x :| (xs ++ [y] ++ ys)