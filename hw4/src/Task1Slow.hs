module Task1Slow
  ( PointSlow(..)
  , slowPerimeter
  , slowDoubleArea
  ) where

-- | Type 2D Point with Int coordinate
data PointSlow = PointSlow
  { x :: Int
  , y :: Int
  } deriving (Show)
  
-- | Returns distance of 2 Points
dist 
  :: PointSlow -- ^ First Point
  -> PointSlow -- ^ Second Point
  -> Double -- ^ Distance of 2 Points
dist a b = sqrt $ fromIntegral $ (x point) * (x point) + (y point) * (y point)
  where
    point = minus a b
  
-- | Returns subtraction of 2 Points  
minus 
  :: PointSlow -- ^ First Point
  -> PointSlow -- ^ Second Point
  -> PointSlow -- ^ Subtraction of 2 Points
minus (PointSlow x1 y1) (PointSlow x2 y2) = PointSlow (x1 - x2) (y1 - y2)
  
-- | Returns perimeter of list of Points
slowPerimeter 
  :: [PointSlow] -- ^ List of Points
  -> Double -- ^ Perimeter
slowPerimeter l
  | length l < 2 = 0
  | otherwise   = perimeter' 0 (last l : l)
  where
     perimeter' acc (p1 : p2 : ps) = perimeter' (acc + dist p1 p2) (p2 : ps)
     perimeter' acc _              = acc

-- | Returns double area of list of Points
slowDoubleArea 
  :: [PointSlow] -- ^ List of Points
  -> Int -- ^ Double area
slowDoubleArea l
  | length l <= 2 = 0
  | otherwise     = doubleArea' 0 $ (last l : l) ++ [head l]
  where
    doubleArea' acc (a : b : c : as) = doubleArea' (acc + (y b) * ((x a) - (x c))) (b : c : as)
    doubleArea' acc _                = acc