package com.example.bps.data.remote.responses

import com.google.gson.annotations.SerializedName

/**
 * Ini adalah data class untuk JSON baru Anda:
 * [ { "category": 1, "subjects": ["...", "..."] } ]
 */
data class CategorySubjectResponse(

    @SerializedName("category") val category: Int,
    @SerializedName("subjects") val subjects: List<String>
)