{-# LANGUAGE CPP #-}
{-# LANGUAGE NoRebindableSyntax #-}
{-# OPTIONS_GHC -fno-warn-missing-import-lists #-}
module Paths_hw4 (
    version,
    getBinDir, getLibDir, getDynLibDir, getDataDir, getLibexecDir,
    getDataFileName, getSysconfDir
  ) where

import qualified Control.Exception as Exception
import Data.Version (Version(..))
import System.Environment (getEnv)
import Prelude

#if defined(VERSION_base)

#if MIN_VERSION_base(4,0,0)
catchIO :: IO a -> (Exception.IOException -> IO a) -> IO a
#else
catchIO :: IO a -> (Exception.Exception -> IO a) -> IO a
#endif

#else
catchIO :: IO a -> (Exception.IOException -> IO a) -> IO a
#endif
catchIO = Exception.catch

version :: Version
version = Version [0,1,0,0] []
bindir, libdir, dynlibdir, datadir, libexecdir, sysconfdir :: FilePath

bindir     = "/Users/egor.bazhenov/hw4/.stack-work/install/x86_64-osx/24d947b13379c1938c55a1c165f714842b104c5757fb42e8d7bb15014fd30688/8.8.4/bin"
libdir     = "/Users/egor.bazhenov/hw4/.stack-work/install/x86_64-osx/24d947b13379c1938c55a1c165f714842b104c5757fb42e8d7bb15014fd30688/8.8.4/lib/x86_64-osx-ghc-8.8.4/hw4-0.1.0.0-8ifzt8t6IHwL1B2bKKspJl-hw4-test"
dynlibdir  = "/Users/egor.bazhenov/hw4/.stack-work/install/x86_64-osx/24d947b13379c1938c55a1c165f714842b104c5757fb42e8d7bb15014fd30688/8.8.4/lib/x86_64-osx-ghc-8.8.4"
datadir    = "/Users/egor.bazhenov/hw4/.stack-work/install/x86_64-osx/24d947b13379c1938c55a1c165f714842b104c5757fb42e8d7bb15014fd30688/8.8.4/share/x86_64-osx-ghc-8.8.4/hw4-0.1.0.0"
libexecdir = "/Users/egor.bazhenov/hw4/.stack-work/install/x86_64-osx/24d947b13379c1938c55a1c165f714842b104c5757fb42e8d7bb15014fd30688/8.8.4/libexec/x86_64-osx-ghc-8.8.4/hw4-0.1.0.0"
sysconfdir = "/Users/egor.bazhenov/hw4/.stack-work/install/x86_64-osx/24d947b13379c1938c55a1c165f714842b104c5757fb42e8d7bb15014fd30688/8.8.4/etc"

getBinDir, getLibDir, getDynLibDir, getDataDir, getLibexecDir, getSysconfDir :: IO FilePath
getBinDir = catchIO (getEnv "hw4_bindir") (\_ -> return bindir)
getLibDir = catchIO (getEnv "hw4_libdir") (\_ -> return libdir)
getDynLibDir = catchIO (getEnv "hw4_dynlibdir") (\_ -> return dynlibdir)
getDataDir = catchIO (getEnv "hw4_datadir") (\_ -> return datadir)
getLibexecDir = catchIO (getEnv "hw4_libexecdir") (\_ -> return libexecdir)
getSysconfDir = catchIO (getEnv "hw4_sysconfdir") (\_ -> return sysconfdir)

getDataFileName :: FilePath -> IO FilePath
getDataFileName name = do
  dir <- getDataDir
  return (dir ++ "/" ++ name)
