package com.android.project.tukang.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.project.tukang.ui.fragment.ProfileFragment

class ProfileSectionsPagerAdapter(activity: ProfileFragment): FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        return fragment as Fragment
    }
}