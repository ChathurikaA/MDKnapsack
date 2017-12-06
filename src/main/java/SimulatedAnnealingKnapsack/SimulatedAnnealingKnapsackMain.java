package SimulatedAnnealingKnapsack;


import java.io.IOException;

public class SimulatedAnnealingKnapsackMain {

    public static void main(String... args) throws IOException, MKPImplemetationException {

        String inputFile = "./data.pref";
        String outPutFile = "resultOfSimulatedAnnealiing.pref";
        Integer temperature = 10000;

        try {
            if(null != args[0] && !args[0].isEmpty()) {
                inputFile = args[0];
            }
            if (null != args[1] && !args[1].isEmpty()) {
                temperature = Integer.parseInt(args[1]);
            }

        } catch (Exception e) {
            System.out.println("If inputs arguments are not given, then"
                    + " default"
                    + "values are taken.");
        }

        Problem problem = new InputReader().readInput(inputFile);

        //Carry out LP realization of original problem to get pseudo-utility ratio.
        SurrogateMultiplierGenerator surrogateMultiplierGenerator = new SurrogateMultiplierGenerator();
        RepairOperator repairOperator = surrogateMultiplierGenerator.generateSortedTasksListBasedOnSUR();

        //Generate a random state.
        State current = RandomStateGenerator.getConstrainedRandomState();

        SimulatedAnnealingSearcher searcher = new SimulatedAnnealingSearcher();
        State next;

        State maxState = current;
        while(temperature > 0) {
            State temp = searcher.getNextState(current);
            next = repairOperator.repairState(temp);
            if(searcher.updateState(current, next, temperature)){
                current = next;
                if (maxState.fitnessScore < current.fitnessScore) {
                    maxState = current;
                }
            }
            temperature--;
        }

        SimulatedAnnealingKnapsack.OutputWriter outputWriter = new SimulatedAnnealingKnapsack.OutputWriter();
        outputWriter.writeOutput(maxState.form, outPutFile);
    }
}
