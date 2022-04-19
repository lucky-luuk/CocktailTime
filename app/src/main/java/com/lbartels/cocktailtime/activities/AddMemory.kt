package com.lbartels.cocktailtime.activities

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.lbartels.cocktailtime.DTO.CameraDTO
import com.lbartels.cocktailtime.R
import com.lbartels.cocktailtime.database.DatabaseHandler
import com.lbartels.cocktailtime.databinding.ActivityAddMemoryBinding
import com.lbartels.cocktailtime.models.MemoryModel
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddMemory : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddMemoryBinding

    private var calendar = Calendar.getInstance()
    private lateinit var dateSetListner: DatePickerDialog.OnDateSetListener
    private var saveImageToInternalStorage : Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMemoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dateSetListner =
            DatePickerDialog.OnDateSetListener{ _, year, monthOfYear, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                updateDateInView()
            }
        updateDateInView()

        binding.etDate.setOnClickListener(this)
        binding.tvAddImage.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view!!.id) {
            R.id.et_date -> {
                DatePickerDialog(
                    this,
                    dateSetListner,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            R.id.tv_add_image -> {
                val pickerDialog = AlertDialog.Builder(this)
                pickerDialog.setTitle("Select Action")
                val pictureDialogItems =
                    arrayOf("Select photo from gallery", "Capture photo from camera")
                pickerDialog.setItems(pictureDialogItems) {
                    _, which ->
                    when(which){
                        0 -> CameraDTO.choosePhotoFromGallery(this, packageName)
                        1 -> CameraDTO.takePhotoFromCamera(this, packageName)
                    }
                }
                pickerDialog.show()
            }
            R.id.btn_save -> {
                when{
                    binding.etTitle.text.isNullOrEmpty() -> {
                        Toast.makeText(this, "Please enter title", Toast.LENGTH_SHORT).show()
                    }
                    saveImageToInternalStorage == null -> {
                        Toast.makeText(this, "Please select a image", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        addToDatabase()
                    }
                }
            }
        }
    }


    private fun addToDatabase() {
        val memoryModel = MemoryModel(
            0,
            binding.etTitle.text.toString(),
            saveImageToInternalStorage.toString(),
            binding.etDate.toString()
        )
        val dbHandler = DatabaseHandler(this)
        val addMemoryToDB = dbHandler.addCocktailMemory(memoryModel)
        if (addMemoryToDB > 0) {
            setResult(Activity.RESULT_OK)
            finish()
        }

    }

    private fun updateDateInView() {
        val myFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        binding.etDate.setText(sdf.format(calendar.time).toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                if(data != null) {
                    val contentURI = data.data
                    try {
                        val selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                        saveImageToInternalStorage = CameraDTO.saveImageToInternalStorage(selectedImageBitmap, applicationContext)
                        binding.ivPlaceImage.setImageBitmap(selectedImageBitmap)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(this, "Faild to loade image", Toast.LENGTH_SHORT).show()
                    }
                }
            }else if (requestCode == CAMERA) {
                val thumbnail : Bitmap = data!!.extras!!.get("data") as Bitmap
                saveImageToInternalStorage = CameraDTO.saveImageToInternalStorage(thumbnail, applicationContext)
                binding.ivPlaceImage.setImageBitmap(thumbnail)

            }
        }
    }

    companion object {
        private const val GALLERY = 1
        private const val CAMERA = 2
    }






}