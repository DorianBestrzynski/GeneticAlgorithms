
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, PythonExecutionException {
        GenerateRandomPopulation gen = new GenerateRandomPopulation();
        CostFunction costFunction = new CostFunction();
        Operators operators = new Operators();
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm();
        var populationEasy = gen.generateRandomPopulation(100, 3, 3, false);
        var populationFlat = gen.generateRandomPopulation(100, 1, 12, false);
        var populationHard = gen.generateRandomPopulation(100, 5, 6, true);

        DataLoader dataLoader = new DataLoader();
        List<Cost> costListEasy = dataLoader.mapCostToObject("easy_cost.json");
        List<Flow> flowListEasy = dataLoader.mapFlowToObject("easy_flow.json");

        List<Cost> flatCost = dataLoader.mapCostToObject("flat_cost.json");
        List<Flow> flatFlow = dataLoader.mapFlowToObject("flat_flow.json");

        List<Cost> hardCost = dataLoader.mapCostToObject("hard_cost.json");
        List<Flow> hardFlow = dataLoader.mapFlowToObject("hard_flow.json");
        ArrayList<Integer> finalList = new ArrayList<>();

        for (int i = 0 ; i< 10; i++) {
            List<Integer> resList = geneticAlgorithm.runGeneticAlgorithm(hardCost, hardFlow, populationHard, 5, 6, false);

            //List<Integer> resList = geneticAlgorithm.runRandomAlgorithm(hardCost,hardFlow,populationHard,5,6, false);
            finalList.add(Collections.min(resList));


        }

        int best = Collections.min(finalList);
        int worst = Collections.max(finalList);
        double avg = finalList.stream().mapToInt(val -> val).average().orElse(0.0);
        double std = operators.sd(finalList);






        //geneticAlgorithm.runGeneticAlgorithm(flatCost,flatFlow,populationFlat,1,12);
//        List<Integer> resultEasy = costFunction.calculateAllPopulation(costListEasy, flowListEasy, populationEasy, 3, 3);
//
       // List<Integer> bestIndividualEasy = operators.tournamentSelectionOperator(costListEasy,flowListEasy,populationEasy, 20, 3, 3);
        //List<Integer> secondBestIndividualEasy = operators.tournamentSelectionOperator(costListEasy,flowListEasy,populationEasy, 3, 3, 3);
        //var sth = operators.singlePointCrossover(bestIndividualEasy,secondBestIndividualEasy, 3,3);
//        List<Integer> bestIndividualFlat = operators.tournamentSelectionOperator(flatCost,flatFlow,populationFlat, 12, 1, 12);
//        List<Integer> bestIndividualHard = operators.tournamentSelectionOperator(hardCost,hardFlow,populationHard, 25, 5, 6);
//
//        List<Integer> resultFlat = costFunction.calculateAllPopulation(flatCost, flatFlow, populationFlat, 1, 12);
//        List<Integer> resultHard = costFunction.calculateAllPopulation(hardCost, hardFlow, populationHard, 5, 6);

       //List<Integer> bestIndividualRoulette = operators.rouletteSelectionOperator(costListEasy,flowListEasy,populationEasy, 3, 3, 6.5);

        //List<Integer> bestIndividualRoulette = operators.rouletteSelectionOperator2(costListEasy,flowListEasy,populationEasy, 3, 3, 2);

        //List<Integer> fCostList = costFunction.calculateCostFunction(flatCost,flatFlow, population);
        System.out.println("sdsa");

    }
}
