package net.codeinreal.projects.bestplace

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import androidx.constraintlayout.motion.widget.KeyTimeCycle

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, "bestplace", null, 2) {
    companion object {
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

    fun addPlace(bestPlace: BestPlace) {
        val writableDatabase = this.writableDatabase//getWritableDatabase()
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, bestPlace.id)
        contentValues.put(KEY_TITLE, bestPlace.title)
        contentValues.put(KEY_IMAGE, bestPlace.image)
        contentValues.put(KEY_DESCRIPTION, bestPlace.description)
        contentValues.put(KEY_DATE, bestPlace.date)
        contentValues.put(KEY_LOCATION, bestPlace.location)
        contentValues.put(KEY_LATITUDE, bestPlace.latitude)
        contentValues.put(KEY_LONGITUDE, bestPlace.longitude)

        writableDatabase.insertWithOnConflict(
            TABLE_NAME,
            null,
            contentValues,
            SQLiteDatabase.CONFLICT_REPLACE
        )
        writableDatabase.close()
    }


    fun getPlaceById(id: String):BestPlace {
        val getQuery = "select * from ${TABLE_NAME} where KEY_ID='$id'"//sql query
        val db = this.readableDatabase
        val cursor = db.rawQuery(getQuery,null)
        cursor.moveToNext()
        val bestPlace = BestPlace(
            cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
            cursor.getString(cursor.getColumnIndex(KEY_IMAGE)),
            cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)),
            cursor.getString(cursor.getColumnIndex(KEY_DATE)),
            cursor.getString(cursor.getColumnIndex(KEY_LOCATION)),
            cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)).toDouble(),
            cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)).toDouble()
        )
        bestPlace.id = cursor.getString(cursor.getColumnIndex(KEY_ID))
        return bestPlace
    }

    fun getAllPlaces(): ArrayList<BestPlace> {
        val getAllQuery = "select * from ${TABLE_NAME}"//sql query
        val db = this.readableDatabase
        val bestplaceArray = ArrayList<BestPlace>()

        try {
            val cursor = db.rawQuery(getAllQuery, null)//result -> curosr
            while (cursor.moveToNext()) {
                val bestPlace = BestPlace(
                    cursor.getString(cursor.getColumnIndex(KEY_TITLE)),
                    cursor.getString(cursor.getColumnIndex(KEY_IMAGE)),
                    cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)),
                    cursor.getString(cursor.getColumnIndex(KEY_DATE)),
                    cursor.getString(cursor.getColumnIndex(KEY_LOCATION)),
                    cursor.getString(cursor.getColumnIndex(KEY_LATITUDE)).toDouble(),
                    cursor.getString(cursor.getColumnIndex(KEY_LONGITUDE)).toDouble()
                )
                bestPlace.id = cursor.getString(cursor.getColumnIndex(KEY_ID))

                bestplaceArray.add(bestPlace)
            }
            if (!cursor.isClosed) cursor.close()
        } catch (ex: SQLiteException) {
            ex.printStackTrace()
        }

        db.close()
        return bestplaceArray
    }
}