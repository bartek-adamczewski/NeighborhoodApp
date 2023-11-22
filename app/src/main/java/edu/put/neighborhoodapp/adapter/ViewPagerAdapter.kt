package edu.put.neighborhoodapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import edu.put.neighborhoodapp.fragments.CategoryFragment
import edu.put.neighborhoodapp.fragments.FragmentType

class ViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val fragmentTypes: List<FragmentType>
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = fragmentTypes.size

    override fun createFragment(position: Int): Fragment {
        return CategoryFragment.newInstance(fragmentTypes[position])
    }
}