package com.thoersch.seeds.machinelearning;

import com.thoersch.seeds.persistence.forumposts.SentencesRepository;
import com.thoersch.seeds.resources.forumposts.SentencesResource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.DBSCAN;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Created by britt on 26/10/2017.
 */
public class ClusterIssues {

    @Autowired
    SentencesRepository sentencesRepository;
    SentencesResource sentencesResource = new SentencesResource(sentencesRepository);
    HashMap<Long, String> allSentences;

    public void main(){

        ClassifySentences sentenceClassifier = new ClassifySentences();
        sentenceClassifier.classifyPostSentences();
        Attribute content = new Attribute("sentenceContent");
        Attribute question_id = new Attribute("questionId");
        FastVector attributes = new FastVector(2);
        attributes.addElement(content);
        attributes.addElement(question_id);
        Instances data = new Instances("QuestionContent", attributes, 100);

        allSentences = sentencesResource.getIssueSentences();
        Instance newData = new Instance(2);
        Iterator mapIterator = allSentences.entrySet().iterator();
        while (mapIterator.hasNext()) {
            Map.Entry pair = (Map.Entry)mapIterator.next();
            newData.setValue(content, pair.getValue().toString());
            newData.setValue(question_id, pair.getKey().toString());
            data.add(newData);
        }

        ClusterEvaluation evaluation = new ClusterEvaluation();
        DBSCAN dbscan = new DBSCAN();
        dbscan.setEpsilon(0.12);
        dbscan.setMinPoints(5);
        try {
            dbscan.buildClusterer(data);
            evaluation.setClusterer(dbscan);
            evaluation.evaluateClusterer(data);
            double[] assignments = evaluation.getClusterAssignments();
            System.out.println("cluster assignment: " + assignments.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
