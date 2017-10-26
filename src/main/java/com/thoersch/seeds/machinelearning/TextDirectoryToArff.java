package com.thoersch.seeds.machinelearning;

/**
 * Created by britt on 25/10/2017.
 */

import java.io.*;
import java.nio.file.Path;

import weka.core.*;
import weka.core.converters.ConverterUtils.DataSource;


/**
 * Builds an arff dataset from the documents in a given directory.
 * Assumes that the file names for the documents end with ".txt".
 *
 * Usage:<p/>
 *
 * TextDirectoryToArff <directory path> <p/>
 *
 * @author Richard Kirkby (rkirkby at cs.waikato.ac.nz)
 * modified by Brittany Walker
 * @version 1.0
 */
public class TextDirectoryToArff {

    public Instances createDataset() throws Exception {

        File f = new File("trainingdata" + File.separator + "training_data.arff");
        BufferedReader reader = new BufferedReader(
                new FileReader(f));
        Instances data = new Instances(reader);
        reader.close();

        return data;
    }
}
