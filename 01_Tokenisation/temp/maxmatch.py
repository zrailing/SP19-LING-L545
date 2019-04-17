import sys      # Used for Command Line Parameters

def read_in():
	return {x.strip() for x in sys.stdin}

def get_dict():
	return sys.argv[1]

def  maxMatch(sentence, dictionary):
    """Look for the best match for pat in the string text."""
    tokens = []

    if sentence == "":
        return tokens

    with open(dictionary, "r") as f:
        dic = f.read()#.decode("utf-8")
 # Try each possible starting spot in text
    for i  in range(len(sentence)-1, 1, -1):
        firstword = sentence[:i] #first i chars of sentence
        remainder = sentence[i:] #get all after i
 
        if firstword in dic:
              tokens.append(firstword)
		#tokens.extend(maxMatch(remainder, dictionary))
              return tokens
#    for token in tokens:
#        print(token, end="\n")


    firstword = sentence[0]
    remainder = sentence[1:]
    tokens.append(firstword)
#    tokens.extend(maxMatch(remainder, dictionary))
    for item in tokens:
         print(item, "\n")

def main():
    lines = read_in()
    dictionary = get_dict()
    for line in lines:
        #line = unicode(line, "utf-8", errors="ignore")
        print(maxMatch(line, dictionary))

if __name__ == '__main__':
    main()


