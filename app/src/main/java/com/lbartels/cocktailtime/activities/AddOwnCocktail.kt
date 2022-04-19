package com.lbartels.cocktailtime.activities

import android.app.Activity
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
import com.lbartels.cocktailtime.databinding.ActivityAddOwnCocktailBinding
import com.lbartels.cocktailtime.models.DrinkModel
import java.io.IOException

class AddOwnCocktail : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddOwnCocktailBinding

    private var saveImageToInternalStorage : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddOwnCocktailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvAddCocktailImage.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when(view!!.id) {
            R.id.tv_add_cocktail_image -> {
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
        val addCocktail = DrinkModel(
            randomID(),
            binding.etTitle.text.toString(),
            binding.etInstructions.text.toString(),
            saveImageToInternalStorage.toString(),
            binding.etIng1.text.toString(),
            binding.etIng2.text.toString(),
            binding.etIng3.text.toString(),
            binding.etIng4.text.toString(),
            binding.etIng5.text.toString(),
            binding.etIng6.text.toString(),

        )
        val dbHandler = DatabaseHandler(this)
        val addOwnCocktail = dbHandler.addCocktailToList(addCocktail)
        if (addOwnCocktail > 0) {
            setResult(Activity.RESULT_OK)
            finish()
        }

    }
    private fun randomID(): String = List(16) {
        (('a'..'z') + ('A'..'Z') + ('0'..'9')).random()
    }.joinToString("")

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

    companion object{
        private const val GALLERY = 1
        private const val CAMERA = 2
    }
}