package com.febers.uestc_bbs.view.adapter

import android.content.Context
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.SearchPostBean
import com.febers.uestc_bbs.utils.TimeUtils
import com.othershe.baseadapter.ViewHolder
import com.othershe.baseadapter.base.CommonBaseAdapter

class SearchAdapter(val context: Context, data: List<SearchPostBean.ListBean>, isLoadMore: Boolean):
        CommonBaseAdapter<SearchPostBean.ListBean>(context, data, isLoadMore) {

    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_search_post
    }

    override fun convert(p0: ViewHolder?, p1: SearchPostBean.ListBean?, p2: Int) {
        p0?.setText(R.id.text_view_item_search_post_title, p1?.title)
        p0?.setText(R.id.text_view_item_search_post_content, p1?.subject)
        p0?.setText(R.id.text_view_item_search_post_user, p1?.user_nick_name)
        p0?.setText(R.id.text_view_item_search_post_hits, p1?.hits.toString())
        p0?.setText(R.id.text_view_item_search_post_reply, p1?.replies.toString())
        p0?.setText(R.id.text_view_item_search_post_time, TimeUtils.stampChange(p1?.last_reply_date))
    }
}