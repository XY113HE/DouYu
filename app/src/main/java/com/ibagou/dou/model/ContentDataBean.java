package com.ibagou.dou.model;

import java.util.List;

/**
 * Created by liumingyu on 2018/10/14.
 */

public class ContentDataBean {

    /**
     * items : [{"id":1,"category_id":0,"title":"这是一个标题","subtitle":"","summary":"","author":"admin","pic_author":null,"video_author":null,"source":"","video":null,"view_all":0,"com_all":0,"recommend":0,"is_top":0,"type":1,"created_by":1,"created_at":1539491518,"updated_at":1539491518,"category_name":"","add_date":"2018-10-14 12:31:58","cover":"http://lion.ibagou.com/static/images/default.png","body":"<p>这里是内容<\/p>"}]
     * _links : {"self":{"href":"http://dou.ibagou.com/api/v1/news/index?page=1&per-page=10"}}
     * _meta : {"totalCount":1,"pageCount":1,"currentPage":1,"perPage":10}
     */

    private LinksBean _links;
    private MetaBean _meta;
    private List<ItemsBean> items;

    public LinksBean get_links() {
        return _links;
    }

    public void set_links(LinksBean _links) {
        this._links = _links;
    }

    public MetaBean get_meta() {
        return _meta;
    }

    public void set_meta(MetaBean _meta) {
        this._meta = _meta;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class LinksBean {
        /**
         * self : {"href":"http://dou.ibagou.com/api/v1/news/index?page=1&per-page=10"}
         */

        private SelfBean self;

        public SelfBean getSelf() {
            return self;
        }

        public void setSelf(SelfBean self) {
            this.self = self;
        }

        public static class SelfBean {
            /**
             * href : http://dou.ibagou.com/api/v1/news/index?page=1&per-page=10
             */

            private String href;

            public String getHref() {
                return href;
            }

            public void setHref(String href) {
                this.href = href;
            }
        }
    }

    public static class MetaBean {
        /**
         * totalCount : 1
         * pageCount : 1
         * currentPage : 1
         * perPage : 10
         */

        private int totalCount;
        private int pageCount;
        private int currentPage;
        private int perPage;

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getPerPage() {
            return perPage;
        }

        public void setPerPage(int perPage) {
            this.perPage = perPage;
        }
    }

    public static class ItemsBean {
        /**
         * id : 1
         * category_id : 0
         * title : 这是一个标题
         * subtitle :
         * summary :
         * author : admin
         * pic_author : null
         * video_author : null
         * source :
         * video : null
         * view_all : 0
         * com_all : 0
         * recommend : 0
         * is_top : 0
         * type : 1
         * created_by : 1
         * created_at : 1539491518
         * updated_at : 1539491518
         * category_name :
         * add_date : 2018-10-14 12:31:58
         * cover : http://lion.ibagou.com/static/images/default.png
         * body : <p>这里是内容</p>
         * spreaded : false
         * zan : 点赞数
         */

        private int id;
        private int category_id;
        private String title;
        private String subtitle;
        private String summary;
        private String author;
        private String pic_author;
        private String video_author;
        private String source;
        private Object video;
        private int view_all;
        private int com_all;
        private int recommend;
        private int is_top;
        private int type;
        private int created_by;
        private int created_at;
        private int updated_at;
        private String category_name;
        private String add_date;
        private String cover;
        private String body;
        private boolean hasSpread;
        private int zan;

        public boolean isHasSpread() {
            return hasSpread;
        }

        public void setHasSpread(boolean hasSpread) {
            this.hasSpread = hasSpread;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getCategory_id() {
            return category_id;
        }

        public void setCategory_id(int category_id) {
            this.category_id = category_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public void setSubtitle(String subtitle) {
            this.subtitle = subtitle;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getPic_author() {
            return pic_author;
        }

        public void setPic_author(String pic_author) {
            this.pic_author = pic_author;
        }

        public String getVideo_author() {
            return video_author;
        }

        public void setVideo_author(String video_author) {
            this.video_author = video_author;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public Object getVideo() {
            return video;
        }

        public void setVideo(Object video) {
            this.video = video;
        }

        public int getView_all() {
            return view_all;
        }

        public void setView_all(int view_all) {
            this.view_all = view_all;
        }

        public int getCom_all() {
            return com_all;
        }

        public void setCom_all(int com_all) {
            this.com_all = com_all;
        }

        public int getRecommend() {
            return recommend;
        }

        public void setRecommend(int recommend) {
            this.recommend = recommend;
        }

        public int getIs_top() {
            return is_top;
        }

        public void setIs_top(int is_top) {
            this.is_top = is_top;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getCreated_by() {
            return created_by;
        }

        public void setCreated_by(int created_by) {
            this.created_by = created_by;
        }

        public int getCreated_at() {
            return created_at;
        }

        public void setCreated_at(int created_at) {
            this.created_at = created_at;
        }

        public int getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(int updated_at) {
            this.updated_at = updated_at;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public String getAdd_date() {
            return add_date;
        }

        public void setAdd_date(String add_date) {
            this.add_date = add_date;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public int getZan() {
            return zan;
        }

        public void setZan(int zan) {
            this.zan = zan;
        }
    }
}
