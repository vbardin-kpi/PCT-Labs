package com.lab;

import java.util.Random;

public class MatrixGenerator {
    Random random = new Random();

    public int[][] generate(int rows, int columns, int upperLimit) {
        int[][] response = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                response[i][j] = random.nextInt(upperLimit);
            }
        }
        return response;
    }
}
