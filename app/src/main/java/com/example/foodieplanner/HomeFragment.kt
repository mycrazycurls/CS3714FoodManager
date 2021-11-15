package com.example.foodieplanner

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.foodieplanner.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        val adapter = CalendarDayCardAdapter()
        binding.homeRecyclerView.adapter = adapter


        return return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    inner class CalendarDayCardAdapter: RecyclerView.Adapter<CalendarDayCardViewHolder>() {

        var data = arrayListOf<calendarDay>(
            calendarDay("Tuesday","16","3 meals","2000 cals","$25"),
            calendarDay("Wednesday","17","3 meals","1500 cals","$34"),
            calendarDay("Thursday","18","4 meals","3000 cals","$60"),
            calendarDay("Friday","19","1 meal","1200 cals","$15"),
            calendarDay("Saturday","20","2 meals","2200 cals","$22"),
            calendarDay("Sunday","21","","",""),
            calendarDay("Monday","22","","",""),
        )
            set(value) {
                field = value
                notifyDataSetChanged()
            }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarDayCardViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.home_calendar_card, parent, false)
            return CalendarDayCardViewHolder(view)
        }

        override fun onBindViewHolder(holder: CalendarDayCardViewHolder, position: Int) {
            holder.date.text = data.get(position).date
            holder.day.text = data.get(position).day
            holder.meals.text = data.get(position).meals
            holder.cals.text = data.get(position).cals
            holder.price.text = data.get(position).price

            if (position == 0) {
                holder.date.setTextColor(resources.getColor(R.color.exodus))
            }

            holder.cardView.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_dayFragment)
            }
        }

        override fun getItemCount(): Int {
            return data.size
        }

    }

    inner class CalendarDayCardViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val day: TextView = view.findViewById(R.id.home_card_day)
        val date: TextView = view.findViewById(R.id.home_card_date)
        val meals: TextView = view.findViewById(R.id.home_card_meals)
        val cals: TextView = view.findViewById(R.id.home_card_cals)
        val price: TextView = view.findViewById(R.id.home_card_price)
        val cardView: CardView = view.findViewById(R.id.home_card_view)
    }


    data class calendarDay(
        var day: String,
        var date: String,
        var meals: String,
        var cals: String,
        var price: String) {
    }

}