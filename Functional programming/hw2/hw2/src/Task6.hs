{-# LANGUAGE InstanceSigs #-}
module Task6
  ( Parser(..)
  ) where
  
import Control.Applicative 
import Data.Bifunctor (first)

-- | Parser
newtype Parser s a = Parser {runParser :: [s] -> Maybe (a, [s])}

instance Functor (Parser s) where
  -- | Returns new Parser by the function with given Parser
  fmap 
    :: (a -> b) -- ^ Fmap function
    -> Parser s a -- ^ Given Parser
    -> Parser s b -- ^ New Parser
  fmap f parser = Parser (fmap (first f) . runParser parser)

instance Applicative (Parser s) where
  -- | Returns Parser with start value
  pure 
    :: a -- ^ Given value
    -> Parser s a -- ^ Parser with value
  pure a = Parser (\x -> Just (a, x))
  -- | Returns new Parser by the function in parser with given Parser
  (<*>) 
    :: Parser s (a -> b) -- ^ Given Parser with function
    -> Parser s a -- ^ Given Parser
    -> Parser s b -- ^ New Parser
  (<*>) p1 p2 = Parser (func . runParser p1) where
    func Nothing       = Nothing
    func (Just (f, x)) = first f <$> runParser p2 x

instance Alternative (Parser s) where
  -- | Returns empty Parser
  empty 
    :: Parser s a -- ^ Empty Parser
  empty = Parser (const Nothing)
  -- | Returns one of two Parser
  (<|>) 
    :: Parser s a -- ^ First Parser
    -> Parser s a -- ^ Second Parser
    -> Parser s a -- ^ New Parser
  (<|>) p1 p2 = Parser (\x -> runParser p1 x <|> runParser p2 x)
  
instance Monad (Parser s) where
  -- | Returns result of binding
  (>>=) 
    :: Parser s a -- ^ Given Parser
    -> (a -> Parser s b) -- ^ Given function
    -> Parser s b -- ^ New Parser
  (>>=) parser f = Parser (\x -> func (first f <$> runParser parser x)) where
    func Nothing           = Nothing
    func (Just (prs, val)) = runParser prs val