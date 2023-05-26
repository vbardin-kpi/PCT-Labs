package com.lab.stripe;

import com.lab.models.Matrix;
import com.lab.models.Result;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CalculatorStripe {
    private final Matrix lhs;
    private final Matrix rhs;

    public CalculatorStripe(Matrix lhs, Matrix rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    public Result multiply() {
        var lhsRows = lhs.getMatrix().length;
        var rhsCols = rhs.getMatrix()[0].length;

        var result = new Result(new int[lhsRows][rhsCols]);
        var executor = Executors.newFixedThreadPool(6);

        var i = 1;
        while (lhsRows - i >= 0 || rhsCols - i >= 0) {
            executor.submit(new MultiplierThreadStripe(lhs, rhs, result, lhsRows - i, rhsCols - i));
            i++;
        }

        try {
            executor.shutdown();
            executor.awaitTermination(100L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }
}
