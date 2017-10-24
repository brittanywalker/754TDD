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

        FastVector atts = new FastVector(2);
        atts.addElement(new Attribute("filename", (FastVector) null));
        atts.addElement(new Attribute("contents", (FastVector) null));
        Instances data = new Instances("text_files_in_" + directoryPath, atts, 0);

        File dir = new File(directoryPath);
        String[] files = dir.list();
        for (int i = 0; i < files.length; i++) {
            if (files[i].endsWith(".txt")) {
                try {
                    double[] newInst = new double[2];
                    newInst[0] = (double)data.attribute(0).addStringValue(files[i]);
                    File txt = new File(directoryPath + File.separator + files[i]);
                    InputStreamReader is;
                    is = new InputStreamReader(new FileInputStream(txt));
                    StringBuffer txtStr = new StringBuffer();
                    int c;
                    while ((c = is.read()) != -1) {
                        txtStr.append((char)c);
                    }
                    newInst[1] = (double)data.attribute(1).addStringValue(txtStr.toString());
                    data.add(new Instance(1.0, newInst));
                } catch (Exception e) {
                    System.err.println("failed to convert file: " + directoryPath + File.separator + files[i]);
                }
            }
        }
        return data;
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


//        if (args.length == 1) {}
//        else {
//            System.out.println("Usage: java TextDirectoryToArff <directory name>");
//        }
    }
}
