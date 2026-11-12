package com.petcusto.app.data.repository

import com.petcusto.app.data.local.LocalStore
import com.petcusto.app.data.model.Breed
import com.petcusto.app.data.model.Budget
import com.petcusto.app.data.model.User
import com.petcusto.app.data.remote.DogApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.security.MessageDigest

class PetRepository(
    private val store: LocalStore,
    private val api: DogApi
) {
    val budgets: Flow<List<Budget>> = store.budgets

    suspend fun hasSession(): Boolean = store.sessionEmail.first() != null

    suspend fun register(name: String, email: String, password: String) {
        val user = User(name.trim(), email.trim(), hash(password))
        store.saveUser(user)
        store.setSession(user.email)
    }

    suspend fun login(email: String, password: String): Boolean {
        val user = store.getUser() ?: return false
        val ok = user.email.equals(email.trim(), ignoreCase = true) &&
            user.passwordHash == hash(password)
        if (ok) store.setSession(user.email)
        return ok
    }

    suspend fun logout() = store.setSession(null)

    suspend fun fetchBreedImage(breed: Breed): String? =
        runCatching { api.randomImage(breed.apiPath).message }.getOrNull()

    suspend fun saveBudget(budget: Budget) =
        store.updateBudgets { listOf(budget) + it }

    suspend fun deleteBudget(id: Long) =
        store.updateBudgets { list -> list.filterNot { it.id == id } }

    private fun hash(text: String): String =
        MessageDigest.getInstance("SHA-256")
            .digest(text.toByteArray())
            .joinToString("") { "%02x".format(it) }
}
