package com.example.foodieplanner

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.foodieplanner.databinding.FormNewMealBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class NewMealDialog: DialogFragment() {

    private lateinit var binding: FormNewMealBinding
    private val toolbar: MaterialToolbar? = null
    private val standardVolumes = arrayListOf(Unit.TSP,Unit.TBSP,Unit.FLOZ,Unit.CUP,Unit.PINT,Unit.QUART,Unit.GAL)
    private val metricVolumes = arrayListOf(Unit.ML,Unit.LITER)
    private val standardWeights = arrayListOf(Unit.LB,Unit.OUNCE)
    private val metricWeights = arrayListOf(Unit.MG,Unit.GRAM,Unit.KG)
    private var adapter: IngredientAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FormNewMealBinding.inflate(layoutInflater)

        adapter = IngredientAdapter()
        binding.ingredientsRecycler.adapter = adapter

        binding.newMealTopBar.setNavigationOnClickListener {
            this.dialog?.cancel()
            this.dismiss()
        }
        binding.newMealTopBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.new_meal_save -> {
                    this.dismiss()
                    true
                }
                else -> false
            }
        }

        binding.newMealOpenNi.setOnClickListener {
            context?.let { context ->
                // open a new alert dialog
                openIngredientDialog()
            }
        }
        return binding.root
    }

    fun openIngredientDialog(existingIngredient: Ingredient? = null, pos: Int? = null) {
        val view: View? = this.layoutInflater.inflate(R.layout.form_new_ingredient,null)
        val quantityTextField = view?.findViewById<TextInputLayout>(R.id.ni_text_field_quantity)
        val nameTextField = view?.findViewById<TextInputLayout>(R.id.ni_text_field_name)
        val chipGroupUnit = view?.findViewById<ChipGroup>(R.id.ni_chip_group_unit)
        val chipGroupStandard = view?.findViewById<ChipGroup>(R.id.ni_chip_group_standard)
        val chipGroupAmount = view?.findViewById<ChipGroup>(R.id.ni_chip_group_amount)

        var currentIngredient = Ingredient()
        if (existingIngredient != null) {
            currentIngredient = existingIngredient
            nameTextField?.editText?.setText(existingIngredient.name)
            quantityTextField?.editText?.setText(existingIngredient.quantity)
        }

        populateChipGroup(chipGroupUnit,currentIngredient)

        chipGroupUnit?.setOnCheckedChangeListener { group, checkedId ->
            currentIngredient.unit = getUnitAbbrev(checkedId)
            addSuffix(checkedId, quantityTextField)
        }

        chipGroupStandard?.setOnCheckedChangeListener { group, checkedId ->
            currentIngredient.setStandard(checkedId)
            addSuffix(Unit.NONE.id(), quantityTextField)
            populateChipGroup(chipGroupUnit,currentIngredient)
        }

        chipGroupAmount?.setOnCheckedChangeListener { group, checkedId ->
            currentIngredient.setMeasure(checkedId)
            addSuffix(Unit.NONE.id(), quantityTextField)
            populateChipGroup(chipGroupUnit,currentIngredient)
        }

        context?.let { it
            val alertDialog: AlertDialog = MaterialAlertDialogBuilder(it)
                .setTitle("Add Ingredient")
                .setView(view)
                .setNeutralButton("Cancel") { dialog, which ->
                    // Respond to neutral button press
                }
                .setPositiveButton("Save") { dialog, which ->
                    // Respond to positive button press
                    if (pos != null) {
                        adapter?.editIngredient(pos, currentIngredient)
                    }
                    else {
                        adapter?.addIngredient(currentIngredient)
                    }
                }
                .setNegativeButton("Delete") { dialog, which ->
                    if (pos != null) {
                        adapter?.deleteIngredient(pos)
                    }
                }
                .create()
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = existingIngredient != null
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).isEnabled = existingIngredient != null

            // handle error on text fields
            nameTextField?.editText?.addTextChangedListener { text ->
                if (text.isNullOrBlank()) {
                    nameTextField.error = "Required"
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                }
                else {
                    nameTextField.error = null
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled =
                        !quantityTextField?.editText?.text.isNullOrBlank()
                    currentIngredient.name = text.toString()
                }
            }
            quantityTextField?.editText?.addTextChangedListener { text ->
                if (text.isNullOrBlank()) {
                    quantityTextField.error = "Required"
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                }
                else {
                    quantityTextField.error = null
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled =
                        !nameTextField?.editText?.text.isNullOrBlank()
                    currentIngredient.quantity = text.toString()
                }

            }

        }
    }



    fun addSuffix(id: Int, textField: TextInputLayout?) {
        textField?.suffixText = when(id) {
            Unit.TSP.id() -> Unit.TSP.suffix()
            Unit.TBSP.id() -> Unit.TBSP.suffix()
            Unit.FLOZ.id() -> Unit.FLOZ.suffix()
            Unit.CUP.id() -> Unit.CUP.suffix()
            Unit.PINT.id() -> Unit.PINT.suffix()
            Unit.QUART.id() -> Unit.QUART.suffix()
            Unit.GAL.id() -> Unit.GAL.suffix()
            Unit.ML.id() -> Unit.ML.suffix()
            Unit.LITER.id() -> Unit.LITER.suffix()
            Unit.LB.id() -> Unit.LB.suffix()
            Unit.OUNCE.id() -> Unit.OUNCE.suffix()
            Unit.GRAM.id() -> Unit.GRAM.suffix()
            Unit.MG.id() -> Unit.MG.suffix()
            Unit.KG.id() -> Unit.KG.suffix()
            Unit.NONE.id() -> Unit.NONE.suffix()
            else -> ""
        }
    }

    fun getUnitAbbrev(id: Int?): Unit {
         return when(id) {
            Unit.TSP.id() -> Unit.TSP
            Unit.TBSP.id() -> Unit.TBSP
            Unit.FLOZ.id() -> Unit.FLOZ
            Unit.CUP.id() -> Unit.CUP
            Unit.PINT.id() -> Unit.PINT
            Unit.QUART.id() -> Unit.QUART
            Unit.GAL.id() -> Unit.GAL
            Unit.ML.id() -> Unit.ML
            Unit.LITER.id() -> Unit.LITER
            Unit.LB.id() -> Unit.LB
            Unit.OUNCE.id() -> Unit.OUNCE
            Unit.GRAM.id() -> Unit.GRAM
            Unit.MG.id() -> Unit.MG
            Unit.KG.id() -> Unit.KG
            Unit.NONE.id() -> Unit.NONE
            else -> Unit.NONE
        }
    }

    fun populateChipGroup(group: ChipGroup?, ingredient: Ingredient) {
        group?.removeAllViews()
        val standard = ingredient.standard
        val measure = ingredient.measure
        if (standard == "us") {
            when(measure) {
                "vol" -> {
                    for (unit in standardVolumes) {
                        group?.addView(createChipView(unit, ingredient))
                    }
                }
                "weight" -> {
                    for (unit in standardWeights) {
                        group?.addView(createChipView(unit, ingredient))
                    }
                }
                "none" -> {
                    group?.addView(createChipView(Unit.NONE, ingredient))
                }
            }
        }
        else if (standard == "metric") {
            when(measure) {
                "vol" -> {
                    for (unit in metricVolumes) {
                        group?.addView(createChipView(unit, ingredient))
                    }
                }
                "weight" -> {
                    for (unit in metricWeights) {
                        group?.addView(createChipView(unit, ingredient))
                    }
                }
                "none" -> {
                    group?.addView(createChipView(Unit.NONE, ingredient))
                }
            }
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    fun createChipView(unit: Unit, ingredient: Ingredient): Chip {
        val chip: Chip = layoutInflater.inflate(R.layout.chip_new_meal_unit, null) as Chip
        chip.text = unit.toString()
        chip.id = unit.id()
        if (chip.id == ingredient.unit?.id()) {
            chip.isChecked = true
        }
        return chip
    }

    // New Ingredients to Populate Recycler View
    inner class IngredientAdapter: RecyclerView.Adapter<NewMealDialog.IngredientViewHolder>() {

        var data = arrayListOf<Ingredient>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewMealDialog.IngredientViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.view_new_meal_ingredient, parent, false)
            return IngredientViewHolder(view)
        }

        override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
            holder.text.text = data[position].name
            holder.quantity.text = data[position].quToString()
            //holder.count.text = "%2d".format(position+1)

            holder.text.setOnClickListener {
                openIngredientDialog(data[position], position)
            }
        }

        override fun getItemCount(): Int {
            return data.size
        }

        fun addIngredient(ingredient: Ingredient?) {
            if (ingredient != null) {
                data.add(ingredient)
                notifyDataSetChanged()
            }
        }

        fun editIngredient(index: Int, ingredient: Ingredient) {
            data[index] = ingredient
            notifyDataSetChanged()
        }

        fun deleteIngredient(pos: Int) {
            data.removeAt(pos)
            notifyDataSetChanged()
        }
    }

    inner class IngredientViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //val count: TextView = view.findViewById(R.id.new_meal_ingredient_number)
        val text: TextView = view.findViewById(R.id.new_meal_ingredient_text)
        val quantity: TextView = view.findViewById(R.id.new_meal_ingredient_quantity)
    }

}