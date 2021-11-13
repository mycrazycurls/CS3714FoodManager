package com.example.foodieplanner

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodieplanner.databinding.FragmentDayBinding


/**
 * A simple [Fragment] subclass.
 * Use the [DayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DayFragment : Fragment() {

    private lateinit var binding: FragmentDayBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDayBinding.inflate(layoutInflater)

        val adapter = MealCardAdapter()
        binding.dayMealRecyclerView.adapter = adapter


        return binding.root
    }




    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DayFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DayFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }



    inner class MealCardAdapter: RecyclerView.Adapter<MealCardViewHolder>() {

        var data = arrayListOf<Pair<String,String>>(
            Pair("Eggs and Toast","200 cals"),
            Pair("Chicken and Rice","900 cals"),
            Pair("Pot Roast","1200 cals"),
            Pair("Apple Pie","500 cals")
        )
            set(value) {
                field = value
                notifyDataSetChanged()
            }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealCardViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.day_meal_card, parent, false)
            return MealCardViewHolder(view)
        }

        override fun onBindViewHolder(holder: MealCardViewHolder, position: Int) {
            holder.title.text = data.get(position).first
            holder.subtitle.text = data.get(position).second
        }

        override fun getItemCount(): Int {
            return data.size
        }

    }

    inner class MealCardViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.day_meal_title)
        val subtitle: TextView = view.findViewById(R.id.day_meal_subtitle)
    }
}