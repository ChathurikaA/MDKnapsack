package DynamicKnapsack;

import java.util.List;

/**
 * class implemented to store sub problem for Muti-dimensional knapsack problem at each iteration.
 */
public class SubProblem {
    Integer taskIndex;
    Integer score;
    List<Integer> boundryValues;
    //List<String> completor;

    public SubProblem(Integer taskIndex, Integer score, List<Integer> boundryValues) {
        this.taskIndex = taskIndex;
        this.score = score;
        this.boundryValues = boundryValues;
    }

    public SubProblem(Integer taskIndex, List<Integer> boundryValues) {
        this.taskIndex = taskIndex;
        this.score = 0;
        this.boundryValues = boundryValues;
        //this.completor = new ArrayList<String>();
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getScore() {
        return score;
    }

    public void setBoundryValues(List<Integer> boundryValues) {
        this.boundryValues = boundryValues;
    }

    public List<Integer> getBoundryValues() {
        return boundryValues;
    }

    public void setTaskIndex(Integer taskIndex) {
        this.taskIndex = taskIndex;
    }

    public Integer getTaskIndex() {
        return taskIndex;
    }

    @Override public String toString() {
        String form = "Sub Problem==================\nTask Index:" + this.taskIndex.toString() + "\nBoundary Values:\n"
                + this.boundryValues.toString() + "\n";
        return form;
    }
}
