package com.petcusto.app.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.petcusto.app.data.local.LocalStore
import com.petcusto.app.data.model.Breed
import com.petcusto.app.data.model.BreedCatalog
import com.petcusto.app.data.model.Budget
import com.petcusto.app.data.model.Treatment
import com.petcusto.app.data.remote.ApiClient
import com.petcusto.app.data.repository.PetRepository
import com.petcusto.app.domain.PriceCalculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterErrors(
    val name: String? = null,
    val email: String? = null,
    val password: String? = null,
    val confirm: String? = null
) {
    val hasAny: Boolean get() = listOf(name, email, password, confirm).any { it != null }
}

data class CalcState(
    val breed: Breed = BreedCatalog.all.first(),
    val treatment: Treatment = Treatment.BANHO,
    val weightKg: Int = 20
) {
    val total: Double get() = PriceCalculator.total(treatment, weightKg)
}

/** ViewModel único do app: sessão, raças, calculadora e orçamentos salvos. */
class AppViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = PetRepository(LocalStore(app), ApiClient.dogApi)

    // ---------- Sessão (Splash condicional) ----------
    private val _isLoggedIn = MutableStateFlow<Boolean?>(null) // null = ainda carregando
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn.asStateFlow()

    // ---------- Autenticação ----------
    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    private val _registerErrors = MutableStateFlow(RegisterErrors())
    val registerErrors: StateFlow<RegisterErrors> = _registerErrors.asStateFlow()

    // ---------- Raças ----------
    val breeds: List<Breed> = BreedCatalog.all

    private val _breedImages = MutableStateFlow<Map<String, String>>(emptyMap())
    val breedImages: StateFlow<Map<String, String>> = _breedImages.asStateFlow()

    // ---------- Calculadora ----------
    private val _calc = MutableStateFlow(CalcState())
    val calc: StateFlow<CalcState> = _calc.asStateFlow()

    // ---------- Orçamentos salvos ----------
    val budgets: StateFlow<List<Budget>> = repo.budgets
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        viewModelScope.launch { _isLoggedIn.value = repo.hasSession() }
        loadBreedImages()
    }

    // ===== Auth =====
    fun clearAuthErrors() {
        _loginError.value = null
        _registerErrors.value = RegisterErrors()
    }

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            if (repo.login(email, password)) {
                _loginError.value = null
                _isLoggedIn.value = true
                onSuccess()
            } else {
                _loginError.value = "E-mail ou senha inválidos"
            }
        }
    }

    fun register(
        name: String,
        email: String,
        password: String,
        confirm: String,
        onSuccess: () -> Unit
    ) {
        val errors = RegisterErrors(
            name = if (name.isBlank()) "Informe seu nome" else null,
            email = if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()) "E-mail inválido" else null,
            password = if (password.length < 6) "Senha com mínimo de 6 caracteres" else null,
            confirm = if (confirm != password) "As senhas não conferem" else null
        )
        _registerErrors.value = errors
        if (errors.hasAny) return

        viewModelScope.launch {
            repo.register(name, email, password)
            _isLoggedIn.value = true
            onSuccess()
        }
    }

    fun logout(onDone: () -> Unit) {
        viewModelScope.launch {
            repo.logout()
            _isLoggedIn.value = false
            onDone()
        }
    }

    // ===== Raças =====
    fun breedById(id: String): Breed? = BreedCatalog.byId(id)

    private fun loadBreedImages() {
        breeds.forEach { breed ->
            viewModelScope.launch {
                repo.fetchBreedImage(breed)?.let { url ->
                    _breedImages.update { it + (breed.id to url) }
                }
            }
        }
    }

    // ===== Calculadora =====
    fun selectBreed(breed: Breed) = _calc.update { it.copy(breed = breed) }
    fun setTreatment(treatment: Treatment) = _calc.update { it.copy(treatment = treatment) }
    fun setWeight(weightKg: Int) = _calc.update { it.copy(weightKg = weightKg) }

    fun saveBudget(onSaved: () -> Unit) {
        val c = _calc.value
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            repo.saveBudget(
                Budget(
                    id = now,
                    breedName = c.breed.name,
                    treatment = c.treatment.name,
                    weightKg = c.weightKg,
                    total = c.total,
                    createdAt = now
                )
            )
            onSaved()
        }
    }

    fun deleteBudget(id: Long) {
        viewModelScope.launch { repo.deleteBudget(id) }
    }
}
