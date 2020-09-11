package com.blibli.simpleapp.feature.user.model.enums

enum class ApiCall(val id: Int) {
    FETCH_SEARCH_RESULTS(0),
    FETCH_FOLLOWING_DATA(1),
    FETCH_FOLLOWERS_DATA(2),
    FETCH_DB(3)
}
