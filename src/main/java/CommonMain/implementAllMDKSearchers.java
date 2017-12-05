package CommonMain;

import DynamicKnapsack.DynamicKnapsackSolver;
import DynamicKnapsack.Problem;
import DynamicKnapsack.SubProblem;
import GeneticSearchKnapsack.GeneticSearcher;
import GeneticSearchKnapsack.MKPImplemetationException;
import GeneticSearchKnapsack.Population;
import GeneticSearchKnapsack.RepairOperator;
import GeneticSearchKnapsack.State;
import GeneticSearchKnapsack.SurrogateMultiplierGenerator;
import SimulatedAnnealingKnapsack.RandomStateGenerator;
import SimulatedAnnealingKnapsack.SimulatedAnnealingSearcher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class implementing to compare the results of different MDK problem Solution methods.
 */
public class implementAllMDKSearchers {
    public static void main(String[] args)
            throws IOException, MKPImplemetationException, SimulatedAnnealingKnapsack.MKPImplemetationException {
        String inputFile = "./data.pref";
        long maximumValue = Long.valueOf(0);
        long startTime;
        long timeFinished;
        long timeDiff;

        //Dynamic approach
        String outPutFileD = "resultD.txt";
        String timeOutPutFileD = "timeD.txt";

        startTime = System.currentTimeMillis();

        //Initialize the problem
        DynamicKnapsack.Problem problem = new DynamicKnapsack.InputReader().readInput(inputFile);
        SubProblem subProblem = new SubProblem(DynamicKnapsack.Problem.noOfTasks - 1,
                DynamicKnapsack.Problem.boundaries);

        //Solve the Problem
        DynamicKnapsackSolver solver = new DynamicKnapsackSolver();
        SubProblem solution = solver.solveProblem(subProblem);
        solver.findSolution(solution.getScore(), 0, new ArrayList<Integer>(), 0);

        timeFinished = System.currentTimeMillis();
        timeDiff = timeFinished - startTime;
        System.out.println("Optimal Maximum Value:" + solution.getScore().toString());

        maximumValue = solution.getScore();

        //Output the results
        writeToFile("./optimumStateDS.txt", solution.getScore() + "\n");
        writeToFile("./totalTimeDS.txt", String.valueOf(timeDiff) + "\n");

        writeToFile("./problem.txt", String.valueOf(Problem.noOfTasks) + " "
                + String.valueOf(Problem.noOfResources)+ "\n");

        System.out.println("Finished Dynamic Search.");



        //Implementing genetic search.
        int count = 0;
        startTime = System.currentTimeMillis();

        //Create the problem using generated file input.
        GeneticSearchKnapsack.Problem problem2 = new GeneticSearchKnapsack.InputReader().readInput(inputFile);

        //Carry out LP realization of original problem to get pseudo-utility ratio.
        SurrogateMultiplierGenerator surrogateMultiplierGenerator = new SurrogateMultiplierGenerator();
        RepairOperator repairOperator = surrogateMultiplierGenerator.generateSortedTasksListBasedOnSUR();

        //Initialize the population.
        Population population = null;
        boolean nofesiableSolution = false;
        try {
            population = new Population(4);
        } catch (MKPImplemetationException e) {
            nofesiableSolution = true;
        }

        GeneticSearcher geneticSearcher = new GeneticSearcher();

        State repaired;

        long startTimeGeneticSearch = System.currentTimeMillis();

        if (!nofesiableSolution) {
        for (count = 0; count <= 10000; count++) {
            int j = 0;
            do {
                //generate state
                State state = geneticSearcher.generateNextState();

                //repair state.
                repaired = repairOperator.repairState(state);

            } while (Population.states.containsKey(repaired.formId));

            //add new state to the population.
            population.addState(repaired);

            if (maximumValue != 0 && maximumValue <= Population.maxState.fitnessScore) {
                break;
            }
        } }

        timeFinished = System.currentTimeMillis();

        Long genericSearchTime = timeFinished - startTimeGeneticSearch;
        Long totalTime = timeFinished - startTime;

        try {
            if (!nofesiableSolution) {
                writeToFile("./optimumStateGS.txt", Population.maxState.fitnessScore.toString() + "\n");
                writeToFile("./noOfIterationsGS.txt", String.valueOf(count) + "\n");
                writeToFile("./totalTimeGS.txt", String.valueOf(totalTime) + "\n");
                writeToFile("./searchTimeGS.txt", String.valueOf(genericSearchTime) + "\n");
            } else {
                writeToFile("./optimumStateGS.txt", String.valueOf(0) + "\n");
                writeToFile("./noOfIterationsGS.txt", String.valueOf(0) + "\n");
                writeToFile("./totalTimeGS.txt", String.valueOf(0) + "\n");
                writeToFile("./searchTimeGS.txt", String.valueOf(0) + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error while writing result to the files.");
        }

        System.out.println("Finished Genetic Search.");
        carryOutSASearcher(maximumValue);

    }

    protected static void writeToFile(String filePath, String data) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }

        FileWriter fileWriter = new FileWriter(file, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(data);
        bufferedWriter.close();
    }

    public static void carryOutSASearcher(long maximumValue) throws IOException, SimulatedAnnealingKnapsack.MKPImplemetationException
    {
        String inputFile = "./data.pref";

        SimulatedAnnealingKnapsack.Problem problem =
                new SimulatedAnnealingKnapsack.InputReader().readInput(inputFile);

        long  startTime = System.currentTimeMillis();
        //Carry out LP realization of original problem to get pseudo-utility ratio.
        SimulatedAnnealingKnapsack.SurrogateMultiplierGenerator surrogateMultiplierGenerator = new
                SimulatedAnnealingKnapsack.SurrogateMultiplierGenerator();
        SimulatedAnnealingKnapsack.RepairOperator repairOperator = surrogateMultiplierGenerator
                .generateSortedTasksListBasedOnSUR();

        long startTimeGeneticSearch = System.currentTimeMillis();
        //Generate a random state.
        SimulatedAnnealingKnapsack.State current = RandomStateGenerator.getConstrainedRandomState();

        Integer temperature = 10000;
        SimulatedAnnealingSearcher searcher = new SimulatedAnnealingSearcher();
        SimulatedAnnealingKnapsack.State next;

        while(temperature > 0){
            SimulatedAnnealingKnapsack.State temp = searcher.getNextState(current);
            next = repairOperator.repairState(temp);
            if(searcher.updateState(current, next, temperature)){
                current = next;
            }

            if (maximumValue != 0 && maximumValue <= current.fitnessScore) {
                break;
            }
            temperature--;
        }

        long timeFinished = System.currentTimeMillis();

        Long genericSearchTime = timeFinished - startTimeGeneticSearch;
        Long totalTime = timeFinished - startTime;

        System.out.println("Optimum State: \n" + current.toString());
        System.out.println("Total Number of iterations: " + (10000-temperature));
        System.out.println("Total time taken: " + totalTime);
        System.out.println("Time taken to genetic search: " + genericSearchTime);

        try {
            writeToFile("./optimumStateSA.txt", current.toString() + "\n");
            writeToFile("./noOfIterationsSA.txt", String.valueOf(10000-temperature) + "\n");
            writeToFile("./totalTimeSA.txt", String.valueOf(totalTime) + "\n");
            writeToFile("./searchTimeSA.txt", String.valueOf(genericSearchTime) + "\n");
        } catch (IOException e) {
            System.out.println("Error while writing result to the files.");
        }

        System.out.println("Finished Analytic Search.");

    }
}
