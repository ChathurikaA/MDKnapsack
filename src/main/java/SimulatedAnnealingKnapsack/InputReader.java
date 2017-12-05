package SimulatedAnnealingKnapsack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class implemented to read the data from input file and create Multi Dimensional Knapsack problem.
 */
public class InputReader {

    public Problem readInput(String pathName) throws IOException, MKPImplemetationException {
        BufferedReader reader= new BufferedReader(new FileReader(pathName));
        String line;

        Integer noOfTasks;
        Integer noOfResources;
        List<Integer> scoreOfTasks = new ArrayList<Integer>();
        List<List<Integer>> weights = new ArrayList<List<Integer>>();
        List<List<Integer>> temparyWeights = new ArrayList<List<Integer>>();
        List<Integer> boundaries = new ArrayList<Integer>();

        try {
            noOfTasks = Integer.parseInt(reader.readLine());
            noOfResources = Integer.parseInt(reader.readLine());

            for(int i = 0; i < noOfTasks; i++){
                line = reader.readLine();
                String[] words= line.split(" ");
                scoreOfTasks.add(Integer.parseInt(words[0]));

                List<Integer> weightsForTask = new ArrayList<Integer>();
                for(int j = 1; j<= noOfResources; j++){
                    weightsForTask.add(Integer.parseInt(words[j]));
                }
                temparyWeights.add(weightsForTask);
            }

            //format the weight as standard matrix
            for(int i = 0; i <noOfResources; i++) {
                List<Integer> coeffList = new ArrayList<Integer>();

                for (int j = 0; j < noOfTasks; j++) {
                    coeffList.add(temparyWeights.get(j).get(i));
                }
                weights.add(coeffList);
            }

            line = reader.readLine();
            String[] words = line.split(" ");
            for(int i =0; i < noOfResources; i++){
                boundaries.add(Integer.parseInt(words[i]));
            }
            return new Problem(noOfTasks, noOfResources, scoreOfTasks, weights, boundaries);
        }catch (Exception e){
            throw new MKPImplemetationException("Input file is not in the correct format", e);
        }
    }

}
