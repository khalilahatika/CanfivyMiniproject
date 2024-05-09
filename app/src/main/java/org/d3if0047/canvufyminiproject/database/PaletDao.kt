package org.d3if0047.canvufyminiproject.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import org.d3if0047.canvufyminiproject.model.Pallete
@Dao
interface PaletDao {
    @Insert
    suspend fun insert(palet: Pallete)

    @Update
    suspend fun update(palet: Pallete)

    @Query("SELECT * FROM palet ORDER BY nama ASC")
    fun getPalet() : Flow<List<Pallete>>

    @Query("SELECT * FROM palet WHERE id = :id")
    suspend fun getPaletById(id: Long):Pallete?

    @Query("DELETE FROM palet WHERE id = :id")
    suspend fun deleteById(id: Long)
}