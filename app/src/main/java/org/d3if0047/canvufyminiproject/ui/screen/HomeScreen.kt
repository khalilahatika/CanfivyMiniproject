package org.d3if0047.canvufyminiproject.ui.screen

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if0047.canvufyminiproject.BuildConfig
import org.d3if0047.canvufyminiproject.R
import org.d3if0047.canvufyminiproject.model.Art
import org.d3if0047.canvufyminiproject.navigation.Screen
import org.d3if0047.canvufyminiproject.network.ApiStatus
import org.d3if0047.canvufyminiproject.network.ArtApi
import org.d3if0047.canvufyminiproject.network.User
import org.d3if0047.canvufyminiproject.network.UserDataStore
import org.d3if0047.canvufyminiproject.ui.theme.CanvufyMiniprojectTheme
import org.d3if0047.canvufyminiproject.ui.theme.alteFontFamily
import org.d3if0047.canvufyminiproject.ui.theme.heavitasFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current
    val dataStore = UserDataStore(context)
    val user by dataStore.userFlow.collectAsState(User())

    val viewModel: AddMomentViewModel  = viewModel()
    val errorMessage by viewModel.errorMessage

    var showDialog by remember { mutableStateOf(false) }
    var showArtDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var currentArtId by remember { mutableStateOf("") }

    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    val launcher = rememberLauncherForActivityResult(CropImageContract()) {
        bitmap = getCroppedImage(context.contentResolver, it)
        if (bitmap != null) showArtDialog = true
    }

    var showList by remember { mutableStateOf(true) }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name), fontFamily = heavitasFontFamily) },
                actions = {
                    IconButton(onClick = {
                        showList = !showList
                    }) {
                        Icon(
                            painter = painterResource(
                                if (showList) R.drawable.grid
                                else R.drawable.list
                            ),
                            contentDescription = stringResource(
                                if (showList) R.string.grid
                                else R.string.list
                            ),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    IconButton(onClick = {
                        if (user.email.isEmpty()){
                            CoroutineScope(Dispatchers.IO).launch { signIn(context,dataStore) }
                        }
                        else{
                            showDialog = true
                        }
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.profil), // Replace with the correct drawable resource
                            contentDescription = stringResource(id = R.string.profil),
                            modifier = Modifier
                                .padding(end = 7.dp)
                                .size(50.dp)

                        )

                    }

                    Image(
                        painter = painterResource(id = R.drawable.info), // Replace with the correct drawable resource
                        contentDescription = stringResource(id = R.string.info),
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(32.dp)
                            .clickable { navController.navigate(Screen.About.route) }
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF00CFFF)
                )
            )
        },
        floatingActionButton = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (user.email.isNotEmpty()) {
                    FloatingActionButton(onClick = {
                        val options = CropImageContractOptions(
                            null, CropImageOptions(
                                imageSourceIncludeGallery = false,
                                imageSourceIncludeCamera = true,
                                fixAspectRatio = true
                            )
                        )
                        launcher.launch(options)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(id = R.string.tambah_art)
                        )
                    }
                }
            }
        }
    ) { padding ->
        ScreenContent(
            showList = showList,
            viewModel = viewModel,
            userId = user.email,
            modifier = Modifier.padding(padding),
            onDeleteRequest = { id ->
                showDeleteDialog = true
                currentArtId = id
                Log.d("HomeScreen", "Current Art ID: $currentArtId")
            },
            isUserLoggedIn = user.email.isNotEmpty()
        )

        if (showDialog) {
            ProfilDialog(
                user = user,
                onDismissRequest = { showDialog = false }) {
                CoroutineScope(Dispatchers.IO).launch { signOut(context, dataStore) }
                showDialog = false
            }
        }
        if (showArtDialog) {
            ArtDialog(
                bitmap = bitmap,
                onDismissRequest = { showArtDialog = false }) { nama, indikasi, frekuensi ->
                viewModel.saveData(user.email, nama, indikasi, frekuensi, bitmap!!)
                showArtDialog = false
            }
        }

        if (errorMessage != null) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            viewModel.clearMessage()
        }

        if (showDeleteDialog) {
            DisplayAlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                onConfirm = {
                    Log.d("HomeScreen", "Deleting Obat ID: $currentArtId")
                    viewModel.deleteData(user.email, currentArtId)
                    showDeleteDialog = false
                }
            )
        }
    }
}




