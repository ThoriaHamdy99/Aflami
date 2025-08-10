package com.amsterdam.localdatasource.daos

import androidx.room.Dao
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.amsterdam.localdatasource.roomDataBase.AflamiDatabase
import com.amsterdam.localdatasource.roomDataBase.daos.AccountDetailsDao
import com.amsterdam.repository.dto.local.profile.AccountDetailsLocalDto
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@Dao
class AccountDetailsDaoTest {
    private lateinit var accountDetailsDao: AccountDetailsDao
    private val appContext by lazy { InstrumentationRegistry.getInstrumentation().targetContext }
    private val aflamiDatabase by lazy {
        Room.inMemoryDatabaseBuilder(appContext, AflamiDatabase::class.java).build()
    }

    @BeforeEach
    fun setup() {
        accountDetailsDao = aflamiDatabase.accountDetailsDao()
    }

    @AfterEach
    fun tearDown() {
        aflamiDatabase.close()
    }

    @Test
    fun upsertAccountDetails_shouldInsertAccountDetails_whenAccountDetailsNotExist() = runTest {
        accountDetailsDao.upsertAccountDetails(accountDetails)
        val result = accountDetailsDao.getAccountDetails()

        assertThat(result).isEqualTo(accountDetails)
    }

    @Test
    fun upsertAccountDetails_shouldUpdateAccountDetails_whenAccountDetailsExist() = runTest {
        accountDetailsDao.upsertAccountDetails(accountDetails)

        accountDetailsDao.upsertAccountDetails(updatedAccountDetails)
        val result = accountDetailsDao.getAccountDetails()

        assertThat(result).isEqualTo(updatedAccountDetails)
    }

    @Test
    fun getAccountDetails_shouldReturnAccountDetails_whenAccountDetailsExist() = runTest {
        accountDetailsDao.upsertAccountDetails(accountDetails)

        val result = accountDetailsDao.getAccountDetails()

        assertThat(result).isEqualTo(accountDetails)
    }

    @Test
    fun getAccountDetails_shouldReturnNull_whenAccountDetailsNotExist() = runTest {
        val result = accountDetailsDao.getAccountDetails()

        assertThat(result).isEqualTo(null)
    }

    @Test
    fun deleteAccountDetails_shouldDeleteAccountDetails() = runTest {
        accountDetailsDao.upsertAccountDetails(accountDetails)

        accountDetailsDao.deleteAccountDetails()
        val result = accountDetailsDao.getAccountDetails()

        assertThat(result).isEqualTo(null)
    }

}

private val accountDetails = AccountDetailsLocalDto(accountId = 1)
private val updatedAccountDetails = accountDetails.copy(username = "new name")