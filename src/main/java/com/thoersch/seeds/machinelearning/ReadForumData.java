package com.thoersch.seeds.machinelearning;
import weka.classifiers.trees.J48;
import java.io.*;
import weka.core.*;

/**
 * Created by britt on 24/10/2017.
 */
public class ReadForumData {

    //Create arff dataset from .txt files in trainingdata directory
    TextDirectoryToArff tdta = new TextDirectoryToArff();
    try {
        Instances dataset = tdta.createDataset(File.separator + "spring-boot-rest-api-seed-master" + File.separator + "trainingdata");
        System.out.println(dataset);
    } catch(Exception e) {
        System.err.println(e.getMessage());
        e.printStackTrace();
    }

    String[] options = new String[1];
    options[0] = "-U"           // unpruned tree
    J48 tree = new J48();         // new instance of tree
    tree.setOptions(options)    // set the options
    tree.buildClassifier(dataset);  // build classifier

//    // load data
//    ArffLoader loader = new ArffLoader();
//    loader.setFile(new File("/some/where/data.arff"));
//    Instances structure = loader.getStructure();
//    structure.setClassIndex(structure.numAttributes() - 1);
}
