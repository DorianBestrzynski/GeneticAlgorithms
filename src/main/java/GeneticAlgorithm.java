import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class GeneticAlgorithm {

    public List<Integer> runGeneticAlgorithm(List<Cost> costList, List<Flow> flowList, List<List<Integer>>population, int xAxis, int yAxis, boolean isFlat) throws PythonExecutionException, IOException {
        List<List<Integer>> newPopulation = new ArrayList<>();
        List<Integer> bestCostFunctionList = new ArrayList<>();
        List<Integer> worstCostFunctionList = new ArrayList<>();
        List<Double> avgCostFunctionList = new ArrayList<>();
        List<Integer> populationCostFunctionList = new ArrayList<>();
        CostFunction costFunction = new CostFunction();
        int counter = 1 ;
        int populationSize = population.size();
        Operators operators = new Operators();
        while (populationCostFunctionList.size() < 1000){
            if(counter > 1) {
                List<Integer> allPopulationCostFunctionList = costFunction.calculateAllPopulation(costList, flowList, newPopulation, xAxis, yAxis, isFlat);
                int bestCostFunction = Collections.min(allPopulationCostFunctionList);
                bestCostFunctionList.add(Collections.min(allPopulationCostFunctionList));
                worstCostFunctionList.add(Collections.max(allPopulationCostFunctionList));
                avgCostFunctionList.add(allPopulationCostFunctionList.stream().mapToInt(val -> val).average().orElse(0.0));
                populationCostFunctionList.add(bestCostFunction);
                System.out.println(counter + " population");
                System.out.println("Best cost function for this generation is: " + bestCostFunction);
                population = new ArrayList<>(newPopulation);
//                if (bestCostFunction == 4818){
//                    break;
//                }
            }
            counter++;
            newPopulation = new ArrayList<>();

            while(newPopulation.size() < populationSize) {
                //List<Integer> bestIndividual = operators.tournamentSelectionOperator(costList, flowList, population, 30, xAxis, yAxis, isFlat);
                //List<Integer> secondBestIndividual = operators.tournamentSelectionOperator(costList, flowList, population, 30, xAxis, yAxis, isFlat);
                //List<Integer> bestIndividual = operators.rouletteSelectionOperator(costList,flowList,population,xAxis,yAxis);
                //List<Integer> secondBestIndividual = operators.rouletteSelectionOperator(costList,flowList,population,xAxis,yAxis);
                List<Integer> bestIndividual = operators.rouletteSelectionOperator2(costList,flowList,population,xAxis,yAxis,20, isFlat);
                List<Integer> secondBestIndividual = operators.rouletteSelectionOperator2(costList,flowList,population,xAxis,yAxis,20, isFlat);
                List<List<Integer>> children = operators.singlePointCrossover(bestIndividual,secondBestIndividual, xAxis,yAxis);
                for(var child : children){
                    operators.mutation(child,0.7f);
                }
                newPopulation.addAll(children);
            }
        }

        operators.printGraph(bestCostFunctionList, worstCostFunctionList, avgCostFunctionList, 1000);
        return populationCostFunctionList;
    }

    public List<Integer> runRandomAlgorithm(List<Cost> costList, List<Flow> flowList, List<List<Integer>>population, int xAxis, int yAxis, boolean isFlat) throws PythonExecutionException, IOException {
        List<Integer> bestCostFunctionList = new ArrayList<>();
        List<Integer> worstCostFunctionList = new ArrayList<>();
        List<Double> avgCostFunctionList = new ArrayList<>();
        List<Integer> populationCostFunctionList = new ArrayList<>();
        CostFunction costFunction = new CostFunction();
        Operators operators = new Operators();
        int i = 0;
        while (i < 1000){
            List<Integer> allPopulationCostFunctionList = costFunction.calculateAllPopulation(costList, flowList, population, xAxis, yAxis, isFlat);
            int bestCostFunction = Collections.min(allPopulationCostFunctionList);
            bestCostFunctionList.add(Collections.min(allPopulationCostFunctionList));
            worstCostFunctionList.add(Collections.max(allPopulationCostFunctionList));
            avgCostFunctionList.add(allPopulationCostFunctionList.stream().mapToInt(val -> val).average().orElse(0.0));
            GenerateRandomPopulation gen = new GenerateRandomPopulation();
            population = gen.generateRandomPopulation(population.size(), xAxis,yAxis,false);
            i++;
            System.out.println(bestCostFunction);
            populationCostFunctionList.add(bestCostFunction);
        }

        operators.printGraph(bestCostFunctionList, worstCostFunctionList, avgCostFunctionList, 1000);

        return populationCostFunctionList;

    }
}
