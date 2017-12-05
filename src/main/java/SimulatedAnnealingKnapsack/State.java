package SimulatedAnnealingKnapsack;

import java.util.List;

/**
 * Class Implemented to store the state
 */
public class State {
   public List<Integer> form;
   public Long formId;
   public Long fitnessScore;
   public Long probability;

    public State(List<Integer> form, Long formId) {
        this.form = form;
        this.formId = formId;
        this.fitnessScore = calculateFitnessScore(form);
        this.probability = new Long(100);
    }

    public static Long calculateFitnessScore(List<Integer> form) {
        Long fitnessScore = new Long(0);
        for (int i = 0; i < Problem.numberOfTasks; i++) {
            fitnessScore += form.get(i) * Problem.scoreOfTasks.get(i);
        }
        return fitnessScore;
    }

    public void setProbability(Long probability) {
        this.probability = probability;
    }

    public String toString() {
        return "Form:" + this.form.toString() + "\nForm Id:" + this.formId.toString() + "\nFitness Score:"
                + this.fitnessScore.toString() + "\nProbability:" + this.probability.toString() + "\n";
    }
}
