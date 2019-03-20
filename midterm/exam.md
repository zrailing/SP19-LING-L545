#Midterm Exam

##1. Tokenization and Segmentation

Tokenization ambiguities:
1. Clitics: No quiso vendérmelo. 
2. Abbreviations: Uds. pueden seguir con eso.
3. Named entities: Se llama Fernando Luis López-Ureña. 

##2. Morphological Analysis

See accompanying .lexc file. 

##3. Morphological Disambiguation

Original sentence: "The current fortification and walls were built by the Persian Sassanian Empire as a defensive structure against hostile nomadic people in the north, and continuously repaired or improved by later Arab, Mongol, Timurid, Shirvan and Iranian kingdoms until the early course of the 19th century, as long as its military function lasted."

Three rules:

1. Adj/noun disambiguation

SELECT adj IF 
	(0 adj)
	(0 n)
	(-1 adv)
	(-1 det)
	(1 adv)
	(1 n) ;


2. Adv/pr disambiguation

REMOVE adv IF
	(0 adv)
	(0 pr)
	(NOT 1 cm)
	(NOT 1 sent) ;

3. N/vblex disambiguation

REMOVE vblex IF
	(0 vblex)
	(0 n)
	(-1 adj)
	(1 vblex) ;

##4. Dependency Parsing

| Stack        | Buffer                    | Action    |
|--------------|---------------------------|-----------|
|              | We went home after lunch. | Shift     |
| We           | went home after lunch.    | Shift     |
| We went      | home after lunch.         | Left-arc  |
| went         | home after lunch.         | Shift     |
| went home    | after lunch.              | Right-arc |
| went         | after lunch.              | Shift     |
| went after   | lunch.                    | Left-arc  |
| went         | lunch.                    | Shift     |
| went lunch   | .                         | Right-arc |
| went         | .                         | Shift     |
| went .       |                           | Right-arc |
