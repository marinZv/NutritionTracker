package rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.R
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.databinding.FragmentTabMainBinding
import rs.raf.projekat_jun_mihajlo_madzarevic_5520rn_milan_marinkovic_7921rn.presentation.view.recycler.adapter.ViewPageAdapter

class TabMainFragment : Fragment() {

    private lateinit var binding: FragmentTabMainBinding

    private lateinit var pager: ViewPager
    private lateinit var tab: TabLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTabMainBinding.inflate(layoutInflater)
        activity?.title = getString(R.string.filter)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
    }

    private fun init(view: View){
        initTab(view)
    }

    private fun initTab(view: View){
        pager = binding.viewPager
        tab = binding.tabs

        val adapter = ViewPageAdapter(childFragmentManager)

        adapter.addFragment(TabCategoryFragment(), "Category")
        adapter.addFragment(TabAreaFragment(), "Area")
        adapter.addFragment(TabIngredientFragment(), "Ingredient")

        pager.adapter = adapter

        tab.setupWithViewPager(pager)

    }

}