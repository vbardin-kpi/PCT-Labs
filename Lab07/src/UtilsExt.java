import java.util.Random;

public class UtilsExt {
    private static final Random random = new Random();

    public static void fillMatrix(double[][] matrix, int startRow, int endRow) {
        for (var i = startRow; i < endRow; i++) {
            for (var j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = random.nextDouble() * 10;
                //[i][j] = 1;

            }
        }
    }

    public static double[] flatten(double[][] matrix, int startRow, int endRow) {
        int rows = endRow - startRow;
        int cols = matrix[0].length;
        double[] array = new double[rows * cols];
        for (int i = startRow; i < endRow; i++) {
            System.arraycopy(matrix[i], 0, array, (i - startRow) * cols, cols);
        }
        return array;
    }
}
