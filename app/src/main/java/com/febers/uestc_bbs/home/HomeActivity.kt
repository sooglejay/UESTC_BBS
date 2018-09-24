/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs.home

import android.support.v4.app.ActivityCompat
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem

import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.utils.AttrUtils
import kotlinx.android.synthetic.main.activity_home.*
import me.yokeyword.fragmentation.ISupportFragment

class HomeActivity: BaseActivity() {

    private var mFragments : MutableList<ISupportFragment> = ArrayList()

    override fun setView(): Int {
        return R.layout.activity_home
    }

    override fun initView() {
        val firstFragment: ISupportFragment? = findFragment(HomeFirstContainer::class.java)
        if (firstFragment == null) {
            with(mFragments) {
                add(0, HomeFirstContainer())
                add(1, HomeSecondContainer())
                add(2, HomeThirdContainer())
                add(3, HomeFourthContainer())
            }
            loadMultipleRootFragment(R.id.activity_home_container, 0,
                    mFragments[0], mFragments[1], mFragments[2], mFragments[3])
        } else {
            with(mFragments) {
                add(0,firstFragment)
                add(1, findFragment(HomeSecondContainer::class.java))
                add(2, findFragment(HomeThirdContainer::class.java))
                add(3, findFragment(HomeFourthContainer::class.java))
            }
        }

        bottom_navigation_home.apply {
            addItem(AHBottomNavigationItem(getString(R.string.home_page), R.drawable.ic_home_gray))
            addItem(AHBottomNavigationItem(getString(R.string.forum_list_page), R.drawable.ic_forum_list_gray))
            addItem(AHBottomNavigationItem(getString(R.string.message_page), R.drawable.ic_message_gray))
            addItem(AHBottomNavigationItem(getString(R.string.more_page), R.drawable.ic_more_gray))
            titleState = AHBottomNavigation.TitleState.ALWAYS_HIDE
            accentColor = AttrUtils.getColor(this@HomeActivity, R.attr.colorAccent)
            setOnTabSelectedListener { position, wasSelected -> onTabSelected(position, wasSelected) }
        }
    }

    private fun onTabSelected(position: Int, wasSelected: Boolean): Boolean {
        if(wasSelected) {
            onTabReselected(position)
            return true
        }
        showHideFragment(mFragments[position])
        return true
    }

    override fun onBackPressedSupport() {
        if(supportFragmentManager.backStackEntryCount > 0) {
            pop()
        } else {
            ActivityCompat.finishAfterTransition(this)
        }
    }

    private fun onTabReselected(position: Int) {

    }
}
