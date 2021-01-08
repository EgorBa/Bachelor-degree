{-# LANGUAGE UnicodeSyntax #-}
module Main where

import Test.Tasty (defaultMain, testGroup)
import Test1 (hspecTest1)
import Test2 (hspecTest2)
import Test3 (hspecTest3)
import Test67 (hspecTest67)

main = defaultMain =<< allTests

allTests = do
    t1 ← hspecTest1
    t2 ← hspecTest2
    t3 ← hspecTest3
    t67 ← hspecTest67
    return $ testGroup "Tests"
      [t1, t2, t3, t67]