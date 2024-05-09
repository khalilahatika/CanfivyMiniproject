package org.d3if0047.canvufyminiproject.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if0047.canvufyminiproject.database.PaletDb
import org.d3if0047.canvufyminiproject.ui.theme.CanvufyMiniprojectTheme
import org.d3if0047.canvufyminiproject.util.ViewModelFactory
import org.d3if0047.canvufyminiproject.R

const val KEY_ID_PALET ="idPalet"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPallete(navController: NavHostController, id: Long? = null) {
    val context = LocalContext.current
    val db = PaletDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailPalet = viewModel(factory = factory)

    var title by remember { mutableStateOf("") }
    var code1 by remember { mutableStateOf("") }
    var code2 by remember { mutableStateOf("") }
    var code3 by remember { mutableStateOf("") }
    var code4 by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false)}



    LaunchedEffect(true) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getPalet(id) ?: return@LaunchedEffect
        if (data != null) {
            title = data.nama
            code1 = data.kode1
            code2 = data.kode2
            code3 = data.kode3
            code4 = data.kode4
            genre = data.Genre

        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.Kembali),
                            tint = Color.White
                        )
                    }
                },
                title = {
                    if (id == null)
                        Text(text = stringResource(id = R.string.tambah_pallete))
                    else
                        Text(text = stringResource(id = R.string.edit))
                },
                actions = {
                    if (id != null){
                        DeleteAction { showDialog = true}
                        DisplayAlertDialog(
                            openDialog = showDialog,
                            onDismissRequest = { showDialog = false }) {
                            showDialog = false
                            viewModel.delete(id)
                            navController.popBackStack()
                        }
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                )

            )


        },
        bottomBar = {
            BottomAppBar(

            ) {
                Button(
                    onClick = {
                        if (title =="" || code1 == "" ||code2 =="" || code3 == ""|| code4 =="" || genre ==""){
                            Toast.makeText(context,R.string.invalid, Toast.LENGTH_LONG).show()
                            return@Button
                        }
                        if (id == null){
                            viewModel.insert(title,code1,code2,code3,code4,genre)
                        }else {
                            viewModel.update(id, title, code1, code2, code3, code4,genre)
                        }
                        navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.pink),
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = stringResource(id = R.string.save))
                }
            }
        }
    ) { padding ->

        FormList(
            title = title,
            code1 = code1,
            code2 = code2,
            code3 = code3,
            code4 = code4,
            genre = genre,

            onTitleChange = { title = it },
            onCode1Change = { code1 = it },
            onCode2Change = { code2 = it },
            onCode3Change = { code3 = it },
            onCode4Change = { code4 = it },
            onGenreChange = {genre = it},
            modifier = Modifier.padding(padding),navController
        )
    }
}


@Composable
fun FormList(

    title: String,
    code1: String,
    code2: String,
    code3: String,
    code4: String,
    genre: String,
    onTitleChange: (String) -> Unit,
    onCode1Change: (String) -> Unit,
    onCode2Change: (String) -> Unit,
    onCode3Change: (String) -> Unit,
    onCode4Change: (String) -> Unit,
    onGenreChange: (String) -> Unit,


    modifier: Modifier,
    navController: NavHostController,id: Long? = null
) {


    val context = LocalContext.current

    val viewModel: DetailPalet = viewModel()



    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        item {
//
            Spacer(modifier = Modifier.height(10.dp))
//


            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                label = { Text(text = stringResource(R.string.Judul)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            OutlinedTextField(
                value = code1,
                onValueChange = onCode1Change,
                label = { Text(text = stringResource(R.string.kode1)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            OutlinedTextField(
                value = code2,
                onValueChange = onCode2Change,
                label = { Text(text = stringResource(R.string.kode2)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            OutlinedTextField(
                value = code3,
                onValueChange = onCode3Change,
                label = { Text(text = stringResource(R.string.kode3)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            OutlinedTextField(
                value = code4,
                onValueChange = onCode4Change,
                label = { Text(text = stringResource(R.string.kode4)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth(),

                verticalArrangement = Arrangement.spacedBy(1.dp)
            ){
                val options = listOf(
                    "Theme: Cute",
                    "Theme: Dark",
                    "Theme: Retro",
                    "Theme: Night"


                )
                options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = genre == option,
                            onClick = { onGenreChange(option) },
                            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                        )
                        Text(
                            text = option,
                            modifier = Modifier.padding(start = 1.dp),
                        )
                    }
                }

            }


        }

    }


}

@Composable
fun DeleteAction(delete: () -> Unit) {
    var expanded by remember {
        mutableStateOf(false)
    }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.lainnya),
            tint = Color.White
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.hapus)) },
                onClick = {
                    expanded = false
                    delete()
                }
            )
        }
    }
}
@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailPalletePreview() {
    CanvufyMiniprojectTheme {
        val navController = rememberNavController()
        DetailPallete(navController)
    }
}