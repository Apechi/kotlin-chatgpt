package com.theprime.primechatters.data.remote

import com.theprime.primechatters.models.MessageModel
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun fetchMessages(conversationId: String): Flow<List<MessageModel>>
    fun createMessage(message: MessageModel): MessageModel
    fun deleteMessage()
}