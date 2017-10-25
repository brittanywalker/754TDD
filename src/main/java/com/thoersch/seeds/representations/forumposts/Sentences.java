package com.thoersch.seeds.representations.forumposts;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by britt on 25/10/2017.
 * Category IDs:
 * 0 - context
 * 1 - problem
 * 2 - code
 * 3 - question
 * 4 - outroduction
 */

@Entity
@Table(name = "sentences")
public class Sentences {

    public Sentences(Long id, String content, Integer category, Integer question_id) {
        this.id = id;
        this.content = content;
        this.category = category;
        this.question_id = question_id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String content;

    @NotNull
    private Integer category;

    @NotNull
    private Integer question_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(Integer question_id) {
        this.question_id = question_id;
    }
}