package com.zaana.ml;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by maytekin on 26.05.2015.
 */
public class ExperimentsRun {

        static Logger LOG = Logger.getLogger(ExperimentsRun.class);
        private ExperimentsRun() {
        }

        static HashMap<String, HashMap<String, Integer>> userRateMap = null;
        private static HashMap<String, HashMap<String, Integer>> itemRateMap = null;
        private static HashMap<String, HashMap<String, Integer>> testDataMap = null;

        //static String dataFilePath = "data/amazon/ratings70-60";
        //static String dataFileBase = "data/amazon/amazon";
        static String dataFilePath = "data/ymusic/ymusic.data";
        static String dataFileBase = "data/ymusic/ymusic";
        //static String dataFilePath = "data/100k/ml.data";
        //static String dataFileBase = "data/100k/ml";
        //static String dataFilePath = "data/amovies-50-20.data";
        //static String dataFileBase = "data/amovies";
        static final String seperator = "\\t";
        static int topN = 20;
        static int kNN = 20;
        static int y = 20; // significance value. Must not be 0!
        static int numOfRun = 10;
        static final int smoothRun = 3;
        // l: number of bands
        // k: number of hash functions
        static int l = 4;
        static int k = 4;

        public static void main(final String[] args) throws IOException
        {
            Scanner scanner = new Scanner(System.in);
            String selection = null;
            do {
                System.out.println("");
                System.out.println("Options");
                System.out.println("===================================");
                System.out.println("02 - Run parameter CV tests");
                System.out.println("1 - Set k-NN ( " + kNN + " )");
                System.out.println("");
                System.out.println("Parameter CV Tests");
                System.out.println("50  - User-based - Prediction & k test");
                System.out.println("52  - Item-based - Prediction & k test");
                System.out.println("500 - User-based - Prediction - & y test");
                System.out.println("520 - Item-based  - Prediction - & y test");
                System.out.println("");
                System.out.println("58  - UBLSH - Prediction - & k test");
                System.out.println("580 - IBLSH - Prediction - & k test");
                System.out.println("");
                System.out.println("Experimental Tests");
                System.out.println("");
                System.out.println("10  - Model Build Time - All");
                System.out.println("");
                System.out.println("5000 - Run HashTables & HashFunctions - Prediction - tests");
                System.out.println("56  - UBLSH - HashTables Prediction - test");
                System.out.println("560 - IBLSH - HashTables Prediction - test");
                System.out.println("57  - UBLSH - HashFunctions Prediction - test");
                System.out.println("570 - IBLSH - HashFunctions Prediction - test");
                System.out.println("100 - User-based - Prediction - test");
                System.out.println("101 - Item-based - Prediction - test");
                System.out.println("59 - UBLSH - Prediction - 2D test");
                System.out.println("83 - IBLSH - Prediction - 2D test");
                System.out.println("");
                System.out.println("99 - Exit");
                System.out.println("===================================");
                System.out.println("Enter a command number to execute:");

                selection = scanner.nextLine();
                LOG.info("Selected menu: " + selection);
                Run.runSelection(selection, dataFilePath, dataFileBase, scanner, seperator, userRateMap, numOfRun, smoothRun, kNN, k, l, y, topN);

            } while (!selection.equals("99"));

            scanner.close();

        }
}
