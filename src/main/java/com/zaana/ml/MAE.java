package com.zaana.ml;

import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.LinkedList;

public final class MAE
{

    static Logger LOG = Logger.getLogger(DataParser.class);

    private MAE() {}

    public static double calculateMAE(final LinkedList<?> outputList,
            final LinkedList<?> targetList)
    {
        LOG.info("Calculating errors...");
        double totalError = 0.0D;
        Iterator<?> outputIter = outputList.iterator();
        Iterator<?> targetIter = targetList.iterator();
        int size = outputList.size();

        while (outputIter.hasNext() && targetIter.hasNext()) {
            Object output = outputIter.next();
            Object target = targetIter.next();
            totalError += Math.abs((Double) output - (Integer) target);
        }

        if (size == 0) return size;
        else return totalError / size;

    }

}
