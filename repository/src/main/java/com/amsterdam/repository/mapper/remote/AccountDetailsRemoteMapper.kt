package com.amsterdam.repository.mapper.remote

import com.amsterdam.entity.AccountDetails
import com.amsterdam.repository.dto.remote.profile.AccountDetailsDto
import com.amsterdam.repository.mapper.shared.EntityMapper
import javax.inject.Inject

class AccountDetailsRemoteMapper @Inject constructor() :
    EntityMapper<AccountDetailsDto, AccountDetails> {
    override fun toEntity(dto: AccountDetailsDto): AccountDetails {
        return AccountDetails(
            id = dto.id,
            username = dto.username,
            avatarUrl = dto.accountAvatar.tmdbData.fullAvatarPath.orEmpty(),
        )
    }

}