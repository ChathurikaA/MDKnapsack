package SimulatedAnnealingKnapsack;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class implementing to store state of the generated population.
 */
public class Population {
    public static Map<Long, State> states = new HashMap<Long, State>();
    public static State maxState;
    public static State minState;
    public static Integer size = new Integer(1);

    public Population(Integer size) {
        this.size = size;
        initialize();
        updateProbability();
        setMaxState();
        setMinState();
    }

    public Population() {
        initialize();
        updateProbability();
        setMaxState();
        setMinState();
    }

    /**
     * Initialize the population with random states.
     */
    public void initialize() {
        int i = 0;

        while (i < this.size) {
            State state;
            do {
                state = RandomStateGenerator.getConstrainedRandomState();
            } while (this.states.containsKey(state.formId));
            states.put(state.formId, state);
            i++;
        }
    }

    /**
     * Set the state with the maximum score.
     */
    public void setMaxState() {
        Set<Long> keys = this.states.keySet();
        Long maxFitnessScore = new Long(0);
        for (Long key : keys) {
            if (this.states.get(key).fitnessScore >= maxFitnessScore) {
                maxFitnessScore = this.states.get(key).fitnessScore;
                this.maxState = this.states.get(key);
            }
        }
    }

    /**
     * Set the state with the minimum fitness score
     */
    public void setMinState() {
        Set<Long> keys = this.states.keySet();
        Long minFitnessScore = Long.MAX_VALUE;
        for (Long key : keys) {
            if (this.states.get(key).fitnessScore <= minFitnessScore) {
                minFitnessScore = this.states.get(key).fitnessScore;
                this.minState = this.states.get(key);
            }
        }
    }

    /**
     * Update probability for each state based on the population.
     */
    public void updateProbability() {
        Set<Long> keys = this.states.keySet();
        Long total = new Long(0);
        for (Long key : keys) {
            total += this.states.get(key).fitnessScore;
        }

        for (Long key : keys) {
            this.states.get(key).setProbability((long) Math.round(this.states.get(key).fitnessScore * 100 / total));
        }
    }

}
