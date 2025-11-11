package com.example.bps.data.remote

// Import semua data class dari folder 'responses'
import com.example.bps.data.remote.responses.* // Import data class dari folder 'responses'
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.example.bps.data.remote.responses.BpsDatasetResponse
import com.example.bps.data.remote.responses.SimpleDatasetResponse

/** Interface ini berisi SEMUA definisi endpoint API yang akan dipanggil menggunakan Retrofit. */
interface ApiService {

    @GET("datasets/{dataset}")
    suspend fun getDatasetDetail(@Path("dataset") datasetId: String): BpsDatasetResponse

    @GET("datasets")
    suspend fun getDatasetList(
        @Query("subject") subject: String? = null,
        @Query("q") searchQuery: String? = null
    ): List<SimpleDatasetResponse>

    @GET("datasets/categories")
    suspend fun getCategories(): List<CategorySubjectResponse>

    //@GET("datasets/{dataset}/history")
    //suspend fun getDatasetHistory(
      //     @Path("dataset") datasetId: String
    //): DatasetHistoryResponse // <- Anda perlu buat data class 'DatasetHistoryResponse'

//     @GET("datasets/{dataset}/insights")
//     suspend fun getDatasetInsights(
//             @Path("dataset") datasetId: String
//     ): DatasetInsightResponse // <- Anda perlu buat data class 'DatasetInsightResponse'

//     @GET("content/news")
//     suspend fun getNews():
//             List<NewsItemResponse> // <- Anda perlu buat data class 'NewsItemResponse'

//     @GET("content/press-releases")
//     suspend fun getPressReleases():
//             List<PressReleaseResponse> // <- Anda perlu buat data class 'PressReleaseResponse'

//     @GET("content/infographics")
//     suspend fun getInfographics():
//             List<InfographicResponse> // <- Anda perlu buat data class 'InfographicResponse'

//     @GET("content/publications")
//     suspend fun getPublications():
//             List<PublicationResponse> // <- Anda perlu buat data class 'PublicationResponse'
}
