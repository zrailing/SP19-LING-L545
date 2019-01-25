##Maxmatch 

import sys
import wer

def maxmatch(dictionary):

## read sentences to tokenize from stdin
     sent_to_token = sys.stdin.read()

## Read in dictionary from a file given as an argument
     with open(dictionary, "r") as f:
          dict_text = f.read().split()

#Write each word, separated by newlines, to the stdin
     wer.wer(sent_to_token, dict_text)