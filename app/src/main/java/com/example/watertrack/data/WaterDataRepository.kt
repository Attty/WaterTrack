package com.example.watertrack.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "water_prefs_datastore") // Трохи інша назва для DataStore файлу

class WaterDataRepository(private val context: Context) {

    private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE // Формат: yyyy-MM-dd

    private fun getPreferenceKey(date: LocalDate): Preferences.Key<Float> {
        return floatPreferencesKey(date.format(dateFormatter))
    }

    suspend fun saveWaterIntake(date: LocalDate, amount: Float) {
        context.dataStore.edit { preferences ->
            preferences[getPreferenceKey(date)] = amount
        }
    }

    fun getWaterIntake(date: LocalDate): Flow<Float> {
        return context.dataStore.data.map { preferences ->
            preferences[getPreferenceKey(date)] ?: 0f
        }
    }

    fun getAllWaterIntakeHistory(): Flow<Map<String, Float>> {
        return context.dataStore.data.map { preferences ->
            preferences.asMap()
                .filterKeys { key ->
                    try {
                        LocalDate.parse(key.name, dateFormatter)
                        true
                    } catch (e: Exception) {
                        false
                    }
                }
                .mapNotNull { (key, value) ->
                    if (value is Float) {
                        key.name to value
                    } else {
                        null
                    }
                }
                .toMap()
                .toSortedMap(compareByDescending { it })
        }
    }
}