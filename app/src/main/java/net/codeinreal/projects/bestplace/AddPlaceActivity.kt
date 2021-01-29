package net.codeinreal.projects.bestplace

import android.R.attr.typeface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import ir.hamsaa.persiandatepicker.Listener
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.util.PersianCalendar
import net.codeinreal.projects.bestplace.databinding.ActivityAddPlaceBinding


class AddPlaceActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddPlaceBinding

    companion object {
        const val TAG = "TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlaceBinding.inflate(LayoutInflater.from(this@AddPlaceActivity))
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.textInputEditTextDate.showSoftInputOnFocus = false

        binding.textInputEditTextDate.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) return@setOnFocusChangeListener

            showDatePicker()


        }


    }

    private fun showDatePicker() {
        val picker = PersianDatePickerDialog(this)
            .setPositiveButtonString("باشه")
            .setNegativeButton("بیخیال")
            .setTodayButton("برو به امروز")
            .setTodayButtonVisible(true)
            .setMinYear(1300)
            .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
            .setActionTextColor(resources.getColor(R.color.primaryDarkColor))
            .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
            .setShowInBottomSheet(false)
            .setListener(object : Listener {
                override fun onDateSelected(persianCalendar: PersianCalendar) {
                    binding.textInputEditTextDate.setText(persianCalendar?.persianLongDate?.toString())

                }

                override fun onDismissed() {
                    Toast.makeText(
                        this@AddPlaceActivity,
                        "باید یک تاریخ انتخاب کنید!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

        picker.show()
    }
}