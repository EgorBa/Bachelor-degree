module Task6
  ( maybeConcat
  , eitherConcat
  ) where

-- | Returns List of values from Maybe
maybeConcat 
  :: [Maybe [a]] -- ^ List of Maybe
  -> [a] -- ^ List of values from Maybe
maybeConcat a = maybeConcat1 a [] where
    maybeConcat1 [] ans = ans
    maybeConcat1 (Nothing:as) ans = maybeConcat1 as ans
    maybeConcat1 ((Just t):ts) ans = t ++ maybeConcat1 ts ans

-- | Return a pair from the results of a monoidal Union of separate 
-- elements inside Left and separate elements inside Right
eitherConcat 
  :: (Monoid m, Monoid n) -- ^ Left ans Right part of Either are Monoids
  => [Either m n] -- ^ List of Either
  -> (m, n) -- ^ Pair from the results of a monoidal Union
eitherConcat a = eitherConcat1 a (mempty, mempty) where
    eitherConcat1 [] ans = ans
    eitherConcat1 ((Left t):ts) (m, n) = eitherConcat1 ts (mappend m t, n)
    eitherConcat1 ((Right t):ts) (m, n) = eitherConcat1 ts (m, mappend n t)