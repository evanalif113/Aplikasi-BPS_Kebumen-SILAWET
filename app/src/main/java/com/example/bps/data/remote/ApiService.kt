package com.example.bps.data.remote

// Import semua data class dari folder 'responses'
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.Response
import com.example.bps.data.remote.responses.DatasetClass
import com.example.bps.data.remote.responses.BpsPublicationResponse
import com.example.bps.data.remote.responses.SimpleDatasetListResponse
import com.example.bps.data.remote.responses.CategorySubjectResponse
import com.example.bps.data.remote.responses.BpsNewsResponse
import com.example.bps.data.remote.responses.BpsInfografisResponse
import com.example.bps.data.remote.responses.GridCategoryResponse
import com.example.bps.data.remote.responses.GridMenuResponse
import com.example.bps.data.remote.responses.BpsIndicatorResponse

/** Interface ini berisi SEMUA definisi endpoint API yang akan dipanggil menggunakan Retrofit. */
interface ApiService {

    // --- ENDPOINT DATASET (Tetap) ---
    @GET("datasets/{id}")
    suspend fun getDatasetDetail(
        @Path("id") id: String,
        @Query("year") year: Int? = null,
        @Query("mode") mode: String? = null
    ): Response<DatasetClass>

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

    @GET("homepage/indicators")
    suspend fun getStrategicIndicators(): BpsIndicatorResponse

    @GET("homepage/grid")
    suspend fun getGridMenu(): GridMenuResponse


    @GET("homepage/grid/{slug}")
    suspend fun getGridByCategory(
        @Path("slug") slug: String
    ): GridCategoryResponse
}
