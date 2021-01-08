module Bench3 where

import Task3
import System.Unsafe (performIO)
import Control.DeepSeq (NFData)
import Criterion.Main (Benchmark, bench, nf)

ht :: ConcurrentHashTable Int Int
ht = performIO newCHT

putTest :: [Benchmark]
putTest = map (benchmarkPut (putCHT 1 1)) [ht]

getTest :: [Benchmark]
getTest = map (benchmarkGet (getCHT 1)) [ht]

sizeTest :: [Benchmark]
sizeTest = map (benchmarkSize sizeCHT) [ht]

benchmark :: NFData a => String -> (ConcurrentHashTable Int Int -> IO a) -> ConcurrentHashTable Int Int -> Benchmark
benchmark st func f = bench st (nf (performIO . func) f)

benchmarkPut :: NFData a => (ConcurrentHashTable Int Int -> IO a) -> ConcurrentHashTable Int Int -> Benchmark
benchmarkPut  = benchmark "put"

benchmarkGet :: NFData a => (ConcurrentHashTable Int Int -> IO a) -> ConcurrentHashTable Int Int -> Benchmark
benchmarkGet  = benchmark "get"

benchmarkSize :: NFData a => (ConcurrentHashTable Int Int -> IO a) -> ConcurrentHashTable Int Int -> Benchmark
benchmarkSize = benchmark "size"

-- My Result of this Bench
-- (each operation takes less than 1 ms, then my implementation supports more then 10^5 requests in second)
--
-- benchmarking put test/put
-- time                 573.5 ns   (564.6 ns .. 591.2 ns)
--                      0.998 R²   (0.996 R² .. 1.000 R²)
-- mean                 568.3 ns   (565.3 ns .. 574.8 ns)
-- std dev              14.19 ns   (6.201 ns .. 26.79 ns)
-- variance introduced by outliers: 34% (moderately inflated)
--
-- benchmarking get test/get
-- time                 541.8 ns   (535.7 ns .. 549.0 ns)
--                      0.999 R²   (0.999 R² .. 1.000 R²)
-- mean                 536.3 ns   (533.2 ns .. 540.7 ns)
-- std dev              12.42 ns   (8.493 ns .. 17.97 ns)
-- variance introduced by outliers: 30% (moderately inflated)
--
-- benchmarking size test/size
-- time                 94.70 ns   (94.41 ns .. 94.98 ns)
--                      1.000 R²   (1.000 R² .. 1.000 R²)
-- mean                 94.73 ns   (94.50 ns .. 95.14 ns)
-- std dev              1.002 ns   (751.3 ps .. 1.338 ns)
-- variance introduced by outliers: 29% (moderately inflated)