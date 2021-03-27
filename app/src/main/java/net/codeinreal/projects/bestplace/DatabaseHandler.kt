package net.codeinreal.projects.bestplace

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.constraintlayout.motion.widget.KeyTimeCycle

class DatabaseHandler(context: Context)
    :SQLiteOpenHelper(context,"bestplace",null,2){
    companion object{
        const val TABLE_NAME = "bestplacetable"
        const val KEY_ID = "KEY_ID"
        const val KEY_TITLE = "_title"
        const val KEY_IMAGE = "_image"
        const val KEY_DESCRIPTION = "_description"
        const val KEY_DATE = "_date"
        const val KEY_LOCATION = "_location"
        const val KEY_LATITUDE = "_latitude"
        const val KEY_LONGITUDE = "_longitude"
    }



    override fun onCreate(db: SQLiteDatabase?) {
         val createTableBestPlace = ("create table " + TABLE_NAME + " ( "
                 + KEY_ID + " text primary key, "
                 + KEY_TITLE + " text, "
                 + KEY_IMAGE + " text, "
                 + KEY_DESCRIPTION + " text, "
                 + KEY_DATE + " text, "
                 + KEY_LOCATION + " text, "
                 + KEY_LATITUDE + " text, "
                 + KEY_LONGITUDE + " text )")
        db?.execSQL(createTableBestPlace)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("drop table if exists ${TABLE_NAME}")
        onCreate(db)
    }

    fun addPlace(bestPlace:BestPlace){
        val writableDatabase = this.writableDatabase//getWritableDatabase()
        val contentValues = ContentValues()
        contentValues.put(KEY_ID,bestPlace.id)
        contentValues.put(KEY_TITLE,bestPlace.title)
        contentValues.put(KEY_IMAGE,bestPlace.image)
        contentValues.put(KEY_DESCRIPTION,bestPlace.description)
        contentValues.put(KEY_DATE,bestPlace.date)
        contentValues.put(KEY_LOCATION,bestPlace.location)
        contentValues.put(KEY_LATITUDE,bestPlace.latitude)
        contentValues.put(KEY_LONGITUDE,bestPlace.longitude)

        writableDatabase.insert(TABLE_NAME,null,contentValues)
        writableDatabase.close()
    }
}