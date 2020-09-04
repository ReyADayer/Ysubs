package neo.atlantis.ysubs.api

import neo.atlantis.ysubs.api.response.ChannelsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeService {
    @GET("channels")
    suspend fun channels(
            @Query("part") part: String,
            @Query("id") id: String,
            @Query("key") key: String
    ): ChannelsResponse
}