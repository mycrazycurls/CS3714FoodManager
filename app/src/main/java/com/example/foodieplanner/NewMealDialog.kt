package com.example.foodieplanner

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.foodieplanner.databinding.NewMealFormBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import androidx.appcompat.app.AlertDialog
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputLayout

class NewMealDialog: DialogFragment() {

    private lateinit var binding: NewMealFormBinding
    private val toolbar: MaterialToolbar? = null
    private val standardVolumes = arrayListOf<Unit>(Unit.TSP,Unit.TBSP,Unit.FLOZ,Unit.CUP,Unit.PINT,Unit.QUART,Unit.GAL)
    private val metricVolumes = arrayListOf<Unit>(Unit.ML,Unit.LITER)
    private val standardWeights = arrayListOf<Unit>(Unit.LB,Unit.OUNCE)
    private val metricWeights = arrayListOf<Unit>(Unit.MG,Unit.GRAM,Unit.KG)

    private var standard: String = "us"
    private var measure: String = "vol"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = NewMealFormBinding.inflate(layoutInflater)

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

        binding.newMealOpenDialog.setOnClickListener {
            context?.let { context ->
                val view: View? = inflater.inflate(R.layout.new_ingredient_form,null)
                val quantityTextField = view?.findViewById<TextInputLayout>(R.id.ni_text_field_quantity)
                val nameTextField = view?.findViewById<TextInputLayout>(R.id.ni_text_field_name)

                val chipGroupUnit = view?.findViewById<ChipGroup>(R.id.ni_chip_group_unit)
                populateChipGroup(chipGroupUnit,standard,measure)
                chipGroupUnit?.setOnCheckedChangeListener { group, checkedId ->
                    addSuffix(checkedId, quantityTextField)
                }

                val chipGroupStandard = view?.findViewById<ChipGroup>(R.id.ni_chip_group_standard)
                chipGroupStandard?.setOnCheckedChangeListener { group, checkedId ->
                    when (checkedId) {
                        R.id.ni_us_chip -> {
                            standard = "us"
                        }
                        R.id.ni_metric_chip -> {
                            standard = "metric"
                        }
                    }
                    addSuffix(Unit.NONE.id(), quantityTextField)
                    populateChipGroup(chipGroupUnit,standard,measure)
                }

                val chipGroupAmount = view?.findViewById<ChipGroup>(R.id.ni_chip_group_amount)
                chipGroupAmount?.setOnCheckedChangeListener { group, checkedId ->
                    when (checkedId) {
                        R.id.ni_volume_chip -> {
                            measure = "vol"
                        }
                        R.id.ni_weight_chip -> {
                            measure = "weight"
                        }
                        R.id.ni_absolute_chip -> {
                            measure = "none"
                        }
                    }
                    addSuffix(Unit.NONE.id(), quantityTextField)
                    populateChipGroup(chipGroupUnit,standard,measure)
                }


                val alertDialog: AlertDialog = MaterialAlertDialogBuilder(context)
                    .setTitle("Add Ingredient")
                    .setView(view)
                    .setNeutralButton("Cancel") { dialog, which ->
                        // Respond to neutral button press
                    }
                    .setPositiveButton("Save") { dialog, which ->
                        // Respond to positive button press
                    }
                    .create()
                alertDialog.setCanceledOnTouchOutside(false)
                alertDialog.show()

            }
        }


        return binding.root
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

    fun populateChipGroup(group: ChipGroup?, standard: String, measure: String) {
        group?.removeAllViews()
        if (standard == "us") {
            when(measure) {
                "vol" -> {
                    for (unit in standardVolumes) {
                        val chip: Chip = layoutInflater.inflate(R.layout.ingredient_units_chip, null) as Chip
                        chip.text = unit.toString()
                        chip.id = unit.id()
                        group?.addView(chip)
                    }
                }
                "weight" -> {
                    for (unit in standardWeights) {
                        val chip: Chip = layoutInflater.inflate(R.layout.ingredient_units_chip, null) as Chip
                        chip.text = unit.toString()
                        chip.id = unit.id()
                        group?.addView(chip)
                    }
                }
                "none" -> {
                    val chip: Chip = layoutInflater.inflate(R.layout.ingredient_units_chip, null) as Chip
                    chip.text = Unit.NONE.toString()
                    chip.id = Unit.NONE.id()
                    group?.addView(chip)
                }
            }
        }
        else if (standard == "metric") {
            when(measure) {
                "vol" -> {
                    for (unit in metricVolumes) {
                        val chip: Chip = layoutInflater.inflate(R.layout.ingredient_units_chip, null) as Chip
                        chip.text = unit.toString()
                        chip.id = unit.id()
                        group?.addView(chip)
                    }
                }
                "weight" -> {
                    for (unit in metricWeights) {
                        val chip: Chip = layoutInflater.inflate(R.layout.ingredient_units_chip, null) as Chip
                        chip.text = unit.toString()
                        chip.id = unit.id()
                        group?.addView(chip)
                    }
                }
                "none" -> {
                    val chip: Chip = layoutInflater.inflate(R.layout.ingredient_units_chip, null) as Chip
                    chip.text = Unit.NONE.toString()
                    chip.id = Unit.NONE.id()
                    group?.addView(chip)
                }
            }
        }

    }




    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

}