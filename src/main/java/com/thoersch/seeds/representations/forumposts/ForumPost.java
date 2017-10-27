package com.thoersch.seeds.representations.forumposts;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Mira on 23/10/2017.
 */

@javax.persistence.Entity
@Table(name = "forumposts")
public class ForumPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long _id;

    @NotNull
    @Column(name = "forum_details_id")
    private Integer _forum_details_id;

    @NotNull
    @Column(name = "text")
    private String _text;

    @Column(name = "date")
    private Date _date;

    @NotNull
    @Length(max = 256)
    @Column(name = "author")
    private String _author;

    @NotNull
    @Column(name = "content")
    private String _content;

    @NotNull
    @Length(max = 256)
    @Column(name = "url")
    private String _url;

    public ForumPost() { }

    public ForumPost(Integer forum_details_id, String text, Date date, String author, String content, String url) {
        _forum_details_id = forum_details_id;
        _text = text;
        _date = date;
        _author = author;
        _content = content;
        _url = url;
    }

    public Long get_id() {
        return _id;
    }

    public Integer get_forum_details_id() {
        return _forum_details_id;
    }

    public void set_forum_details_id(Integer _forum_details_id) {
        this._forum_details_id = _forum_details_id;
    }

    public String get_text() {
        return _text;
    }

    public void set_text(String _text) {
        this._text = _text;
    }

    public Date get_date() {
        return _date;
    }

    public void set_date(Date _date) {
        this._date = _date;
    }

    public String get_author() {
        return _author;
    }

    public void set_author(String _author) {
        this._author = _author;
    }

    public String get_content() {
        return _content;
    }

    public String get_url() {
        return _url;
    }

    public void set_url(String _url) {
        this._url = _url;
    }

    public void set_content(String _content) {
        this._content = _content;
    }

    @Override
    public String toString() {
        return "ForumPost id " + this._id;
    }
}
