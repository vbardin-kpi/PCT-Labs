package com.lab.newfox;

import com.lab.models.Matrix;
import com.lab.models.Result;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FoxCalculator {

    private final Matrix lhs;
    private final Matrix rhs;
    private final int processingSize;
    private final int lhsRowsBlock;
    private final int rhsBlockRows;
    private final int rhsBlockCols;

    public FoxCalculator(Matrix lhs, Matrix rhs, int processingSize) {

        this.lhs = lhs;
        this.rhs = rhs;
        this.processingSize = processingSize;

        var lhsRows = lhs.getMatrix().length;
        var rhsRows = rhs.getMatrix().length;
        var rhsCols = rhs.getMatrix()[0].length;

        lhsRowsBlock = (int) Math.ceil((double) lhsRows / processingSize);
        rhsBlockRows = (int) Math.ceil((double) rhsRows / processingSize);
        rhsBlockCols = (int) Math.ceil((double) rhsCols / processingSize);
    }

    public Result multiply() {
        var lhsRows = lhs.getMatrix().length;
        var rhsCols = rhs.getMatrix()[0].length;

        var result = new Result(new int[lhsRows][rhsCols]);

        var executorAdd = Executors.newCachedThreadPool();
        for (var i = 0; i < processingSize; i++) {
            for (var j = 0; j < processingSize; j++) {
                var toAdd = getSubMatrices(i, j);
                executorAdd.submit(new AddThread(toAdd, result, i * lhsRowsBlock, j * rhsBlockCols));
            }
        }

        try {
            executorAdd.shutdown();
            executorAdd.awaitTermination(100L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

    private List<Matrix> getSubMatrices(int i, int j) {
        var executorMultiplier = Executors.newCachedThreadPool();

        List<Matrix> toAdd = Collections.synchronizedList(new ArrayList<>());

        for (int k = 0; k < processingSize; k++) {
            var lhsStartRow = i * lhsRowsBlock;
            var rhsStartCol = j * rhsBlockCols;
            var startC = k * rhsBlockRows;

            executorMultiplier.submit(new MultiplierThread(toAdd, lhs, rhs,
                    new Coordinates(lhsStartRow, startC, lhsStartRow + lhsRowsBlock, startC + rhsBlockRows),
                    new Coordinates(startC, rhsStartCol, startC + rhsBlockRows, rhsStartCol + rhsBlockCols)));
        }

        try {
            executorMultiplier.shutdown();
            executorMultiplier.awaitTermination(1L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return toAdd;
    }
}
