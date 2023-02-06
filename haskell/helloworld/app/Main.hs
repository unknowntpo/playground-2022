module Main (main) where

import Lib

main :: IO ()
main = 
  let res = someFunc
  putStrLn res 
