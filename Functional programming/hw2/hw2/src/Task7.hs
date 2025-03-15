module Task7
  ( ok
  , eof
  , satisfy
  , element
  , stream
  ) where

import Task6

-- | Returns Parser that never crash
ok 
  :: Parser s () -- ^ Parser that never crashed
ok = Parser (\x -> Just((), x))

-- | Returns Parser that crash if non empty input
eof 
  :: Parser s () -- ^ Parser that crash if non empty input
eof = Parser func where
  func [] = Just ((), [])
  func _  = Nothing

-- | Returns Parser that crash if predicate is not True
satisfy 
  :: (s -> Bool) -- ^ Given predicate
  -> Parser s s -- ^ Parser that crash if predicate is not True
satisfy predicate = Parser func where
  func []         = Nothing
  func (a:as)
    | predicate a = Just (a, as)
    | otherwise   = Nothing

-- | Returns Parser that crash if element is not equal given element
element 
  :: Eq s -- ^ Element should be Eq
  => s -- ^ Given element
  -> Parser s s -- ^ Parser that crash if element is not equal given element
element chr = satisfy (chr ==)

-- | Returns Parser that crash if stream is not equal given stream
stream 
  :: Eq s -- ^ Elements should be Eq
  => [s] -- ^ Given stream
  -> Parser s [s] -- ^ Parser that crash if stream is not equal given stream
stream = traverse element