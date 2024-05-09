package org.d3if0047.canvufyminiproject.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Palet")
data class Pallete(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nama: String,
    val kode1: String,
    val kode2: String,
    val kode3: String,
    val kode4: String,
    val Genre: String



)