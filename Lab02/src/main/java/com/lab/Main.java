package com.lab;

import com.lab.models.Matrix;
import com.lab.newfox.FoxCalculator;
import com.lab.stripe.CalculatorStripe;

import java.time.Duration;
import java.time.Instant;

public class Main {
    public static void main(String[] args) {
        MatrixGenerator matrixGenerator = new MatrixGenerator();

        int firstRows = 3;
        int secondCols = 3;

        Matrix first = new Matrix(matrixGenerator.generate(firstRows, firstRows, 5));
        Matrix second = new Matrix(matrixGenerator.generate(firstRows, secondCols, 5));

        CalculatorStripe calculatorStripe = new CalculatorStripe(first, second);
        FoxCalculator calculatorFox = new FoxCalculator(first, second, 40);

        Instant startStrip = Instant.now();
        var stripeRes = calculatorStripe.multiply();
        System.out.println("Strip: " + Duration.between(startStrip, Instant.now()).toMillis());

        Instant startFox = Instant.now();
        var foxResult = calculatorFox.multiply();
        System.out.println("Fox: " + Duration.between(startFox, Instant.now()).toMillis());
    }
}
