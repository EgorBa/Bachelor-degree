module Task1
  ( DayWeek(..)
  , afterDays
  , daysToParty
  , isWeekend
  , nextDay
  ) where

-- | Type of days of the week
data DayWeek = Monday | Tuesday | Wednesday | Thursday | Friday | Saturday | Sunday
    deriving Show

-- | Returns next day of the week for given
nextDay 
  :: DayWeek -- ^ Given day of the week
  -> DayWeek -- ^ Next day of the week
nextDay Monday = Tuesday
nextDay Tuesday = Wednesday
nextDay Wednesday = Thursday
nextDay Thursday = Friday
nextDay Friday = Saturday
nextDay Saturday = Sunday
nextDay Sunday = Monday

-- | Returns day after given count of days for given day of the week
afterDays 
  :: Int -- ^ Given count of days
  -> DayWeek -- ^ Given day of the week
  -> DayWeek -- ^ Day after given count of days for given day of the week
afterDays n x
    | n > 0      = afterDays (n - 1) (nextDay x)
    | n == 0     = x
    | otherwise  = undefined

-- | Returns True if given day of the week is weekend, else False
isWeekend 
  :: DayWeek -- ^ Given day of the week
  -> Bool -- ^ True if given day of the week is weekend, else False
isWeekend Saturday   = True
isWeekend Sunday   = True
isWeekend _   = False

-- | Returns the count of days before Friday
daysToParty 
  :: DayWeek -- ^ Given day of the week
  -> Int -- ^ Count days for Friday
daysToParty Friday = 0
daysToParty x = 1 + daysToParty (nextDay x)