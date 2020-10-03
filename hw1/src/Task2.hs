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

data Nat = Z | S Nat
    deriving Show

sum :: Nat -> Nat -> Nat
sum Z y = y
sum (S x) y = sum x (S y)

multiply :: Nat -> Nat -> Nat
multiply Z _ = Z
multiply (S x) y = sum y (multiply x y)

sub :: Nat -> Nat -> Nat
sub Z _ = Z
sub x Z = x
sub (S x) (S y) = sub x y

eq :: Nat -> Nat -> Bool
eq Z Z = True
eq (S x) (S y) = eq x y
eq _ _ = False

more :: Nat -> Nat -> Bool
more (S _) Z = True
more (S x) (S y) = more x y
more _ _ = False

less :: Nat -> Nat -> Bool
less x y = not $ eq x y || more x y

even' :: Nat -> Bool
even' Z = True
even' (S x) = not (even' x)

mod' :: Nat -> Nat -> Nat
mod' x y
    | eq y Z    = undefined
    | otherwise = sub x $ multiply y $ div' x y

div' :: Nat -> Nat -> Nat
div' x y
    | eq y Z    = undefined
    | eq x y    = S Z
    | more x y  = S $ div' (sub x y) y
    | otherwise = Z

fromIntToNat :: Int -> Nat
fromIntToNat 0 = Z
fromIntToNat x
    | x > 0     = S (fromIntToNat (x - 1))
    | x == 0    = Z
    | otherwise = Z

fromNatToInt :: Nat -> Int
fromNatToInt x
    | eq x Z    = 0
    | otherwise = 1 + fromNatToInt (sub x (S Z))