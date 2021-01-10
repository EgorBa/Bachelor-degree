module Bench3 where

import Task3
import System.Unsafe (performIO)
import Control.DeepSeq (NFData)
import Criterion.Main (Benchmark, bench, nf)

ht :: ConcurrentHashTable Int Int
ht = performIO newCHT

doOp :: Int -> (ConcurrentHashTable Int Int -> IO a) -> ConcurrentHashTable Int Int -> IO a
doOp 0 op hashTable     = op hashTable
doOp count op hashTable = do
  _ <- op hashTable 
  doOp (count - 1) op hashTable 

putTest :: [Benchmark]
putTest = map (benchmarkPut $ doOp 100000 (putCHT 1 1)) [ht]

getTest :: [Benchmark]
getTest = map (benchmarkGet $ doOp 100000 (getCHT 1)) [ht]

sizeTest :: [Benchmark]
sizeTest = map (benchmarkSize $ doOp 100000 sizeCHT) [ht]

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
-- time                 51.90 ms   (45.19 ms .. 58.49 ms)
--                      0.971 R²   (0.947 R² .. 0.996 R²)
-- mean                 44.26 ms   (42.64 ms .. 47.51 ms)
-- std dev              4.424 ms   (2.284 ms .. 7.441 ms)
-- variance introduced by outliers: 34% (moderately inflated)
--                 
-- benchmarking get test/get
-- time                 41.25 ms   (40.09 ms .. 43.64 ms)
--                      0.994 R²   (0.987 R² .. 0.999 R²)
-- mean                 39.96 ms   (39.29 ms .. 40.83 ms)
-- std dev              1.831 ms   (1.399 ms .. 2.483 ms)
-- variance introduced by outliers: 12% (moderately inflated)
--                  
-- benchmarking size test/size
-- time                 2.279 ms   (2.273 ms .. 2.285 ms)
--                      1.000 R²   (1.000 R² .. 1.000 R²)
-- mean                 2.278 ms   (2.273 ms .. 2.282 ms)
-- std dev              14.69 μs   (11.78 μs .. 19.22 μs)
