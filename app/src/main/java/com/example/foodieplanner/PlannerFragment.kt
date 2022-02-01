package com.example.foodieplanner

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.core.util.Pair
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.foodieplanner.databinding.FragmentPlannerBinding
import com.example.foodieplanner.models.Ingredient
import com.google.android.material.datepicker.*


/**
 * A simple [Fragment] subclass.
 * Use the [PlannerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlannerFragment : Fragment() {

    private lateinit var binding: FragmentPlannerBinding
    private var groceryListRecyclerView: RecyclerView? = null
    private val model: Model by activityViewModels()
    private val groceryListModel: GroceryListModel by activityViewModels()
    val adapter = SingleLineItemAdapter()

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

        // Build the Material.io dialog containing the date range picker
        val constraints = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
            .build()
        var selection = groceryListModel.getSelection()

        var dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select dates")
                .setCalendarConstraints(constraints)
                .setSelection(Pair(selection?.first,selection?.second))
                .build()

        // Called when the user exits the date range picker dialog
        dateRangePicker.addOnDismissListener {
            val pair = dateRangePicker.selection
            if (pair?.first != null && pair.second != null) {
                // Update the view model with the new selection of dates
                groceryListModel.updateRange(pair.first, pair.second)
            }
        }

        // When date range display button is selected, show the dialog
        binding.planCalendarButton.setOnClickListener {
            activity?.let {
                dateRangePicker.show(
                    it.supportFragmentManager,
                    "groceryDateRange"
                )
            }
        }

        // Observe the view model date range to change the list accordingly
        groceryListModel.dateRange.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                    binding.planCalendarButton.text = groceryListModel.getRangeText()
                var days = model.days.value
                if (days != null) {
                    adapter.data = groceryListModel.getIngredients(days)
                }
            }
        })

        // Pull Days From Database
//        val locationListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                var days = arrayListOf<Day>()
//                for (snapShot in dataSnapshot.children) {
//                    val day = snapShot.getValue<Day>()
//                    if (day != null) {
//                        days.add(day)
//                    }
//                }
//                groceryListModel.days.postValue(days)
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Getting Post failed, log a message
//                Log.e("MainRetrieveData", "loadPost:onCancelled", databaseError.toException())
//            }
//        }
//        model.database.child("Days").addValueEventListener(locationListener)

        // Observe the list of days held in the view model
        model.days.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                adapter.data = groceryListModel.getIngredients(it)
            }
        })

        // top bar configuration
//      binding.planTopBar.setNavigationOnClickListener {
//            // Handle navigation icon press
//        }
//        binding.planTopBar.setOnMenuItemClickListener { menuItem ->
//            when (menuItem.itemId) {
//                else -> false
//            }
//        }

        // open calendar picker
//        binding.planCalendarButton.setOnClickListener {
//            activity?.let {
//                dateRangePicker.show(
//                    it.supportFragmentManager,
//                    "groceryDateRange"
//                )
//            }
//        }

        // initialize views


        // populate recycler view
        groceryListRecyclerView = binding.planGroceryListRecycler
        groceryListRecyclerView?.adapter = adapter

        // Called when the user clicks the complete button for the grocery list
        binding.plannerComplete.setOnClickListener {
            adapter.clearIngredients()
            // TODO - update status of day/ change color on home UI
        }

        return return binding.root
    }

    // to populate recycler view
    inner class SingleLineItemAdapter: RecyclerView.Adapter<SingleLineItemViewHolder>() {

        var data = arrayListOf<Ingredient>()
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleLineItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.view_planner_listitem, parent, false) as LinearLayout
            return SingleLineItemViewHolder(view)
        }

        override fun onBindViewHolder(holder: SingleLineItemViewHolder, position: Int) {
            holder.checkbox.text = "  " + data.get(position).name
            holder.quantity.text = data.get(position).quToString()
        }

        override fun getItemCount(): Int {
            return data.size
        }

//        fun addIngredient(ingredient: Ingredient) {
//            data.add(ingredient)
//            notifyDataSetChanged()
//        }

        fun clearIngredients() {
            data.clear()
            notifyDataSetChanged()
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