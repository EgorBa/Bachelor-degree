{-# LANGUAGE Strict #-}
module Task1
  ( Point(..)
  , plus
  , minus
  , scalarProduct
  , crossProduct
  , dist
  , perimeter
  , doubleArea
  ) where

-- | Type 2D Point with Int coordinate
data Point = Point
  { x :: Int
  , y :: Int
  } deriving (Show)

-- | Returns sum of 2 Points
plus 
  :: Point -- ^ First Point
  -> Point -- ^ Second Point
  -> Point -- ^ Sum of Points
plus (Point x1 y1) (Point x2 y2) = Point (x1 + x2) (y1 + y2)

-- | Returns subtraction of 2 Points
minus 
  :: Point -- ^ First Point
  -> Point -- ^ Second Point
  -> Point -- ^ Subtraction of 2 Points
minus (Point x1 y1) (Point x2 y2) = Point (x1 - x2) (y1 - y2)

-- | Returns scalar product of 2 Points
scalarProduct 
  :: Point -- ^ First Point
  -> Point -- ^ Second Point
  -> Int -- ^ Scalar product of 2 Points
scalarProduct (Point x1 y1) (Point x2 y2) = x1 * x2 + y1 * y2

-- | Returns cross product of 2 Points
crossProduct 
  :: Point -- ^ First Point
  -> Point -- ^ Second Point
  -> Int -- ^ Cross product of 2 Points
crossProduct (Point x1 y1) (Point x2 y2) = x1 * y2 - y1 * x2

-- | Returns distance of 2 Points
dist 
  :: Point -- ^ First Point
  -> Point -- ^ Second Point
  -> Double -- ^ Distance of 2 Points
dist a b = sqrt $ fromIntegral $ (x point) * (x point) + (y point) * (y point)
  where
    point = minus a b

-- | Returns perimeter of list of Points
perimeter 
  :: [Point] -- ^ List of Points
  -> Double -- ^ Perimeter
perimeter l
  | length l < 2 = 0
  | otherwise    = perimeter' 0 (last l : l)
  where
     perimeter' acc (p1 : p2 : ps) = perimeter' (acc + dist p1 p2) (p2 : ps)
     perimeter' acc _              = acc

-- | Returns double area of list of Points
doubleArea 
  :: [Point] -- ^ List of Points
  -> Int -- ^ Double area
doubleArea l
  | length l <= 2 = 0
  | otherwise     = doubleArea' 0 $ (last l : l) ++ [head l]
  where
    doubleArea' acc (p1 : p2 : p3 : ps) = doubleArea' (acc + (y p2) * ((x p1) - (x p3))) (p2 : p3 : ps)
    doubleArea' acc _                   = acc