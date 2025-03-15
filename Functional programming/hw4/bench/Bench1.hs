module Bench1 where

import Task1 (Point(..), perimeter, doubleArea)
import Task1Slow (PointSlow(..), slowPerimeter, slowDoubleArea)
import Control.DeepSeq (NFData)
import Criterion.Main (Benchmark, bench, nf)

sizes :: [Int]
sizes = [1000000, 10000000]

pointsFast :: [[Point]]
pointsFast = map (\l -> map (\t -> Point t t) [0 .. l]) sizes

pointsSlow :: [[PointSlow]]
pointsSlow = map (\l -> map (\t -> PointSlow t t) [0 .. l]) sizes

fastPerimeterTest :: [Benchmark]
fastPerimeterTest = map (benchmark perimeter) pointsFast

fastDoubleAreaTest :: [Benchmark]
fastDoubleAreaTest = map (benchmark doubleArea) pointsFast

slowPerimeterTest :: [Benchmark]
slowPerimeterTest = map (benchmark slowPerimeter) pointsSlow

slowDoubleAreaTest :: [Benchmark]
slowDoubleAreaTest = map (benchmark slowDoubleArea) pointsSlow

benchmark :: NFData b => ([a] -> b) -> [a] -> Benchmark
benchmark f point = bench (show ((length point) - 1)) (nf f point)

-- My Result of this Bench
--
-- benchmarking fast perimeter/1000000
-- time                 16.80 ms   (16.14 ms .. 17.55 ms)
--                      0.991 R²   (0.983 R² .. 0.997 R²)
-- mean                 16.75 ms   (16.42 ms .. 17.18 ms)
-- std dev              842.7 μs   (671.2 μs .. 1.001 ms)
-- variance introduced by outliers: 20% (moderately inflated)
--
-- benchmarking fast perimeter/10000000
-- time                 208.9 ms   (179.5 ms .. 241.6 ms)
--                      0.990 R²   (0.969 R² .. 1.000 R²)
-- mean                 189.3 ms   (178.6 ms .. 199.9 ms)
-- std dev              14.72 ms   (12.14 ms .. 17.50 ms)
-- variance introduced by outliers: 15% (moderately inflated)
--
-- benchmarking fast double area/1000000
-- time                 34.51 ms   (26.66 ms .. 42.02 ms)
--                      0.845 R²   (0.631 R² .. 0.985 R²)
-- mean                 41.68 ms   (37.82 ms .. 47.52 ms)
-- std dev              9.449 ms   (6.719 ms .. 13.54 ms)
-- variance introduced by outliers: 79% (severely inflated)
--
-- benchmarking fast double area/10000000
-- time                 243.9 ms   (209.6 ms .. 288.6 ms)
--                      0.983 R²   (0.969 R² .. 1.000 R²)
-- mean                 219.0 ms   (212.2 ms .. 235.1 ms)
-- std dev              14.25 ms   (1.269 ms .. 20.33 ms)
-- variance introduced by outliers: 15% (moderately inflated)
--
-- benchmarking slow perimetr/1000000
-- time                 26.84 ms   (25.77 ms .. 28.47 ms)
--                      0.990 R²   (0.980 R² .. 0.998 R²)
-- mean                 25.12 ms   (24.17 ms .. 26.04 ms)
-- std dev              1.981 ms   (1.432 ms .. 2.598 ms)
-- variance introduced by outliers: 30% (moderately inflated)
--
-- benchmarking slow perimetr/10000000
-- time                 255.6 ms   (237.9 ms .. 271.3 ms)
--                      0.999 R²   (0.997 R² .. 1.000 R²)
-- mean                 256.3 ms   (252.6 ms .. 261.5 ms)
-- std dev              5.460 ms   (2.539 ms .. 7.312 ms)
-- variance introduced by outliers: 16% (moderately inflated)
--
-- benchmarking slow double area/1000000
-- time                 76.82 ms   (74.61 ms .. 79.12 ms)
--                     0.996 R²   (0.988 R² .. 0.999 R²)
-- mean                 72.40 ms   (67.63 ms .. 75.01 ms)
-- std dev              5.701 ms   (2.852 ms .. 9.408 ms)
-- variance introduced by outliers: 26% (moderately inflated)
--
-- benchmarking slow double area/10000000
-- time                 307.9 ms   (305.4 ms .. 310.4 ms)
--                      1.000 R²   (1.000 R² .. 1.000 R²)
-- mean                 307.6 ms   (306.7 ms .. 308.3 ms)
-- std dev              946.3 μs   (521.9 μs .. 1.417 ms)
-- variance introduced by outliers: 16% (moderately inflated)