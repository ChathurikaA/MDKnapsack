package SimulatedAnnealingKnapsack;

import java.util.ArrayList;
import java.util.List;

/**
 * Class implemented to carryout repair operation for the randomly generated State.
 */
public class RepairOperator {

    private static List<Integer> sortedTasksListBasedOnSUR;
    private List<Integer> form = new ArrayList<Integer>();
    private List<Integer> accumulatedResourcesList;

    private RepairOperator() {
    }

    /**
     * Constructor that take Sorted list of tasks in ascending order according to the pseudo-utility ratio.
     */
    protected RepairOperator(List<Integer> sortedTasksList) {
        this.sortedTasksListBasedOnSUR = sortedTasksList;
    }

    public State repairState(State state) {
        this.form = state.form;
        accumulatedResourcesList = new ArrayList<Integer>();
        generateAccumulatedResourcesMap();
        boolean isFeasible = isFeasibleState();

        if (isFeasible) {
            doAddOperation();
        } else {
            doDropOperation();
        }
        return new State(form, calculateId(form));
    }

    /**
     * Method to calculate the resource consumption.
     */
    private void generateAccumulatedResourcesMap() {
        for (int i = 0; i < Problem.numberOfResources; i++) {
            int accumulatedResource = 0;

            for (int j = 0; j < Problem.numberOfTasks; j++) {
                accumulatedResource = accumulatedResource + (form.get(j)) * (Problem.weights.get(i).get(j));
            }
            accumulatedResourcesList.add(accumulatedResource);
        }
    }

    /**
     * Check the the feasibility of the state.
     */
    private Boolean isFeasibleState() {
        int i = 0;
        boolean isFeasible = true;
        for (Integer value : accumulatedResourcesList) {
            if (value > Problem.boundaries.get(i)) {
                isFeasible = false;
            }
            if (!isFeasible) {
                break;
            }
            i++;
        }
        return isFeasible;
    }

    /**
     * Carry out drop operation if the state is infeasible.
     */
    private void doDropOperation() {
        boolean isFeasible = false;
        int arrayIndex = 0;
        while (!isFeasible) {
            int task = sortedTasksListBasedOnSUR.get(arrayIndex);
            if (form.get(task) == 1) {
                form.remove(task);
                form.add(task, 0);

                for (int i = 0; i < Problem.numberOfResources; i++) {
                    int total = accumulatedResourcesList.get(i) - Problem.weights.get(i).get(task);
                    accumulatedResourcesList.remove(i);
                    accumulatedResourcesList.add(i, total);
                }
                isFeasible = isFeasibleState();
            }
            arrayIndex++;
            if (arrayIndex == Problem.numberOfTasks) {
                break;
            }
        }
    }

    /**
     * Carry out add operation if the state is feasible.
     */
    private void doAddOperation() {
        boolean isFeasiable = true;
        int arrayIndex = 0;
        while (isFeasiable) {
            int task = sortedTasksListBasedOnSUR.get((Problem.numberOfTasks - 1) - arrayIndex);
            if (form.get(task) == 0) {
                for (int i = 0; i < Problem.numberOfResources; i++) {
                    int total = accumulatedResourcesList.get(i) + Problem.weights.get(i).get(task);
                    accumulatedResourcesList.remove(i);
                    accumulatedResourcesList.add(i, total);
                }

                isFeasiable = isFeasibleState();

                if (isFeasiable) {
                    form.remove(task);
                    form.add(task, 1);
                }
            }
            arrayIndex++;

            if (arrayIndex == Problem.numberOfTasks) {
                break;
            }
        }
    }

    /**
     * Calculate the form Id of the state based on the decimal value of the binary state.
    **/
    protected Long calculateId(List<Integer> values) {
        Long id = new Long(0);
        for (int i = Problem.numberOfTasks - 1; i >= 0; i--) {
            id += values.get(i) * (long) Math.pow(2, i);
        }
        return id;
    }
}