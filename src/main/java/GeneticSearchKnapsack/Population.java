package GeneticSearchKnapsack;

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
    public static Integer size = new Integer(2);

    public Population(Integer size) throws MKPImplemetationException {
        this.size = size;
        initialize();
        updateProbability();
        setMaxState();
        setMinState();
    }

    public Population() throws MKPImplemetationException {
        initialize();
        updateProbability();
        setMaxState();
        setMinState();
    }

    /**
     * Initialize the population with random states.
     */
    private void initialize() throws MKPImplemetationException {
        RandomStateGenerator randomStateGenerator = new RandomStateGenerator();
        int i = 0;
        int expire;
        while(i < this.size){
            State state = randomStateGenerator.getConstrainedRandomState();
            expire = 0;
            do {
                state = randomStateGenerator.getConstrainedRandomState();
                expire ++;
            } while(this.states.containsKey(state.formId) && expire != 10000);

            states.put(state.formId, state);

            if(expire == 10000) {
                throw new MKPImplemetationException("Couldn't initialize the initial population of size '" + size + "'"
                        + " with feasible states. Please reduce the initial population size and retry.");
            }

            i++;
        }
    }

    /**
     * Set the state with the maximum score.
     */
    private void setMaxState() {
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
     * Set the state with the maximum score with a given new state.
     */
    private void setMaxState(State state) {
        if (state.fitnessScore >= this.maxState.fitnessScore) {
            this.maxState = state;
        }
    }

    /**
     * Set the state with the minimum fitness score
     */
    private void setMinState() {
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
    private void updateProbability() {
        Set<Long> keys = this.states.keySet();
        Long total = new Long(0);
        for (Long key : keys) {
            total += this.states.get(key).fitnessScore;
        }

        for (Long key : keys) {
            this.states.get(key).setProbability((long) Math.round(this.states.get(key).fitnessScore * 100 / total));
        }
    }

    /**
     * Add new state to the population by replacing minState
     *
     * @param state State Object need to be added.
     */
    public void addState(State state) {
        if (this.minState.fitnessScore < state.fitnessScore) {
            this.states.remove(this.minState.formId);
            this.states.put(state.formId, state);
            setMinState();
            setMaxState(state);
            updateProbability();
        }
    }
}
