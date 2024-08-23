package com.theprime.primechatters.data.remote

import com.theprime.primechatters.models.ConversationModel

interface ConversationRepository {
    suspend fun fetchConversations() : MutableList<ConversationModel>
    fun newConversation(conversation: ConversationModel) : ConversationModel
    suspend fun deleteConversation(conversationId: String)
}