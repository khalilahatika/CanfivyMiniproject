package org.d3if0047.canvufyminiproject.ui.screen


import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if0047.canvufyminiproject.R
import org.d3if0047.canvufyminiproject.ui.theme.CanvufyMiniprojectTheme
import org.d3if0047.canvufyminiproject.ui.theme.alteFontFamily
import org.d3if0047.canvufyminiproject.ui.theme.heavitasFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutApp(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary

                        )
                    }
                },
                title = { Text(text = stringResource(id = R.string.tentang), fontFamily = heavitasFontFamily) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color(0xFF00CFFF)
                )
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                AboutAppContent()
            }
        },
        containerColor = Color(0xFFF0F0F0)
    )
}

@Composable
fun AboutAppContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Image(
            painter = painterResource(id = R.drawable.logos_adios), // Ganti dengan drawable resource yang benar untuk logo
            contentDescription = stringResource(id = R.string.logo),
            modifier = Modifier.size(300.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.descapp),
                fontFamily = alteFontFamily,
                color = Color.Black,
                fontSize = 16.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun AboutAppPreview() {
    CanvufyMiniprojectTheme {
        AboutApp(navController = rememberNavController())
    }
}