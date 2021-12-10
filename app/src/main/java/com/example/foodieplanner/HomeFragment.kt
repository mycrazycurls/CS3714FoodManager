package com.example.foodieplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.foodieplanner.databinding.FragmentHomeBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        var calendar: Calendar = Calendar.getInstance()
        val month = SimpleDateFormat("MMM")
        val dayOfWeek = SimpleDateFormat("EEEE")
        val dayOfMonth = SimpleDateFormat("dd")

        val adapter = CalendarDayCardAdapter()
        binding.homeRecyclerView.adapter = adapter

        var i = 7
        while (i>0) {
            adapter.addCalendarDay(
                CalendarDay(
                    month.format(calendar.time),
                    dayOfWeek.format(calendar.time),
                    dayOfMonth.format(calendar.time),
                    "3 meals", "2000 cals", "$25"
                )
            )
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            i--
        }
        return binding.root
    }


    inner class CalendarDayCardAdapter: RecyclerView.Adapter<CalendarDayCardViewHolder>() {

        var data = arrayListOf<CalendarDay>()
            set(value) {
                field = value
                notifyDataSetChanged()
            }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarDayCardViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.card_home_calendar, parent, false)
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
                findNavController().navigate(R.id.action_homeFragment_to_dayFragment,
                    bundleOf("month" to data.get(position).month,
                "date" to data.get(position).date, "day" to data.get(position).day))
            }
        }

        override fun getItemCount(): Int {
            return data.size
        }

        fun addCalendarDay(calendarDay: CalendarDay) {
            data.add(calendarDay)
            notifyDataSetChanged()
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


    data class CalendarDay(
        var month: String,
        var day: String,
        var date: String,
        var meals: String,
        var cals: String,
        var price: String)
}