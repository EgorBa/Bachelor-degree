module Task1
  ( DayWeek(..)
  , afterDays
  , daysToParty
  , isWeekend
  , nextDay
  ) where

data DayWeek = Monday | Tuesday | Wednesday | Thursday | Friday | Saturday | Sunday
    deriving Show

nextDay :: DayWeek -> DayWeek
nextDay Monday = Tuesday
nextDay Tuesday = Wednesday
nextDay Wednesday = Thursday
nextDay Thursday = Friday
nextDay Friday = Saturday
nextDay Saturday = Sunday
nextDay Sunday = Monday

afterDays :: Int -> DayWeek -> DayWeek
afterDays n x
    | n > 0      = afterDays (n - 1) (nextDay x)
    | n == 0     = x
    | otherwise  = undefined

isWeekend :: DayWeek -> Bool
isWeekend Saturday   = True
isWeekend Sunday   = True
isWeekend _   = False

daysToParty :: DayWeek -> Int
daysToParty Friday = 0
daysToParty x = 1 + daysToParty (nextDay x)