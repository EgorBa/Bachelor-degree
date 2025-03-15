{-# LANGUAGE FlexibleContexts  #-}
{-# LANGUAGE FlexibleInstances #-}
{-# LANGUAGE TypeFamilies      #-}

module Task4
  ( HalyavaScript (..)
  , log2
  , runLog2
  , appendHaskell
  , runAppendHaskell
  , multiplicityTwo
  , runMultiplicityTwo
  ) where

import Control.Monad.ST (ST, runST)
import Control.Monad (join)
import Data.STRef (STRef, newSTRef, readSTRef, writeSTRef)

-- | Class HSValue is value of HalyavaScript
class (Ord v, Show v) => HSValue v

instance HSValue Int
instance HSValue Double
instance HSValue Bool
instance HSValue String

-- | Class HalyavaScript is a tagelss final embedded domain-specific language
class HalyavaScript s where
  type Value s :: * -> *

  var :: HSValue a => a -> s (Value s a)

  toVar :: HSValue a => a -> s a

  eRead :: HSValue a => Value s a -> s a

  sWithVar :: HSValue a => s (Value s a) -> (Value s a -> s b) -> s b

  sFun1 :: (s a -> s b) -> s (a -> s b)

  sWhile :: s Bool -> s () -> s ()

  sIf :: s Bool -> s () -> s () -> s ()

  infixl 1 #
  (#) :: s a -> s b -> s b

  infixr 4 @=
  (@=) :: HSValue a => Value s a -> s a -> s ()

  infixr 4 @==
  (@==) :: HSValue a => s a -> s a -> s Bool

  infixr 4 @!=
  (@!=) :: HSValue a => s a -> s a -> s Bool

  infixr 4 @>
  (@>) :: HSValue a => s a -> s a -> s Bool

  infixr 4 @>=
  (@>=) :: HSValue a => s a -> s a -> s Bool

  infixr 4 @<
  (@<) :: HSValue a => s a -> s a -> s Bool

  infixr 4 @<=
  (@<=) :: HSValue a => s a -> s a -> s Bool

  infixr 5 @%
  (@%) :: (Integral a, HSValue a) => s a -> s a -> s a

  infixr 6 @+
  (@+) :: (Num a, HSValue a) => s a -> s a -> s a

  infixr 6 @-
  (@-) :: (Num a, HSValue a) => s a -> s a -> s a

  infixr 7 @*
  (@*) :: (Num a, HSValue a) => s a -> s a -> s a

  infixr 5 @++
  (@++) :: (HSValue [a]) => s [a] -> s [a] -> s [a]

instance HalyavaScript (ST s) where
  type Value (ST s) = STRef s

  var        = newSTRef
  toVar      = return
  eRead      = readSTRef
  sWithVar   = (>>=)
  sFun1 f    = return (f . pure)
  (#)        = (>>)
  (@=) v1 v2 = v2 >>= (writeSTRef v1)

  sIf cond b1 b2 = do
    cond' <- cond
    if cond'
      then b1
      else b2

  sWhile cond body = do
    cond' <- cond
    if cond'
      then body >> sWhile cond body
      else return ()

  (@==)  = realizeOp (==)
  (@!=)  = realizeOp (/=)
  (@>)   = realizeOp (>)
  (@>=)  = realizeOp (>=)
  (@<)   = realizeOp (<)
  (@<=)  = realizeOp (<=)
  (@%)   = realizeOp (mod)
  (@+)   = realizeOp (+)
  (@-)   = realizeOp (-)
  (@*)   = realizeOp (*)
  (@++)  = realizeOp (++)

-- | Realize operation
realizeOp
  :: Monad m -- ^ m should be Monad
  => (t1 -> t2 -> b) -- ^ Operation
  -> m t1 -- ^ First argument
  -> m t2 -- ^ Second argument
  -> m b -- ^ Result os realization operation
realizeOp f x1 x2 = do
  x1' <- x1
  x2' <- x2
  return (f x1' x2')

-- | Function for count logarithm base 2 rounded up
log2
  :: HalyavaScript a -- ^ a should be HalyavaScript
  => a (Int -> a Int) -- ^ Function for count logarithm base 2 rounded up
log2 = sFun1 $ \a ->
  sWithVar (var (0 :: Int)) $ \logCnt ->
    sWithVar (var (1 :: Int)) $ \accum ->
      sWhile (a @> eRead accum)
        ( accum @= eRead accum @+ eRead accum #
          logCnt @= eRead logCnt @+ (toVar 1)
        ) #
      eRead logCnt

-- | Count logarithm base 2 rounded up
runLog2 
  :: Int -- ^ Given number
  -> Int -- ^ Result
runLog2 a = runST (join $ log2 <*> toVar a)

-- | Function for append str " Haskell!"
appendHaskell
  :: HalyavaScript a -- ^ a should be HalyavaScript
  => a (String -> a String) -- ^ Function for append str " Haskell!"
appendHaskell = sFun1 $ \a ->
  sWithVar (var ("" :: String)) $ \str ->
    str @= a @++ (toVar " Haskell!") #
    eRead str

-- | Append to String str " Haskell!"
runAppendHaskell
  :: String -- ^ Given String
  -> String -- ^ Result
runAppendHaskell a = runST (join $ appendHaskell <*> toVar a)

-- | Function for checking multiplicity 2
multiplicityTwo
  :: HalyavaScript a -- ^ a should be HalyavaScript
  => a (Int -> a Bool) -- ^ Function for checking multiplicity 2
multiplicityTwo= sFun1 $ \a ->
  sWithVar (var (0 :: Int)) $ \m ->
    sWithVar (var (False :: Bool)) $ \flag ->
      m @= a @% (toVar 2) #
      flag @= eRead m @== toVar 0 #
      eRead flag
      
-- | Check multiplicity 2
runMultiplicityTwo
  :: Int -- ^ Given Number
  -> Bool -- ^ Result
runMultiplicityTwo a = runST (join $ multiplicityTwo <*> toVar a)      
      
-- Example how to launch HalyavaScript code : runST (join $ \your function\ <*> toVar \argument of function\)