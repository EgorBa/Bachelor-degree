module Task6
  ( maybeConcat
  , eitherConcat
  ) where

maybeConcat :: [Maybe [a]] -> [a]
maybeConcat a = maybeConcat1 a [] where
    maybeConcat1 [] ans = ans
    maybeConcat1 (Nothing:as) ans = maybeConcat1 as ans
    maybeConcat1 ((Just t):ts) ans = t ++ (maybeConcat1 ts ans)

eitherConcat :: (Monoid m, Monoid n) => [Either m n] -> (m, n)
eitherConcat a = eitherConcat1 a (mempty, mempty) where
    eitherConcat1 [] ans = ans
    eitherConcat1 ((Left t):ts) (m, n) = eitherConcat1 ts ((mappend m t), n)
    eitherConcat1 ((Right t):ts) (m, n) = eitherConcat1 ts (m, (mappend n t))