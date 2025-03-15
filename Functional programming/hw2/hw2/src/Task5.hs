module Task5
  ( moving
  ) where

import Control.Monad.State

-- | Returns moving average
moving 
  :: Fractional a -- ^ a should be Fractional
  => Int -- ^ Size of window
  -> [a] -- ^ Given list
  -> [a] -- ^ List of moving average
moving a s 
  |a > 0     = evalState (avgList s) (a, [])
  |otherwise = []
  
-- | Returns monad State with moving average 
avgList 
  :: Fractional a -- ^ a should be Fractional
  => [a] -- ^ Given list
  -> State (Int, [a]) [a] -- ^ Monad State with moving average 
avgList []     = return []
avgList (x:xs) = do
    (a, s) <- get
    if a > 0
    then put (a - 1, s ++ [x])
    else put (a, tail s ++ [x])
    (_, s1) <- get
    (sum s1 / fromIntegral (length s1) :) <$> avgList xs
