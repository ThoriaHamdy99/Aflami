package com.amsterdam.repository.mapper

import com.amsterdam.entity.Country
import com.amsterdam.repository.dto.local.CountryLocalDto
import com.amsterdam.repository.dto.remote.CountryRemoteDto
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class CountryMapperTest {
    private val countryEntity = Country(countryName = "Egypt", countryIsoCode = "EG")

    private val countryLocalDto = CountryLocalDto(
        name = "Egypt",
        isoCode = "EG",
        storedLanguage = "en"
    )

    private val countryRemoteDto = CountryRemoteDto(
        isoCode = "EG",
        englishName = "Egypt",
        nativeName = "مصر"
    )

    @Nested
    inner class LocalMappers {
        @Test
        fun `toEntity should map CountryLocalDto to Country entity`() {
            val result = countryLocalDto.toEntity()

            assertThat(result).isEqualTo(countryEntity)
        }

        @Test
        fun `toLocalDto should map Country entity to CountryLocalDto`() {
            val storedLanguage = "ar"

            val result = countryEntity.toLocalDto(storedLanguage)

            assertThat(result).isEqualTo(
                CountryLocalDto(
                    name = "Egypt",
                    isoCode = "EG",
                    storedLanguage = storedLanguage
                )
            )
        }
    }

    @Nested
    inner class RemoteMappers {

        @Test
        fun `toEntity should map CountryRemoteDto to Country entity using nativeName`() {
            val result = countryRemoteDto.toEntity()

            assertThat(result).isEqualTo(
                Country(
                    countryName = "مصر",
                    countryIsoCode = "EG"
                )
            )
        }

        @Test
        fun `toLocalDto should map CountryRemoteDto to CountryLocalDto using nativeName`() {
            val storedLanguage = "en"

            val result = countryRemoteDto.toLocalDto(storedLanguage)

            assertThat(result).isEqualTo(
                CountryLocalDto(
                    name = "مصر",
                    isoCode = "EG",
                    storedLanguage = storedLanguage
                )
            )
        }

        @Test
        fun `toLocalDtoList should map a list of CountryRemoteDto to a list of CountryLocalDto`() {
            val storedLanguage = "fr"
            val remoteList = listOf(
                CountryRemoteDto(isoCode = "EG", englishName = "Egypt", nativeName = "مصر"),
                CountryRemoteDto(isoCode = "SA", englishName = "Saudi Arabia", nativeName = "المملكة العربية السعودية")
            )

            val result = remoteList.toLocalDtoList(storedLanguage)

            assertThat(result).isEqualTo(
                listOf(
                    CountryLocalDto(name = "مصر", isoCode = "EG", storedLanguage = storedLanguage),
                    CountryLocalDto(name = "المملكة العربية السعودية", isoCode = "SA", storedLanguage = storedLanguage)
                )
            )
        }

        @Test
        fun `toLocalDtoList should return an empty list when given an empty list`() {
            val emptyRemoteList = emptyList<CountryRemoteDto>()

            val result = emptyRemoteList.toLocalDtoList("en")

            assertThat(result).isEmpty()
        }
    }
}