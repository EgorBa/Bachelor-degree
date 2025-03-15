{-# LANGUAGE Rank2Types #-}
module Task7
  ( cd
  , file
  , ls
  ) where

import Control.Lens ((^.), Traversal', traversed, filtered)
import Task6 (FS(..), contents, name, _Dir, _File)

-- | Go to next directory
cd
  :: FilePath -- ^ Name of Dir
  -> Traversal' FS FS -- ^ Result of 'cd'
cd dirName = _Dir . contents . traversed . filtered (\fs -> fs ^. _Dir . name == dirName)

-- | Returns contents of Dir
ls :: Traversal' FS FilePath
ls = _Dir . contents . traversed . name

-- | Returns name of File
file
  :: FilePath -- ^ Name of File
  -> Traversal' FS String -- ^ Result of 'file'
file fileName = _Dir . contents . traversed . filtered (\fs -> fs ^. _File . name == fileName) . name