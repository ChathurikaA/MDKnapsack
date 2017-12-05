package SimulatedAnnealingKnapsack;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Implemented to carryout simulatedAnnealing search.
 */
public class SimulatedAnnealingSearcher {
    public State getNextState(State state){

        //Random permutation
        Integer index = (int)Math.round(Math.random()*(Problem.numberOfTasks-1));

        List<Integer> form = new ArrayList<Integer>();
        form.addAll(state.form);

        if(form.get(index) == 1){
            form.remove((int)index);
            form.add(index, 0);
        }
        else{
            form.remove((int)index);
            form.add(index, 1);
        }

        Long formId = RandomStateGenerator.calculateId(form);
        return new State(form, formId);
    }

    public boolean updateState(State current, State next, Integer temperature){
        if(current.fitnessScore < next.fitnessScore) return true;
        else{
            Long energyDiff = current.fitnessScore - next.fitnessScore;
            double probability = Math.exp(-1*energyDiff/temperature);
            if(Math.random() <= probability){
                return true;
            }
            else{
                return false;
            }
        }
    }

    public boolean updateState(State current, State next){
        if(current.fitnessScore < next.fitnessScore) return true;
        return false;
    }
}
