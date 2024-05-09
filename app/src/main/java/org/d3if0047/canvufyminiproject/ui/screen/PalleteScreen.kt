package org.d3if0047.canvufyminiproject.ui.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

import org.d3if0047.canvufyminiproject.R
import org.d3if0047.canvufyminiproject.model.Pallete
import org.d3if0047.canvufyminiproject.ui.theme.CanvufyMiniprojectTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PalleteScreen(navHostController: NavHostController){
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {navHostController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack ,
                            contentDescription = stringResource(R.string.Kembali),
                            tint = Color.White)
                    }
                },
                title = {
                    Text(text = stringResource(id = R.string.pallete))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                )

            )
        }
    )
    { padding ->
        ScreenContent(Modifier.padding(padding))
    }
}


@Composable
fun ScreenContent(modifier: Modifier){
    val viewModel: MainViewModel = viewModel()
    val data = viewModel.data
    val context = LocalContext.current

    if (data.isEmpty()){
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(text = stringResource(id = R.string.list_kosong))
        }
    }
    else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom =  84.dp)
        ) {
            items(data) {
                ListItem(palette = it){
                    val pesan = context.getString(R.string.x_diklik,it.nama)
                    Toast.makeText(context, pesan, Toast.LENGTH_SHORT).show()
                }
                Divider()
            }
        }
    }


}
@Composable

fun ListItem(palette: Pallete, onClick: () -> Unit) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Image(
                painter = painterResource(id = palette.imageResId),
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )
            Text(
                text = palette.nama,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = palette.kode1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = palette.kode2,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = palette.kode3,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
            Text(text = palette.kode4)
        }
        Button(
            onClick = {
                val data = context.getString(R.string.bagikan_template)+"\n" +
                        context.getString(R.string.name)+ ": ${palette.nama}\n" +
                        context.getString(R.string.kode1)+": ${palette.kode1}\n" +
                        context.getString(R.string.kode2)+": ${palette.kode2}\n" +
                        context.getString(R.string.kode3)+": ${palette.kode3}\n" +
                        context.getString(R.string.kode4)+": ${palette.kode4}"
                shareData(
                    context = context,
                    message = data
                )
            },
            modifier = Modifier.padding(start = 8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text(text = stringResource(R.string.bagikan))
        }
    }
}

private  fun shareData(context: Context, message: String){
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT,message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null){
        context.startActivity(shareIntent)
    }
}
@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun PalleteScreenPreview(){
    CanvufyMiniprojectTheme {
        PalleteScreen(rememberNavController())
    }
}