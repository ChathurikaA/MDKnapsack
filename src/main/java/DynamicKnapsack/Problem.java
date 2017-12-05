package DynamicKnapsack;

import java.util.List;

public class Problem {
    public static Integer noOfTasks;
    public static Integer noOfResources;
    public static List<Integer> values;
    public static List<List<Integer>> weights;
    public static List<Integer> boundaries;

    public Problem(Integer noOfTasks, Integer noOfResources, List<Integer> values, List<List<Integer>> weights, List<Integer> boundaries){
        this.noOfTasks = noOfTasks;
        this.noOfResources = noOfResources;
        this.values = values;
        this.weights = weights;
        this.boundaries = boundaries;
    }

    public String toString(){
        String form = "Problem================\nnumberOfTasks:" + this.noOfTasks.toString() + "\nnumberOfResources:" + this.noOfResources.toString() + "\n";
        form += "\nValues:" + values.toString();
        form += "\nWeights:" + weights.toString();
        form += "\nBoundaries:" + boundaries.toString();
        return form;
    }
}
