package com.example.spork.recommendation;

import org.apache.commons.math3.analysis.function.Sigmoid;
import org.apache.commons.math3.stat.StatUtils;

public class ZScore {
    public double[] compute(double[] values) {
        double[] newScores = new double[values.length];
        double variance = StatUtils.populationVariance(values);
        double sd = Math.sqrt(variance);
        double mean = StatUtils.mean(values);
        for (int i = 0; i < values.length; i++) {
            double stdScore = (values[i] - mean) / sd;
            newScores[i] = sigmoid(stdScore);
        }
        return newScores;
    }

    private double sigmoid(double x) {
        Sigmoid sigmoid = new Sigmoid(-1.5, 1.5);
        return sigmoid.value(x) + 3.5;
    }
}
