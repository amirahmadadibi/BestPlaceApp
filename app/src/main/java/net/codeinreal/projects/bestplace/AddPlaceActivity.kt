package net.codeinreal.projects.bestplace

import android.Manifest
import android.R.attr.typeface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.DexterBuilder
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
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

        binding.buttonSelectImage.setOnClickListener {

            AlertDialog.Builder(this@AddPlaceActivity)
                .setTitle("انتخاب عکس")
                .setItems(
                    arrayOf(
                        "انتخاب عکس از گالری",
                        "گرفتن عکس با دوربین"
                    )
                ) { dialog, options ->
                    when (options) {
                        0 -> {
                            choosePhotoFromGallery()
                        }
                        1 -> {
                            Toast.makeText(this@AddPlaceActivity, "camera", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
                .show()
        }
    }

    private fun choosePhotoFromGallery() {
        Dexter.withContext(this@AddPlaceActivity)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {

                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    showRationalPermiison()
                }

            })
            .check()
    }

    private fun showRationalPermiison() {
        AlertDialog.Builder(this@AddPlaceActivity)
            .setTitle("اجازه دسترسی به فایل ها و دوربین")
            .setPositiveButton("برو به تنظیمات") { _, _ ->
                val settinsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri  = Uri.fromParts("packge",packageName,null)
                settinsIntent.data  = uri
                startActivity(settinsIntent)
            }
            .setNegativeButton("اجازه نمیدم"){dialog,_ ->
                finish()
            }
            .show()
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