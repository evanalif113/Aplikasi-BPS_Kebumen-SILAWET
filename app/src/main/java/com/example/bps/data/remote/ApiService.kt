package com.example.bps.data.remote

// Import semua data class dari folder 'responses'
import com.example.bps.data.remote.responses.* // Import data class dari folder 'responses'
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.Response
import com.example.bps.data.remote.responses.BpsDatasetClass
import com.example.bps.data.remote.responses.BpsPublicationResponse
/** Interface ini berisi SEMUA definisi endpoint API yang akan dipanggil menggunakan Retrofit. */
interface ApiService {

    // --- ENDPOINT DATASET (Tetap) ---
    @GET("datasets/{id}")
    suspend fun getDatasetDetail(
        @Path("id") id: String,
        @Query("year") year: Int? = null,
        @Query("mode") mode: String? = null
    ): Response<BpsDatasetClass>

    @GET("datasets")
    suspend fun getDatasetList(
        @Query("subject") subject: String? = null,
        @Query("q") searchQuery: String? = null
    ): SimpleDatasetListResponse

    @GET("datasets/categories")
    suspend fun getCategories(): Map<String, CategorySubjectResponse>

    // --- ENDPOINT KONTEN BERANDA ---

    // 1. Berita Kegiatan (News)
    @GET("content/news")
    suspend fun getNews(): BpsNewsResponse

    // 2. Berita Resmi Statistik (BRS)
    @GET("content/press-releases")
    suspend fun getPressReleases(): BpsNewsResponse

    // 3. Infografis
    @GET("content/infographics")
    suspend fun getInfographics(): BpsInfografisResponse

    // 4. Publikasi
    @GET("content/publications")
    suspend fun getPublications(): BpsPublicationResponse
}
