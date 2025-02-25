import java.text.DecimalFormat;
import java.util.concurrent.Semaphore;

public class BaseVersion extends Thread {
    private final int numThreads;
    private final int[][] distanceMatrix;
    private final GeneticAlgorithm[] geneticAlgorithms;
    private final Thread[] threads;
    private int populationSize;
    private double mutationProbability;
    private int bestDistance = Integer.MAX_VALUE;
    private int[] bestPathGlobal;
    private long maxRunningTimeMillis;
    private final Semaphore semaphore = new Semaphore(1);

    public BaseVersion(int numThreads, int[][] distanceMatrix, int populationSize, double mutationProbability,long maxRunningTimeMillis ) {
        this.numThreads = numThreads;
        this.distanceMatrix = distanceMatrix;
        this.geneticAlgorithms = new GeneticAlgorithm[numThreads];
        this.threads = new Thread[numThreads];
        this.populationSize = populationSize;
        this.mutationProbability = mutationProbability;
        this.maxRunningTimeMillis = maxRunningTimeMillis;

        // Inicializa cada instância do algoritmo genético
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < numThreads; i++) {
            geneticAlgorithms[i] = new GeneticAlgorithm(distanceMatrix, populationSize, mutationProbability, this.maxRunningTimeMillis);
            geneticAlgorithms[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            try {
                geneticAlgorithms[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        double totalExecutionTimeSeconds = (System.currentTimeMillis() - startTime) / 1000.0;


        DecimalFormat df = new DecimalFormat("#.####");
        String formattedExecutionTime = df.format(totalExecutionTimeSeconds);


        System.out.println("Best Path: " + arrayToString(bestPathGlobal));
        System.out.println("Best Distance: " + bestDistance);
        System.out.println("Total Execution Time: " + formattedExecutionTime + " seconds");
    }





/*
    public void runGeneticAlgorithm(long maxRunningTimeMillis) throws InterruptedException {
        long startTime = System.currentTimeMillis();

        // Cria e inicia uma thread para cada instância do algoritmo genético
        for (int i = 0; i < numThreads; i++) {
            final int threadIndex = i;  // Cria uma variável final local
            threads[i] = new Thread(() -> runGeneticAlgorithmInThread(threadIndex, maxRunningTimeMillis));
            threads[i].start();
        }

        // Aguarda o término de todas as threads
        for (int i = 0; i < numThreads; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        double totalExecutionTimeSeconds = (System.currentTimeMillis() - startTime) / 1000.0;

        // Formata o tempo total de execução para exibir quatro casas decimais
        DecimalFormat df = new DecimalFormat("#.####");
        String formattedExecutionTime = df.format(totalExecutionTimeSeconds);


        System.out.println("Best Path: " + arrayToString(bestPath));
        System.out.println("Best Distance: " + bestDistance);
        System.out.println("Total Execution Time: " + formattedExecutionTime + " seconds");
    }*/

   /* private void runGeneticAlgorithmInThread(int threadIndex, long maxRunningTimeMillis) {

        geneticAlgorithms[threadIndex].run(maxRunningTimeMillis);


        int[] localBestPath = geneticAlgorithms[threadIndex].getBestPath();
        int localBestDistance = geneticAlgorithms[threadIndex].getTotalDistance();


        try {
            semaphore.acquire();

            if (localBestDistance < bestDistance) {
                bestDistance = localBestDistance;
                bestPath = localBestPath;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }*/

    private String arrayToString(int[] array) {
        StringBuilder sb = new StringBuilder();
        for (int value : array) {
            sb.append(value).append(" ");
        }
        return sb.toString().trim();
    }
}
