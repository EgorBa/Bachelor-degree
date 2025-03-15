{-# LANGUAGE InstanceSigs #-}
module Task10
  ( Cont(..)
  ) where

-- | Cont
newtype Cont r a = Cont { runCont :: (a -> r) -> r }

instance Functor (Cont r) where
  -- | Returns new Cont by the function with given Cont
  fmap 
    :: (a -> b) -- ^ Fmap function
    -> Cont r a -- ^ Given Cont
    -> Cont r b -- ^ New Cont
  fmap f cont = Cont $ \x -> runCont cont (x . f)

instance Applicative (Cont r) where
  -- | Returns Cont with start value
  pure 
    :: a -- ^ Given value
    -> Cont r a -- ^ Cont with value
  pure a = Cont ($ a)
  -- | Returns new Cont by the function in Cont with given Cont
  (<*>) 
    :: Cont r (a -> b) -- ^ Given Cont with function
    -> Cont r a -- ^ Given Cont
    -> Cont r b -- ^ New Cont
  (<*>) cont1 cont2 = Cont $ \x -> runCont cont1 (\y -> runCont cont2 (x . y))

instance Monad (Cont r) where
  -- | Returns result of binding
  (>>=) 
    :: Cont r a -- ^ Given Cont
    -> (a -> Cont r b) -- ^ Given function
    -> Cont r b -- ^ New Cont
  (>>=) cont f = Cont $ \x -> runCont cont (\a -> runCont (f a) x)