package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.data.managers.CacheManager
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.models.data.Chat
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.models.data.ChatType
import ru.skillbranch.devintensive.repositories.ChatRepository


class MainViewModel: ViewModel() {
    private val query = mutableLiveData("")
    private val chatRepository = ChatRepository
    private val chats = Transformations.map(chatRepository.loadChats()) {chats->
        return@map chats
            .asSequence()
            .groupBy { it.isArchived }
            .map {
                if (it.key) {
                    val last = it.value.last()
                    var mcount = 0
                    it.value.forEach {
                        mcount += it.messages.filter { !it.isReaded }.count()
                    }
                    listOf(last.toChatItem().copy(id = "-1", messageCount = mcount, chatType = ChatType.ARCHIVE))
                } else {
                    it.value.map { it.toChatItem() }
                }
            }
            .reduce { acc, list ->  acc+list}
            .sortedBy { it.id.toInt() }
            .toList()
    }

    fun getChatData() : LiveData<List<ChatItem>> {
        val result = MediatorLiveData<List<ChatItem>>()

        val filterF = {
            val queryStr = query.value!!
            val chats = chats.value!!

            result.value = if(queryStr.isEmpty()) chats
            else chats.filter { it.title.contains(queryStr, true)}
        }
        result.addSource(chats){filterF.invoke()}
        result.addSource(query){filterF.invoke()}
        return result
    }

    fun addToArchive(chatId: String) {
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = true))
    }

    fun restoreFromArchive(chatId: String){
        val chat = chatRepository.find(chatId)
        chat ?: return
        chatRepository.update(chat.copy(isArchived = false))
    }

    fun handleSearchQuery(text: String) {
        query.value = text

    }
}