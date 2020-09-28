module Task7
  ( NonEmpty(..)
  , ThisOrThat(..)
  , Name(..)
  , Endo(..)
  ) where

data NonEmpty a = a :| [a]
    deriving Show

instance Semigroup (NonEmpty a) where
    (<>) (a :| as) (b :| bs) = (a :| (as ++ [b] ++ bs))

data ThisOrThat a b = This a | That b | Both a b
    deriving Show

instance Semigroup (ThisOrThat a b) where
    (<>) (This a) (This _) = This a
    (<>) (That b) (That _) = That b
    (<>) (Both a b) _ = Both a b
    (<>) (This a) (That b) = Both a b
    (<>) (This a) (Both _ b) = Both a b
    (<>) (That b) (This a) = Both a b
    (<>) (That b) (Both a _) = Both a b

newtype Name = Name String
    deriving Show

instance Semigroup Name where
    (<>) (Name a) (Name b) = Name (a ++ "." ++ b)

instance Monoid Name where
    mempty = Name ""
    mappend (Name a) (Name b)
        |((length a) == 0) && ((length b) == 0) = mempty
        |(length a) == 0                        = Name b
        |(length b) == 0                        = Name a
        |otherwise                              = Name (a ++ "." ++ b)

newtype Endo a = Endo { getEndo :: a -> a }

instance  Semigroup (Endo a) where
    (<>) (Endo f) (Endo g) = Endo (f . g)

instance  Monoid (Endo a) where
    mempty = (Endo id)
    mappend (Endo f) (Endo g) = Endo (f . g)