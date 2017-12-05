package SimulatedAnnealingKnapsack;

public class DistributedRandomStateGenerator {
    /**
     * Select random state from the distributed population.
     *
     * @return Max State from
     */
    public static State selectState() {
        Integer randomNumber = (int) Math.round(Math.random() * 100);
        Long offset = new Long(0);

        for (Long key : Population.states.keySet()) {
            if (randomNumber <= Population.states.get(key).probability + offset)
                return Population.states.get(key);
            else
                offset += Population.states.get(key).probability;
        }
        return Population.maxState;
    }
}
