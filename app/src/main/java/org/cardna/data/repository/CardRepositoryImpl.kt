package org.cardna.data.repository

import org.cardna.data.remote.datasource.MainCardDataSource
import org.cardna.data.remote.mapper.CardToRepresentCardListMapper
import org.cardna.domain.model.RepresentCardListData
import org.cardna.domain.repository.CardRepository
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(private val dataSource: MainCardDataSource) :
    CardRepository {

    override suspend fun getRepresentCardList(): RepresentCardListData {
        return CardToRepresentCardListMapper.mainCardToRepresent(dataSource.getMainCard())
    }

}