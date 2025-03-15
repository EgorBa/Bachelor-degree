module Task_4
  ( Expr(..)
  , ArithmeticError (..)
  , eval
  ) where
  
-- | Arithmetic expression
data Expr 
  = Add Expr Expr -- ^ Sum of expressions
  | Sub Expr Expr -- ^ Subtraction of expressions
  | Multiply Expr Expr -- ^ Multiply of expressions
  | Divide Expr Expr -- ^ Division of expressions
  | Pow Expr Expr -- ^ Power of expressions
  | Const Int -- ^ Constant
    deriving Show
  
-- | Arithmetic error
data ArithmeticError 
  = DivisionByZero -- ^ Division by zero
  | NegativePow -- ^ Negative power
    deriving Show
  
instance Eq ArithmeticError where
  DivisionByZero == DivisionByZero = True
  NegativePow == NegativePow       = True
  _ == _                           = False

-- | Returns Either result of evaluation Expression
eval 
  :: Expr -- ^ Given Expression
  -> Either ArithmeticError Int -- ^ Either result of evaluation
eval (Const a)        = Right a
eval (Add e1 e2)      = (+) <$> eval e1 <*> eval e2
eval (Sub e1 e2)      = (-) <$> eval e1 <*> eval e2
eval (Multiply e1 e2) = (*) <$> eval e1 <*> eval e2
eval (Divide e1 e2)   = divide (eval e1) (eval e2)
eval (Pow e1 e2)      = pow (eval e1) (eval e2)

-- | Returns Either result of dividing
divide 
  :: Either ArithmeticError Int -- ^ First Expression
  -> Either ArithmeticError Int -- ^ Second Expression
  -> Either ArithmeticError Int -- ^ Either result of dividing
divide (Right _) (Right 0) = Left DivisionByZero
divide a b                 = div <$> a <*> b

-- | Returns Either result of power
pow 
  :: Either ArithmeticError Int -- ^ Given Expression
  -> Either ArithmeticError Int -- ^ Power Expression
  -> Either ArithmeticError Int -- ^ Either result of powering
pow (Right a) (Right b) 
  |b < 0     = Left NegativePow
  |otherwise = Right (a ^ b)
pow a b      = (^) <$> a <*> b