# MDKnapsack
This is an implementation of different types of algorithm to solve Multidimensional Knapsack Problem. 

This package consists:
* Dynamic Programming based algorithm 
* Genetic Search based algorithm 
* Simulated Annealing based algorithm 

## How to run your programs

Go to the *ExecutableJarFiles* directory.

* **To run the Dynamic Programming based implementation: go to ExecutableJarFiles directly and run the following command.**
 
 ```
 java -jar DynamicProgrammingMKP.jar
 ```
 This will read the data from the file *data.pref* in the project directly and out put the result to the  *resultOfDynamicProgramming.pref* file.
 
```
 java -jar DynamicProgrammingMKP.jar <file_path>
 ```
 This will read the data from the file specified in the *file_path* and out put the result to the *resultOfDynamicProgramming.pref* file.
 
 * **To run the Genetic Search based implementation: go to ExecutableJarFiles directly and run the following command.**
 ```
  java -jar GeneticSearchMKP.jar 
  ```
 This will read the data from the file *data.pref* in the project directly and out put the result to the *resultOfGenetic.pref* file.
 
 ```
  java -jar GeneticSearchMKP.jar <file_path> 
  ```
  This will read the data from the file specified in the *file_path* and out put the result to the *resultOfGenetic.pref* the file. 
  ```
    java -jar GeneticSearchMKP.jar <file_path> <siz_of_the_intial_population> <no_of_iteration>
    
   ```
   *siz_of_the_intial_population* argument sets the initial population size to the given value. The default population size is 4. no_of_iteration specify the number of new children (chromosome) needed to be generated. The default value if 10000. 
 
  
 * **To run the Simulated Annealing based implementation: Go to ExecutableJarFiles directly and run the following command.**
 ```
  java -jar SimulatedAnnealingMKP.jar
  ```
 This will read the data from the file *data.pref* in the project directly and out put the result to the *resultOfSimulatedAnnealiing.preff* file.
 
 ```
  java -jar SimulatedAnnealingMKP.jar <file_path> 
  ```
  This will read the data from the file specified in the *file_path* and out put the result to the *resultOfSimulatedAnnealiing.pref* file. 
  ```
    java -jar SimulatedAnnealingMKP.jar <file_path> <initial_temperature_value> 
    
   ```
   *initial_temperature-value* argument sets the initial temperature value to the given value. Default temperature value is 10000.
    
## How to run source code.
* Open the project as a maven project. 
* Go to project directory and run the command `mvn clean install` to build the project.
* you can run the main class named with suffix **Main** in the each package except the commonMain package. This will read the data from "data.pref" file in the same directory and out put the result to the file name with **result** prefix.   
 
