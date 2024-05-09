package org.d3if0047.canvufyminiproject.model

import androidx.annotation.DrawableRes

data class Pallete(
    val nama: String,
    val kode1: String,
    val kode2: String,
    val kode3: String,
    val kode4: String,
    @DrawableRes val imageResId : Int

)