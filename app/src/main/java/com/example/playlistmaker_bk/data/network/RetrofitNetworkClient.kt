package com.example.playlistmaker_bk.data.network

import com.example.playlistmaker_bk.data.dto.BaseResponse
import com.example.playlistmaker_bk.data.dto.TracksSearchRequest
import com.example.playlistmaker_bk.data.dto.TracksSearchResponse

// Добавил itunesApi в конструктор, чтобы Koin мог его передать
class RetrofitNetworkClient(private val itunesApi: ITunesApi) {
    // Мб NetworkClient
    // пока оставить
}