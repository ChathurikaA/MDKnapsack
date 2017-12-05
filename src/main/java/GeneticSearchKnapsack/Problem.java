package GeneticSearchKnapsack;

import java.util.List;

/**
 * Class implemeting to store the MultiDimensionalKnapsack Problem.
 */
public class Problem {
    public static Integer numberOfTasks;
    public static Integer numberOfResources;
    public static List<Integer> scoreOfTasks;
    public static List<List<Integer>> weights;
    public static List<Integer> boundaries;

    private Problem() {
    }

    public Problem(Integer noOfTasks, Integer noOfResources, List<Integer> values, List<List<Integer>> weights,
            List<Integer> boundaries) {
        this.numberOfTasks = noOfTasks;
        this.numberOfResources = noOfResources;
        this.scoreOfTasks = values;
        this.weights = weights;
        this.boundaries = boundaries;
    }

    public String toString() {
        String form = "Problem================\nnumberOfTasks:" + this.numberOfTasks.toString() +
                "\nnumberOfResources:"
                + this.numberOfResources.toString() + "\n";
        form += "\nValues:" + scoreOfTasks.toString();
        form += "\nWeights:" + weights.toString();
        form += "\nBoundaries:" + boundaries.toString();
        return form;
    }
}
