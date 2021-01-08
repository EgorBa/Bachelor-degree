module Task3 
  ( ConcurrentHashTable(..)
  , newCHT
  , sizeCHT
  , getCHT
  , putCHT
  )
  where

import Data.Hashable (Hashable, hash)
import Data.Vector (Vector, replicateM, length, (!))
import Control.Concurrent.STM.TMVar (isEmptyTMVar, newEmptyTMVar, putTMVar, takeTMVar)
import Control.Concurrent.STM.TVar (TVar, newTVar, readTVar, writeTVar, modifyTVar)
import Control.Monad (when)
import Control.Monad.STM (STM, atomically)
import Data.Maybe (isJust, isNothing, fromJust)

-- | Type of HashTable
newtype HashTable k v
  = HT
    { _vector :: Vector (TVar (Maybe (k, v)))
    }

-- | Type of Concurrent HashTable
data ConcurrentHashTable k v
  = CHT
    { _hashTable :: TVar (HashTable k v)
    , _size      :: TVar Int
    }

-- | Start size of Concurrent HashTable
startSize :: Int
startSize = 16

-- | Create new Concurrent HashTable
newCHT :: IO (ConcurrentHashTable k v)
newCHT = atomically $ do
  vector    <- replicateM startSize (newTVar Nothing)
  hashTable <- newTVar (HT vector)
  size      <- newTVar 0
  return (CHT hashTable size)

-- | Returns size of Concurrent HashTable
sizeCHT
  :: ConcurrentHashTable k v -- ^ Concurrent HashTable
  -> IO Int -- ^ Size of Concurrent HashTable
sizeCHT (CHT _ s) = atomically $ readTVar s

-- | If Concurrent HashTable contains key returns Just value else Nothing
getCHT
  :: (Hashable k, Eq k) -- ^ Key should be Eq and Hashable
  => k -- ^ Given key
  -> ConcurrentHashTable k v -- ^ Concurrent HashTable
  -> IO (Maybe v) -- ^ Just value if table contains value else Nothing
getCHT key (CHT t _) = atomically $ do
  hashTable <- readTVar t
  tVar      <- findElement key hashTable
  element   <- readTVar tVar
  if isJust element
    then return $ Just $ snd $ fromJust element
    else return Nothing

-- | Put (key, value) to Concurrent HashTable
putCHT
  :: (Hashable k, Eq k) -- ^ Key should be Eq and Hashable
  => k -- ^ Given key
  -> v -- ^ Given value
  -> ConcurrentHashTable k v -- ^ Concurrent HashTable
  -> IO () -- ^ IO result of putting
putCHT key value (CHT t s) = atomically $ do
  hashTable <- readTVar t
  tVar      <- findElement key hashTable
  element   <- readTVar tVar
  when (isNothing element) $ modifyTVar s (+1)
  writeTVar tVar (Just (key, value))
  size      <- readTVar s
  when (size == Prelude.length (_vector hashTable)) $ do
    newVector <- rehash hashTable (2 * size)
    writeTVar t newVector
  where
    rehash (HT vector) newSize = do
      newVector <- replicateM newSize (newTVar Nothing)
      mapM_ (addElement (HT newVector)) vector
      return (HT newVector)
    addElement ht tVar = do
      element <- readTVar tVar
      when (isJust element) $ do
        tVar' <- findElement (fst (fromJust element)) ht
        writeTVar tVar' element

-- | Returns TVar to position of element by key
findElement
  :: (Hashable k, Eq k) -- ^ Key should be Eq and Hashable
  => k -- ^ Given key
  -> HashTable k v -- ^ Hashtable
  -> STM (TVar (Maybe (k, v))) -- ^ TVar to result
findElement key (HT vector) = do
  tMVar <- newEmptyTMVar
  mapM_ (checkElements tMVar) iterateList
  takeTMVar tMVar
    where
      checkElements tMVar index = do
        element <- readTVar (vector ! index)
        if isJust element
          then when ((fst $ fromJust element) == key) $ putTMVar tMVar (vector ! index)
          else do
            isEmpty <- isEmptyTMVar tMVar
            when isEmpty $ putTMVar tMVar (vector ! index)
      expectedPosition = mod (hash key) (Data.Vector.length vector)
      iterateList = [expectedPosition .. Data.Vector.length vector - 1] ++ [0 .. expectedPosition - 1]