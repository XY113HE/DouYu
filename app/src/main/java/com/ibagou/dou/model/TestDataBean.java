package com.ibagou.dou.model;

/**
 * Created by liumingyu on 2018/8/28.
 */

public class TestDataBean {
    private String content = "";
    private String isLike = "0";
    private boolean spreaded = false;
    private int id;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIsLike() {
        return isLike;
    }

    public void setIsLike(String isLike) {
        this.isLike = isLike;
    }

    public boolean isSpreaded() {
        return spreaded;
    }

    public void setSpreaded(boolean spreaded) {
        this.spreaded = spreaded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
