package GeneticSearchKnapsack;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Class implemented to solve LP relaxation of the original MKP. In here, using shadow price of each variable,
 * surrogate-multipliers ara calculated. Then, pseudo-utility ratios are obtained using calculated surrogate-multipliers.
 */
public class SurrogateMultiplierGenerator {

    private LinearObjectiveFunction linearObjectiveFunction;
    private Collection<LinearConstraint> constraintsListForTask = new ArrayList<LinearConstraint>();
    private Collection<LinearConstraint> constraintsListForResources = new ArrayList<LinearConstraint>();
    private List<List<Double>> coeffAsDouble = new ArrayList<List<Double>>();
    private List<Double> fitnessAsDouble = new ArrayList<Double>();
    private List<Double> surrogatesMultipliers = new ArrayList<Double>();

    /**
     * Convert every coefficient to double value.
     */
    private void setCoeffInDouble() {
        for (int i = 0; i < Problem.numberOfResources; i++) {
            List<Double> list = new ArrayList<>();
            Problem.weights.get(i).forEach(j -> list.add(j.doubleValue()));
            coeffAsDouble.add(list);
        }

        Problem.scoreOfTasks.forEach(j -> fitnessAsDouble.add(j.doubleValue()));
    }

    /**
     * Objective function is defined.
     */
    private void setObjectiveFunction() {
        double[] array = new double[Problem.numberOfTasks];
        IntStream.range(0, array.length).forEach(i -> array[i] = fitnessAsDouble.get(i));
        linearObjectiveFunction = new LinearObjectiveFunction(array, 0.0);
    }

    /**
     * Set constraints for each tasks to convert the binary problem to LP relaxation problem.
     */
    private void setTaskConstraints() {
        for (int i = 0; i < Problem.numberOfTasks; i++) {
            double[] coeff = new double[Problem.numberOfTasks];
            coeff[i] = 1;
            constraintsListForTask.add(new LinearConstraint((coeff), Relationship.LEQ, 1));
        }
    }

    /**
     * Set constraints for each resources to convert the binary problem to LP relaxation problem.
     */
    private void setResourcesConstraints() {
        int count = 0;
        for (int i = 0; i < Problem.numberOfResources; i++) {
            double amount = Problem.boundaries.get(i).doubleValue();
            double[] array = new double[Problem.numberOfTasks];
            int finalI = i;
            IntStream.range(0, array.length).forEach(j -> {
                array[j] = (coeffAsDouble.get(finalI)).get(j);
            });
            constraintsListForResources.add(new LinearConstraint(array, Relationship.LEQ, amount));
        }
    }

    /**
     * Obtain the maximum value of the objective function using SimplexSolver API.
     */
    private Double getLPRelaxationSolution(int resourceIndex) throws MKPImplemetationException {
        Collection<LinearConstraint> allLinearConstraint = new ArrayList<>();
        allLinearConstraint.addAll(constraintsListForResources);
        allLinearConstraint.addAll(constraintsListForTask);
        if (resourceIndex >= 0 && resourceIndex < Problem.numberOfResources) {
            //allLinearConstraint.remove((Integer) resourceIndex);
            Double amount = Problem.boundaries.get(resourceIndex).doubleValue() + 1;
            double[] array = new double[Problem.numberOfTasks];
            IntStream.range(0, array.length).forEach(j -> {
                array[j] = (coeffAsDouble.get(resourceIndex).get(j));
            });
            allLinearConstraint.remove(new LinearConstraint(array, Relationship.LEQ, amount - 1));
            allLinearConstraint.add(new LinearConstraint(array, Relationship.LEQ, amount));
        }

        PointValuePair solution;

        try {
            solution = new SimplexSolver()
                    .optimize(linearObjectiveFunction, new LinearConstraintSet(allLinearConstraint), GoalType.MAXIMIZE,
                            new NonNegativeConstraint(true));
            return solution.getValue();
        } catch (Exception e) {
            throw new MKPImplemetationException("Error while getting optimum solution using Liner Relaxation", e);
        }
    }

    /**
     * Calculate the pseudo-utility ratio and store them in an array list in ascending order.
     *
     * @return Sorted list of tasks in ascending order according to the pseudo-utility ratio.
     * @throws MKPImplemetationException
     */
    public RepairOperator generateSortedTasksListBasedOnSUR() throws MKPImplemetationException {
        setCoeffInDouble();
        setTaskConstraints();
        setResourcesConstraints();
        setObjectiveFunction();

        try {
            double originalOptimalValue = getLPRelaxationSolution(-1);
            for (int i = 0; i < Problem.numberOfResources; i++) {
                Double optimum = getLPRelaxationSolution(i);
                Double shadowPrice = optimum - originalOptimalValue;
                surrogatesMultipliers.add(shadowPrice);
            }
        } catch (Exception e) {
            throw new MKPImplemetationException("Error while calculating the shadow price.", e);
        }

        Map<Integer, Double> pseudoUtilityRationMap = new HashMap<>();

        for (int i = 0; i < Problem.numberOfTasks; i++) {
            double sum = 0;
            for (int j = 0; j < Problem.numberOfResources; j++) {
                sum = sum + (coeffAsDouble.get(j).get(i)) * surrogatesMultipliers.get(j);
            }
            double psuedoUtilityRation = (Problem.scoreOfTasks.get(i) / sum);
            pseudoUtilityRationMap.put(i, psuedoUtilityRation);
        }

        return new RepairOperator(sortTaskBySUR(pseudoUtilityRationMap, true));
    }

    /**
     * Sort the tasks in ascending order according to the pseudo-utility ratio.
     *
     * @param unsortMap Map contains key as task and value as  pseudo-utility ratio
     * @param order     If true then sort by ascending order.
     * @return Sorted list of tasks in ascending order according to the pseudo-utility ratio.
     */
    private static List<Integer> sortTaskBySUR(Map<Integer, Double> unsortMap, final boolean order) {

        List<Map.Entry<Integer, Double>> list = new LinkedList<Map.Entry<Integer, Double>>(unsortMap.entrySet());
        // Sorting the list based on scoreOfTasks
        Collections.sort(list, new Comparator<Map.Entry<Integer, Double>>() {
            public int compare(Map.Entry<Integer, Double> o1, Map.Entry<Integer, Double> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());
                } else {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        Map<Integer, Double> sortedMap = new LinkedHashMap<Integer, Double>();
        for (Map.Entry<Integer, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        List<Integer> sortedTasksListBasedOnSUR = new ArrayList<>();
        sortedTasksListBasedOnSUR.addAll(sortedMap.keySet());

        return sortedTasksListBasedOnSUR;
    }
}

