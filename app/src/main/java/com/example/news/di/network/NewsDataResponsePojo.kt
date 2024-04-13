package com.example.news.di.network

import com.google.gson.annotations.SerializedName

class NewsDataResponsePojo(
    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Int,
    @SerializedName("results")
    val results: List<NewsDataContent>,
    @SerializedName("nextPage")
    val nextPage: String
)

class NewsDataContent(
    @SerializedName("article_id")
    val articleId: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("creator")
    val creator: List<String>?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("pubDate")
    val pubDate: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("source_icon")
    val sourceIcon: String
)