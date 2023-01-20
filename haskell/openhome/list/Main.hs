module Main (main) where

leng :: [a] -> Int
leng lt =
  if null lt
    then 0
    else 1 + (leng $ tail lt)

main = do
  putStrLn $ show $ leng []
  putStrLn $ show $ leng [1,2]
  putStrLn $ show $ leng [3,4,5]
