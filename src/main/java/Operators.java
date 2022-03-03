import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Operators {

    public List<Integer> tournamentSelectionOperator(List<Cost> costList, List<Flow> flowList, List<List<Integer>>population, int N, int xAxis, int yAxis) {
        Random random = new Random();
        CostFunction costFunction = new CostFunction();
        List<Integer> bestIndividual = new ArrayList<>();
        Integer compareValue = Integer.MAX_VALUE;
        if (N > population.size()){
            N = population.size();
        }
        for (int i = 0; i < N; i++) {
            int drawedIndex = random.nextInt(population.size());
            List<Integer> potentialBestIndividual = population.get(drawedIndex);
            Integer costFun = costFunction.calculateCostFunction(costList,flowList,potentialBestIndividual, xAxis, yAxis);
            population.remove(drawedIndex);
            if (costFun < compareValue){
                compareValue = costFun;
                bestIndividual = potentialBestIndividual;
            }

        }

        return bestIndividual;
    }
}
