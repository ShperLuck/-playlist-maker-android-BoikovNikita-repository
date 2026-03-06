package com.example.playlistmaker_bk.data.dto

import com.google.gson.annotations.SerializedName

data class TrackDto(
    val trackId: Int, // Используем Int для консистентности
    val trackName: String?,
    val artistName: String?,
    @SerializedName("trackTimeMillis") val trackTimeMillis: Long?,
    @SerializedName("artworkUrl100") val artworkUrl100: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?
)

data class TrackSearchResponse(
    val resultCount: Int,
    val results: List<TrackDto>
)