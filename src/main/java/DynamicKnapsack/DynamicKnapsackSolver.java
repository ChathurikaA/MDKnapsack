package DynamicKnapsack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class implemented to solve each sub problem and to get the optimum state.
 */
public class DynamicKnapsackSolver {

    private static Map<String, Integer> globalTable = new HashMap<>();
    private static List<Integer> globalSolution = new ArrayList<>();

    public SubProblem solveProblem(SubProblem subProblem) {
        Integer maxValue = new Integer(0);
        List<Integer> boundryValues = subProblem.boundryValues;
        String key = subProblem.taskIndex.toString();
        for (Integer value : boundryValues) {
            key += "+" + value.toString();
        }
        if (subProblem.taskIndex == -1) {
            subProblem.setScore(0);
            return subProblem;
        } else if (boundryValues.contains(0)) {
            subProblem.setScore(0);
            return subProblem;
        } else if (globalTable.containsKey(key)) {
            subProblem.setScore(globalTable.get(key));
            return subProblem;
        } else {
            List<Integer> updatedBoundryValues = new ArrayList<Integer>();
            int i;
            for (i = 0; i < boundryValues.size(); i++) {
                if ((boundryValues.get(i) - Problem.weights.get(subProblem.taskIndex).get(i)) < 0)
                    break;
                updatedBoundryValues.add(boundryValues.get(i) - Problem.weights.get(subProblem.taskIndex).get(i));
            }

            if (i != boundryValues.size()) {
                SubProblem subSubProblem = new SubProblem(subProblem.taskIndex - 1, boundryValues);
                maxValue = solveProblem(subSubProblem).getScore();
            } else {
                SubProblem subSubProblem1 = new SubProblem(subProblem.taskIndex - 1, boundryValues);
                SubProblem subSubProblem2 = new SubProblem(subProblem.taskIndex - 1, updatedBoundryValues);
                maxValue = Math.max(solveProblem(subSubProblem1).getScore(),
                        solveProblem(subSubProblem2).getScore() + Problem.values.get(subProblem.taskIndex));
            }

            globalTable.put(key, maxValue);
            subProblem.setScore(maxValue);
            return subProblem;
        }
    }

    public void findSolution(Integer maxValue, Integer checkPoint, List<Integer> solution, Integer partialSum) {
        List<Integer> tempSolution = new ArrayList<Integer>();
        if (partialSum == (int) maxValue) {
            if (checkFeasiblity(solution)) {
                globalSolution = solution;
                return;
            }
        } else if (partialSum > maxValue) {
            return;
        }

        for (int i = checkPoint; i < Problem.values.size(); i++) {
            Integer value = Problem.values.get(i);
            tempSolution.clear();
            tempSolution.addAll(solution);
            tempSolution.add(i);
            findSolution(maxValue, i + 1, tempSolution, partialSum + value);
        }
    }

    public boolean checkFeasiblity(List<Integer> solution) {
        List<Integer> boundryValues = new ArrayList<Integer>();
        boundryValues.addAll(Problem.boundaries);

        for (int i = 0; i < solution.size(); i++) {
            for (int j = 0; j < Problem.noOfResources; j++) {
                Integer value = boundryValues.get(j) - Problem.weights.get(i).get(j);
                if (value >= 0) {
                    boundryValues.remove((int) j);
                    boundryValues.add(j, value);
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Integer> getGlobalSolution() {
        return globalSolution;
    }
}
