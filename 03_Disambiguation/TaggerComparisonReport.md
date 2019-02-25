# Tagger Comparison Report

For the following tests of part of speech taggers, the same Finnish dataset will be used. This dataset comes from the Universal Dependencies treebank for Finnish and can be found on their [Github](https://github.com/UniversalDependencies/UD_Finnish-TDT). 

##Tagger 1: UDPipe
The first POS tagger I tested was the UDPipe model. 

Training the UDPipe model involved a series of 20 iterations. At the end of these 20 iterations, the final result for accuracy was 99.84%.

When evaluated with the CoNLL-2017 evaluation script, the resulting precision percentage was 94.64%.

##Tagger 2: MarMot tagger
The second POS tagger I chose to put to the test with Finnish was the [MarMot tagger](http://cistern.cis.lmu.de/marmot/) by way of a [fork](https://github.com/ftyers/cistern) of the project that allows for native conllu format support. 

##Tagger 3: