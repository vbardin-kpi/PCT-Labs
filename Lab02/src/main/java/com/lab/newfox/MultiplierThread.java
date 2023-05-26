package com.lab.newfox;

import com.lab.models.Matrix;

import java.util.List;

public class MultiplierThread implements Runnable {
    private final List<Matrix> matrixList;
    private final Matrix lhs;
    private final Matrix rhs;

    private final Coordinates firstC;
    private final Coordinates secondC;

    public MultiplierThread(
            List<Matrix> matrixList,
            Matrix lhs,
            Matrix rhs,
            Coordinates lhsCoords,
            Coordinates rhsCoords) {
        this.matrixList = matrixList;
        this.lhs = lhs;
        this.rhs = rhs;
        this.firstC = lhsCoords;
        this.secondC = rhsCoords;
    }

    @Override
    public void run() {
        matrixList.add(new Matrix(multiply(lhs.getMatrix(), rhs.getMatrix())));
    }

    private int[][] multiply(int[][] lhs, int[][] rhs) {
        int lhsRows = firstC.endRow() - firstC.startRow();
        int rhsCols = secondC.endCol() - secondC.startCol();

        int[][] result = new int[lhsRows][rhsCols];

        for (int lhsRow = firstC.startRow(); lhsRow < firstC.endRow(); lhsRow++) {
            for (int rhsCol = secondC.startCol(); rhsCol < secondC.endCol(); rhsCol++) {
                int item = 0;

                for (int lhsCol = firstC.startCol(); lhsCol < firstC.endCol(); lhsCol++) {
                    item += lhs[lhsRow][lhsCol] * rhs[lhsCol][rhsCol];
                }

                result[lhsRow%lhsRows][rhsCol%rhsCols] = item;
            }
        }
        return result;
    }
}
