package DynamicKnapsack;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class OutputWriter extends GeneticSearchKnapsack.OutputWriter {
    public void writeOutput(List<Integer> solution, String filePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

        for(int i = 0; i < Problem.noOfTasks; i++){
            if(solution.contains(i)){
                writer.write("1\n");
            }
            else{
                writer.write("0\n");
            }
        }
        writer.close();
        System.out.println("Success");
    }
}


