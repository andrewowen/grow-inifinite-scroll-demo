package com.example.infinitepagedemo.data

import com.squareup.moshi.Json
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

data class BeerDto(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "tagline") val tagline: String
)

const val BEER_API = "https://api.punkapi.com/"

class BeerRepository {

    private val beerService = createWebService(BEER_API, BeerService::class.java)

    suspend fun getBeers(query: String? = null, page: Int = 1, limit: Int = 10) =
        beerService.getBeers(query, page, limit)
}


interface BeerService {
    @GET("v2/beers")
    suspend fun getBeers(
        @Query("beer_name") nameQuery: String? = null,
        @Query("page") page: Int,
        @Query("per_page") limit: Int
    ): List<BeerDto>
}