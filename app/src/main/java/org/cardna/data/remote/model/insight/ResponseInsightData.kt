package org.cardna.data.remote.model.insight

data class ResponseInsightData(
    val message: String,
    val status: Int,
    val success: Boolean,
    val data: Data,
) {
    data class Data(
        val blindAreaCard: BlindAreaCard,
        val openAreaCard: OpenAreaCard,
    )
}

data class OpenAreaCard(
    val id: Int,
    val imageUrl: String,
    val isInsight: Boolean,
    val title: String
)

data class BlindAreaCard(
    val id: Int,
    val imageUrl: String,
    val isInsight: Boolean,
    val title: String
)
