package com.lab.newfox;

import com.lab.models.Matrix;
import com.lab.models.Result;

import java.util.List;

public class AddThread implements Runnable {
    private final List<Matrix> toAdd;
    private final Result result;

    private final int row;
    private final int column;

    public AddThread(List<Matrix> toAdd, Result result, int row, int column) {
        this.toAdd = toAdd;
        this.result = result;
        this.row = row;
        this.column = column;
    }

    @Override
    public void run()
    {
        toAdd.forEach(matrix -> add(result.getMatrix(), matrix.getMatrix(), row, column));
    }

    private void add(int[][] result, int[][] add, int row, int column) {
        for (int i = 0; i < add.length; i++) {
            for (int j = 0; j < add[0].length; j++) {
                result[i+row][j+column] += add[i][j];
            }
        }
    }

}
