package com.thoersch.seeds.representations.forumposts;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Mira on 23/10/2017.
 */

@Entity
@Table(name = "forumPosts")
public class ForumPost {

    @Id
    private Long _question_id;

    @NotNull
    private Integer _forum_details_id;

    @NotNull
    private String _text;

    @NotNull
    private Date _date;

    @NotNull
    @Length(max = 256)
    private String _author;

    @NotNull
    private String _content;

    @NotNull
    @Length(max = 256)
    private String _url;

    public ForumPost(Integer forum_details_id, String text, Date date, String author, String content, String url) {
        _forum_details_id = forum_details_id;
        _text = text;
        _date = date;
        _author = author;
        _content = content;
        _url = url;
    }


}
