package com.thoersch.seeds.resources.forumposts;

import com.thoersch.seeds.persistence.forumposts.SentencesRepository;
import com.thoersch.seeds.representations.forumposts.Sentences;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by britt on 25/10/2017.
 */

@Component
@Transactional
public class SentencesResource {

    private final SentencesRepository sentencesRepository;

    @Inject
    public SentencesResource(SentencesRepository forumPostsRepository) {
        this.sentencesRepository = forumPostsRepository;
    }

    @GET
    public List<String> getIssueSentences() {
        List<Sentences> issueSentences = sentencesRepository.findAll();
        List<String> sentences = null;
        HashMap<Long, String> sentenceMap = null;
        
        for (Sentences sentence : issueSentences) {
            if (sentence.getCategory() == 1){
                if (sentenceMap.containsKey(sentence.getQuestion_id())){
                    String content = sentenceMap.get(sentence.getQuestion_id());
                    content = content + sentence.getContent();
                    sentenceMap.put(sentence.getQuestion_id(), content);
                } else {
                    sentenceMap.put(sentence.getQuestion_id(), sentence.getContent());
                }
            }
        }
        sentences = new ArrayList<>(sentenceMap.values());
        
        return sentences;
    }

    @POST
    public Sentences saveSentence(@Valid Sentences sentence) {
        return sentencesRepository.save(sentence);
    }
}
