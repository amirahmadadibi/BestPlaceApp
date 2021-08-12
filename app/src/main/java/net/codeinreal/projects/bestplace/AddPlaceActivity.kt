package net.codeinreal.projects.bestplace

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import ir.hamsaa.persiandatepicker.Listener
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog
import ir.hamsaa.persiandatepicker.util.PersianCalendar
import net.codeinreal.projects.bestplace.databinding.ActivityAddPlaceBinding
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class AddPlaceActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddPlaceBinding
    var imageAddressLocation: String? = null
    private var id: String? = null
    private var bestPlaceSelecetToEdit: BestPlace? = null
    private var lat: Double? = null
    private var lon: Double? = null

    companion object {
        const val TAG = "TAG"
        const val REQUEST_CODE_CAMERA = 13134
        const val REQUEST_CODE_GALLERY = 13135
        const val REQUEST_CODE_SELECT_PLACE = 131412
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
        binding.buttonSelectLocation.setOnClickListener {
            val intentSelectLocation =
                Intent(this@AddPlaceActivity, SelectPlaceActivity::class.java)
            startActivityForResult(intentSelectLocation, REQUEST_CODE_SELECT_PLACE)
        }
        binding.edtDate.showSoftInputOnFocus = false

        binding.edtDate.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) return@setOnFocusChangeListener

            showDatePicker()


        }

        if (intent.extras?.getString("id") != null) {
            id = intent.extras?.getString("id")
            val db = DatabaseHandler(this@AddPlaceActivity)
            val bestPlace = db.getPlaceById(id!!)
            bestPlaceSelecetToEdit = bestPlace
            binding.edtTitle.setText(bestPlace.title)
            binding.edtDescription.setText(bestPlace.description)
            binding.edtDate.setText(bestPlace.date)
            binding.edtLocation.setText(bestPlace.location)
            val imageBitmap = MediaStore.Images.Media.getBitmap(
                contentResolver,
                Uri.fromFile(File(bestPlace.image))
            )

            binding.imageViewSelectedPicture.setImageBitmap(imageBitmap)
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
                            choosePhotoByCamera()
                        }
                    }
                }
                .show()
        }



        binding.buttonAddPlace.setOnClickListener {
            val title = binding.edtTitle.text.toString()
            val description = binding.edtDescription.text.toString()
            val date = binding.edtDate.text.toString()
            val location = binding.edtLocation.text.toString()

            if (imageAddressLocation == null) {
                imageAddressLocation = bestPlaceSelecetToEdit?.image//uri
            }
            val myBestPlace =
                BestPlace(
                    title,
                    imageAddressLocation!!,
                    description,
                    date,
                    location,
                    lat ?: 0.0,
                    lon ?: 0.0
                )

            id?.let {
                myBestPlace.id = it
            }
            val db = DatabaseHandler(this)
            db.addPlace(myBestPlace)
            setResult(Activity.RESULT_OK)
            finish()
        }
    }


    private fun choosePhotoByCamera() {
        Dexter.withContext(this@AddPlaceActivity)
            .withPermissions(
                Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        capturePictureByCamera()
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
//                        capturePictureByCamera()
                        val galleryIntent =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY)
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


    private fun capturePictureByCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA)
    }

    private fun showRationalPermiison() {
        AlertDialog.Builder(this@AddPlaceActivity)
            .setTitle("اجازه دسترسی به فایل ها و دوربین")
            .setPositiveButton("برو به تنظیمات") { _, _ ->
                val settinsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("packge", packageName, null)
                settinsIntent.data = uri
                startActivity(settinsIntent)
            }
            .setNegativeButton("اجازه نمیدم") { dialog, _ ->
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
                    binding.edtDate.setText(persianCalendar?.persianLongDate?.toString())

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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) {
                val bitmpatImage = data?.extras?.get("data") as Bitmap

                imageAddressLocation = saveImageInInternalStorage(bitmpatImage)
                binding.imageViewSelectedPicture.setImageBitmap(bitmpatImage)
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_GALLERY) {
                if (data != null) {
                    val contentUri = data.data//uri
                    try {
                        val imageBitmap =
                            MediaStore.Images.Media.getBitmap(this.contentResolver, contentUri)
                        imageAddressLocation = saveImageInInternalStorage(imageBitmap)

                        binding.imageViewSelectedPicture.setImageBitmap(imageBitmap)
                    } catch (ex: FileNotFoundException) {
                        Toast.makeText(this, "فایل انتخابی شما پیدا نشد", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            if (requestCode == REQUEST_CODE_CAMERA) {
                Toast.makeText(this, "عکسی نگرفتید", Toast.LENGTH_SHORT).show()
            }

            if (requestCode == REQUEST_CODE_GALLERY) {
                Toast.makeText(this, "فایلی انتخاب نکردید", Toast.LENGTH_SHORT).show()
            }
        }

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_PLACE) {
                lat = data?.getDoubleExtra("lat", 0.0)
                lon = data?.getDoubleExtra("lon", 0.0)

                Toast.makeText(this@AddPlaceActivity, "${lat}${lon}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun saveImageInInternalStorage(bitmap: Bitmap): String {
        val fileAndPath = buildFile("test${System.currentTimeMillis()}.jpg")
        try {
            val portal = buildOutputStream(fileAndPath)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, portal)
            portal.flush()
            portal.close()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        return fileAndPath.absolutePath
    }

    fun buildOutputStream(file: File): FileOutputStream {
        return FileOutputStream(file)
    }

    fun buildFile(fileName: String): File {//amir.mp4
        return File(this.filesDir, fileName)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        setResult(Activity.RESULT_CANCELED)
    }
}