@Composable
fun ScreenContent(
    showList: Boolean,
    viewModel: AddMomentViewModel ,
    userId: String,
    modifier: Modifier,
    onDeleteRequest: (String) -> Unit,
    isUserLoggedIn: Boolean

) {
    val context = LocalContext.current
    val data by viewModel.data
    val status by viewModel.status.collectAsState()

    LaunchedEffect(userId) {
        viewModel.retrieveData(userId)
    }
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
            if (showList) {
                LazyVerticalGrid(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(data.filter { it.auth.isEmpty() || it.auth == userId }) { art ->
                        ListItem(
                            art = art,
                            onDeleteRequest = onDeleteRequest,
                            isUserLoggedIn = isUserLoggedIn,
                            currentUserId = userId,
                            context = context
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(data.filter { it.auth.isEmpty() || it.auth == userId }) { art ->
                        ListItem(
                            art = art,
                            onDeleteRequest = onDeleteRequest,
                            isUserLoggedIn = isUserLoggedIn,
                            currentUserId = userId,
                            context = context
                        )
                    }
                }
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
                    onClick = { viewModel.retrieveData(userId) },
                    modifier = Modifier.padding(top = 16.dp),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    Text(text = stringResource(id = R.string.try_again))
                }
            }
        }
    }
}

@Composable
fun ListItem(
    art : Art,
    onDeleteRequest: (String) -> Unit,
    isUserLoggedIn: Boolean,
    currentUserId: String,
    context: Context
) {
    if (art.auth.isEmpty() || art.auth == currentUserId) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .border(1.dp, Color.Gray),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .border(1.dp, Color.Gray)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(ArtApi.getArtUrl(art.gambar))
                        .crossfade(true)
                        .build(),
                    contentDescription = stringResource(R.string.gambar, art.deskripsi),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.loading_img),
                    error = painterResource(id = R.drawable.broken_image),
                    modifier = Modifier.fillMaxSize()
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .background(Color(red = 0f, green = 0f, blue = 0f, alpha = 0.5f))
                    .padding(4.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = art.deskripsi,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Row {
                        Text(
                            text = stringResource(R.string.alamat),
                            fontStyle = FontStyle.Italic,
                            fontSize = 10.sp,
                            color = Color.Black
                        )
                        Text(
                            text = " ${art.alamat}",
                            fontStyle = FontStyle.Italic,
                            fontSize = 10.sp,
                            color = Color.Black
                        )
                    }
                    Row {
                        Text(
                            text = stringResource(R.string.harga),
                            fontStyle = FontStyle.Italic,
                            fontSize = 10.sp,
                            color = Color.Black
                        )
                        Text(
                            text = " ${art.harga}",
                            fontStyle = FontStyle.Italic,
                            fontSize = 10.sp,
                            color = Color.Black
                        )

                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    if (isUserLoggedIn && art.auth == currentUserId) {
                        IconButton(
                            onClick = {
                                if (art.id.isNotEmpty()) {
                                    onDeleteRequest(art.id)
                                } else {
                                    Log.d("ListItem", "Invalid Art ID")
                                }
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = stringResource(R.string.hapus),
                                tint = Color.White
                            )
                        }
                    }
                    IconButton(
                        onClick = {
                            shareArt(context, art.deskripsi, art.alamat, art.harga)
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.share),
                            contentDescription = stringResource(R.string.bagikan),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

private fun shareArt(context:Context, deskripsi:String, alamat:String, harga:String) {
    val shareText = context.getString(
        R.string.template_bagikan

    )


    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, shareText)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    context.startActivity(shareIntent)
}



private suspend fun signIn(context: Context, dataStore: UserDataStore) {
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(BuildConfig.API_KEY)
        .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    try {
        val credentialManager = CredentialManager.create(context)
        val result = credentialManager.getCredential(context, request)
        handleSignIn(result,dataStore)
    } catch (e: GetCredentialException) {
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
    }
}

private suspend fun handleSignIn(
    result: GetCredentialResponse,
    dataStore: UserDataStore
) {
    val credential = result.credential
    if (credential is CustomCredential &&
        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        try {
            val googleId = GoogleIdTokenCredential.createFrom(credential.data)
            val name = googleId.displayName ?: ""
            val email = googleId.id
            val photoUrl = googleId.profilePictureUri.toString()
            dataStore.saveData((User(name,email,photoUrl)))
        } catch (e: GoogleIdTokenParsingException) {
            Log.e("SIGN-IN", "Error: ${e.message}")
        }
    }
    else {
        Log.e("SIGN-IN", "Error: unrecognized custom credential type.")
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

private fun getCroppedImage(
    resolver: ContentResolver,
    result: CropImageView.CropResult
): Bitmap? {
    if (!result.isSuccessful) {
        Log.e("IMAGE", "Error: ${result.error}")
        return null
    }

    val uri = result.uriContent ?: return null

    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
        MediaStore.Images.Media.getBitmap(resolver, uri)
    } else {
        val source = ImageDecoder.createSource(resolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScreenPreview() {
    CanvufyMiniprojectTheme {
        val navController = rememberNavController()
        HomeScreen(navController)
    }
}