
import java.util.*;

public class GenerateRandomPopulation {

    public List<List<Integer>> generateRandomPopulation(int populationSize, int xAxis, int yAxis, boolean isHard) {
        List<List<Integer>> population = new ArrayList<>();
        List<Integer> populationList = new ArrayList<>(xAxis * yAxis);


        for (int i = 0; i < xAxis * yAxis; i++) {
            populationList.add(i);
        }

        for (int i = 0; i < populationSize; i++) {
             Collections.shuffle(populationList);
             List<Integer> tmp = new ArrayList<>(populationList);
            if (isHard){
                List<Integer> hardList = new ArrayList<>(24);
                for (int j = 0; j< 24; j++) {
                    hardList.add(tmp.get(j));
                }
                population.add(hardList);
            }
            else {

                population.add(tmp);
            }

        }

        return population;
    }



}

