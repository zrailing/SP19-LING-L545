import pyconll

my_conll_file_location = "./UD_English-GUM/train.conll"
train = pyconll.load_from_file(my_conll_file_location)


for sentence in train:
	for token in sentence:
		if token.pos == 'NOUN':
			noun_token_transormation(token)


