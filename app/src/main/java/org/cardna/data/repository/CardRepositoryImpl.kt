package org.cardna.data.repository

import org.cardna.data.remote.datasource.CardDataSource
import org.cardna.data.remote.model.card.*
import org.cardna.domain.repository.CardRepository
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(private val cardDataSource: CardDataSource) :
    CardRepository {

    override suspend fun getDetailCard(cardId: Int): ResponseDetailCardData {
        return cardDataSource.getDetailCard(cardId)
    }

    override suspend fun deleteCard(cardId: Int): ResponseDeleteCardData {
        return cardDataSource.deleteCard(cardId)
    }

    override suspend fun putKeepOrAddCard(cardId: Int): ResponseKeepOrAddCardData {
        return cardDataSource.putKeepOrAddCard(cardId)
    }

    override suspend fun getCardMe(): ResponseCardMeData {
        return cardDataSource.getCardMe()
    }

    override suspend fun getOtherCardMe(cardId: Int): ResponseCardMeData {
        return cardDataSource.getOtherCardMe(cardId)
    }

    override suspend fun getCardYou(): ResponseCardYouData {
        return cardDataSource.getCardYou()
    }

    override suspend fun getOtherCardYou(cardId: Int): ResponseCardYouData {
        return cardDataSource.getOtherCardYou(cardId)
    }

    override suspend fun getMainCard(): ResponseMainCardData {
        return cardDataSource.getMainCard()
    }

    override suspend fun putEditCard(cardList: RequestEditCardData): ResponseEditCardData {
        return cardDataSource.putEditCard(cardList)
    }

    override suspend fun getCardYouStore(): ResponseCardYouStoreData {
        return cardDataSource.getCardYouStore()
    }
}
