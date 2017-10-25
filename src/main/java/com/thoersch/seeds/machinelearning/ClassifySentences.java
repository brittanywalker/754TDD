package com.thoersch.seeds.machinelearning;
import com.thoersch.seeds.persistence.forumposts.ForumPostsRepository;
import com.thoersch.seeds.persistence.forumposts.SentencesRepository;
import com.thoersch.seeds.representations.forumposts.ForumPost;
import com.thoersch.seeds.representations.forumposts.Sentences;
import com.thoersch.seeds.resources.forumposts.ForumPostsResource;
import com.thoersch.seeds.resources.forumposts.SentencesResource;
import org.springframework.beans.factory.annotation.Autowired;

import weka.classifiers.trees.J48;
import java.io.*;
import java.util.List;
import weka.core.*;

/**
 * Created by britt on 24/10/2017.
 */
public class ClassifySentences {

    @Autowired
    ForumPostsRepository forumPostsRepository;
    SentencesRepository sentencesRepository;

    List<Sentences> allPostSentences = null;

    public void classifyPostSentences(){

        TextDirectoryToArff tdta = new TextDirectoryToArff();
        Instances dataset;
        J48 classifier;

        try {
            dataset = tdta.createDataset(File.separator + "spring-boot-rest-api-seed-master" + File.separator + "trainingdata");
            System.out.println(dataset);
            String[] options = new String[1];
            options[0] = "-U";           // unpruned tree
            classifier = new J48();         // new instance of tree
            classifier.setOptions(options);
            if (dataset != null) {
                classifier.buildClassifier(dataset);  // build classifier and train on dataset
            }

            // Get all posts to split into sentences and then categorise.
            ForumPostsResource forumPostsResource = new ForumPostsResource(forumPostsRepository);
            List<ForumPost> posts = forumPostsResource.getPosts();
            Attribute Attribute2 = new Attribute("content");

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

        if (allPostSentences.size() > 0){
            saveSentences(allPostSentences);
        }
    }

    public void saveSentences(List<Sentences> sentencesList){

        SentencesResource sentencesResource = new SentencesResource(sentencesRepository);
        for (Sentences sentence : sentencesList){
            sentencesResource.saveSentence(sentence);
        }
    }
}
