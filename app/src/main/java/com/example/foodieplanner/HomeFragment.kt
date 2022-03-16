package com.example.foodieplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.foodieplanner.adapters.CalendarDayCardAdapter
import com.example.foodieplanner.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val model: Model by activityViewModels()
    private lateinit var adapter: CalendarDayCardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)
        adapter = CalendarDayCardAdapter { pos -> onItemClicked(pos) }
        binding.homeRecyclerView.adapter = adapter

        // Observe view model list of days
        model.days.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

    private fun onItemClicked(pos: Int) {
        model.selectedDay = pos
        findNavController().navigate(R.id.action_homeFragment_to_dayFragment)
    }

}