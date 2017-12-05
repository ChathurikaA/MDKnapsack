package SimulatedAnnealingKnapsack;


import java.io.IOException;

public class SimulatedAnnealingKnapsackMain {

    public static void main(String... args) throws IOException, MKPImplemetationException {

        String inputFile = "./data.pref";
        String outPutFile = "result.pref";
        Integer temperature = 10000;

        try {
            if(null != args[0] && !args[0].isEmpty()) {
                inputFile = args[0];
            }
            if(null != args[1] && !args[1].isEmpty()) {
                outPutFile = args[0];
            }
            if (null != args[2] && !args[2].isEmpty()) {
                temperature = Integer.parseInt(args[2]);
            }

        } catch (Exception e) {
            System.out.println("If all input arguments are not given "
                    + "default"
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

        while(temperature > 0){
            State temp = searcher.getNextState(current);
            next = repairOperator.repairState(temp);
            if(searcher.updateState(current, next, temperature)){
                current = next;
            }
            temperature--;
        }

        SimulatedAnnealingKnapsack.OutputWriter outputWriter = new SimulatedAnnealingKnapsack.OutputWriter();
        outputWriter.writeOutput(current.form, outPutFile);
    }

}
