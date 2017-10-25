package com.thoersch.seeds.machinelearning;

import com.thoersch.seeds.persistence.forumposts.SentencesRepository;
import com.thoersch.seeds.resources.forumposts.SentencesResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.DBSCAN;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instances;

/**
 * Created by britt on 26/10/2017.
 */
public class ClusterIssues {

    @Autowired
    SentencesRepository sentencesRepository;
    SentencesResource sentencesResource = new SentencesResource(sentencesRepository);
    List<String> allSentences;

    public void createClusters(){

        ClassifySentences sentenceClassifier = new ClassifySentences();
        sentenceClassifier.classifyPostSentences();

        allSentences = sentencesResource.getIssueSentences();

        Attribute content = new Attribute("sentenceContent");
        FastVector attributes = new FastVector(1);
        attributes.addElement(content);
        Instances data = new Instances("QuestionContent", attributes, 100);

        ClusterEvaluation evaluation = new ClusterEvaluation();
        DBSCAN dbscan = new DBSCAN();
        dbscan.setEpsilon(0.12);
        dbscan.setMinPoints(5);
        try {
            dbscan.buildClusterer(data);
            evaluation.setClusterer(dbscan);
            evaluation.evaluateClusterer(data);
            System.out.println("num of clusters: " + evaluation.getNumClusters());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
