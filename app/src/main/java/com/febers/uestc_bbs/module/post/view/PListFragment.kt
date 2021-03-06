package com.febers.uestc_bbs.module.post.view

import android.os.Bundle
import androidx.annotation.UiThread
import com.google.android.material.appbar.AppBarLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.view.adapter.PostListAdapter
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.BoardListBean_
import com.febers.uestc_bbs.entity.PostListBean
import com.febers.uestc_bbs.module.post.contract.PListContract
import com.febers.uestc_bbs.module.post.presenter.PListPresenterImpl
import com.febers.uestc_bbs.utils.ViewClickUtils
import com.febers.uestc_bbs.utils.ViewClickUtils.clickToPostDetail
import com.febers.uestc_bbs.view.adapter.StickyPostAdapter
import com.febers.uestc_bbs.view.helper.FABBehaviorHelper
import com.febers.uestc_bbs.view.helper.finishFail
import com.febers.uestc_bbs.view.helper.finishSuccess
import com.febers.uestc_bbs.view.helper.initAttrAndBehavior
import kotlinx.android.synthetic.main.fragment_post_list.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.runOnUiThread
import java.lang.IndexOutOfBoundsException

class PListFragment: BaseSwipeFragment(), PListContract.View {

    private val stickyPostList: MutableList<PostListBean.TopTopicListBean> = ArrayList()
    private val postList: MutableList<PostListBean.ListBean> = ArrayList()
    private val boardNames: MutableList<String> = ArrayList()
    private val boardIds: MutableList<Int> = ArrayList()
    private lateinit var pListPresenter: PListContract.Presenter
    private var stickyPostAdapter: StickyPostAdapter? = null
    private var boardSpinnerAdapter: ArrayAdapter<String>? = null
    private var boardSpinner: Spinner? = null
    private var postListAdapter: PostListAdapter? = null
    private var itemShowStickyPost: MenuItem? = null
    private var isShowStickyPost = false
    private var title: String? = "板块名称"
    private var page: Int = 1

    override fun setToolbar(): Toolbar? = toolbar_post_list

    override fun registerEventBus(): Boolean = true

    override fun setContentView(): Int {
        arguments?.let {
            title = it.getString("title")
        }
        return R.layout.fragment_post_list
    }

