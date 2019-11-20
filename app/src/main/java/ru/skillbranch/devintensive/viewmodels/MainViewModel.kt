package ru.skillbranch.devintensive.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.skillbranch.devintensive.extensions.insertIf
import ru.skillbranch.devintensive.data.managers.CacheManager
import ru.skillbranch.devintensive.extensions.mutableLiveData
import ru.skillbranch.devintensive.extensions.shortFormat
import ru.skillbranch.devintensive.extensions.shortMessage
import ru.skillbranch.devintensive.models.data.Chat
import ru.skillbranch.devintensive.models.data.ChatItem
import ru.skillbranch.devintensive.models.data.ChatType
import ru.skillbranch.devintensive.repositories.ChatRepository


class MainViewModel: ViewModel() {
    private val query = mutableLiveData("")
    private val chatRepository = ChatRepository
    //TODO Создание элемента списка "Архив чатов"
    /*
Необходимо реализовать верстку элемента списка согласно макетам
+1
Сверстай элемента списка, он должен содержать в себе следующие View:
Иконку элемента (статический drawable)
Заголовок (статический текст "Архив чатов")
Имя автора последнего сообщения@+id/tv_message_author_archive
Текст последнего сообщения@+id/tv_message_archive
Дата последнего сообщения@+id/tv_date_archive
Количество непрочитанных сообщений@+id/tv_counter_archive

ArchiveItem должен реализовывать следующий функционал : При имеющихся архтвных чатах первый элемент
*  списка rv_chat_list (MainActivity) должен являться item_chat_archive, количество сообщений в нем
* tv_archive_counter должно быть равно сумме непрочитанных сообщений архивных чатов. Отображаемое
* сообщение tv_message_archive и его автор tv_message_author_archive должны соответствовать самому
* последнему входящему сообщению в архивных чатах. При клике на ArchiveItem (item_chat_archive)
* должно открываеться ArchiveActivity. Не отображать ArchiveItem (item_chat_archive) если нет
* зархивированных чатов. Свайп на ArchiveItem не должен срабатывать
     */
    private val chats = Transformations.map(chatRepository.loadChats()){chats ->
        val allArchivedMessages = chats.filter { it.isArchived }
            .flatMap { it.messages }
            .sortedBy { it.date.time }
        val lastMessage = allArchivedMessages.lastOrNull()
        val (lastMessageShort, lastMessageAuthor)= shortMessage(lastMessage)
        chats.orEmpty()
            .filter { !it.isArchived }
            .map { it.toChatItem() }
            .sortedBy { it.id }
            .toMutableList()
            .insertIf(
                ChatItem.archiveItem(
                    lastMessageShort,
                    allArchivedMessages.size,
                    lastMessage?.date?.shortFormat() ?: "Никогда",
                    lastMessageAuthor
                ),
                0) { chats.any { it.isArchived }}
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