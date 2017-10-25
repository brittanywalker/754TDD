package com.thoersch.seeds.machinelearning;
import com.sun.xml.internal.bind.v2.TODO;
import com.thoersch.seeds.persistence.forumposts.ForumPostsRepository;
import com.thoersch.seeds.representations.forumposts.ForumPost;
import com.thoersch.seeds.representations.forumposts.Sentences;
import com.thoersch.seeds.resources.forumposts.ForumPostsResource;
import org.springframework.beans.factory.annotation.Autowired;

import weka.classifiers.trees.J48;
import java.io.*;
import java.util.List;
import weka.core.*;

/**
 * Created by britt on 24/10/2017.
 */
public class ReadForumData {

    @Autowired
    ForumPostsRepository forumPostsRepository;

    public void main(String[] args){

        TextDirectoryToArff tdta = new TextDirectoryToArff();
        Instances dataset = null;
        J48 classifier = null;

        try {
            dataset = tdta.createDataset(File.separator + "spring-boot-rest-api-seed-master" + File.separator + "trainingdata");
            System.out.println(dataset);
            String[] options = new String[1];
            options[0] = "-U";           // unpruned tree
            classifier = new J48();         // new instance of tree
            classifier.setOptions(options);
            if (dataset != null) {
                classifier.buildClassifier(dataset);  // build classifier
            }


            ForumPostsResource forumPostsResource = new ForumPostsResource(forumPostsRepository);
            List<ForumPost> posts = forumPostsResource.getPosts();
            Attribute Attribute2 = new Attribute("content");
            List<Sentences> allPostSentences = null;

            for (int i = 0; i < posts.size(); i++){
                String content = posts.get(i).get_content();
                String[] sentences = content.split(".");
                for (int j = 0; j < sentences.length; j++) {
                    Instance data = new Instance(1);
                    data.setValue(Attribute2, sentences[i]);
                    dataset.add(data);
                    double category = classifier.classifyInstance(data);

                    Sentences classifiedSentence = new Sentences(Long.valueOf(i+j), sentences[j], (int)category, posts.get(i).get_question_id());
                    allPostSentences.add(classifiedSentence);
                }
            }
        } catch(Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public static J48 buildTrainClassifier(){
        //Create arff dataset from .txt files in trainingdata directory
        TextDirectoryToArff tdta = new TextDirectoryToArff();
        Instances dataset;
        J48 classifier = new J48();

        try {
            dataset = tdta.createDataset(File.separator + "spring-boot-rest-api-seed-master" + File.separator + "trainingdata");
            System.out.println(dataset);
            String[] options = new String[1];
            options[0] = "-U";           // unpruned tree
            classifier = new J48();         // new instance of tree
            classifier.setOptions(options);
            if (dataset != null) {
                classifier.buildClassifier(dataset);  // build classifier
            }
        } catch(Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return classifier;
    }
}
