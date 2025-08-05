package com.amsterdam.viewmodel.lists

interface ListsInteractionListener {
    fun onClickAddList()

    fun onCreateNewListClick(listName: String)

    fun onListClick(
        listId: Long,
        listName: String,
    )

    fun onClickRetryFetchList()

    fun onDismiss()

    fun onNavigateToLoginClicked()
}
