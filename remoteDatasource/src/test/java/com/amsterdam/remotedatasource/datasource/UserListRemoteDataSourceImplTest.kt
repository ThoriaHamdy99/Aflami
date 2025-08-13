package com.amsterdam.remotedatasource.datasource

import com.amsterdam.domain.exceptions.NetworkException
import com.amsterdam.remotedatasource.api.UserListApiService
import com.amsterdam.repository.dto.remote.AddItemToListResponse
import com.amsterdam.repository.dto.remote.CreateUserListResponse
import com.amsterdam.repository.dto.remote.RemoteUserListResponse
import com.amsterdam.repository.dto.remote.UserListDetailsResponse
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

    private val remoteListResponse = UserListDetailsResponse(
        id = 1L,
        name = "Test List",
        description = "Test description",
        itemCount = 1,
        favoriteCount = 0,
        createdBy = "testUser",
        items = emptyList(),
        posterPath = null
    )

    @BeforeEach
    fun setup() {
        userListApiService = mockk()
        val jsonMock: Json = mockk(relaxed = true)
        userListRemoteDataSourceImpl = UserListRemoteDataSourceImpl(
            userListApiService = userListApiService,
            json = jsonMock
        )
    }

    @Test
    fun `createNewList should return a new user list response on success`() = runTest {
        val listName = "My list"
        val language = "en"
        val expectedResponse = CreateUserListResponse(
            listId = 1,
            statusCode = 1,
            statusMessage = "Success",
            success = true,
        )

        coEvery { userListApiService.createNewList(listName = listName, language = language,) } returns expectedResponse

        val result = userListRemoteDataSourceImpl.createNewList(listName, language)

        assertThat(result).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { userListApiService.createNewList(listName = listName, language = language,) }
    }

    @Test
    fun `createNewList should throw NetworkException when api call fails`() = runTest {
        val listName = "My list"
        val description = "A list of movies"
        val language = "en"

        coEvery {
            userListApiService.createNewList(
                listName = listName,
                description = description,
                language = language,
            )
        } throws NetworkException()

        assertThrows<NetworkException> {
            userListRemoteDataSourceImpl.createNewList(listName, language)
        }
    }

    @Test
    fun `getUserLists should return remote user list response on success`() = runTest {
        val accountId = 1
        val page = 1
        val expectedResponse = RemoteUserListResponse(
            results = emptyList(),
            page = 1,
            totalPages = 1,
            totalResults = 1,
        )

        coEvery {
            userListApiService.getUserLists(
                accountId,
                page,
            )
        } returns expectedResponse

        val result = userListRemoteDataSourceImpl.getUserLists(accountId, page)

        assertThat(result).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { userListApiService.getUserLists(accountId, page) }
    }

    @Test
    fun `addMovieToList should return a response on success`() = runTest {
        val listId = 1L
        val movieId = 1L
        val expectedResponse = AddItemToListResponse(1, "success", true)

        coEvery {
            userListApiService.addMediaItemToList(
                listId,
                movieId,
            )
        } returns expectedResponse

        val result = userListRemoteDataSourceImpl.addMovieToList(listId, movieId)

        assertThat(result).isEqualTo(expectedResponse)
        coVerify(exactly = 1) { userListApiService.addMediaItemToList(listId, movieId) }
    }

    @Test
    fun `addMovieToList should throw NetworkException when api call fails`() = runTest {
        val listId = 1L
        val movieId = 1L

        coEvery {
            userListApiService.addMediaItemToList(
                listId,
                movieId,
            )
        } throws NetworkException()

        assertThrows<NetworkException> {
            userListRemoteDataSourceImpl.addMovieToList(listId, movieId)
        }
    }

    @Test
    fun `getMoviesFromList should return a list of movies and call the api service`() = runTest {
        val listId = 1L
        coEvery {
            userListApiService.getMoviesAndTvShowsFromList(
                listId,
                1
            )
        } returns remoteListResponse

        val result = userListRemoteDataSourceImpl.getMoviesAndTvShowsFromList(listId, 1)

        assertThat(result).isEqualTo(remoteListResponse)
        coVerify(exactly = 1) { userListApiService.getMoviesAndTvShowsFromList(listId, 1) }
    }

    @Test
    fun `getMoviesFromList should throw NetworkException when api call fails`() = runTest {
        val listId = 1L
        coEvery {
            userListApiService.getMoviesAndTvShowsFromList(
                listId,
                1
            )
        } throws NetworkException()

        assertThrows<NetworkException> {
            userListRemoteDataSourceImpl.getMoviesAndTvShowsFromList(
                listId,
                1
            )
        }
    }

    @Test
    fun `deleteList should call api service deleteList`() = runTest {
        val listId = 1L
        coEvery { userListApiService.deleteList(listId) } returns Unit

        userListRemoteDataSourceImpl.deleteList(listId)

        coVerify(exactly = 1) { userListApiService.deleteList(listId) }
    }

    @Test
    fun `deleteList should throw NetworkException when api call fails`() = runTest {
        val listId = 1L
        coEvery { userListApiService.deleteList(listId) } throws NetworkException()

        assertThrows<NetworkException> {
            userListRemoteDataSourceImpl.deleteList(
                listId,
            )
        }
    }

    @Test
    fun `removeMovieFromList should call api service removeMovieFromList`() = runTest {
        val listId = 1L
        val movieId = 1L
        coEvery { userListApiService.removeMovieFromList(listId, movieId) } returns Unit

        userListRemoteDataSourceImpl.deleteMovieFromList(listId, movieId)

        coVerify(exactly = 1) { userListApiService.removeMovieFromList(listId, movieId) }
    }

    @Test
    fun `removeMovieFromList should throw NetworkException when api call fails`() = runTest {
        val listId = 1L
        val movieId = 1L
        coEvery {
            userListApiService.removeMovieFromList(
                listId,
                movieId
            )
        } throws NetworkException()

        assertThrows<NetworkException> {
            userListRemoteDataSourceImpl.deleteMovieFromList(listId, movieId)
        }
    }
}