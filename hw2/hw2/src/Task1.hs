module Task1
  ( stringSum
  ) where
 
import Text.Read

-- | Returns Maybe sum od string
stringSum
  :: String -- ^ Given String
  -> Maybe Int -- ^ Maybe sum of string
stringSum s = sumMaybe $ traverse (readMaybe :: String -> Maybe Int) $ words s

-- | Returns Maybe sum of Maybe list
sumMaybe
  :: Maybe [Int] -- ^ Given Maybe
  -> Maybe Int -- ^ Maybe sum of list
sumMaybe Nothing    = Nothing
sumMaybe a@(Just _) = foldr sumJust (Just 0) (sequenceA a) where
  sumJust a1 a2 = (+) <$> a1 <*> a2