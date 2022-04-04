import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.math.DoubleMath.mean;

public class Operators {

    public List<Integer> tournamentSelectionOperator(List<Cost> costList, List<Flow> flowList, List<List<Integer>> population, int N, int xAxis, int yAxis, boolean isFlat) {
        Random random = new Random();
        List<List<Integer>> tmp = new ArrayList<>(population);
        CostFunction costFunction = new CostFunction();
        List<Integer> bestIndividual = new ArrayList<>();
        Integer compareValue = Integer.MAX_VALUE;
        if (N > population.size()) {
            N = population.size();
        }
        for (int i = 0; i < N; i++) {
            int drawedIndex = random.nextInt(tmp.size());
            List<Integer> potentialBestIndividual = tmp.get(drawedIndex);
            Integer costFun = costFunction.calculateCostFunction(costList, flowList, potentialBestIndividual, xAxis, yAxis, isFlat);
            tmp.remove(drawedIndex);
            if (costFun < compareValue) {
                compareValue = costFun;
                bestIndividual = potentialBestIndividual;
            }

        }

        return bestIndividual;
    }

    public List<Integer> rouletteSelectionOperator(List<Cost> costList, List<Flow> flowList, List<List<Integer>> population, int xAxis, int yAxis, boolean isFlat) {
        Random random = new Random();
        List<Double> probabilityList = new ArrayList<>();
        CostFunction costFunction = new CostFunction();
        List<Integer> costFunctionList = costFunction.calculateAllPopulation(costList, flowList, population, xAxis, yAxis, isFlat);
        int sumOfCostFunctions = costFunctionList.stream().mapToInt(Integer::intValue).sum();
        int maxCostFunction = Collections.max(costFunctionList);

        for (int i = 0; i < costFunctionList.size(); i++) {
            double result = (double) costFunctionList.get(i) / (double) sumOfCostFunctions;
            double reversedProbability = (1 - result) / (costFunctionList.size() - 1);
            if (i > 0) {
                reversedProbability += probabilityList.get(i - 1);
            }

            probabilityList.add(reversedProbability);
        }
        adjustTopNumber(probabilityList);

        int drawedNumber = 0;
        double rouletteValue = random.nextDouble();
        for (var probability : probabilityList) {
            if (probability > rouletteValue) {
                drawedNumber = probabilityList.indexOf(probability);
                break;
            }
        }

        return population.get(drawedNumber);
    }

    public List<Integer> rouletteSelectionOperator2(List<Cost> costList, List<Flow> flowList, List<List<Integer>> population, int xAxis, int yAxis, double selectionPressure, boolean isFlat) {
        Random random = new Random();
        List<Double> probabilityList = new ArrayList<>();
        CostFunction costFunction = new CostFunction();
        List<Integer> costFunctionList = costFunction.calculateAllPopulation(costList, flowList, population, xAxis, yAxis, isFlat);
        List<Double> costProbsList = new ArrayList<>();

        //List<Integer> costFunctionList = new ArrayList<>();
//        costFunctionList.add(5500);
//        costFunctionList.add(6200);
//        costFunctionList.add(6300);
//        costFunctionList.add(8000);
//        costFunctionList.add(7300);


        double probsSum = 0;

        int maxCostFunction = Collections.max(costFunctionList);

        for (int i = 0; i < costFunctionList.size(); i++) {
            double expProb = Math.exp(-selectionPressure * costFunctionList.get(i) / maxCostFunction);
            costProbsList.add(expProb);
            probsSum += expProb;
        }

        for (int i = 0; i < costProbsList.size(); i++) {
            double probabilityExp = costProbsList.get(i) / probsSum;
            if (i > 0) {
                probabilityExp += probabilityList.get(i - 1);
            }
            probabilityList.add(probabilityExp);
        }

        adjustTopNumber(probabilityList);

        int drawedNumber = 0;
        double rouletteValue = random.nextDouble();
        for (var probability : probabilityList) {
            if (probability > rouletteValue) {
                drawedNumber = probabilityList.indexOf(probability);
                break;
            }
        }

        return population.get(drawedNumber);


    }

