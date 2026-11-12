package com.petcusto.app.data.local

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.petcusto.app.data.model.Budget
import com.petcusto.app.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore by preferencesDataStore(name = "petcusto")

/** Persistência local simples (DataStore + JSON): conta, sessão e orçamentos salvos. */
class LocalStore(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true }

    private val keyUser = stringPreferencesKey("user")
    private val keySession = stringPreferencesKey("session_email")
    private val keyBudgets = stringPreferencesKey("budgets")

    val sessionEmail: Flow<String?> = context.dataStore.data.map { it[keySession] }

    val budgets: Flow<List<Budget>> = context.dataStore.data.map { decodeBudgets(it) }

    suspend fun getUser(): User? =
        context.dataStore.data.first()[keyUser]?.let {
            runCatching { json.decodeFromString<User>(it) }.getOrNull()
        }

    suspend fun saveUser(user: User) {
        context.dataStore.edit { it[keyUser] = json.encodeToString(user) }
    }

    suspend fun setSession(email: String?) {
        context.dataStore.edit {
            if (email == null) it.remove(keySession) else it[keySession] = email
        }
    }

    suspend fun updateBudgets(transform: (List<Budget>) -> List<Budget>) {
        context.dataStore.edit { prefs ->
            prefs[keyBudgets] = json.encodeToString(transform(decodeBudgets(prefs)))
        }
    }

    private fun decodeBudgets(prefs: Preferences): List<Budget> =
        prefs[keyBudgets]?.let {
            runCatching { json.decodeFromString<List<Budget>>(it) }.getOrNull()
        } ?: emptyList()
}
