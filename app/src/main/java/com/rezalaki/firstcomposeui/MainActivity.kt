package com.rezalaki.firstcomposeui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Login
import androidx.compose.material.icons.rounded.SendToMobile
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rezalaki.firstcomposeui.ui.theme.FirstComposeUITheme
import com.rezalaki.firstcomposeui.ui.theme.colorBg
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirstComposeUITheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun MainScreen() {
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        content = {
            ModalBottomSheetLayout(
                sheetShape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp),
                sheetState = bottomSheetState,
                sheetContent = {
                    BottomSheetBody(
                        coroutineScope = coroutineScope,
                        modalBottomSheetState = bottomSheetState
                    )
                }
            ) {
                ScreenBody(
                    bottomSheetState = bottomSheetState,
                    coroutineScope = coroutineScope,
                    scaffoldState = scaffoldState
                )
            }
        }
    )


}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenBody(
    bottomSheetState: ModalBottomSheetState,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    val mobileInput = remember {
        mutableStateOf("")
    }
    val passwordInput = remember {
        mutableStateOf("")
    }
    val passwordVisible = rememberSaveable {
        mutableStateOf(false)
    }

    // close bottom sheet when OnBackPressed
    BackHandler(enabled = bottomSheetState.isVisible) {
        coroutineScope.launch { bottomSheetState.hide() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorBg),

        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Card(
            modifier = Modifier
                .height(340.dp)
                .width(320.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = 1.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "IranDocs", modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center
                )
                OutlinedTextField(
                    modifier = Modifier.padding(bottom = 8.dp),
                    value = mobileInput.value,
                    onValueChange = {
                        if (it.length <= 11) mobileInput.value = it
                    },
                    label = { Text(text = "Mobile") },
                    placeholder = {
                        Text(text = "09*********")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                )
                OutlinedTextField(
                    modifier = Modifier.padding(bottom = 48.dp),
                    value = passwordInput.value,
                    onValueChange = {
                        passwordInput.value = it
                    },
                    label = { Text(text = "Password") },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible.value)
                            Icons.Filled.Visibility
                        else
                            Icons.Filled.VisibilityOff

                        val description = if (passwordVisible.value) "Hide" else "Show"

                        IconButton(onClick = {
                            passwordVisible.value = !passwordVisible.value
                        }) {
                            Icon(imageVector = image, description)
                        }
                    }
                )
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    onClick = {
                        if (Utils.isValidPhone(mobileInput.value).not()) {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("Mobile is not Valid")
                            }
                            return@Button
                        }
                        if (passwordInput.value.length < 5) {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("Password is incorrect")
                            }
                            return@Button
                        }
                        coroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("Welcome :)")
                        }
                    }
                ) {
                    Text(text = "Enter")
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(imageVector = Icons.Rounded.Login, contentDescription = "")
                }
            }

        }
        Text(
            modifier = Modifier
                .width(300.dp)
                .padding(vertical = 10.dp)
                .height(36.dp)
                .wrapContentHeight(align = Alignment.CenterVertically)
                .clickable {
                    coroutineScope.launch {
                        if (bottomSheetState.isVisible.not()) {
                            bottomSheetState.show()
                        }
                    }
                },
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            text = "forgot your password... tap on me",
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetBody(
    coroutineScope: CoroutineScope,
    modalBottomSheetState: ModalBottomSheetState
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 32.dp)
    ) {
        val forgetMobileInput = remember {
            mutableStateOf("")
        }
        Text(
            text = "please enter your phone number below, in order to receive SMS code",
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth(),
            value = forgetMobileInput.value,
            onValueChange = {
                if (it.length <= 11) forgetMobileInput.value = it
            },
            label = { Text(text = "Mobile") },
            placeholder = {
                Text(text = "09*********")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .clip(RoundedCornerShape(12.dp)),
            onClick = {
                if (Utils.isValidPhone(forgetMobileInput.value).not()) {
                    Toast.makeText(context, "Mobile is not Valid", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                Toast.makeText(context, "code will be sent soon :)", Toast.LENGTH_LONG).show()
                coroutineScope.launch {
                    modalBottomSheetState.hide()
                }
            }
        ) {
            Text(text = "Send Code")
            Spacer(modifier = Modifier.width(8.dp))
            Icon(imageVector = Icons.Rounded.SendToMobile, contentDescription = "")
        }
    }
}