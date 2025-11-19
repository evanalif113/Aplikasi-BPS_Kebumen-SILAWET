package com.example.bps.data.remote

// Import semua data class dari folder 'responses'
import com.example.bps.data.remote.responses.* // Import data class dari folder 'responses'
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import com.example.bps.data.remote.responses.BpsDatasetResponse
import com.example.bps.data.remote.responses.SimpleDatasetResponse
import com.example.bps.data.remote.responses.PublicationItem
import com.example.bps.data.remote.responses.NewsItem

/** Interface ini berisi SEMUA definisi endpoint API yang akan dipanggil menggunakan Retrofit. */
interface ApiService {

    // --- ENDPOINT DATASET (Tetap) ---
    @GET("datasets/{dataset}")
    suspend fun getDatasetDetail(@Path("dataset") datasetId: String): BpsDatasetResponse

    @GET("datasets")
    suspend fun getDatasetList(
        @Query("subject") subject: String? = null,
        @Query("q") searchQuery: String? = null
    ): List<SimpleDatasetResponse>

    @GET("datasets/categories")
    suspend fun getCategories(): List<CategorySubjectResponse>

    // --- ENDPOINT KONTEN BERANDA (DIPERBAIKI) ---

    // 1. Berita Kegiatan (News)
    // FIX: Ubah return type dari NewsItem menjadi BpsNewsResponse
    @GET("content/news")
    suspend fun getNews(): BpsNewsResponse

    // 2. Berita Resmi Statistik (BRS)
    // Kita gunakan BpsNewsResponse juga karena strukturnya sama (List of NewsItem)
    @GET("content/press-releases")
    suspend fun getPressReleases(): BpsNewsResponse

    // 3. Infografis
    // Menggunakan wrapper yang sudah kita siapkan
    @GET("content/infographics")
    suspend fun getInfographics(): BpsInfografisResponse

    // 4. Publikasi
    // Menggunakan wrapper khusus untuk buku
    @GET("content/publications")
    suspend fun getPublications(): PublicationItem
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
