package com.thoersch.seeds.persistence.forumposts;

import com.thoersch.seeds.representations.forumposts.Sentences;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by britt on 25/10/2017.
 */
public interface SentencesRepository extends JpaRepository<Sentences, Long> {
}
