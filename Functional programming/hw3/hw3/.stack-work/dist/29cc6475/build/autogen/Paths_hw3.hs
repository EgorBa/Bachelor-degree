{-# LANGUAGE CPP #-}
{-# LANGUAGE NoRebindableSyntax #-}
{-# OPTIONS_GHC -fno-warn-missing-import-lists #-}
module Paths_hw3 (
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

bindir     = "C:\\Users\\Bazhenov Egor\\git hw3\\.stack-work\\install\\8c3a1984\\bin"
libdir     = "C:\\Users\\Bazhenov Egor\\git hw3\\.stack-work\\install\\8c3a1984\\lib\\x86_64-windows-ghc-8.8.4\\hw3-0.1.0.0-E9lmyHoxrFFxTtD8UpzP5"
dynlibdir  = "C:\\Users\\Bazhenov Egor\\git hw3\\.stack-work\\install\\8c3a1984\\lib\\x86_64-windows-ghc-8.8.4"
datadir    = "C:\\Users\\Bazhenov Egor\\git hw3\\.stack-work\\install\\8c3a1984\\share\\x86_64-windows-ghc-8.8.4\\hw3-0.1.0.0"
libexecdir = "C:\\Users\\Bazhenov Egor\\git hw3\\.stack-work\\install\\8c3a1984\\libexec\\x86_64-windows-ghc-8.8.4\\hw3-0.1.0.0"
sysconfdir = "C:\\Users\\Bazhenov Egor\\git hw3\\.stack-work\\install\\8c3a1984\\etc"

getBinDir, getLibDir, getDynLibDir, getDataDir, getLibexecDir, getSysconfDir :: IO FilePath
getBinDir = catchIO (getEnv "hw3_bindir") (\_ -> return bindir)
getLibDir = catchIO (getEnv "hw3_libdir") (\_ -> return libdir)
getDynLibDir = catchIO (getEnv "hw3_dynlibdir") (\_ -> return dynlibdir)
getDataDir = catchIO (getEnv "hw3_datadir") (\_ -> return datadir)
getLibexecDir = catchIO (getEnv "hw3_libexecdir") (\_ -> return libexecdir)
getSysconfDir = catchIO (getEnv "hw3_sysconfdir") (\_ -> return sysconfdir)

getDataFileName :: FilePath -> IO FilePath
getDataFileName name = do
  dir <- getDataDir
  return (dir ++ "\\" ++ name)
