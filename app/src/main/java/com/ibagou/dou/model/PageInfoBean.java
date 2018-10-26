package com.ibagou.dou.model;

import java.util.List;

/**
 * Created by liumingyu on 2018/9/19.
 */

public class PageInfoBean {

    /**
     * code : 0
     * msg :
     * data : [{"id":"64","description":"\n\n真人真事，身边哥们发生的。（前提年前公司没有发工资，他老婆还是个大学生）                                             女：老公我看上一双鞋！                                                 男：老公身上没钱了就一百多块了，过几天好么老婆。                       女：我不管我不管我就要我就要！！！                                      男：行，好的，后天给你买。                                             两天之后男的给女的买鞋了（男的干了两天兼职赚了二百块钱）对于这样的人我只能呵呵了，男的不求女的能干什么，只希望稍微体谅一下自己的老爷们，仅此而已。\n\n"},{"id":"63","description":"\n\n路过称体重的地方，看到一个体型庞大的哥们在称体重，我对身边娇小的女友调侃道：估计这哥们的体重是你的两倍，包括胸。女友捂着胸口满脸惊恐：\u201c真的吗？他有四个奶 子？\u201d\n\n"},{"id":"62","description":"\n\n饭后溜一圈，见一老爷子推着轮椅在广场上转圈，无聊问老爷子为啥推着轮椅，难到腿脚不好吗？老爷子说，等老太婆跳好广场舞累了，好推她回家，我\u2026\u2026\u2026\u2026\u2026\u2026\n\n"},{"id":"61","description":"\n\n闺蜜网恋了一男朋友，两个人在网络上互相交换了照片，确定了情侣关系，到处秀恩爱。昨晚男生要和闺蜜语音，闺蜜爸妈正好都在旁边，所以就没同意。男生急了，问:你为啥不同意语音，你到底有没有骗我？闺蜜:我骗你什么了？男生:我就想确认一下，和我谈恋爱的不是个男人\u2026\u2026\n\n"},{"id":"60","description":"\n\n部门主管离职了，我觉得机会来了，赶紧拍经理马屁。我自告奋勇提出请客，请他去吃饭。很快菜齐上桌，酒过三巡，我脑子晕乎乎，端起酒杯敬酒:   经理，祝你跟嫂子好合好散，妻离子散\u2026\u2026经理:  \u2026\u2026\n\n"},{"id":"59","description":"\n\n真事就刚刚，新手不会割。才睡醒去洗手间洗脸，手里挤点洗面奶开始洗，搓啊搓搓啊搓，一不小心小拇指直接滑进鼻孔，当场血流不止\u2026\u2026够不够糗？大神给过吧\u2026\n\n"},{"id":"58","description":"\n\n公司停车难，运气不好就要停到远处的停车场，，，今天停到一个\u2018黄金\u2019车位，下班时候没舍得开车，得意洋洋的坐公交车回家\u2026\n\n"},{"id":"57","description":"\n\n不知道是哪只王八蛋定的今年8.19，也就是昨天是第一个医师节，实在是脑残啦！一年365天，哪天不行啊？偏偏把谐音\u201c不要救\u201d定为医师节，为什么不定9.19呢？\u201c就要救\u201d呢？！\n\n"},{"id":"56","description":""},{"id":"55","description":"\n\n我闺蜜通过珍爱网认识一男孩，跟我闺蜜一样也是单亲家庭，他家很有钱，我和闺蜜说你这是要嫁入豪门了呀！然后在双方家长的陪同下吃了个饭，结束后，闺蜜感觉还行，但她妈说她们不合适，于是就没有来往了，两个月后，闺蜜的妈妈再婚了，再婚对象就是之前认识的男孩的爸爸。\n\n"}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 64
         * description :

         真人真事，身边哥们发生的。（前提年前公司没有发工资，他老婆还是个大学生）                                             女：老公我看上一双鞋！                                                 男：老公身上没钱了就一百多块了，过几天好么老婆。                       女：我不管我不管我就要我就要！！！                                      男：行，好的，后天给你买。                                             两天之后男的给女的买鞋了（男的干了两天兼职赚了二百块钱）对于这样的人我只能呵呵了，男的不求女的能干什么，只希望稍微体谅一下自己的老爷们，仅此而已。


         */

        private String id;
        private String description;
        private boolean hasSpread;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public boolean isHasSpread() {
            return hasSpread;
        }

        public void setHasSpread(boolean hasSpread) {
            this.hasSpread = hasSpread;
        }
    }
}
