{-# LANGUAGE InstanceSigs #-}
module Task2
  ( Tree(..)
  ) where
  
-- | Type of Binary Tree
data Tree a
  = Branch (Tree a) (Tree a) -- ^ Tree's branch
  | Leaf a -- ^ Tree's leaf with value
    deriving Show
  
instance Foldable Tree where
  -- | Returns result of folding Tree by the function with start value
  foldr 
    :: (a -> b -> b) -- ^ Folding function
    ->  b -- ^ Start value for folding function
    -> Tree a -- ^ Given Tree
    -> b -- ^ Result of folding Tree by the function with start value
  foldr f k (Leaf a)     = f a k
  foldr f k (Branch x y) = foldr f (foldr f k y) x
  
instance Functor Tree where
  -- | Returns new Tree by the function with given Tree
  fmap 
    :: (a -> b) -- ^ Fmap function
    -> Tree a -- ^ Given Tree
    -> Tree b -- ^ New tree
  fmap f (Leaf a)     = Leaf (f a)
  fmap f (Branch x y) = Branch (fmap f x) (fmap f y)
  
instance Applicative Tree where
  -- | Returns Leaf with start value
  pure 
    :: a -- ^ Given value
    -> Tree a -- ^ Leaf with value
  pure a = Leaf a
  (<*>) 
  -- | Returns new Tree by the function in Tree with given Tree
    :: Tree (a -> b) -- ^ Given Tree with function
    -> Tree a -- ^ Given Tree
    -> Tree b -- ^ New Tree
  (<*>) (Leaf f) (Leaf x)       = Leaf (f x)
  (<*>) l@(Leaf _) (Branch x y) = Branch (l <*> x) (l <*> y)
  (<*>) (Branch f1 f2) t        = Branch (f1 <*> t) (f2 <*> t)
  
instance Traversable Tree where
  -- | Returns new Tree in Applicative f by the function with given Tree
  traverse 
    :: Applicative f -- ^ f should be Applicative
    => (a -> f b) -- ^ Given function
    -> Tree a -- ^ Given Tree
    -> f (Tree b) -- ^ New Tree in Applicative f
  traverse f (Leaf a)     = pure <$> f a
  traverse f (Branch x y) = Branch <$> traverse f x <*> traverse f y