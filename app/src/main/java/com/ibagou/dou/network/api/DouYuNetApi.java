package com.ibagou.dou.network.api;

import com.ibagou.dou.model.BaseBean;
import com.ibagou.dou.model.CheckVersionBean;
import com.ibagou.dou.model.ContentDataBean;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import io.reactivex.Observable;

import static com.ibagou.dou.network.NetWorkManager.DOUYU_VERSION;

/**
 * Created by liumingyu on 2018/8/20.
 */

public interface DouYuNetApi {
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);

    //获取段子内容
    @GET(DOUYU_VERSION + "news/index?")
    Observable<ContentDataBean> getPage(@Query("page") String page, @Query("per-page") String per_page);

    //点赞
    @POST(DOUYU_VERSION + "news/up")
    @FormUrlEncoded
    Observable<BaseBean> upLikeInfo(@Field("rel_id") String rel_id);

    //取消点赞
    @POST(DOUYU_VERSION + "news/down")
    @FormUrlEncoded
    Observable<BaseBean> downLikeInfo(@Field("rel_id") String rel_id);

    //版本更新
    @GET(DOUYU_VERSION + "app-version/compare?")
    Observable<CheckVersionBean> checkVersion(@Query("version") String verison);

    //添加文章
    @POST(DOUYU_VERSION + "news/create")
    @FormUrlEncoded
    Observable<BaseBean> addArticle(@Field("body") String body, @Field("author") String author);


    //登录
    @POST(DOUYU_VERSION + "wechat-open/login")
    @FormUrlEncoded
    Observable<BaseBean> wxLogin(@Field("code") String code);

}
