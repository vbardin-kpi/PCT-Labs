package com.lab.stripe;

import com.lab.models.Matrix;
import com.lab.models.Result;

public class MultiplierThreadStripe implements Runnable {
    private final Matrix lhs;
    private final Matrix rhs;
    private final Result result;
    private final int rowIndex;
    private int columnIndex;
    private final int columnCount;

    public MultiplierThreadStripe(Matrix lhs, Matrix rhs, Result result, int rowIndex, int columnIndex) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.result = result;

        columnCount = rhs.getMatrix()[0].length;
    }

    @Override
    public void run() {
        int[] row = lhs.getRow(rowIndex);

        for (int i = 0; i < columnCount; i++) {
            int elem = 0;

            columnIndex = normalizeColumnIndex(columnIndex);
            int[] column = rhs.getColumn(columnIndex);

            for (int j = 0; j < row.length; j++) {
                elem += row[j] * column[j];
            }

            result.append(rowIndex, columnIndex, elem);
        }
    }

    private int normalizeColumnIndex(int col) {
        int resp = col - 1;
        return resp >= 0 ? resp : columnCount - 1;
    }
}
