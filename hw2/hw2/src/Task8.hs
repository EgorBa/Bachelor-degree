module Task8
  ( correctBracketSequenceParser
  , numberParser
  ) where

import Task7
import Task6
import Data.Char
import Control.Applicative 

-- | Returns Parser that parse ( 
leftBracketParser 
  :: Parser Char Char -- ^ Parser that parse (
leftBracketParser = element '('

-- | Returns Parser that parse )
rightBracketParser 
  :: Parser Char Char -- ^ Parser that parse )
rightBracketParser = element ')'

-- | Returns Parser that crash if stream isn't correct bracket sequence
correctBracketSequenceParser 
  :: Parser Char () -- ^ Parser that crash if stream isn't correct bracket sequence
correctBracketSequenceParser = bracketParser >> eof where
  bracketParser = many (leftBracketParser >> bracketParser >> rightBracketParser)

-- | Returns Parser that parse + or -
signParser 
  :: Parser Char (Int -> Int) -- ^ Parser that parse + or -
signParser = signFunc <$> (element '-' <|> element '+') where
  signFunc '-' = negate
  signFunc _   = id

-- | Parser that parse digits
digitsParser 
  :: Parser Char Int -- ^ Parser that parse digits
digitsParser = read <$> some (satisfy isDigit)

-- | Returns Parser that parse number
numberParser 
  :: Parser Char Int -- ^ Parser that parse number
numberParser = (signParser <|> pure id) <*> digitsParser