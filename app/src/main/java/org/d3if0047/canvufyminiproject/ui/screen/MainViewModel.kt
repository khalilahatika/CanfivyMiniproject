package org.d3if0047.canvufyminiproject.ui.screen

import androidx.lifecycle.ViewModel
import org.d3if0047.canvufyminiproject.R
import org.d3if0047.canvufyminiproject.model.Pallete

class MainViewModel : ViewModel() {
    val data = getDataDummy()

    private fun getDataDummy():List<Pallete>{
        val data = mutableListOf<Pallete>()
        data.add(
            Pallete(
                "GIRLY",
                "#FF6C6C",
                "#172A31",
                "#CEADDD",
                "#7C68FC",
                R.drawable.girly
            )
        )

        data.add(
            Pallete(
                "NEON",
                "#6E62FF",
                "#D737FF",
                "#62FFFF",
                "#6F6F6F",
                R.drawable.neon
            )
        )

        data.add(
            Pallete(
                "RETRO",
                "#FFEE59",
                "#3895B2",
                "#110D40",
                "#AA3C3C",
                R.drawable.old
            )
        )

        data.add(
            Pallete(
                "SEA",
                "#4891FF",
                "#60D9FF",
                "#FFC121",
                "#DCFFEA",
                R.drawable.sea
            )
        )

        data.add(
            Pallete(
                "WARM",
                "#F51F1F",
                "#F57D1F",
                "#FFF500",
                "#FFFFFF",
                R.drawable.warm
            )
        )


        return data
    }
}