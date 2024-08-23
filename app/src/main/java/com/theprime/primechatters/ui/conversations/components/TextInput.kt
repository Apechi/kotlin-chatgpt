package com.theprime.primechatters.ui.conversations.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.theprime.primechatters.ui.conversations.ConversationViewModel
import kotlinx.coroutines.launch

@Composable
fun TextInput(
    conversationViewModel: ConversationViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    TextInputIn(
        sendMessage = { text ->
            coroutineScope.launch {
                conversationViewModel.sendMessage(text)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TextInputIn(
    sendMessage: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    var text by remember { mutableStateOf(TextFieldValue("")) }

    Box(
        modifier = Modifier
            .navigationBarsPadding()
            .imePadding()
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column {
            Divider(Modifier.height(0.5.dp).background(Color.Gray))
            Box(
                Modifier
//                    .padding(horizontal = 8.dp)
//                    .padding(top = 8.dp, bottom = 14.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
//
                        .padding(12.dp)
                ) {
                    TextField(
                        value = text,
                        onValueChange = {
                            text = it
                        },
                        placeholder = {
                            Text("Ask me anything...", fontSize = 14.sp)
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                            .background(Color.Transparent),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        textStyle = LocalTextStyle.current.copy(fontSize = 14.sp),
                        singleLine = true
                    )
                    IconButton(
                        onClick = {
                            scope.launch {
                                val textClone = text.text
                                text = TextFieldValue("")
                                sendMessage(textClone)
                            }
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                            .padding(all = 4.dp)

                    ) {
                        Icon(
                            Icons.Filled.Send,
                            contentDescription = "Send",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewTextInput() {
    TextInputIn(
        sendMessage = {}
    )
}
