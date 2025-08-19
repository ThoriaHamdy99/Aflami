package com.amsterdam.viewmodel.lists

interface ListsInteractionListener {
    fun onClickAddList()
    fun onListNameChange(listName: String)
    fun onCreateNewListClick()

    fun onListClick(listId: Long, listName: String)
    fun onClickRetry()
    fun onDismiss()
    fun onNavigateToLoginClicked()
}
