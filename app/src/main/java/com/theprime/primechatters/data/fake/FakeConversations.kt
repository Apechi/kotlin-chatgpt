package com.theprime.primechatters.data.fake

import com.theprime.primechatters.models.ConversationModel
import java.util.*

val fakeConversations: List<ConversationModel> = listOf(
    ConversationModel(
        id = "1",
        title = "What's Flutter?",
        createdAt = Date(),
    ),
    ConversationModel(
        id = "2",
        title = "What's Compose?",
        createdAt = Date(),
    ),
    ConversationModel(
        id = "3",
        title = "What's ChatGPT?",
        createdAt = Date(),
    ),
)