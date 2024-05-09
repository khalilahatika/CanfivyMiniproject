package org.d3if0047.canvufyminiproject.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.d3if0047.canvufyminiproject.model.Pallete

@Database(entities = [Pallete::class], version = 4, exportSchema = false)
abstract class PaletDb : RoomDatabase() {
    abstract val dao: PaletDao
    companion object {

        @Volatile
        private var INSTANCE: PaletDb? = null

        fun getInstance(context: Context): PaletDb {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PaletDb::class.java,
                        "pallete.db"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}