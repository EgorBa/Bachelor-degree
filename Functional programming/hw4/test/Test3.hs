module Test3 where

import Test.Tasty (TestTree)
import Test.Tasty.Hspec (Spec, describe, it, shouldBe, shouldSatisfy, testSpec)

import Task3

hspecTest3 :: IO TestTree
hspecTest3 = testSpec "Task3" spec

manyPuts :: Int -> ConcurrentHashTable Int Int -> IO ()
manyPuts x ht
  | x < 0     = return ()
  | otherwise = do
  putCHT x x ht
  manyPuts (x - 1) ht

spec :: Spec
spec = do
  describe "put and size ConcurrentHashTable" $
    it "put and size" $ do
      ht <- newCHT :: IO (ConcurrentHashTable Int Int)
      s  <- sizeCHT ht
      s  `shouldBe` 0
      putCHT 1 1 ht
      s1 <- sizeCHT ht
      s1 `shouldBe` 1
      putCHT 1 0 ht
      s2 <- sizeCHT ht
      s2 `shouldBe` 1
  describe "put and get ConcurrentHashTable" $
    it "put and get" $ do
      ht        <- newCHT :: IO (ConcurrentHashTable Int Int)
      putCHT 1 1 ht
      element   <- getCHT 1 ht
      element   `shouldBe` Just 1
      putCHT 1 0 ht
      element'  <- getCHT 1 ht
      element'' <- getCHT 9 ht
      element'  `shouldBe` Just 0
      element'' `shouldBe` Nothing  
  describe "all functions ConcurrentHashTable" $
    it "all functions" $ do
      ht        <- newCHT :: IO (ConcurrentHashTable Int Int)
      s         <- sizeCHT ht
      s         `shouldBe` 0
      putCHT 1 1 ht
      element   <- getCHT 1 ht
      element   `shouldBe` Just 1
      s1        <- sizeCHT ht
      s1        `shouldBe` 1
      putCHT 1 0 ht
      putCHT 2 2 ht
      element'  <- getCHT 1 ht
      element'' <- getCHT 9 ht
      element'  `shouldBe` Just 0
      element'' `shouldBe` Nothing
      s2        <- sizeCHT ht
      s2        `shouldBe` 2
  describe "rehash ConcurrentHashTable" $
    it "rehash" $ do
      ht         <- newCHT :: IO (ConcurrentHashTable Int Int)
      s          <- sizeCHT ht
      s          `shouldBe` 0
      manyPuts 20 ht
      element'   <- getCHT 1 ht
      element''  <- getCHT 19 ht
      element''' <- getCHT 30 ht
      element'   `shouldBe` Just 1
      element''  `shouldBe` Just 19
      element''' `shouldBe` Nothing
      s2         <- sizeCHT ht
      s2         `shouldBe` 21