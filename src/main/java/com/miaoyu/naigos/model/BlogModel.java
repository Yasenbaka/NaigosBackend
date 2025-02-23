package com.miaoyu.naigos.model;

public class BlogModel {
    private String name;
    private String label;
    private String content;
    private String author;
    private long upload_date;
    private long last_date;
    private String attachment;
    private String classify_id;
    private String blog_id;
    private String cover_image;
    private String cover_image_800;
    private String cover_image_1200;
    private String bg_image;

    public String getCover_image_800() {
        return cover_image_800;
    }

    public void setCover_image_800(String cover_image_800) {
        this.cover_image_800 = cover_image_800;
    }

    public String getCover_image_1200() {
        return cover_image_1200;
    }

    public void setCover_image_1200(String cover_image_1200) {
        this.cover_image_1200 = cover_image_1200;
    }

    public String getCover_image() {
        return cover_image;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    public String getBg_image() {
        return bg_image;
    }

    public void setBg_image(String bg_image) {
        this.bg_image = bg_image;
    }

    public String getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(String blog_id) {
        this.blog_id = blog_id;
    }

    public String getClassify_id() {
        return classify_id;
    }

    public void setClassify_id(String classify_id) {
        this.classify_id = classify_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(long upload_date) {
        this.upload_date = upload_date;
    }

    public long getLast_date() {
        return last_date;
    }

    public void setLast_date(long last_date) {
        this.last_date = last_date;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }
}
