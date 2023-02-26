package com.demo.syscomponent

import android.content.SearchRecentSuggestionsProvider


class RecentSearchProvider : SearchRecentSuggestionsProvider() {

    companion object {
        const val AUTHORITY = "com.demo.syscomponent.recentsearchprovider"
        const val MODE = DATABASE_MODE_QUERIES or DATABASE_MODE_2LINES
    }

    init {
        setupSuggestions(AUTHORITY, MODE)
    }

}