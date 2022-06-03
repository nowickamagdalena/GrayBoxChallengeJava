# GrayBoxChallengeJava
This is an optimization genetic algorithm for finding best solution to Max3Sat problem defined in a text file.
This code creates a population of possible solutions - "Individuals". Every individual has a genotype, which is an array of boolean values. Every value indicates if variable of the problem at this index should be true or false.
Algorithm runs iterations, one of each includes creating new solutions population.
During each iteration new Individuals are created by crossover and mutation of genotype.
After population is fully replaced, new best solution is found by comparing, how many clauses are satisfied with each genotype.
In the end algorithm returns a string of "1"s and "0"s indicating which variables should be true or false to satisfy maximum number of clauses in the problem.
