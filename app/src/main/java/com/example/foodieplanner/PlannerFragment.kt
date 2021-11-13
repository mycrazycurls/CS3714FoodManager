package com.example.foodieplanner

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.example.foodieplanner.databinding.FragmentPlannerBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [PlannerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlannerFragment : Fragment() {

    private lateinit var binding: FragmentPlannerBinding
    private var groceryListRecyclerView: RecyclerView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // indicate that there is an options menu present
        setHasOptionsMenu(true)

        arguments?.let {

        }
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlannerBinding.inflate(layoutInflater)

        // Initial range of dates
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val later = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        later.timeInMillis = today
        later.add(Calendar.DAY_OF_MONTH,5)

        // Initialize date range text
        binding.planTopBar.title = milisecDateToString(today, later.timeInMillis)
        //binding.planTopBar.subtitle = "Groceries"

        // Configure dateRangePicker popup
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select dates")
                .setSelection(
                    Pair(
                        today,
                        later.timeInMillis
                    )
                )
                .setCalendarConstraints(constraintsBuilder.build())
                .build()

        dateRangePicker.addOnDismissListener {
            val pair = dateRangePicker.selection
            if (pair?.first != null && pair.second != null)
                binding.planTopBar.title = milisecDateToString(pair.first, pair.second)
                //binding.planSelectDates.text = milisecDateToString(pair.first, pair.second)
        }

        // top bar configuration
//        topAppBar.setNavigationOnClickListener {
//            // Handle navigation icon press
//        }
        binding.planTopBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.plan_menu_calender -> {
                    activity?.let {
                        dateRangePicker.show(
                            it.supportFragmentManager,
                            "groceryDateRange"
                        )
                    }
                    true
                }
                else -> false
            }
        }

        // populate recycler view
        val adapter = SingleLineItemAdapter()
        groceryListRecyclerView = binding.planGroceryListRecycler
        groceryListRecyclerView?.adapter = adapter

        return return binding.root
    }

    fun milisecDateToString(first : Long, second: Long) : String {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = first
        var str = "${calendar[Calendar.MONTH]}/${calendar[Calendar.DATE]}"
        calendar.timeInMillis = second
        str += " - ${calendar[Calendar.MONTH]}/${calendar[Calendar.DATE]}"
        return str
    }

    // to populate recycler view
    inner class SingleLineItemAdapter: RecyclerView.Adapter<SingleLineItemViewHolder>() {

        var data = arrayListOf<Pair<String,String>>(
            Pair("Baking Soda","5 tsp"),
            Pair("Flour","6 cups"),
            Pair("Sugar","1/2 cup"),
            Pair("Eggs","1 dozen"),
            Pair("New York Strip","3"),
            Pair("Bread Loaf","1"),
            Pair("Cheddar Cheese","16 ounces"),
            Pair("Milk","1 gallon"),
            Pair("Tomato","5"),
            Pair("Strawberries","2 lbs"),
        )
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleLineItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.material_list_item_single_line, parent, false) as LinearLayout
            return SingleLineItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: SingleLineItemViewHolder, position: Int) {
            holder.checkbox.text = "  " + data.get(position).first
            holder.quantity.text = data.get(position).second
        }

        override fun getItemCount(): Int {
            return data.size
        }


    }

    inner class SingleLineItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val checkbox: CheckBox = view.findViewById(R.id.plan_grocery_checkbox)
        val quantity: TextView = view.findViewById(R.id.plan_grocery_quantity)

    }







    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment PlannerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PlannerFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}