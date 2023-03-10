package com.codestracture.data.local

import kotlinx.coroutines.flow.Flow

interface LocalRepository {

    fun getTripsData(): Flow<List<String>>
}