package com.codestracture.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor() : LocalRepository {
    override fun getTripsData(): Flow<List<String>> {
        val list = listOf("Test", "Test1", "Test2")
        return flow { emit(list) }
    }
}