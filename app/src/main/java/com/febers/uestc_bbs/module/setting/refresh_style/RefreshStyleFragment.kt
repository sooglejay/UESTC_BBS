package com.febers.uestc_bbs.module.setting.refresh_style

import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseSwipeFragment
import com.febers.uestc_bbs.utils.PreferenceUtils
import com.febers.uestc_bbs.view.adapter.RefreshStyleAdapter
import kotlinx.android.synthetic.main.fragment_refresh_style.*

class RefreshStyleFragment: BaseSwipeFragment() {

    private lateinit var styleAdapter: RefreshStyleAdapter

    override fun setToolbar(): Toolbar? {
        return toolbar_refresh_style
    }

    override fun setContentView(): Int {
        return R.layout.fragment_refresh_style
    }

    override fun initView() {
        setSwipeBackEnable(false)
        var styleCode by PreferenceUtils(context!!, REFRESH_HEADER_CODE, 0)
        btn_choose_refresh_style.setOnClickListener {
            styleCode = view_pager_refresh_style.currentItem
        }

        styleAdapter = RefreshStyleAdapter(childFragmentManager)
        view_pager_refresh_style.offscreenPageLimit = 7
        view_pager_refresh_style.adapter = styleAdapter
        view_pager_refresh_style.currentItem = styleCode

        indicator_refresh_style.setMaxSize(HEAD_COUNT)
        indicator_refresh_style.setCurIndex(styleCode)

        view_pager_refresh_style.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                indicator_refresh_style.setCurIndex(position)
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(): RefreshStyleFragment = RefreshStyleFragment()
    }
}