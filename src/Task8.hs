module Task8 where

import Control.Comonad (Comonad, extract, duplicate, extend)
import Control.Monad (liftM2)
import Data.List (intercalate)
import System.Random (randomRIO)
import System.Unsafe (performIO)

newtype Grid a =
  Grid
    { unGrid :: ListZipper (ListZipper a)
    }

data ListZipper a =
  LZ [a] a [a]

instance Functor ListZipper where
  fmap f (LZ ls x rs) = LZ (map f ls) (f x) (map f rs)

instance Comonad ListZipper where
  extract (LZ _ x _) = x
  duplicate = genericMove listLeft listRight

listLeft :: ListZipper a -> ListZipper a
listLeft (LZ (a:as) x bs) = LZ as a (x : bs)
listLeft _                = error "listLeft"

listRight :: ListZipper a -> ListZipper a
listRight (LZ as x (b:bs)) = LZ (x : as) b bs
listRight _                = error "listRight"

listWrite :: a -> ListZipper a -> ListZipper a
listWrite x (LZ ls _ rs) = LZ ls x rs

toList :: ListZipper a -> Int -> [a]
toList (LZ ls x rs) n = reverse (take n ls) ++ [x] ++ take n rs

iterateTail :: (a -> a) -> a -> [a]
iterateTail f = tail . iterate f

genericMove :: (z a -> z a) -> (z a -> z a) -> z a -> ListZipper (z a)
genericMove f g e = LZ (iterateTail f e) e (iterateTail g e)

instance Functor Grid where
  fmap f (Grid (LZ ls x rs)) =
    Grid $ LZ (map (fmap f) ls) (fmap f x) (map (fmap f) rs)

instance Comonad Grid where
  extract = gridRead
  duplicate = Grid . fmap horizontal . vertical

up :: Grid a -> Grid a
up (Grid g) = Grid (listLeft g)

down :: Grid a -> Grid a
down (Grid g) = Grid (listRight g)

left :: Grid a -> Grid a
left (Grid g) = Grid (fmap listLeft g)

right :: Grid a -> Grid a
right (Grid g) = Grid (fmap listRight g)

gridRead :: Grid a -> a
gridRead (Grid g) = extract $ extract g

gridWrite :: a -> Grid a -> Grid a
gridWrite x (Grid g) = Grid $ listWrite newLine g
  where
    oldLine = extract g
    newLine = listWrite x oldLine

horizontal :: Grid a -> ListZipper (Grid a)
horizontal = genericMove left right

vertical :: Grid a -> ListZipper (Grid a)
vertical = genericMove up down

neighbours :: [Grid a -> Grid a]
neighbours = horizontals ++ verticals ++ liftM2 (.) horizontals verticals
  where
    horizontals = [left, right]
    verticals   = [up, down]

-- | Type of People
data Man
  = Health -- ^ Healthy man
  | Immunity Int -- ^ Man with Immunity
  | Disease Int -- ^ Diseased man
  | Infection Int -- ^ Infected man

-- | Days with immunity
imDays :: Int
imDays = 5

-- | Days with disease
diDays :: Int
diDays= 5

-- | Days with infection
inDays :: Int
inDays = 5

-- | Size of shown game
sizeOfGame :: Int
sizeOfGame = 10

-- | Chance to be infection
infectionChance :: Double
infectionChance = 0.1

-- | Returns picture of game
showSimulation
  :: Grid Man -- ^ Given Grid Man
  -> Int -- ^ Size of game
  -> String -- ^ Picture of game
showSimulation man size = intercalate "|\n" $ map (intercalate "") list
  where
    dividingLine = replicate (2 * size + 1) "-"
    listOfMen = map (flip toList size) $ toList (unGrid (getString <$> man)) size
    list = (dividingLine : listOfMen) ++ [dividingLine ++ ["|\n"]]

-- | Returns String of Man
getString :: Man -> String
getString Health        = " "
getString (Immunity _)  = "@"
getString (Disease _)   = "#"
getString (Infection _) = "?"

-- | Started Grid Man
startPosition :: Grid Man
startPosition = gridWrite (Disease diDays) ((\_ -> Health) <$> startGrid)

-- | Started Grid Integer
startGrid :: Grid Integer
startGrid = Grid $ duplicate $ LZ (iterate (flip (-) 1) (-1)) 0 (iterate (+ 1) 1)

-- | Extend rule
evolve
  :: Grid Man -- ^ Given Grid Man
  -> Grid Man -- ^ Next Grid Man
evolve = extend rule

-- ^ Returns next Man by given
rule
  :: Grid Man -- ^ Given Grid Man
  -> Man -- ^ Next Man
rule g = case (extract g) of
  Disease days   -> if days == 0 then Immunity imDays else Disease (days - 1)
  Immunity days  -> if days == 0 then checkNeighbours (getNeighbours g) else Immunity (days - 1)
  Infection days -> if days == 0 then Disease diDays else Infection (days - 1)
  Health         -> checkNeighbours (getNeighbours g)
  where
    checkNeighbours list = do
      if foldl tryInfection False list
        then Infection inDays
        else Health
    tryInfection flag man = do
      if isDangerous man
        then flag || (performIO (randomRIO (0, 1)) < infectionChance)
        else flag

-- | Returns True if man is dangerous for other else False
isDangerous
  :: Man -- ^ Given Man
  -> Bool -- ^ True if man is dangerous for other else False
isDangerous (Infection _) = True
isDangerous (Disease _)   = True
isDangerous _             = False

-- | Returns list of neighbours
getNeighbours
  :: Grid Man -- ^ Given Man
  -> [Man] -- ^ List of neighbours
getNeighbours g = map (\direction -> extract $ direction g) neighbours

-- | Returns Grid Man after n days
simulationNDays
  :: Int -- ^ Count of days
  -> Grid Man -- ^ Start Grid Man
  -> Grid Man -- ^ Grid Man after n days
simulationNDays n man
  | n == 0    = man
  | otherwise = simulationNDays (n - 1) (evolve man)
  
-- Code example to see result : putStr $ showSimulation (simulationNDays 15 startPosition) sizeOfGame
-- You can change these parameters to see a different results of game : 
-- imDays, diDays, inDays, infectionChance, sizeOfGame
