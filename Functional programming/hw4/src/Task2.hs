module Task2
  ( integrate
  , integrateConcurrently
  ) where

import System.Random (randomRIO)
import System.Unsafe (performIO)
import Control.Monad.Par (runPar, parMap)

-- | Count of iteration
n :: Double
n = 1048576.0 -- 2 ^ 20

-- | Considers a definite integral
integrate 
  :: Double -- ^ Bottom bound
  -> Double -- ^ Upper bound
  -> (Double -> Double) -- ^ Function
  -> Double -- ^ Integral of function
integrate a b f = integrate' a b f n 0
  where
    integrate' a' b' _ 0 ans  = ((b' - a') / n) * ans
    integrate' a' b' f' c ans = integrate' a' b' f' (c - 1) $ ans + (f' $ performIO $ randomRIO (a', b'))

-- | Considers a definite integral parallel
integrateConcurrently 
  :: Double -- ^ Bottom bound
  -> Double -- ^ Upper bound
  -> (Double -> Double) -- ^ Function
  -> Double -- ^ Integral of function
integrateConcurrently a b f = ((b - a) / n) * integrate' a b f n
  where
    integrate' a' b' f' c
      | c <= 1    = f' $ performIO $ randomRIO (a', b')
      | otherwise = sum $ runPar $ parMap (integrate' a' b' f') (take 4 (repeat (c / 4)))