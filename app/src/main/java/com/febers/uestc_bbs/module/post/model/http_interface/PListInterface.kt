package com.febers.uestc_bbs.module.post.model.http_interface

import com.febers.uestc_bbs.entity.BoardListBean_
import com.febers.uestc_bbs.entity.PostListBean
import com.febers.uestc_bbs.utils.ApiUtils
import retrofit2.Call
import retrofit2.http.*

interface PListInterface {

    @FormUrlEncoded
    @POST(ApiUtils.BBS_HOME_POST_URL)
    fun hotPosts(@Field("r")r: String, @Field("moduleId")moduleId: String,
                 @Field("page")page: String, @Field("pageSize")pageSize: String):
            Call<PostListBean>

    @FormUrlEncoded
    @POST(ApiUtils.BBS_HOME_POST_URL)
    fun newPosts(@Field("r")r: String, @Field("boardId")boardId: String,
                 @Field("page")page: String, @Field("pageSize")pageSize: String,
                 @Field("sortby")sortby: String): Call<PostListBean>


    @FormUrlEncoded
    @POST(ApiUtils.BBS_TOPIC_LIST_URL)
    fun normalPosts(@Field("boardId")boardId: String, @Field("page")page: String,
                    @Field("pageSize")pageSize: String, @Field("sortby")sortby: String,
                    @Field("filterType")filterType: String, @Field("isImageList")isImageList: String,
                    @Field("topOrder")topOrder: String): Call<PostListBean>

    @FormUrlEncoded
    @POST(ApiUtils.BBS_FORUM_LIST)
    fun boardList(@Field("fid")fid: String): Call<BoardListBean_>
}
