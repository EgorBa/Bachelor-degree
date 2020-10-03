module Task2
  ( Nat(..)
  , sum
  , multiply
  , sub
  , eq
  , more
  , less
  , div'
  , even'
  , mod'
  , fromIntToNat
  , fromNatToInt
  ) where

import Prelude hiding (sum)

-- | Type of natural numbers
data Nat = Z | S Nat
    deriving Show

-- | Returns sum of natural numbers
sum
  :: Nat -- ^ First natural number
  -> Nat -- ^ Second natural number
  -> Nat -- ^ Sum of given numbers
sum Z y = y
sum (S x) y = sum x (S y)

-- | Returns multiply of given numbers
multiply
  :: Nat -- ^ First natural number
  -> Nat -- ^ Second natural number
  -> Nat -- ^ Multiply of given numbers
multiply Z _ = Z
multiply (S x) y = sum y (multiply x y)

-- | Returns subtraction of given numbers
sub
  :: Nat -- ^ First natural number
  -> Nat -- ^ Second natural number
  -> Nat -- ^ Subtraction of given numbers
sub Z _ = Z
sub x Z = x
sub (S x) (S y) = sub x y

-- | Returns True if number are equal, else False
eq
  :: Nat -- ^ First natural number
  -> Nat -- ^ Second natural number
  -> Bool -- ^ True if number are equal, else False
eq Z Z = True
eq (S x) (S y) = eq x y
eq _ _ = False

-- | Returns True if first number's more then second, else False
more
  :: Nat -- ^ First natural number
  -> Nat -- ^ Second natural number
  -> Bool -- ^ True if first number's more then second, else False
more (S _) Z = True
more (S x) (S y) = more x y
more _ _ = False

-- | Returns True if first number's less then second, else False
less
  :: Nat -- ^ First natural number
  -> Nat -- ^ Second natural number
  -> Bool -- ^ True if first number's less then second, else False
less x y = not $ eq x y || more x y

-- | Returns True if number is even, else False
even'
  :: Nat -- ^ Given natural number
  -> Bool -- ^ True if number is even, else False
even' Z = True
even' (S x) = not (even' x)

-- | Returns module of given numbers
mod'
  :: Nat -- ^ First natural number
  -> Nat -- ^ Second natural number
  -> Nat -- ^ Module of given numbers
mod' x y
    | eq y Z    = undefined
    | otherwise = sub x $ multiply y $ div' x y

-- | Returns integer division of given numbers
div'
  :: Nat -- ^ First natural number
  -> Nat -- ^ Second natural number
  -> Nat -- ^ Integer division of given numbers
div' x y
    | eq y Z    = undefined
    | eq x y    = S Z
    | more x y  = S $ div' (sub x y) y
    | otherwise = Z

-- | Returns natural number of given Int
fromIntToNat
  :: Int -- ^ Given Int number
  -> Nat -- ^ Natural number of given Int
fromIntToNat 0 = Z
fromIntToNat x
    | x > 0     = S (fromIntToNat (x - 1))
    | x == 0    = Z
    | otherwise = Z

-- | Returns Int of given natural number
fromNatToInt
  :: Nat -- ^ Given natural nunber
  -> Int -- ^ Int of given natural number
fromNatToInt x
    | eq x Z    = 0
    | otherwise = 1 + fromNatToInt (sub x (S Z))