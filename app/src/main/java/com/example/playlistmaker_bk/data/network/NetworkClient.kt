package com.example.playlistmaker_bk.domain.api

import com.example.playlistmaker_bk.data.dto.BaseResponse

interface NetworkClient {
    fun doRequest(dto: Any): BaseResponse
}