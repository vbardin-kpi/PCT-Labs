package org.example;

import java.util.Arrays;

public class MatrixHelpers {
    public static int[] flattenMatrix(int[][] matrix) {
        var flat = new int[matrix.length * matrix[0].length];
        for (var i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, flat, i * matrix[0].length, matrix[0].length);
        }
        return flat;
    }

    public static int[][] reshapeMatrix(int[] flat, int rowLen) {
        var rows = flat.length / rowLen;
        var matrix = new int[flat.length / rowLen][rowLen];

        for (var i = 0; i < rows; i++) {
            System.arraycopy(flat, i * matrix[0].length, matrix[i], 0, rowLen);
        }

        return matrix;
    }

    public static int[][] getSubMatrix(int[][] matrix, int startRow, int endRow) {
        var subMatrix = new int[endRow - startRow][matrix[0].length];

        for (var i = 0; i < endRow - startRow; i++) {
            System.arraycopy(matrix[i], 0, subMatrix[i], 0, matrix[0].length);
        }

        return subMatrix;
    }

    public static void print(int[][] matrix) {
        Arrays.stream(matrix).forEach(arr -> {
            Arrays.stream(arr).forEach(elem -> System.out.print(elem + " "));
            System.out.println();
        });
    }
}