    private void adjustTopNumber(List<Double> probabilityList) {
        probabilityList.set(probabilityList.size() - 1, 1.0);
    }

    public List<List<Integer>> singlePointCrossover(List<Integer> firstIndividual, List<Integer> secondIndividual, int xAxis, int yAxis) {
        List<List<Integer>> resList = new ArrayList<>();
        firstIndividual = new ArrayList<>(firstIndividual);
        secondIndividual = new ArrayList<>(secondIndividual);
        Random random = new Random();
        int splitPoint = random.nextInt(firstIndividual.size());
        if (splitPoint == 0) splitPoint++;

        List<Integer> firstPartF = firstIndividual.subList(0, splitPoint);
        List<Integer> secondPartF = secondIndividual.subList(splitPoint, secondIndividual.size());
        List<Integer> finalFirstChild = new ArrayList<>(firstPartF);
        finalFirstChild.addAll(secondPartF);

        List<Integer> firstPartS = secondIndividual.subList(0, splitPoint);
        List<Integer> secondPartS = firstIndividual.subList(splitPoint, firstIndividual.size());
        List<Integer> finalSecondChild = new ArrayList<>(firstPartS);
        finalSecondChild.addAll(secondPartS);


        fixGenotypes(finalFirstChild, xAxis * yAxis);
        fixGenotypes(finalSecondChild, xAxis * yAxis);

        resList.add(finalFirstChild);
        resList.add(finalSecondChild);
        return resList;
    }

    private List<Integer> fixGenotypes(List<Integer> individual, int numberOfFields) {
        Random random = new Random();
        HashSet<Integer> positionsSet = new HashSet<>(individual);
        HashSet<Integer> currentSet = new HashSet<>();
        int index = 0;
        if (positionsSet.size() == individual.size()) return individual;

        for (int item : individual) {
            if (currentSet.contains(item)) {
                int newPosition = item;
                HashSet<Integer> drawnNumbers = new HashSet<>();
                while (positionsSet.contains(newPosition)) {
                    newPosition = random.nextInt(numberOfFields);
                    drawnNumbers.add(newPosition);
                    if (drawnNumbers.size() == numberOfFields) break;
                }
                positionsSet.add(newPosition);
                individual.set(index, newPosition);
            }
            index++;
            currentSet.add(item);
        }

        return individual;
    }

    public List<Integer> mutation(List<Integer> individual, float mutationProbability) {
        Random random = new Random();
        if (random.nextFloat() < mutationProbability) {
            swapRandomIndexes(individual);
        }
        return individual;
    }

    private void swapRandomIndexes(List<Integer> individual) {
        Random r = new Random();
        int firstIndex = r.nextInt(individual.size());
        int secondIndex = r.nextInt(individual.size());
        while (secondIndex == firstIndex) {
            secondIndex = r.nextInt(individual.size());
        }
        Collections.swap(individual, firstIndex, secondIndex);
    }

    public void printGraph(List<Integer> result, List<Integer> worst, List<Double> avg, int generationNum) throws PythonExecutionException, IOException {

        List<Integer> x = IntStream.range(0, generationNum).boxed().collect(Collectors.toList());

        Plot plt = Plot.create();
        plt.plot().add(x, result).linestyle("-").label("best");
        plt.plot().add(x, worst).linestyle("-").label("worst");
        plt.plot().add(x, avg).linestyle("-").label("avg");
        plt.legend().loc("upper right");
        plt.show();
    }

    public double sd (ArrayList<Integer> table)
    {
        // Step 1:
        double mean = mean(table);
        double temp = 0;

        for (int i = 0; i < table.size(); i++)
        {
            int val = table.get(i);

            // Step 2:
            double squrDiffToMean = Math.pow(val - mean, 2);

            // Step 3:
            temp += squrDiffToMean;
        }

        // Step 4:
        double meanOfDiffs = (double) temp / (double) (table.size());

        // Step 5:
        return Math.sqrt(meanOfDiffs);
    }
}


