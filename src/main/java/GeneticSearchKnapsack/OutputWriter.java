package GeneticSearchKnapsack;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Class implemented to write the optimum state to a file.
 */
public class OutputWriter {
    public void writeOutput(List<Integer> solution, String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

        for (Integer value : solution) {
            writer.write(String.valueOf(value) + "\n");
        }
        writer.close();
        System.out.println("Success");
    }
}


