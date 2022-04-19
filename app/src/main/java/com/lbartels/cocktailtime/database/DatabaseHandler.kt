package com.lbartels.cocktailtime.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.lbartels.cocktailtime.models.DrinkModel
import com.lbartels.cocktailtime.models.MemoryModel

class DatabaseHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "CocktailMemory"
        private const val TABLE_MEMORY = "Memory"
        private const val TABLE_OWN_COCKTAIL = "OwnCocktail"

        //All the Columns names
        private const val KEY_ID = "_id"
        private const val KEY_TITLE = "title"
        private const val KEY_IMAGE = "image"
        private const val KEY_DATE = "date"

        private const val KEY_DRINK_ID = "_id"
        private const val KEY_DRINK = "drink"
        private const val KEY_DRINK_IMAGE = "image"
        private const val KEY_INSTRUCTIONS= "instructions"
        private const val KEY_ING_1= "ingredient1"
        private const val KEY_ING_2= "ingredient2"
        private const val KEY_ING_3= "ingredient3"
        private const val KEY_ING_4= "ingredient4"
        private const val KEY_ING_5= "ingredient5"
        private const val KEY_ING_6= "ingredient6"
        private const val KEY_ING_7= "ingredient7"
        private const val KEY_ING_8= "ingredient8"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_MEMORY = ("CREATE TABLE " + TABLE_MEMORY + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_DATE + " TEXT)")
        db?.execSQL(CREATE_TABLE_MEMORY)

        val CREATE_OWN_COCKTAIL = ("CREATE TABLE " + TABLE_OWN_COCKTAIL + "("
                + KEY_DRINK_ID + " CHAR PRIMARY KEY,"
                + KEY_DRINK + " TEXT,"
                + KEY_DRINK_IMAGE + " TEXT,"
                + KEY_INSTRUCTIONS + " TEXT,"
                + KEY_ING_1 + " TEXT,"
                + KEY_ING_2 + " TEXT,"
                + KEY_ING_3 + " TEXT,"
                + KEY_ING_4 + " TEXT,"
                + KEY_ING_5 + " TEXT,"
                + KEY_ING_6 + " TEXT,"
                + KEY_ING_7 + " TEXT,"
                + KEY_ING_8 + " TEXT)")
        db?.execSQL(CREATE_OWN_COCKTAIL)

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_MEMORY")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_OWN_COCKTAIL")
        onCreate(db)
    }

    fun addCocktailMemory(memoryModel: MemoryModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_TITLE, memoryModel.title)
        contentValues.put(KEY_IMAGE, memoryModel.image)
        contentValues.put(KEY_DATE, memoryModel.date)

        val result = db.insert(TABLE_MEMORY, null, contentValues)

        db.close()
        return result
    }

    fun addCocktailToList(drink: DrinkModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(KEY_DRINK_ID, drink.idDrink)
        contentValues.put(KEY_DRINK, drink.strDrink)
        contentValues.put(KEY_INSTRUCTIONS, drink.strInstructions)
        contentValues.put(KEY_DRINK_IMAGE, drink.strDrinkThumb)
        contentValues.put(KEY_ING_1, drink.strIngredient1)
        contentValues.put(KEY_ING_2, drink.strIngredient2)
        contentValues.put(KEY_ING_3, drink.strIngredient3)
        contentValues.put(KEY_ING_4, drink.strIngredient4)
        contentValues.put(KEY_ING_5, drink.strIngredient5)
        contentValues.put(KEY_ING_6, drink.strIngredient6)
        contentValues.put(KEY_ING_7, drink.strIngredient7)
        contentValues.put(KEY_ING_8, drink.strIngredient8)

        val result = db.insert(TABLE_OWN_COCKTAIL, null, contentValues)

        db.close()
        return result
    }

    fun getCocktailMemoryList(): ArrayList<MemoryModel> {
        val cocktailMemoryList : ArrayList<MemoryModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_MEMORY"

        val db = this.readableDatabase

        try {
            val cursor: Cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val place = MemoryModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_IMAGE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATE)),
                    )
                    cocktailMemoryList.add(place)
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        return cocktailMemoryList
    }

    fun getOwnCocktailList(): ArrayList<DrinkModel> {
        val ownCocktailList : ArrayList<DrinkModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_OWN_COCKTAIL"

        val db = this.readableDatabase

        try {
            val cursor: Cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val place = DrinkModel(
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DRINK_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DRINK)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_INSTRUCTIONS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_DRINK_IMAGE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_ING_1)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_ING_2)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_ING_3)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_ING_4)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_ING_5)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_ING_6)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_ING_7)),
                        cursor.getString(cursor.getColumnIndexOrThrow(KEY_ING_8)),
                    )
                    ownCocktailList.add(place)
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        return ownCocktailList
    }

    fun deleteMemoryPlace(cocktailMemoryModel: MemoryModel): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_MEMORY, KEY_ID + "=" + cocktailMemoryModel.id, null)
        db.close()
        return success
    }

    fun deleteOwnCocktail(cocktailOwn: DrinkModel): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_OWN_COCKTAIL, KEY_DRINK_ID + "=" + "'${cocktailOwn.idDrink}'", null)
        db.close()
        return success
    }


}