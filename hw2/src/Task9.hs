module Task9
  ( listlistParser
  ) where

import Task6
import Task8
import Task7
import Control.Applicative 

-- | Returns Parser that parse spaces
spacesParser 
  :: Parser Char () -- ^ Parser that parse spaces
spacesParser = many (element ' ') >> ok

-- | Returns Parser that parse comma
commaParser 
  :: Parser Char () -- ^ Parser that parse comma
commaParser = element ',' >> ok

-- | Returns Parser that parse list of numbers
listParser 
  :: Parser Char [Int] -- ^ Parser that parse list of numbers
listParser = do
  spacesParser
  count <- numberParser
  spacesParser
  commaParser
  valuesOfListParser count

-- | Returns Parser that parse values of list of numbers
valuesOfListParser 
  :: Int -> Parser Char [Int] -- ^ Parser that parse values of list of numbers
valuesOfListParser count
  | count < 0  = empty
  | count == 0 = return []
  | otherwise  = do
      spacesParser
      a <- numberParser
      spacesParser
      commaParser <|> eof
      as <- valuesOfListParser (count - 1)
      return (a:as)

-- | Returns Parser that parse list of lists of numbers
listlistParser 
  :: Parser Char [[Int]] -- ^ Parser that parse list of lists of numbers
listlistParser = do
  list <- some listParser
  eof
  return list

