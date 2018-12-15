package com.job.manager.model;

import java.util.List;

/**
 * Created by shijiayi on 2016/9/5.
 * 用于获取combotree
 */
public class NewsClassJson {

    private String id;
    private String text;
    private String parentId;
    private String attributes;
    private String state;
    private String checked;
    private List<NewsClassJson> children;

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<NewsClassJson> getChildren() {
        return children;
    }

    public void setChildren(List<NewsClassJson> children) {
        this.children = children;
    }


}
