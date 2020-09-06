package neo.atlantis.ysubs.api.response

data class ChannelsResponse(
        val kind: String,
        val etag: String,
        val pageInfo: PageInfo,
        val items: List<Item>
)

data class PageInfo(
        val resultsPerPage: Int
)

data class Item(
        val kind: String,
        val etag: String,
        val id: String,
        val snippet: Snippet,
        val statistics: Statistics
)

data class Snippet(
        val title: String,
        val description: String
)

data class Statistics(
        val viewCount: Long,
        val subscriberCount: Long,
        val videoCount: Long
)