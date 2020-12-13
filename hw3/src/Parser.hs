module Parser
  ( commandParser
  , opts
  ) where

import Options.Applicative
  (Parser, ParserInfo, argument, many, auto, command, helper, idm, info, metavar, progDesc, str,
  subparser, (<**>))

import Types(Command(..))

-- | Parser of commands with help
opts :: ParserInfo Command
opts = info (commandParser <**> helper) idm

-- | Parser of commands
commandParser :: Parser Command
commandParser = subparser
  (  command "help"  (info (pure HELP) 
                           (progDesc "Rules of FileSystem")
                     )
  <> command "exit"  (info (pure EXIT) 
                           (progDesc "Save FileSystem")
                     )
  <> command "dir"   (info (pure DIR) 
                           (progDesc "Show the content of current folder")
                     )
  <> command "cd"    (info (CD <$> argument str (metavar "FOLDER")) 
                            (progDesc "Go to given folder")
                     )
  <> command "info"  (info (INFO <$> argument str (metavar "FOLDER|FILE")) 
                           (progDesc "Show information of given file|folder")
                     )       
  <> command "mkdir" (info (MKDIR <$> argument str (metavar "FOLDER")) 
                           (progDesc "Create folder")
                     )
  <> command "touch" (info (TOUCH <$> argument str (metavar "FILE")) 
                           (progDesc "Create file")
                     )
  <> command "cat"   (info (CAT <$> argument str (metavar "FILE")) 
                           (progDesc "Show the content of given file")
                     )
  <> command "rm"    (info (RM <$> argument str (metavar "FILE")) 
                           (progDesc "Remove file")
                     )
  <> command "rmdir" (info (RMDIR <$> argument str (metavar "FOLDER")) 
                           (progDesc "Remove folder")
                     )
  <> command "find"  (info (FIND <$> argument str (metavar "FILE")) 
                           (progDesc "Show full path of given file if file exists in current folder")
                     )
  <> command "write" (info (WRITE <$> argument str (metavar "FILE") <*> many (argument auto (metavar "CONTENT"))) 
                           (progDesc "Write text to given file")
                     )
  <> command "ls"    (info (LS <$> argument str (metavar "FILE")) 
                           (progDesc "Show content of given folder")
                     )
  )