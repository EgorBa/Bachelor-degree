module Bench3 where

import Task3
import System.Unsafe (performIO)
import Control.DeepSeq (NFData)
import Criterion.Main (Benchmark, bench, nf)

ht :: ConcurrentHashTable Int Int
ht = performIO newCHT

manyOps :: Int -> Int -> ConcurrentHashTable Int Int -> IO ()
manyOps mode x hashTable
  | x < 0     = return ()
  | otherwise = do
  chooseOp mode x hashTable
  manyOps mode (x - 1) hashTable
  
chooseOp :: Int -> Int -> ConcurrentHashTable Int Int -> IO ()
chooseOp mode x hashTable
  | mode == 1 = putCHT x x hashTable
  | mode == 2 = do
  _ <- getCHT x hashTable
  return ()
  | otherwise = do
  _ <- sizeCHT hashTable
  return ()

putTest :: [Benchmark]
putTest = map (benchmarkPut $ manyOps 1 100000) [ht]

getTest :: [Benchmark]
getTest = map (benchmarkGet $ manyOps 2 100000) [ht]

sizeTest :: [Benchmark]
sizeTest = map (benchmarkSize $ manyOps 3 100000) [ht]

benchmark :: NFData a => String -> (ConcurrentHashTable Int Int -> IO a) -> ConcurrentHashTable Int Int -> Benchmark
benchmark st func f = bench st (nf (performIO . func) f)

benchmarkPut :: NFData a => (ConcurrentHashTable Int Int -> IO a) -> ConcurrentHashTable Int Int -> Benchmark
benchmarkPut  = benchmark "put"

benchmarkGet :: NFData a => (ConcurrentHashTable Int Int -> IO a) -> ConcurrentHashTable Int Int -> Benchmark
benchmarkGet  = benchmark "get"

benchmarkSize :: NFData a => (ConcurrentHashTable Int Int -> IO a) -> ConcurrentHashTable Int Int -> Benchmark
benchmarkSize = benchmark "size"

-- My Result of this Bench
-- benchmarking put test/put
-- time                 26.07 ms   (25.76 ms .. 26.43 ms)
--                      0.999 R²   (0.998 R² .. 1.000 R²)
-- mean                 25.98 ms   (25.84 ms .. 26.15 ms)
-- std dev              371.4 μs   (296.7 μs .. 479.6 μs)
--                 
-- benchmarking get test/get
-- time                 13.39 ms   (13.22 ms .. 13.59 ms)
--                      0.999 R²   (0.998 R² .. 0.999 R²)
-- mean                 13.59 ms   (13.46 ms .. 13.83 ms)
-- std dev              470.3 μs   (279.1 μs .. 755.9 μs)
-- variance introduced by outliers: 11% (moderately inflated)
--                 
-- benchmarking size test/size
-- time                 2.119 ms   (2.102 ms .. 2.138 ms)
--                      0.997 R²   (0.993 R² .. 0.999 R²)
-- mean                 2.176 ms   (2.151 ms .. 2.226 ms)
-- std dev              124.3 μs   (62.20 μs .. 226.7 μs)
-- variance introduced by outliers: 41% (moderately inflated)

