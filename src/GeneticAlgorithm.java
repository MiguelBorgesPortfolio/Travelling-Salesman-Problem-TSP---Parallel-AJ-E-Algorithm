
import java.util.Arrays;
import java.util.Random;

public class GeneticAlgorithm extends Thread{

    private int[][] distanceMatrix;
    private int populationSize;
    private double mutationProbability;

    private int[] bestPath;
    private int totalDistance;
    private long maxRunningTimeMillis;

    public GeneticAlgorithm(int[][] distanceMatrix, int populationSize, double mutationProbability,long maxRunningTimeMillis) {
        this.distanceMatrix = distanceMatrix;
        this.populationSize = populationSize;
        this.mutationProbability = mutationProbability;
        this.maxRunningTimeMillis = maxRunningTimeMillis;
    }

   // public void run(long maxRunningTimeMillis) {
    @Override
   public void run() {
        long startTime = System.currentTimeMillis();
        long elapsedTime = 0;

        int[][] population = initializePopulation();
        int iteration = 0;

        while (elapsedTime < maxRunningTimeMillis) {
            int[][] evaluatedPopulation = evaluatePopulation(population);
            int[][] selectedPopulation = selectPopulation(evaluatedPopulation, population);
            int[][] offspringPopulation = generateOffspring(selectedPopulation);
            population = replacePopulation(population, evaluatedPopulation, offspringPopulation);

            iteration++;
            elapsedTime = System.currentTimeMillis() - startTime;
        }

        int[][] evaluatedPopulation = evaluatePopulation(population);
        bestPath = getBestPath(population, evaluatedPopulation);
        totalDistance = calculateTotalDistance(bestPath);
       // System.out.println("Best Path: " + arrayToString(bestPath));
        //System.out.println("Total Distance: " + totalDistance);
       // System.out.println(iteration);
    }

    private int[][] initializePopulation() {
        int[][] population = new int[populationSize][distanceMatrix.length];

        for (int i = 0; i < populationSize; i++) {
            population[i] = getRandomPath();
        }

        return population;
    }

    private int[] getRandomPath() {
        int[] path = new int[distanceMatrix.length];
        for (int i = 0; i < path.length; i++) {
            path[i] = i + 1;
        }

        for (int i = path.length - 1; i > 0; i--) {
            int j = (int) (Math.random() * (i + 1));
            int temp = path[i];
            path[i] = path[j];
            path[j] = temp;
        }

        return path;
    }

    private int[][] evaluatePopulation(int[][] population) {
        int[][] evaluatedPopulation = new int[populationSize][2];

        for (int i = 0; i < populationSize; i++) {
            int[] path = population[i];
            int totalDistance = calculateTotalDistance(path);
            evaluatedPopulation[i][0] = i;
            evaluatedPopulation[i][1] = totalDistance;
        }

        Arrays.sort(evaluatedPopulation, (a, b) -> Integer.compare(a[1], b[1]));

        return evaluatedPopulation;
    }

    private int[][] selectPopulation(int[][] evaluatedPopulation, int[][] population) {
        int[][] selectedPopulation = new int[populationSize][distanceMatrix.length];

        for (int i = 0; i < populationSize / 2; i++) {
            int index = evaluatedPopulation[i][0];
            selectedPopulation[i] = Arrays.copyOf(population[index], population[index].length);
        }

        return selectedPopulation;
    }

    private int[][] generateOffspring(int[][] selectedPopulation) {
        int[][] offspringPopulation = new int[populationSize][distanceMatrix.length];

        for (int i = 0; i < populationSize; i += 2) {
            int parent1Index = i % (populationSize / 2);
            int parent2Index = (i + 1) % (populationSize / 2);

            int[] parent1 = selectedPopulation[parent1Index];
            int[] parent2 = selectedPopulation[parent2Index];

            int[] child1 = pmx(parent1, parent2);
            int[] child2 = pmx(parent2, parent1);

            if (Math.random() <= mutationProbability) {
                child1 = exchangeMutation(child1);
            }

            if (Math.random() <= mutationProbability) {
                child2 = exchangeMutation(child2);
            }

            offspringPopulation[i] = child1;
            offspringPopulation[i + 1] = child2;
        }

        return offspringPopulation;
    }

    private int[] pmx(int[] parent1, int[] parent2) {
        int n = parent1.length;
        int[] child = new int[n];

        Random rand = new Random();
        int cuttingPoint1 = rand.nextInt(n);
        int cuttingPoint2 = rand.nextInt(n);

        while (cuttingPoint1 == cuttingPoint2) {
            cuttingPoint2 = rand.nextInt(n);
        }

        if (cuttingPoint1 > cuttingPoint2) {
            int temp = cuttingPoint1;
            cuttingPoint1 = cuttingPoint2;
            cuttingPoint2 = temp;
        }

        for (int i = cuttingPoint1; i <= cuttingPoint2; i++) {
            child[i] = parent1[i];
        }

        for (int i = 0; i < n; i++) {
            if (i < cuttingPoint1 || i > cuttingPoint2) {
                int gene = parent2[i];
                while (contains(child, gene, cuttingPoint1, cuttingPoint2)) {
                    gene = parent2[indexOf(parent1, gene)];
                }
                child[i] = gene;
            }
        }

        return child;
    }

    private int[] exchangeMutation(int[] path) {
        int mutationPoint1 = (int) (Math.random() * path.length);
        int mutationPoint2;

        do {
            mutationPoint2 = (int) (Math.random() * path.length);
        } while (mutationPoint1 == mutationPoint2);

        int temp = path[mutationPoint1];
        path[mutationPoint1] = path[mutationPoint2];
        path[mutationPoint2] = temp;

        return path;
    }

    private int[][] replacePopulation(int[][] population, int[][] evaluatedPopulation, int[][] offspringPopulation) {
        int[][] newPopulation = new int[populationSize][distanceMatrix.length];

        for (int i = 0; i < populationSize / 2; i++) {
            int index = evaluatedPopulation[i + populationSize / 2][0];
            newPopulation[i] = Arrays.copyOf(offspringPopulation[index], offspringPopulation[index].length);
        }

        for (int i = populationSize / 2; i < populationSize; i++) {
            int index = evaluatedPopulation[i][0];
            newPopulation[i] = Arrays.copyOf(population[index], population[index].length);
        }

        return newPopulation;
    }

    public int[] getBestPath() {
        return Arrays.copyOf(bestPath, bestPath.length);
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    private int[] getBestPath(int[][] population, int[][] evaluatedPopulation) {
        int index = evaluatedPopulation[0][0];
        return Arrays.copyOf(population[index], population[index].length);
    }

    private int calculateTotalDistance(int[] path) {
        int totalDistance = 0;
        for (int i = 0; i < path.length - 1; i++) {
            totalDistance += distanceMatrix[path[i] - 1][path[i + 1] - 1];
        }
        totalDistance += distanceMatrix[path[path.length - 1] - 1][path[0] - 1];
        return totalDistance;
    }

    private String arrayToString(int[] array) {
        StringBuilder sb = new StringBuilder();
        for (int value : array) {
            sb.append(value).append(" ");
        }
        return sb.toString().trim();
    }

    private boolean contains(int[] array, int value, int start, int end) {
        for (int i = start; i <= end; i++) {
            if (array[i] == value) {
                return true;
            }
        }
        return false;
    }

    private int indexOf(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }
}
