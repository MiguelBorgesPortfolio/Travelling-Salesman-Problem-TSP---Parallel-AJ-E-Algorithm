import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Main {
    public static void main(String[] args) {
        String fileName = "tsp_tests/fri26.txt";
        int populationSize = 80;
        double mutationProbability = 0.001;
        long maxRunningTimeSeconds = 360;


        int[][] distanceMatrix = readMatrixFromFile(fileName);

        BaseVersion baseVersion = new BaseVersion(12, distanceMatrix, populationSize, mutationProbability,maxRunningTimeSeconds);
        /*try {
            baseVersion.runGeneticAlgorithm(maxRunningTimeSeconds * 1000);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    private static int[][] readMatrixFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String firstLine = br.readLine();
            if (firstLine == null || firstLine.trim().isEmpty()) {
                System.err.println("Error: Empty or invalid first line in the file.");
                return null;
            }

            int numCities = Integer.parseInt(firstLine.trim());

            int[][] distanceMatrix = new int[numCities][numCities];
            for (int i = 0; i < numCities; i++) {
                String[] distances = br.readLine().trim().split("\\s+");
                for (int j = 0; j < numCities; j++) {
                    distanceMatrix[i][j] = Integer.parseInt(distances[j]);
                }
            }
            return distanceMatrix;
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }
}

