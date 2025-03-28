{-# LANGUAGE CPP #-}
{-# LANGUAGE NoRebindableSyntax #-}
{-# OPTIONS_GHC -fno-warn-missing-import-lists #-}
module Paths_hw2 (
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

bindir     = "C:\\Users\\Bazhenov Egor\\git hw2\\.stack-work\\install\\46faa29f\\bin"
libdir     = "C:\\Users\\Bazhenov Egor\\git hw2\\.stack-work\\install\\46faa29f\\lib\\x86_64-windows-ghc-8.8.4\\hw2-0.1.0.0-5v80KHtYaDVIOeZ3gwijTr-hw2-test"
dynlibdir  = "C:\\Users\\Bazhenov Egor\\git hw2\\.stack-work\\install\\46faa29f\\lib\\x86_64-windows-ghc-8.8.4"
datadir    = "C:\\Users\\Bazhenov Egor\\git hw2\\.stack-work\\install\\46faa29f\\share\\x86_64-windows-ghc-8.8.4\\hw2-0.1.0.0"
libexecdir = "C:\\Users\\Bazhenov Egor\\git hw2\\.stack-work\\install\\46faa29f\\libexec\\x86_64-windows-ghc-8.8.4\\hw2-0.1.0.0"
sysconfdir = "C:\\Users\\Bazhenov Egor\\git hw2\\.stack-work\\install\\46faa29f\\etc"

getBinDir, getLibDir, getDynLibDir, getDataDir, getLibexecDir, getSysconfDir :: IO FilePath
getBinDir = catchIO (getEnv "hw2_bindir") (\_ -> return bindir)
getLibDir = catchIO (getEnv "hw2_libdir") (\_ -> return libdir)
getDynLibDir = catchIO (getEnv "hw2_dynlibdir") (\_ -> return dynlibdir)
getDataDir = catchIO (getEnv "hw2_datadir") (\_ -> return datadir)
getLibexecDir = catchIO (getEnv "hw2_libexecdir") (\_ -> return libexecdir)
getSysconfDir = catchIO (getEnv "hw2_sysconfdir") (\_ -> return sysconfdir)

getDataFileName :: FilePath -> IO FilePath
getDataFileName name = do
  dir <- getDataDir
  return (dir ++ "\\" ++ name)
