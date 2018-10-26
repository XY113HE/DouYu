package com.ibagou.dou.model;

/**
 * Created by liumingyu on 2018/10/18.
 */

public class CheckVersionBean {

    /**
     * code : 0
     * msg : success
     * data : {"force":"NO","download_uri":"abc","note":"","new_version":"YES", "version":"1.5"}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * force : NO 是否强制更新
         * download_uri : abc  新apk下载地址
         * note :
         * new_version : YES
         */

        private String force;
        private String download_uri;
        private String note;
        private String new_version;
        private String version;

        public String getForce() {
            return force;
        }

        public void setForce(String force) {
            this.force = force;
        }

        public String getDownload_uri() {
            return download_uri;
        }

        public void setDownload_uri(String download_uri) {
            this.download_uri = download_uri;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getNew_version() {
            return new_version;
        }

        public void setNew_version(String new_version) {
            this.new_version = new_version;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}
