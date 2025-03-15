module Bench2 where

import Task2 (integrate, integrateConcurrently)
import Criterion.Main (Benchmark, bench, nf)

functions :: [((Double -> Double), String)]
functions = [((+ 4), "y=x+4"), ((\i -> 1 / tan (i * i) - cos (i)), "y=1/tg(x^2)-cos(x)")]

slowIntegrate :: [Benchmark]
slowIntegrate = map (benchmark (integrate 0.0 100.0)) functions

fastIntegrate :: [Benchmark]
fastIntegrate = map (benchmark (integrateConcurrently 0.0 100.0)) functions

benchmark :: ((Double -> Double) -> Double) -> ((Double -> Double), String) -> Benchmark
benchmark func f = bench (snd f) (nf func (fst f))

-- My Result of this Bench
--
-- benchmarking parallel integrate/y=x+4
-- time                 239.5 μs   (237.5 μs .. 241.5 μs)
--                      1.000 R²   (0.999 R² .. 1.000 R²)
-- mean                 240.0 μs   (239.0 μs .. 241.3 μs)
-- std dev              4.040 μs   (3.259 μs .. 5.630 μs)
--
-- benchmarking parallel integrate/y=1/tg(x^2)-cos(x)
-- time                 241.3 μs   (240.1 μs .. 242.6 μs)
--                      1.000 R²   (1.000 R² .. 1.000 R²)
-- mean                 240.7 μs   (239.5 μs .. 242.2 μs)
-- std dev              4.408 μs   (3.248 μs .. 6.323 μs)
-- variance introduced by outliers: 11% (moderately inflated)
--
-- benchmarking integrate/y=x+4
-- time                 1.100 s    (1.085 s .. 1.110 s)
--                      1.000 R²   (1.000 R² .. 1.000 R²)
-- mean                 1.102 s    (1.100 s .. 1.105 s)
-- std dev              2.920 ms   (2.448 ms .. 3.128 ms)
-- variance introduced by outliers: 19% (moderately inflated)
--
-- benchmarking integrate/y=1/tg(x^2)-cos(x)
-- time                 1.166 s    (1.150 s .. 1.205 s)
--                      1.000 R²   (1.000 R² .. 1.000 R²)
-- mean                 1.152 s    (1.148 s .. 1.163 s)
-- std dev              7.281 ms   (164.6 μs .. 8.802 ms)
-- variance introduced by outliers: 19% (moderately inflated)