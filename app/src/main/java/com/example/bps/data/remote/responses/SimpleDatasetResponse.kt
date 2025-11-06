package com.example.bps.data.remote.responses

/**
 * Ini adalah data class RINGAN yang digunakan HANYA untuk
 * menampilkan daftar dataset.
 *
 * Sesuai dengan JSON:
 * [
 * { "id": 5, "dataset_name": "...", "subject": "..." }
 * ]
 */
data class SimpleDatasetResponse(
    val id: Int, // Pastikan tipe data 'id' sesuai (Int atau String)
    val dataset_name: String,
    val subject: String
)