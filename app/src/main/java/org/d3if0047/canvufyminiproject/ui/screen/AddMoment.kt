package org.d3if0047.canvufyminiproject.ui.screen

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.exceptions.ClearCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import org.d3if0047.canvufyminiproject.R
import org.d3if0047.canvufyminiproject.model.Art
import org.d3if0047.canvufyminiproject.network.ApiStatus
import org.d3if0047.canvufyminiproject.network.ArtApi
import org.d3if0047.canvufyminiproject.network.User
import org.d3if0047.canvufyminiproject.network.UserDataStore
import org.d3if0047.canvufyminiproject.ui.theme.CanvufyMiniprojectTheme
import org.d3if0047.canvufyminiproject.ui.theme.alteFontFamily
import org.d3if0047.canvufyminiproject.ui.theme.heavitasFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMomentScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = { Text(text = stringResource(id = R.string.Add), fontFamily = heavitasFontFamily) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF00CFFF)
                )
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5))
            ) {
                ScreenContent(Modifier.padding())
            }
        },
        bottomBar = {
            AddButton(
                painter = painterResource(id = R.drawable.add),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        },
        containerColor = Color(0xFFF5F5F5)
    )
}

@Composable
fun ScreenContent(modifier: Modifier) {
    val viewModel: AddMomentViewModel = viewModel()
    val data by viewModel.data
    val status by viewModel.status.collectAsState()

    when (status) {
        ApiStatus.LOADING -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        ApiStatus.SUCCESS -> {
            LazyVerticalGrid(
                modifier = modifier.fillMaxSize().padding(4.dp),
                columns = GridCells.Fixed(2)
            ) {
                items(data) { ListItem(art = it) }
            }
        }
        ApiStatus.FAILED -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.error))
                Button(
                    onClick = { viewModel.retrieveData() },
                    modifier = Modifier.padding(top = 16.dp),
                    contentPadding = PaddingValues(horizontal=32.dp, vertical=16.dp)
                ) {
                    Text(text = stringResource(id = R.string.try_again))
                }
            }
        }
    }
}

private suspend fun signOut(context: Context, dataStore: UserDataStore) {
    try {
        val credentialManager = CredentialManager.create(context)
        credentialManager.clearCredentialState(
            ClearCredentialStateRequest()
        )
        dataStore.saveData(User())
    } catch (e: ClearCredentialException) {
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
    }
}
@Composable
fun ListItem(art: Art) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .fillMaxWidth()
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.BottomStart
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            AsyncImage(

                model = ImageRequest.Builder(LocalContext.current)
                    .data(ArtApi.getArtUrl(art.gambar))
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.gambar, art.id),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.loading_img),
                error = painterResource(id = R.drawable.broken_image),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)  // Adjust the height as per your requirement
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFFC1C1))
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = art.deskripsi,
                fontFamily = alteFontFamily,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = art.alamat,
                fontFamily = alteFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = art.harga,
                fontFamily = alteFontFamily,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Composable
fun AddButton(painter: Painter, modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painter,
            contentDescription = "Add Button",
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)  // Adjust the height as per your requirement
                .clip(RoundedCornerShape(28.dp))  // Adjust corner radius if needed
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun AddMomentScreenPreview() {
    CanvufyMiniprojectTheme {
        AddMomentScreen(navController = rememberNavController())
    }
}