package com.zaana.ml;

import org.apache.log4j.Logger;

import java.util.LinkedList;

public abstract class AbstractPrediction
{
    static Logger LOG = Logger.getLogger(AbstractPrediction.class);

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
