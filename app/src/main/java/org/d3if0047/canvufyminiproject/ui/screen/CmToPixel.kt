package org.d3if0047.canvufyminiproject.ui.screen


import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if0047.canvufyminiproject.R
import org.d3if0047.canvufyminiproject.navigation.Screen
import org.d3if0047.canvufyminiproject.ui.theme.CanvufyMiniprojectTheme
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CmToPixel(navHostController: NavHostController){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                navigationIcon = {
                    IconButton(onClick = {navHostController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack ,
                            contentDescription = stringResource(R.string.Kembali),
                            tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                ),


            )
        }
    ){ padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.judul_pixelcm),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            CmToPixelScreen()
        }
    }
}
@Composable
fun CmToPixelScreen() {
    var cmText by rememberSaveable { mutableStateOf("") }
    var pixelValue by rememberSaveable { mutableStateOf(0f) }
    var roundValue by rememberSaveable { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        TextField(
            value = cmText,
            onValueChange = {
                if (it.all { char -> char.isDigit() || char == '.' }) {
                    cmText = it
                    errorMessage = null
                } else {
                    errorMessage = context.getString(R.string.error1)
                }
            },
            label = { Text(text = stringResource(R.string.input_value_cm)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = errorMessage != null,
            singleLine = true
        )

        errorMessage?.let { message ->
            Text(
                text = message,
                color = Color.Red,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = stringResource(R.string.bulatkan))
            Spacer(modifier = Modifier.width(8.dp))
            Checkbox(
                checked = roundValue,
                onCheckedChange = { roundValue = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(
                onClick = {
                    if (cmText.isEmpty()) {
                        errorMessage = context.getString(R.string.error2)
                        return@Button
                    }
                    val cm = cmText.toFloatOrNull()
                    cm?.let {
                        pixelValue = convertCmToPixel(cm, roundValue)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text(stringResource(id = R.string.convert))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    // Clear data
                    cmText = ""
                    pixelValue = 0f
                    errorMessage = null
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text(stringResource(id = R.string.clear))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = stringResource(id = R.string.Hasil_pixel, pixelValue))
    }
}

fun convertCmToPixel(cm: Float, round: Boolean): Float {
    val pixelPerCm = 37.795275591f
    var result = cm * pixelPerCm
    if (round) {
        result = result.roundToInt().toFloat()
    }
    return result
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScreenPreview() {
    CanvufyMiniprojectTheme {
        CmToPixel(rememberNavController())
    }
}