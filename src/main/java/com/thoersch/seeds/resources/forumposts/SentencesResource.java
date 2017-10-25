package com.thoersch.seeds.resources.forumposts;

import com.thoersch.seeds.persistence.forumposts.SentencesRepository;
import com.thoersch.seeds.representations.forumposts.Sentences;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
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
    public List<Sentences> getSentences() {
        List<Sentences> sentences = sentencesRepository.findAll();
        return sentences;
    }
}
