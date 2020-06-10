package com.сode5150.mercury_task3.network

import com.google.gson.GsonBuilder
import com.сode5150.mercury_task3.network.data.Issue
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val USER: String = "square"
const val REPO: String = "retrofit"
const val BASE_URL: String = "https://api.github.com/"
const val ISSUES: String = "repos/$USER/$REPO/issues"

interface GithubApiInterface {
    @GET(ISSUES)
    suspend fun getIssues(@Query("state") state: String = "open"): List<Issue>

    companion object {
        operator fun invoke(): GithubApiInterface {
            val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(GithubApiInterface::class.java)
        }
    }
}