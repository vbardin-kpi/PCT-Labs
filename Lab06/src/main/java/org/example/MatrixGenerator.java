package org.example;

import java.util.concurrent.ThreadLocalRandom;

public class MatrixGenerator {
    public int[][] generate(int rows, int columns, int upperLimit) {
        var matrix = new int[rows][columns];
        for (var i = 0; i < rows; i++) {
            for (var j = 0; j < columns; j++) {
                matrix[i][j] = ThreadLocalRandom.current().nextInt(upperLimit);
            }
        }
        return matrix;

    }
}
