package com.thoersch.seeds.machinelearning;
import com.thoersch.seeds.persistence.forumposts.ForumPostsRepository;
import com.thoersch.seeds.representations.forumposts.ForumPost;
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
        buildTrainClassifier();
        ForumPostsResource forumPostsResource = new ForumPostsResource(forumPostsRepository);
        List<ForumPost> posts = forumPostsResource.getPosts();

        for (ForumPost forumPost:posts){
            String content = forumPost.get_content();
            String[] sentences = content.split(".");

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
