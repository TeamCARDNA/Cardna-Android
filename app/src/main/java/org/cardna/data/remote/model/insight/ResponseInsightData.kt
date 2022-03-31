package org.cardna.data.remote.model.insight

data class ResponseInsightData(
    val message: String,
    val status: Int,
    val success: Boolean,
    val data: Data,
) {
    data class Data(
        val blindArea: BlindArea,
        val openArea: OpenArea,
    )
}

data class OpenArea(
    val id: Int,
    val image: String,
    val isInsight: Boolean,
    val title: String
)

data class BlindArea(
    val id: Int,
    val image: String,
    val isInsight: Boolean,
    val title: String
)
