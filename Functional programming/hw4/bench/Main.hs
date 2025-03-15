module Main where

import Criterion.Main (bgroup, defaultMain)
import Bench1 (fastPerimeterTest, fastDoubleAreaTest, slowPerimeterTest, slowDoubleAreaTest)
import Bench2 (slowIntegrate, fastIntegrate)
import Bench3 (putTest, getTest, sizeTest)

main :: IO ()
main = defaultMain [ bgroup "fast perimeter"   fastPerimeterTest
                   , bgroup "fast double area" fastDoubleAreaTest
                   , bgroup "slow perimetr" slowPerimeterTest
                   , bgroup "slow double area" slowDoubleAreaTest
                   , bgroup "parallel integrate" fastIntegrate
                   , bgroup "integrate" slowIntegrate
                   , bgroup "put test" putTest
                   , bgroup "get test" getTest
                   , bgroup "size test" sizeTest ]