module Main where

main :: IO ()
main = do
    string <- getLine
    putStrLn (expr string)

expr str = helper str 0 [] where
    helper [] b ans = dis ans
    helper (x:xs) b ans
       | x == ')' = helper xs (b-1) (')':ans)
       | x == '(' = helper xs (b+1) ('(':ans)
       | x == ' ' || x == '>' = helper xs b ans
       | otherwise = if (x=='-' && b==0) then ("(->," ++ (dis ans) ++","++ (expr xs) ++ ")") else helper xs b (x:ans)

dis str = helper str 0 [] where
    helper [] b ans = con ans
    helper (x:xs) b ans
       | x == ')' = helper xs (b-1) (')':ans)
       | x == '(' = helper xs (b+1) ('(':ans)
       | x == ' ' = helper xs b ans
       | otherwise = if (x=='|' && b==0) then ("(|," ++(dis xs) ++","++ (con ans) ++ ")") else helper xs b (x:ans)

con str = helper (reverse str) 0 [] where
    helper [] b ans = neg ans
    helper (x:xs) b ans
       | x == ')' = helper xs (b-1) (')':ans)
       | x == '(' = helper xs (b+1) ('(':ans)
       | x == ' ' = helper xs b ans
       | otherwise = if (x=='&' && b==0) then ("(&," ++ (con (reverse xs)) ++","++ (neg ans) ++ ")") else helper xs b (x:ans)

neg str = helper (reverse $ normolise str []) where
    helper ('!':xs) =  "(!" ++ neg xs ++")"
    helper ('(':xs) =  expr (init xs)
    helper a = a

normolise (x:xs) ans = if (x==' ') then normolise xs ans else normolise xs (x:ans)
normolise [] ans = ans