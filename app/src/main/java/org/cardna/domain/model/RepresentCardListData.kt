package org.cardna.domain.model

data class RepresentCardListData(
    val mainCardList: List<Data>,
) {
    data class Data(
        val cardImg: String,
        val id: Int,
        val isMe: Boolean,
        val mainOrder: Int,
        val title: String
    )
}
//
//data class RepresentCardListData(
//    val cardImg: String,
//    val id: Int,
//    val isMe: Boolean,
//    val mainOrder: Int,
//    val title: String
//)