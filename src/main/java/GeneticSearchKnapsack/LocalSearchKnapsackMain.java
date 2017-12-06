package GeneticSearchKnapsack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Class implementing main method for genetic search method.
 */
public class LocalSearchKnapsackMain {

    public static void main(String[] args) throws IOException, MKPImplemetationException {

        String inputFile = "./data.pref";
        String outPutFile = "resultOfGenetic.pref";
        boolean noFesiableSolution = false;
        int initialPopulation = 4;
        int noOfIteration = 10000;
        int count = 0;

        try {
            if (null != args[0] && !args[0].isEmpty()) {
                inputFile = args[0];
            }

            if (null != args[1] && !args[1].isEmpty()) {
                initialPopulation = Integer.parseInt(args[1]);
            }

            if (null != args[2] && !args[2].isEmpty()) {
                noOfIteration = Integer.parseInt(args[2]);
            }

        } catch (Exception e) {
            System.out.println("If input arguments are not given then, default"
                    + "values are taken.");
        }

        //Create the problem using generated file input.
        Problem problem = new InputReader().readInput(inputFile);

        //Carry out LP realization of original problem to get pseudo-utility ratio.
        SurrogateMultiplierGenerator surrogateMultiplierGenerator = new SurrogateMultiplierGenerator();
        RepairOperator repairOperator = surrogateMultiplierGenerator.generateSortedTasksListBasedOnSUR();

        //Initialize the population.
        Population population = null;
        try {
           population = new Population(initialPopulation);
        } catch (MKPImplemetationException e) {
            noFesiableSolution = true;
        }

        GeneticSearcher geneticSearcher = new GeneticSearcher();

        //Carryout genetic search operation.
        State repaired;

        if (!noFesiableSolution) {
        for (count = 0; count <= noOfIteration; count++) {
            boolean expire = false;
            int j = 0;
            do {
                //generate state
                State state = geneticSearcher.generateNextState();

                //repair state.
                repaired = repairOperator.repairState(state);

                if(j > 100) {
                    expire = true;
                }
                j++;

            } while (Population.states.containsKey(repaired.formId) || !expire);

            //add new state to the population.
            population.addState(repaired);
        } }

        //Output the results
        OutputWriter writer = new OutputWriter();

        if (!noFesiableSolution) {
            writer.writeOutput(Population.maxState.form, outPutFile);
        } else {
            if (Population.states.size() != 0) {
                //if it is impossible to fill the initial population then get the state with maximum fitness as optimum
                //solution
                List<Integer> maxForm = new ArrayList<>();
                long maxFitness = 0;
                for (Map.Entry<Long, State> entry : Population.states.entrySet()) {
                    if (maxFitness <= entry.getValue().fitnessScore) {
                        maxForm = entry.getValue().form;
                    }
                }
                writer.writeOutput(maxForm, outPutFile);
            } else {
                 throw new MKPImplemetationException("Couldn't initialize the initial population. Please"
                         + "reduce the population size and retry");
            }
        }
    }
}
