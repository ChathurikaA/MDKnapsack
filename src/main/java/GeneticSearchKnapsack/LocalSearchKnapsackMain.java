package GeneticSearchKnapsack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Class implementing main method for genetic search method.
 */
public class LocalSearchKnapsackMain {

    public static void main(String[] args) throws IOException, MKPImplemetationException {

        String inputFile = "./data.pref";
        String outPutFile = "result.pref";
        boolean nofesiableSolution = false;
        int initialPopulation = 4;
        int count = 0;

        try {
            if (null != args[0] && !args[0].isEmpty()) {
                inputFile = args[0];
            }

            if (null != args[1] && !args[1].isEmpty()) {
                outPutFile = args[1];
            }

            if (null != args[2] && !args[2].isEmpty()) {
                initialPopulation = Integer.parseInt(args[2]);
            }

        } catch (Exception e) {
            System.out.println("If all input arguments are not given default"
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
            nofesiableSolution = true;
//            if(Population.states.size() == 1 && Population.size != 1) {
//                nofesiableSolution = true;
//            } else {
//                throw new MKPImplemetationException(e);
//            }
        }

        GeneticSearcher geneticSearcher = new GeneticSearcher();

        //Carryout genetic search operation.
        State repaired;

        if (!nofesiableSolution) {
        for (count = 0; count <= 10000; count++) {
            boolean expire = false;
            int j = 0;
            do {
                //generate state
                State state = geneticSearcher.generateNextState();

                //repair state.
                repaired = repairOperator.repairState(state);

                if(j > 10) {
                    expire = true;
                }
                j++;

            } while (Population.states.containsKey(repaired.formId) || !expire);

            //add new state to the population.
            population.addState(repaired);
        } }

        //Output the results
        OutputWriter writer = new OutputWriter();

        if (!nofesiableSolution) {
            writer.writeOutput(Population.maxState.form, outPutFile);
        } else {
            List<Integer> zeroStateform = new ArrayList<>();
            IntStream.range(0, Problem.numberOfTasks).forEach(i -> {
                zeroStateform.add(9);
            });
          // writer.writeOutput((Population.states.get(0)).form, outPutFile);
        }
    }
}
