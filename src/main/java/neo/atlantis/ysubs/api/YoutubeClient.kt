package neo.atlantis.ysubs.api

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import neo.atlantis.ysubs.api.response.ChannelsResponse
import neo.atlantis.ysubs.config.PluginPreference
import okhttp3.OkHttpClient
import org.bukkit.Server
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class YoutubeClient(
        private val server: Server,
        private val pluginPreference: PluginPreference
) {
    private val baseUrl = "https://www.googleapis.com/youtube/v3/"
    private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    private val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(getClient())
            .build()
    private val service = retrofit.create(YoutubeService::class.java)

    fun getChannels(channelIds: List<String>) {
        GlobalScope.launch {
            try {
                val responses: MutableList<ChannelsResponse> = mutableListOf()
                channelIds.chunked(20).forEach {
                    val response = service.channels(
                            "id,statistics,snippet",
                            it.joinToString(","),
                            pluginPreference.apiKey
                    )
                    responses.add(response)
                }
                responses.forEach { response ->
                    response.items.forEach {
                        val channelId = it.id
                        if (channelId.isNotEmpty()) {
                            pluginPreference.setChannelSubscriberCount(channelId, it.statistics.subscriberCount)
                            pluginPreference.setChannelName(channelId, it.snippet.title)
                        }
                    }
                }
                server.broadcast("Success", Server.BROADCAST_CHANNEL_ADMINISTRATIVE)
            } catch (e: HttpException) {
                server.broadcast("Failed ${e.code()} ${e.message()}", Server.BROADCAST_CHANNEL_ADMINISTRATIVE)
            } catch (e: Exception) {
                server.broadcast("Failed $e", Server.BROADCAST_CHANNEL_ADMINISTRATIVE)
            }
        }
    }

    private fun getClient(): OkHttpClient {
        return OkHttpClient
                .Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build()
    }
}