package DynamicKnapsack;

import java.io.IOException;
import java.util.ArrayList;
/**
 * Class implementing main method for dynamic programming method.
 */
public class DynamicKnapsackMain {

    public static void main(String... args) throws IOException {

        String inputFile = "./data.pref";
        String outPutFile = "resultOfDynamicProgramming.pref";

        try {
            if(null != args[0] && !args[0].isEmpty()) {
                inputFile = args[0];
            }
        } catch (Exception e) {
            System.out.println("If all input arguments are not given default"
                    + "values are taken.");
        }

        //Intialize the problem
        Problem problem = new InputReader().readInput(inputFile);
        SubProblem subProblem = new SubProblem(Problem.noOfTasks - 1, Problem.boundaries);

        //Solve the Problem
        DynamicKnapsackSolver solver = new DynamicKnapsackSolver();
        SubProblem solution = solver.solveProblem(subProblem);
        solver.findSolution(solution.getScore(), 0, new ArrayList<Integer>(), 0);

        //Output the results
        OutputWriter writer = new OutputWriter();
        writer.writeOutput(solver.getGlobalSolution(), outPutFile);
    }
}
