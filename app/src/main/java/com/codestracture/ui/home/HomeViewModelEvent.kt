package com.codestracture.ui.home


sealed interface HomeViewModelEvent {
    data class TripDetailsSuccess(val data: List<String>): HomeViewModelEvent
}