    override fun initView() {
        setHasOptionsMenu(true)
        toolbar_post_list.inflateMenu(R.menu.menu_post_list)
        toolbar_post_list.title = ""

        pListPresenter = PListPresenterImpl(this)
        postListAdapter = PostListAdapter(context!!, postList)

//        coo_layout_post_list_fragment.title = title
//        coo_layout_post_list_fragment.isTitleEnabled = false
//        coo_layout_post_list_fragment.setBackgroundColor(ThemeHelper.getColorPrimary())
        onAppbarLayoutOffsetChange()
        FABBehaviorHelper.fabBehaviorWithScrollView(scroll_view_post_list, fab_post_list)

        pListPresenter.pListRequest(fid = mFid, page = page)
        pListPresenter.boardListRequest(mFid)

        boardNames.add(title.toString())
        boardIds.add(mFid)
        boardSpinnerAdapter = ArrayAdapter(context!!,
                R.layout.item_layout_spinner,
                R.id.text_view_item_spinner,
                boardNames).apply {
            setDropDownViewResource(R.layout.item_layout_spinner_dropdown)
        }
        boardSpinner = Spinner(toolbar_post_list.context).apply {
            adapter = boardSpinnerAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    mFid = boardIds[position]
                    refresh_layout_post_list.autoRefresh()
                }
            }
        }
        toolbar_post_list.addView(boardSpinner, 0)
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        postListAdapter?.apply {
            setOnItemClickListener { viewHolder, simplePostBean, i ->
                clickToPostDetail(context,simplePostBean.topic_id ?: simplePostBean.source_id)
            }
            setOnItemChildClickListener(R.id.image_view_item_post_avatar) {
                viewHolder, simplePListBean, i -> ViewClickUtils.clickToUserDetail(context, simplePListBean.user_id)
            }
        }
        recyclerview_post_list.apply {
            adapter = postListAdapter
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL)) }

        refresh_layout_post_list.apply {
            initAttrAndBehavior()
            setOnRefreshListener {
                page = 1
                getPost(page) }
            setOnLoadMoreListener {
                getPost(++page) }
        }
        fab_post_list.setOnClickListener {
            ViewClickUtils.clickToPostEdit(context, mFid, title!!)
        }
    }

    private fun getPost(page: Int) {
        refresh_layout_post_list?.setNoMoreData(false)
        pListPresenter.pListRequest(fid = mFid, page = page)
    }

    @UiThread
    override fun showPList(event: BaseEvent<PostListBean>) {
        refresh_layout_post_list?.finishSuccess()
        //置顶帖数据
        stickyPostList.clear()
        stickyPostList.addAll(event.data.topTopicList!!)
        if (stickyPostAdapter != null) (stickyPostAdapter as StickyPostAdapter).notifyDataSetChanged()

        if (page == 1) {
            postListAdapter?.setNewData(event.data.list)
            return
        }
        if (event.code == BaseCode.SUCCESS_END) {
            refresh_layout_post_list?.finishLoadMoreWithNoMoreData()
            return
        }
        postListAdapter?.setLoadMoreData(event.data.list)
    }

    /**
     * 进入总的版块之后，后台获取该板块下的所有子版块，并创建一个spinner显示
     *
     */
    @UiThread
    override fun showBoardList(event: BaseEvent<BoardListBean_>) {
        try {
            event.data.list?.get(0)?.board_list?.forEach {
                boardNames.add(it.board_name.toString())
                boardIds.add(it.board_id)
            }
            boardSpinnerAdapter?.notifyDataSetChanged()
        } catch (e: IndexOutOfBoundsException) {
        }

    }

    /**
     * 接收到新帖发布之后
     * 刷新界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPostNew(event: BaseEvent<PostNewEvent>) {
        if (event.code == BaseCode.SUCCESS) {
            refresh_layout_post_list.autoRefresh()
        }
    }

    /**
     * 显示或者隐藏置顶帖
     * 默认置顶帖的可见性是GONE
     * 点击菜单之后将其设为visibility
     */
    private fun showOrHideStickyPost() {
        if (stickyPostList.isEmpty()) return
        if (!isShowStickyPost) {
            if (stickyPostAdapter == null) {
                stickyPostAdapter = StickyPostAdapter(context!!, stickyPostList).apply {
                    setOnItemClickListener { viewHolder, topTopicListBean, i ->
                        ViewClickUtils.clickToPostDetail(context, topTopicListBean.id)
                    }
                }
            }
            recyclerview_post_list_sticky?.apply {
                adapter = stickyPostAdapter
                visibility = View.VISIBLE
            }
        } else {
            recyclerview_post_list_sticky.visibility = View.GONE
        }
    }

    /**
     * 根据Appbar的偏移量来控制其中控件的显示与隐藏
     * 全部展开时，偏移量应该为0
     * 网上收缩时，逐渐减小
     */
    private fun onAppbarLayoutOffsetChange() {
        app_bar_post_list.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            when {
//                verticalOffset == 0 -> {
//                    //CollapsingToolBar展开
//                }
//                Math.abs(verticalOffset) >= app_bar_post_list.totalScrollRange -> {
//                    //CollapsingToolBar折叠
//                }
//                Math.abs(verticalOffset) <= 150 -> {
//                    text_view_post_list_all_num.visibility = View.VISIBLE
//                    text_view_post_list_today_num.visibility = View.VISIBLE
//                    //linear_layout_post_list_header.visibility = View.VISIBLE
//                }
//                Math.abs(verticalOffset) >= 150 -> {
//                    text_view_post_list_all_num.visibility = View.INVISIBLE
//                    text_view_post_list_today_num.visibility = View.INVISIBLE
                    //linear_layout_post_list_header.visibility = View.GONE
//                }
            }
        })
    }

    override fun showError(msg: String) {
        context?.runOnUiThread {
            showToast(msg)
            refresh_layout_post_list?.finishFail()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_post_list, menu)
        if (menu != null) itemShowStickyPost = menu.findItem(R.id.menu_item_post_list_sticky)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.menu_item_post_list_sticky) {
            showOrHideStickyPost()
            if (!isShowStickyPost) itemShowStickyPost?.title = "隐藏置顶帖"
            if (isShowStickyPost) itemShowStickyPost?.title = "显示置顶帖"
            isShowStickyPost = !isShowStickyPost
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        @JvmStatic
        fun newInstance(fid: Int, title: String, showBottomBarOnDestroy: Boolean) =
                PListFragment().apply {
                    arguments = Bundle().apply {
                        putInt(FID, fid)
                        putString("title", title)
                        putBoolean(SHOW_BOTTOM_BAR_ON_DESTROY, showBottomBarOnDestroy)
                    }
                }
    }

    override fun onDestroy() {
        super.onDestroy()
        postList.clear()
        stickyPostList.clear()
    }
}