package GeneticSearchKnapsack;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Implemented to carryout crossover and Random permutation operation.
 */
public class GeneticSearcher {
    public State generateNextState(){
        DistributedRandomStateGenerator distributedRandomStateGenerator = new DistributedRandomStateGenerator();

        //Select a pair from a Binary Tournament
        State state1 = distributedRandomStateGenerator.selectState();
        State state2 = distributedRandomStateGenerator.selectState();


        //Crossover Pair to generate a single state
        RandomStateGenerator randomStateGenerator = new RandomStateGenerator();
        State stateCrossover = randomStateGenerator.getRandomState();

        List<Integer> form= new ArrayList<Integer>();
        for(int i = 0; i < Problem.numberOfTasks; i++){
            if(stateCrossover.form.get(i) == 0){
                form.add(state1.form.get(i));
            }
            else{
                form.add(state2.form.get(i));
            }
        }

        //Random permutation
        Integer index = (int)Math.round(Math.random()*(Problem.numberOfTasks-1));
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

}
