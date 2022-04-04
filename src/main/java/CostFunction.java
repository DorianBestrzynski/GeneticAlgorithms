
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CostFunction {

    public Integer calculateCostFunction(List<Cost> costs, List<Flow> flows, List<Integer> generatedData, int xAxis, int yAxis, boolean isFlat) {
        int distance = 0;


        if (!isFlat) {
            for (int j = 0; j < costs.size(); j++) {
                int manhattanDistance = computeManhattanDistance(generatedData.get(costs.get(j).getSource()) % yAxis, generatedData.get(costs.get(j).getSource()) / xAxis,
                        generatedData.get(costs.get(j).getDest()) % yAxis, generatedData.get(costs.get(j).getDest()) / xAxis);
                distance += manhattanDistance * costs.get(j).getCost() * flows.get(j).getAmount();
            }
        }
        else {
            for (int j = 0; j < costs.size(); j++) {
                int manhattanDistance = computeManhattanDistance(0, generatedData.get(costs.get(j).getSource()) / xAxis, 0, generatedData.get(costs.get(j).getDest()) / xAxis);
                distance += manhattanDistance * costs.get(j).getCost() * flows.get(j).getAmount();
            }
        }

        return distance;
    }

    private int computeManhattanDistance(int xi, int yi, int xj, int yj) {
        return Math.abs(xi - xj) + Math.abs(yi - yj);
    }

    public List<Integer> calculateAllPopulation(List<Cost> costs, List<Flow> flows, List<List<Integer>> generatedData, int xAxis, int yAxis, boolean isFlat) {
        List<Integer> returnList = new ArrayList<>();
        for (List<Integer> generatedDatum : generatedData) {
            returnList.add(calculateCostFunction(costs, flows, generatedDatum, xAxis, yAxis, isFlat));
        }
        return returnList;
    }
    }

