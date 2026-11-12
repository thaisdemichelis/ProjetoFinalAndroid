package com.petcusto.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.petcusto.app.ui.components.AppTextField
import com.petcusto.app.ui.components.PrimaryButton
import com.petcusto.app.viewmodel.AppViewModel

@Composable
fun RegisterScreen(
    vm: AppViewModel,
    onRegistered: () -> Unit,
    onGoLogin: () -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirm by rememberSaveable { mutableStateOf("") }
    val errors by vm.registerErrors.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { vm.clearAuthErrors() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(Modifier.height(24.dp))
        Text("Criar conta", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        AppTextField(name, { name = it }, "Nome", error = errors.name)
        AppTextField(email, { email = it }, "E-mail", error = errors.email, keyboardType = KeyboardType.Email)
        AppTextField(password, { password = it }, "Senha", isPassword = true, error = errors.password)
        AppTextField(confirm, { confirm = it }, "Confirmar senha", isPassword = true, error = errors.confirm)

        Spacer(Modifier.height(16.dp))
        PrimaryButton("Cadastrar", onClick = { vm.register(name, email, password, confirm, onRegistered) })

        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text("Já tem conta?")
            Text(
                "Entrar",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(onClick = onGoLogin)
            )
        }
    }
}
