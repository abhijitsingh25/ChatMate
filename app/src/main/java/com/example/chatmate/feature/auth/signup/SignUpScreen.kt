package com.example.chatmate.feature.auth.signup

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chatmate.R

@Composable
fun SignUpScreen(navController: NavController) {
    val viewModel: SignUpViewModel = hiltViewModel()
    val uiState = viewModel.state.collectAsState()


    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var name by remember {
        mutableStateOf("")
    }
    var confirm by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current
    LaunchedEffect(key1 = uiState.value) {

        when(uiState.value){
            is SignUpState.Success -> {
                navController.navigate("home")
            }
            is SignUpState.Error ->{
                Toast.makeText(context,"Sign In failed", Toast.LENGTH_SHORT).show()
            }
            else ->{}

        }
    }


    Scaffold(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Image(
                painter = painterResource(id = R.drawable.logo), contentDescription = null,
                modifier = Modifier.size(200.dp)
            )

            Text(text = "ChatMate", fontSize = 30.sp)
            Spacer(modifier = Modifier.size(16.dp))
            OutlinedTextField(
                value = name, onValueChange = { name = it },
                label = { Text(text = "Name") }
            )
            Spacer(modifier = Modifier.size(16.dp))
            OutlinedTextField(
                value = email, onValueChange = { email = it },
                label = { Text(text = "Email") }
            )
            Spacer(modifier = Modifier.size(16.dp))
            OutlinedTextField(
                value = password, onValueChange = { password = it },
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.size(16.dp))
            OutlinedTextField(
                value = confirm, onValueChange = { confirm = it },
                label = { Text(text = "Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                isError = password.isNotEmpty() && confirm.isNotEmpty() && password != confirm
            )
//            Spacer(modifier = Modifier.size(.dp))

            if(uiState.value == SignUpState.Loading){
                CircularProgressIndicator()
            }
            else{
                Button(
                    onClick = {
                        viewModel.signUp(name,email,password)
                    }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    enabled = name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirm.isNotEmpty() && password == confirm
                ) {
                    Text(text = "Sign Up")
                }
                TextButton(onClick = {navController.popBackStack()}) {
                    Text(text = "Already have an account? Sign In ->")
                }
            }


        }
    }
}