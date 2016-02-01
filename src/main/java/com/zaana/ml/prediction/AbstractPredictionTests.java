package com.zaana.ml.prediction;

import org.apache.log4j.Logger;

import java.util.LinkedList;

public abstract class AbstractPredictionTests
{
    static Logger LOG = Logger.getLogger(AbstractPredictionTests.class);

    static LinkedList<Double> outputList;
    static LinkedList<Integer> targetList;
    static Double avg_candidate_set_size;

    public static LinkedList<Double> getOutputList()
    {
        return outputList;
    }

    public static LinkedList<Integer> getTargetList()
    {
        return targetList;
    }

    public static Double getAvg_candidate_set_size()
    {
        return avg_candidate_set_size;
    }

}
