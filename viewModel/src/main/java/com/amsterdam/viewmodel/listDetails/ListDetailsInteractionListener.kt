package com.amsterdam.viewmodel.listDetails

import com.amsterdam.viewmodel.shared.uiStates.media.MediaType

interface ListDetailsInteractionListener {
    fun onClickMediaItem(mediaId: Long, mediaType: MediaType)
    fun onClickBack()
    fun onClickRetryLoading()
}