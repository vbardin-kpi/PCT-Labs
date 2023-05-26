package com.lab.models;

public class Result extends Base {

    public Result(int[][] matrix) {
        super(matrix);
    }

    public void append(int row, int col, int value) {
        this.matrix[row][col] = value;
    }
}
