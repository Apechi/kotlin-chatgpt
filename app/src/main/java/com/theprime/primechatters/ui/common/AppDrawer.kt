package com.theprime.primechatters.ui.common

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.outlined.AddComment
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.theprime.primechatters.constants.urlToGithub
import com.theprime.primechatters.constants.urlToImageAppIcon
import com.theprime.primechatters.constants.urlToImageAuthor
import com.theprime.primechatters.helpers.UrlLauncher
import com.theprime.primechatters.models.ConversationModel
import com.theprime.primechatters.ui.conversations.ConversationViewModel
import kotlinx.coroutines.launch

@Composable
fun AppDrawer(
    onChatClicked: (String) -> Unit,
    onNewChatClicked: () -> Unit,
    conversationViewModel: ConversationViewModel = hiltViewModel(),
    onIconClicked: () -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    AppDrawerIn(
        onChatClicked = onChatClicked,
        onNewChatClicked = onNewChatClicked,
        onIconClicked = onIconClicked,
        conversationViewModel = { conversationViewModel.newConversation() },
        deleteConversation = { conversationId ->
            coroutineScope.launch {
                conversationViewModel.deleteConversation(conversationId)
            }
        },
        onConversation = { conversationModel: ConversationModel ->
            coroutineScope.launch { conversationViewModel.onConversation(conversationModel) }
        },
        currentConversationState = conversationViewModel.currentConversationState.collectAsState().value,
        conversationState = conversationViewModel.conversationsState.collectAsState().value
    )
}

@Composable
private fun AppDrawerIn(
    onChatClicked: (String) -> Unit,
    onNewChatClicked: () -> Unit,
    onIconClicked: () -> Unit,
    conversationViewModel: () -> Unit,
    deleteConversation: (String) -> Unit,
    onConversation: (ConversationModel) -> Unit,
    currentConversationState: String,
    conversationState: List<ConversationModel>,
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image with overlay
        Image(
            painter = rememberAsyncImagePainter("https://i.pinimg.com/originals/18/62/d3/1862d38a31600405ea4526333dfc7168.jpg"), // Replace with your image URL
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f)), // Semi-transparent overlay
            contentScale = ContentScale.Crop
        )

        // Drawer content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f)) // Adjust opacity as needed
        ) {
            Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
            DrawerHeader(clickAction = onIconClicked)
            DividerItem()
            DrawerItemHeader("Chats", MaterialTheme.colorScheme.primary)
            ChatItem("New Chat", Icons.Outlined.AddComment, false) {
                onNewChatClicked()
                conversationViewModel()
            }
            HistoryConversations(
                onChatClicked,
                deleteConversation,
                onConversation,
                currentConversationState,
                conversationState
            )
            DividerItem(modifier = Modifier.padding(horizontal = 28.dp))
            DrawerItemHeader("About", MaterialTheme.colorScheme.primary)
            ProfileItem(
                "lambiengcode (author)",
                urlToImageAuthor,
            ) {
                UrlLauncher().openUrl(context = context, urlToGithub)
            }
            ProfileItem(
                "arepsy (modifier)",
                "https://cdn-icons-png.flaticon.com/512/25/25231.png",
            ) {
                Toast.makeText(context, "reskinned", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


@Composable
private fun DrawerHeader(
    clickAction: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAsyncImagePainter(urlToImageAppIcon),
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
            Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                Text(
                    "Chatters",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    "Powered by OpenAI",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                )
            }
        }

        IconButton(onClick = { clickAction.invoke() }) {
            Icon(
                Icons.Filled.WbSunny,
                contentDescription = "Theme Toggle",
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun ColumnScope.HistoryConversations(
    onChatClicked: (String) -> Unit,
    deleteConversation: (String) -> Unit,
    onConversation: (ConversationModel) -> Unit,
    currentConversationState: String,
    conversationState: List<ConversationModel>
) {
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .weight(1f, false),
    ) {
        items(conversationState.size) { index ->
            RecycleChatItem(
                text = conversationState[index].title,
                icon = Icons.Filled.Message,
                selected = conversationState[index].id == currentConversationState,
                onChatClicked = {
                    onChatClicked(conversationState[index].id)
                    onConversation(conversationState[index])
                },
                onDeleteClicked = {
                    deleteConversation(conversationState[index].id)
                }
            )
        }
    }
}

@Composable
private fun DrawerItemHeader(text: String, textColor: Color) {
    Box(
        modifier = Modifier
            .heightIn(min = 52.dp)
            .padding(horizontal = 28.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text,
            style = MaterialTheme.typography.titleMedium,
            color = textColor
        )
    }
}

@Composable
private fun ChatItem(
    text: String,
    icon: ImageVector,
    selected: Boolean,
    onChatClicked: () -> Unit
) {
    val background = if (selected) {
        Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
    } else {
        Modifier
    }
    Row(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .then(background)
            .clickable(onClick = onChatClicked),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val iconTint = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
        Icon(
            icon,
            tint = iconTint,
            modifier = Modifier
                .padding(start = 16.dp)
                .size(25.dp),
            contentDescription = null,
        )
        Text(
            text,
            style = MaterialTheme.typography.bodyLarge,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier.padding(start = 12.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun RecycleChatItem(
    text: String,
    icon: ImageVector,
    selected: Boolean,
    onChatClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    val background = if (selected) {
        Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
    } else {
        Modifier
    }
    Row(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .then(background)
            .clickable(onClick = onChatClicked),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val iconTint = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
        Icon(
            icon,
            tint = iconTint,
            modifier = Modifier
                .padding(start = 16.dp)
                .size(25.dp),
            contentDescription = null,
        )
        Text(
            text,
            style = MaterialTheme.typography.bodyLarge,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier
                .padding(start = 12.dp)
                .fillMaxWidth(0.85f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Icon(
            imageVector = Icons.Outlined.Delete,
            contentDescription = "Delete",
            tint = iconTint,
            modifier = Modifier
                .padding(end = 12.dp)
                .clickable { onDeleteClicked() }
                .size(25.dp)
        )
    }
}

@Composable
private fun ProfileItem(text: String, urlToImage: String?, onProfileClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onProfileClicked),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = rememberAsyncImagePainter(urlToImage),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun DividerItem(modifier: Modifier = Modifier) {
    Divider(
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f),
        thickness = 1.dp,
        modifier = modifier.padding(vertical = 4.dp)
    )
}

@Preview()
@Composable
fun PreviewAppDrawerIn(
) {
    AppDrawerIn(
        onChatClicked = {},
        onNewChatClicked = {},
        onIconClicked = {},
        conversationViewModel = {},
        deleteConversation = {},
        conversationState = mutableListOf(),
        currentConversationState = String(),
        onConversation = { _: ConversationModel -> }

    )

}
