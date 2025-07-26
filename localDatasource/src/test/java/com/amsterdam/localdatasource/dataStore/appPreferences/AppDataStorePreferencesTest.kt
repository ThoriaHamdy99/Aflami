package com.amsterdam.localdatasource.dataStore.appPreferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class AppDataStorePreferencesTest {
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var file: File
    private lateinit var appDataStorePreferences: AppDataStorePreferences
    @BeforeEach
    fun setup() {
        file = File.createTempFile("test", ".preferences_pb").apply {
            deleteOnExit()
        }
        dataStore = PreferenceDataStoreFactory.create { file }
        appDataStorePreferences = AppDataStorePreferences(dataStore)
    }

    @AfterEach
    fun cleanup() {
        file.delete()
    }

    @Test
    fun `getSessionType should return empty string when no value is set`() = runTest {
        //When
        val result = appDataStorePreferences.getSessionType()
        //Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `setSessionType should save provided value`() = runTest {
        //Given
        val userType = "guest"
        //When
        appDataStorePreferences.setSessionType(userType)
        //Then
        val result = appDataStorePreferences.getSessionType()
        assertThat(result).isEqualTo(userType)
    }
}