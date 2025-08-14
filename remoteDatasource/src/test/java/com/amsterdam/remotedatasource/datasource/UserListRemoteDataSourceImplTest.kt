package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.UserListApiService
import com.amsterdam.repository.dto.remote.AddItemToListRemoteResponse
import com.amsterdam.repository.dto.remote.CreateUserListRemoteResponse
import com.amsterdam.repository.dto.remote.UserListDetailsRemoteResponse
import com.amsterdam.repository.dto.remote.UserListRemoteResponse
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserListRemoteDataSourceImplTest {

    private lateinit var userListRemoteDataSourceImpl: UserListRemoteDataSourceImpl
    private lateinit var userListApiService: UserListApiService
    private lateinit var jsonMock: Json


    @BeforeEach
    fun setup() {
        userListApiService = mockk()
        jsonMock = mockk(relaxed = true)
        userListRemoteDataSourceImpl = UserListRemoteDataSourceImpl(
            userListApiService = userListApiService,
            json = jsonMock
        )
    }

    @Test
    fun `createNewList should return a new user list response on success`() = runTest {
        coEvery {
            userListApiService.createNewList(
                listName = listName,
                language = language
            )
        } returns createListSuccessResponse
        val result = userListRemoteDataSourceImpl.createNewList(listName, language)
        assertThat(result).isEqualTo(createListSuccessResponse)
        coVerify(exactly = 1) {
            userListApiService.createNewList(
                listName = listName,
                language = language
            )
        }
    }

    @Test
    fun `createNewList should throw NetworkException when api call fails`() = runTest {
        coEvery {
            userListApiService.createNewList(
                listName = listName,
                language = language
            )
        } throws NetworkException()
        assertThrows<NetworkException> {
            userListRemoteDataSourceImpl.createNewList(
                listName,
                language
            )
        }
        coVerify(exactly = 1) { userListApiService.createNewList(any(), any(), any()) }
    }

    @Test
    fun `getUserLists should return remote user list response on success`() = runTest {
        coEvery {
            userListApiService.getUserLists(
                accountId,
                page
            )
        } returns userListsSuccessResponse
        val result = userListRemoteDataSourceImpl.getUserLists(accountId, page)
        assertThat(result).isEqualTo(userListsSuccessResponse)
        coVerify(exactly = 1) { userListApiService.getUserLists(accountId, page) }
    }

    @Test
    fun `addMovieToList should return a response on success`() = runTest {
        coEvery {
            userListApiService.addMediaItemToList(
                listId,
                movieId
            )
        } returns addItemSuccessResponse
        val result = userListRemoteDataSourceImpl.addMovieToList(listId, movieId)
        assertThat(result).isEqualTo(addItemSuccessResponse)
        coVerify(exactly = 1) { userListApiService.addMediaItemToList(listId, movieId) }
    }

    @Test
    fun `addMovieToList should throw NetworkException when api call fails`() = runTest {
        coEvery { userListApiService.addMediaItemToList(listId, movieId) } throws NetworkException()
        assertThrows<NetworkException> {
            userListRemoteDataSourceImpl.addMovieToList(
                listId,
                movieId
            )
        }
        coVerify(exactly = 1) { userListApiService.addMediaItemToList(any(), any()) }
    }

    @Test
    fun `getMoviesFromList should return a list of movies and call the api service`() = runTest {
        coEvery {
            userListApiService.getMoviesAndTvShowsFromList(
                listId,
                page
            )
        } returns remoteListResponse
        val result = userListRemoteDataSourceImpl.getMoviesAndTvShowsFromList(listId, page)
        assertThat(result).isEqualTo(remoteListResponse)
        coVerify(exactly = 1) { userListApiService.getMoviesAndTvShowsFromList(listId, page) }
    }

    @Test
    fun `getMoviesFromList should throw NetworkException when api call fails`() = runTest {
        coEvery {
            userListApiService.getMoviesAndTvShowsFromList(
                listId,
                page
            )
        } throws NetworkException()
        assertThrows<NetworkException> {
            userListRemoteDataSourceImpl.getMoviesAndTvShowsFromList(
                listId,
                page
            )
        }
        coVerify(exactly = 1) { userListApiService.getMoviesAndTvShowsFromList(any(), any()) }
    }

    @Test
    fun `deleteList should call api service deleteList`() = runTest {
        coEvery { userListApiService.deleteList(listId) } returns Unit
        userListRemoteDataSourceImpl.deleteList(listId)
        coVerify(exactly = 1) { userListApiService.deleteList(listId) }
    }

    @Test
    fun `deleteList should throw NetworkException when api call fails`() = runTest {
        coEvery { userListApiService.deleteList(listId) } throws NetworkException()
        assertThrows<NetworkException> { userListRemoteDataSourceImpl.deleteList(listId) }
        coVerify(exactly = 1) { userListApiService.deleteList(any()) }
    }

    @Test
    fun `removeMovieFromList should call api service removeMovieFromList`() = runTest {
        coEvery { userListApiService.removeMovieFromList(listId, movieId) } returns Unit
        userListRemoteDataSourceImpl.deleteMovieFromList(listId, movieId)
        coVerify(exactly = 1) { userListApiService.removeMovieFromList(listId, movieId) }
    }

    @Test
    fun `removeMovieFromList should throw NetworkException when api call fails`() = runTest {
        coEvery {
            userListApiService.removeMovieFromList(
                listId,
                movieId
            )
        } throws NetworkException()
        assertThrows<NetworkException> {
            userListRemoteDataSourceImpl.deleteMovieFromList(
                listId,
                movieId
            )
        }
        coVerify(exactly = 1) { userListApiService.removeMovieFromList(any(), any()) }
    }

    private val listName = "My list"
    private val language = "en"
    private val listId = 1L
    private val movieId = 1L
    private val accountId = 1
    private val page = 1

    private val remoteListResponse = UserListDetailsRemoteResponse(
        id = listId,
        name = "Test List",
        description = "Test description",
        itemCount = 1,
        favoriteCount = 0,
        createdBy = "testUser",
        items = emptyList(),
        posterPath = null
    )

    private val createListSuccessResponse = CreateUserListRemoteResponse(
        listId = 1,
        statusCode = 1,
        statusMessage = "Success",
        success = true,
    )

    private val userListsSuccessResponse = UserListRemoteResponse(
        results = emptyList(),
        page = 1,
        totalPages = 1,
        totalResults = 1,
    )

    private val addItemSuccessResponse = AddItemToListRemoteResponse(
        statusCode = 1,
        statusMessage = "success",
        success = true
    )
}