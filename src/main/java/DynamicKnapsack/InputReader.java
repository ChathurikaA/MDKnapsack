package DynamicKnapsack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class implemented to write the optimum state to a file.
 */
public class InputReader {

    public Problem readInput(String pathName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(pathName));
        String line;

        Integer noOfTasks;
        Integer noOfResources;
        List<Integer> values = new ArrayList<Integer>();
        List<List<Integer>> weights = new ArrayList<List<Integer>>();
        List<Integer> boundaries = new ArrayList<Integer>();

        try {
            noOfTasks = Integer.parseInt(reader.readLine());
            noOfResources = Integer.parseInt(reader.readLine());

            for (int i = 0; i < noOfTasks; i++) {
                line = reader.readLine();
                String[] words = line.split(" ");
                values.add(Integer.parseInt(words[0]));

                List<Integer> weightsForTask = new ArrayList<Integer>();
                for (int j = 1; j <= noOfResources; j++) {
                    weightsForTask.add(Integer.parseInt(words[j]));
                }
                weights.add(weightsForTask);
            }

            line = reader.readLine();
            String[] words = line.split(" ");
            for (int i = 0; i < noOfResources; i++) {
                boundaries.add(Integer.parseInt(words[i]));
            }
            return new Problem(noOfTasks, noOfResources, values, weights, boundaries);
        } catch (Exception e) {
            System.out.print("Input file is not in the correct format");
        }

        return null;
    }
}
