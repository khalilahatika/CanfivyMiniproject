package org.d3if0047.canvufyminiproject.ui.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if0047.canvufyminiproject.R
import org.d3if0047.canvufyminiproject.database.PaletDb
import org.d3if0047.canvufyminiproject.model.Pallete
import org.d3if0047.canvufyminiproject.navigation.Screen
import org.d3if0047.canvufyminiproject.ui.theme.CanvufyMiniprojectTheme
import org.d3if0047.canvufyminiproject.util.SettingsDataStore
import org.d3if0047.canvufyminiproject.util.ViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun PalleteScreen(navController: NavHostController){
    val dataStore = SettingsDataStore(LocalContext.current)
    val showList by dataStore.layoutFlow.collectAsState(true)
    Scaffold(
        topBar = {
            TopAppBar(

                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.pallete),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = stringResource(id = R.string.app_name))
                    }
                },


                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                ),
                actions = {
                    IconButton(
                        onClick = {navController.navigate(Screen.Home.route)
                        }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.text),
                            contentDescription = stringResource(R.string.tentang_aplikasi),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    IconButton(onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            dataStore.saveLayout(!showList)
                        }
                        }
                    ) {
                        Icon(
                            painter = painterResource(
                                if (showList) R.drawable.baseline_grid_view_24
                                else R.drawable.baseline_view_list_24
                            ),
                            contentDescription = stringResource(
                                if (showList) R.string.grid
                                else R.string.list
                            ),
                            tint = Color.White
                        )
                    }
                },


            )
        },

        )
    { padding ->

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            ScreenContent(showList,Modifier.padding(padding),navController)
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.FormBaru.route)
                },

                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 26.dp),
                containerColor = colorResource(id = R.color.lavender),
                contentColor = Color.White

            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(R.string.tambah_catatan),

                    )
            }
        }
    }
}

@Composable
fun GridItem(palette: Pallete, onClick: () -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, Color.Gray)
    ){
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ){
            Text(
                text = palette.nama ,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = palette.kode1,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = palette.kode2,
                maxLines = 6,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = palette.kode3,
                maxLines = 8,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = palette.kode4,
                maxLines = 10,
                overflow = TextOverflow.Ellipsis
            )

            Text(text = palette.Genre)
        }

    }
}


@Composable
fun ScreenContent(showList:Boolean, modifier: Modifier,navController: NavHostController){
    val context = LocalContext.current
    val db = PaletDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel : MainViewModel = viewModel(factory = factory)
    val data by viewModel.data.collectAsState()


    if (data.isEmpty()){
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = painterResource(id = R.drawable.nodata),
                contentDescription = "Deskripsi Gambar",
                modifier = Modifier.size(200.dp)
            )
            Text(text = stringResource(id = R.string.list_kosong))
        }
    }
    else {
        if (showList) {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 84.dp)
            ) {
                items(data) {
                    ListItem(palette = it) {
                        navController.navigate(Screen.FormUbah.withID(it.id))
                    }
                    Divider()
                }
            }
        }else{
            LazyVerticalStaggeredGrid(
                modifier = modifier.fillMaxSize(),
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp, 8.dp,8.dp,84.dp)
            ){
                items(data){
                    GridItem(palette = it) {
                        navController.navigate(Screen.FormUbah.withID(it.id ))
                    }
                }
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
            Text(
                text = palette.kode4,
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )
            Text(text = palette.Genre)
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
                containerColor = colorResource(id = R.color.pink),
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
fun PalleteScreenPreview() {
    CanvufyMiniprojectTheme {
        PalleteScreen(rememberNavController())
    }
}
