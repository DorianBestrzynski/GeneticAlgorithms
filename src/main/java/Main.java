import org.javatuples.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        GenerateRandomPopulation gen = new GenerateRandomPopulation();
        CostFunction costFunction = new CostFunction();
        Operators operators = new Operators();
        var populationEasy = gen.generateRandomPopulation(20, 3, 3, false);
        var populationFlat = gen.generateRandomPopulation(20, 1, 12, false);
        var populationHard = gen.generateRandomPopulation(20, 5, 6, true);

        DataLoader dataLoader = new DataLoader();
        List<Cost> costListEasy = dataLoader.mapCostToObject("easy_cost.json");
        List<Flow> flowListEasy = dataLoader.mapFlowToObject("easy_flow.json");

        List<Cost> flatCost = dataLoader.mapCostToObject("flat_cost.json");
        List<Flow> flatFlow = dataLoader.mapFlowToObject("flat_flow.json");

        List<Cost> hardCost = dataLoader.mapCostToObject("hard_cost.json");
        List<Flow> hardFlow = dataLoader.mapFlowToObject("hard_flow.json");
        List<Integer> resultEasy = costFunction.calculateAllPopulation(costListEasy, flowListEasy, populationEasy, 3, 3);

        List<Integer> bestIndividualEasy = operators.tournamentSelectionOperator(costListEasy,flowListEasy,populationEasy, 25, 3, 3);
        List<Integer> bestIndividualFlat = operators.tournamentSelectionOperator(flatCost,flatFlow,populationFlat, 12, 1, 12);
        List<Integer> bestIndividualHard = operators.tournamentSelectionOperator(hardCost,hardFlow,populationHard, 25, 5, 6);

        List<Integer> resultFlat = costFunction.calculateAllPopulation(flatCost, flatFlow, populationFlat, 1, 12);
        List<Integer> resultHard = costFunction.calculateAllPopulation(hardCost, hardFlow, populationHard, 5, 6);



        //List<Integer> fCostList = costFunction.calculateCostFunction(flatCost,flatFlow, population);
        System.out.println("sdsa");

    }
}
