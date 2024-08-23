package com.theprime.primechatters.data.remote

import com.theprime.primechatters.models.TextCompletionsParam
import kotlinx.coroutines.flow.Flow

interface OpenAIRepository {
    fun textCompletionsWithStream(params: TextCompletionsParam): Flow<String>
}