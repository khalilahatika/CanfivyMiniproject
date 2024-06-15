package org.d3if0047.canvufyminiproject.ui.screen

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if0047.canvufyminiproject.BuildConfig
import org.d3if0047.canvufyminiproject.R
import org.d3if0047.canvufyminiproject.navigation.Screen
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

    var showDialog by remember {
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name), fontFamily = heavitasFontFamily) },
                actions = {
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
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                ScreenContent(navController = navController)
                if (showDialog){
                    ProfilDialog(
                        user = user ,
                        onDismissRequest = { showDialog = false}) {
                        CoroutineScope(Dispatchers.IO).launch { signOut(context, dataStore) }
                        showDialog = false
                    }
                }
            }
        },
        containerColor = Color(0xFFF0F0F0)
    )
}

@Composable
fun ScreenContent(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp) // Add padding to avoid being too close to the edge of the screen
                .clip(RoundedCornerShape(16.dp)) // Add corner radius
                .background(Color.White)
                .padding(16.dp) // Add padding inside the white box
                .clickable { navController.navigate(Screen.AddMoment.route) }, // Navigate to AddMoment on click
            contentAlignment = Alignment.Center // Center the content inside the white box
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = stringResource(id = R.string.camera),
                    modifier = Modifier.size(100.dp)
                )
                Text(
                    text = stringResource(id = R.string.moment),
                    fontFamily = alteFontFamily,
                    color = Color.Black,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
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
@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ScreenPreview() {
    CanvufyMiniprojectTheme {
        val navController = rememberNavController()
        HomeScreen(navController)
    }
}