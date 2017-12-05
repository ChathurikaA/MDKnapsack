package SimulatedAnnealingKnapsack;

import java.util.ArrayList;
import java.util.List;

/**
 * Class implementing RandomStateGenerator to generate random state
 */
public class RandomStateGenerator {

    /**
     * Generate Random State.
     * @return State Object.
     */
    public static State getRandomState() {
        List<Integer> stateValues = new ArrayList<Integer>();

        for (int i = 0; i < Problem.numberOfTasks; i++) {
            stateValues.add((int) (Math.round(Math.random())));
        }

        Long id = calculateId(stateValues);
        return new State(stateValues, id);
    }

    /**
     * Generate Random States within the constrained environment. (Random Feasible State is generated)
     * @return State Object.
     */
    public static State getConstrainedRandomState() {
        List<Integer> stateValues = new ArrayList<Integer>();
        Integer index = 0;
        List<Integer> boundryValues = new ArrayList<Integer>();
        boundryValues.addAll(Problem.boundaries);

        List<Integer> tempBoundryValues = new ArrayList<Integer>();

        for (int i = 0; i < Problem.numberOfTasks; i++) {
            stateValues.add(0);
        }

        for (int j = 0; j < Problem.numberOfTasks; j++) {
            do {
                index = (int) (Math.round(Math.random() * (Problem.numberOfTasks - 1)));
            } while (stateValues.get(index) == 1);

            int i;
            for (i = 0; i < Problem.numberOfResources; i++) {
                if ((boundryValues.get(i) - Problem.weights.get(i).get(index)) >= 0) {
                    tempBoundryValues.add(boundryValues.get(i) - Problem.weights.get(i).get(index));
                } else {
                    break;
                }
            }

            if (i == Problem.numberOfResources) {
                stateValues.remove((int) index);
                stateValues.add(index, 1);
                boundryValues.clear();
                boundryValues.addAll(tempBoundryValues);
                tempBoundryValues.clear();
            } else {
                break;
            }
        }

        Long id = calculateId(stateValues);
        return new State(stateValues, id);
    }

    /**
     * Calculate Form Id which is decimal value of the state when consider form of the state as binary sequence.
     * FormID help to identify state uniquely.
     * @param values List of binary scoreOfTasks to convert to decimal value.
     * @return id decimal converted value.
     */
    public static Long calculateId(List<Integer> values) {
        Long id = new Long(0);

        for (int i = Problem.numberOfTasks - 1; i >= 0; i--) {
            id += values.get(i) * (long) Math.pow(2, i);
        }
        return id;
    }
}
