package org.cardna.data.remote.mapper

import org.cardna.data.remote.model.card.ResponseMainCardData
import org.cardna.domain.model.RepresentCardListData

object CardToRepresentCardListMapper {
    fun mainCardToRepresent(responseMainCardData: ResponseMainCardData): RepresentCardListData {
        return RepresentCardListData(
            mainCardList = responseMainCardData.data.mainCardList.map {
                RepresentCardListData.Data(
                    cardImg = it.cardImg,
                    id = it.id,
                    isMe = it.isMe,
                    mainOrder = it.mainOrder,
                    title = it.title
                )
            }
        )
    }
}