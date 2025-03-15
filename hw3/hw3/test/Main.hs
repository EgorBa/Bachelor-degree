{-# LANGUAGE UnicodeSyntax #-}
module Main where

import Test.Tasty (defaultMain, testGroup)
import Test (hspecTest)

main = defaultMain =<< allTests

allTests = do
    t ← hspecTest
    return $ testGroup "Tests"
      [t]