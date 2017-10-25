package com.thoersch.seeds.machinelearning;

/**
 * Created by britt on 25/10/2017.
 */

import java.io.*;
import weka.core.*;

/**
 * Builds an arff dataset from the documents in a given directory.
 * Assumes that the file names for the documents end with ".txt".
 *
 * Usage:<p/>
 *
 * TextDirectoryToArff <directory path> <p/>
 *
 * @author Richard Kirkby (rkirkby at cs.waikato.ac.nz)
 * @version 1.0
 */
public class TextDirectoryToArff {

    public Instances createDataset(String directoryPath) throws Exception {

        // Declare two numeric attributes
        Attribute Attribute1 = new Attribute("id");
        Attribute Attribute2 = new Attribute("content");

        // Declare a nominal attribute along with its values
        FastVector category = new FastVector(5);
        category.addElement(0);
        category.addElement(1);
        category.addElement(2);
        category.addElement(3);
        category.addElement(4);
        Attribute Attribute3 = new Attribute("category", category);

        Attribute Attribute4 = new Attribute("question_id");

        // Declare the feature vector
        FastVector fvWekaAttributes = new FastVector(4);
        fvWekaAttributes.addElement(Attribute1);
        fvWekaAttributes.addElement(Attribute2);
        fvWekaAttributes.addElement(Attribute3);
        fvWekaAttributes.addElement(Attribute4);

        // Create an empty training set
        Instances trainingSet = new Instances("Training", fvWekaAttributes, 10);
        // Set class index
        trainingSet.setClassIndex(3);

        File dir = new File(directoryPath);
        String[] files = dir.list();
        for (int i = 0; i < files.length; i++) {
            if (files[i].endsWith(".txt")) {
                try {
                    double[] newInst = new double[2];
                    newInst[0] = (double)trainingSet.attribute(0).addStringValue(files[i]);
                    File txt = new File(directoryPath + File.separator + files[i]);
                    InputStreamReader is;
                    is = new InputStreamReader(new FileInputStream(txt));
                    StringBuffer txtStr = new StringBuffer();
                    int c;
                    while ((c = is.read()) != -1) {
                        txtStr.append((char)c);
                    }
                    newInst[1] = (double)trainingSet.attribute(1).addStringValue(txtStr.toString());
                    trainingSet.add(new Instance(1.0, newInst));
                } catch (Exception e) {
                    System.err.println("failed to convert file: " + directoryPath + File.separator + files[i]);
                }
            }
        }
        return trainingSet;
    }

    public static void main(String[] args) {

        TextDirectoryToArff tdta = new TextDirectoryToArff();
        try {
            Instances dataset = tdta.createDataset(File.separator + "spring-boot-rest-api-seed-master" + File.separator + "trainingdata");
            System.out.println(dataset);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

    }
}