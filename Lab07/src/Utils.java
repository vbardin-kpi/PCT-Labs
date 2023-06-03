import java.util.Random;

public class Utils {
    private static final Random random = new Random();

    public static double[] flatten(double[][] matrix) {
        var rows = matrix.length;
        var cols = matrix[0].length;
        var array = new double[rows * cols];

        for (var i = 0; i < rows; i++) {
            System.arraycopy(matrix[i], 0, array, i * cols, cols);
        }

        return array;
    }

    public static double[][] toMatrix(double[] array, int rows, int cols) {
        var matrix = new double[rows][cols];

        for (var i = 0; i < rows; i++) {
            System.arraycopy(array, i * cols, matrix[i], 0, cols);
        }

        return matrix;
    }

    public static void fillMatrix(double[][] matrix) {
        for (var i = 0; i < matrix.length; i++) {
            for (var j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = random.nextDouble() * 10;
            }
        }
    }

    public static void printMatrix(double[][] matrix) {
        for (double[] doubles : matrix) {
            for (double aDouble : doubles) {
                System.out.print(aDouble + " ");
            }

            System.out.println();
        }
    }
}
