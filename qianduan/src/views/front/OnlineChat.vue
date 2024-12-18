<script setup>
import {onMounted, ref, watch, onUnmounted} from 'vue'
import {useUserStore} from '@/stores/userInfo'
import {useChatStore} from '@/stores/chatStore'
import request from "@/utils/request"

const userStore = useUserStore()
const chatStore = useChatStore()
const message = ref('')
const activeChat = ref(null)
const messagesContainer = ref(null)

// 扩展表情数据
const emojis = [
  // 表情和情绪
  '😀', '😃', '😄', '😁', '😅', '😂', '🤣', '😊',
  '😇', '🙂', '😉', '😌', '😍', '🥰', '😘', '😗',
  '😙', '😚', '😋', '😛', '😝', '😜', '🤪', '🤨',
  '🧐', '🤓', '😎', '🤩', '🥳', '😏', '😒', '😞',

  // 手势和动作
  '👍', '👎', '👏', '🙌', '👋', '🤝', '🤗', '🤔',
  '✌️', '🤞', '❤️', '🤟', '🤘', '👌',

  // 心形和爱心
  '❤️', '🧡', '💛', '💚', '💙', '💜', '🤎', '🖤',
  '💕', '💞', '💓', '💗', '💖', '💝', '💘', '💟',

  // 动物
  '🐱', '🐶', '🐼', '🐨', '🦊', '🐰', '🐯', '🦁',

  // 食物和饮料
  '☕', '🍵', '🧋', '🥤', '🍰', '🍭', '🍪', '🍩',

  // 其他符号
  '✨', '⭐', '🌟', '💫', '🎉', '🎊', '🎈', '🎀',
  '🌈', '🌸', '🌺', '🌼', '🌻', '🍀', '🎵', '🎶'
]
const showEmojiPicker = ref(false)

// 插入表情到消息
const insertEmoji = (emoji) => {
  message.value += emoji
  showEmojiPicker.value = false
}

// Get current user info
const getCurrentUser = async () => {
  try {
    const res = await request.get('/system/curryUser')
    if (res.data.code === 200) {
      chatStore.initWebSocket(res.data.data)
    }
  } catch (error) {
    console.error('Failed to get user info:', error)
  }
}

// Send message
const sendMessage = () => {
  if (!message.value.trim() || !activeChat.value) return

  chatStore.sendMessage(activeChat.value.username, message.value)
  message.value = ''
  scrollToBottom()
}

// Select chat user
const selectChat = (user) => {
  activeChat.value = user
  // 设置当前聊天用户
  window.activeChatUser = user.username
  // Clear unread count
  chatStore.unreadMessages[user.username] = 0
  scrollToBottom()
}

// Scroll to bottom
const scrollToBottom = () => {
  setTimeout(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  }, 100)
}

// Watch for new messages
watch(() => activeChat.value?.username && chatStore.chatMessages[activeChat.value.username],
    () => scrollToBottom(),
    {deep: true}
)

// 组件卸载时清除当前聊天用户
onUnmounted(() => {
  window.activeChatUser = null
})

onMounted(() => {
  chatStore.loadFromLocalStorage()
  if (!chatStore.currentUser) {
    getCurrentUser()
  }
})
</script>

<template>
  <div class="chat-container">
    <!-- 左侧用户列表 -->
    <div class="users-list">
      <div class="list-header">
        <h3>Online users</h3>
      </div>
      <div class="users">
        <div
            v-for="user in chatStore.onlineUsers"
            :key="user.username"
            class="user-item"
            :class="{ active: activeChat?.username === user.username }"
            @click="selectChat(user)"
        >
          <div class="user-avatar">
            <el-avatar :size="40" :src="user.avatar">
              {{ user.username.charAt(0) }}
            </el-avatar>
            <el-badge
                v-if="chatStore.unreadMessages[user.username]"
                :value="chatStore.unreadMessages[user.username]"
                class="unread-badge"
            />
          </div>
          <div class="user-info">
            <div class="username">{{ user.username }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 右侧聊天区域 -->
    <div class="chat-area">
      <template v-if="activeChat">
        <div class="chat-header">
          <div class="chat-user-info">
            <el-avatar :size="32" :src="activeChat.avatar">
              {{ activeChat.username.charAt(0) }}
            </el-avatar>
            <h3>{{ activeChat.username }}</h3>
          </div>
        </div>
        <div class="messages-container" ref="messagesContainer">
          <div
              v-for="(msg, index) in chatStore.chatMessages[activeChat.username]"
              :key="index"
              class="message"
              :class="{ 'message-self': msg.isSelf }"
          >
            <el-avatar
                :size="32"
                :src="msg.isSelf ? chatStore.currentUser.avatar : msg.avatar || activeChat.avatar"
                class="message-avatar"
            >
              {{ (msg.isSelf ? chatStore.currentUser.username : activeChat.username).charAt(0) }}
            </el-avatar>
            <div class="message-content">
              <p>{{ msg.content }}</p>
              <span class="message-time">{{ msg.timestamp }}</span>
            </div>
          </div>
        </div>
        <div class="input-area">
          <div class="emoji-toolbar">
            <el-button
                class="emoji-button"
                @click="showEmojiPicker = !showEmojiPicker"
                text
            >
              😊
            </el-button>

            <!-- 表情选择器 -->
            <div v-if="showEmojiPicker" class="emoji-picker">
              <div class="emoji-grid">
                <button
                    v-for="emoji in emojis"
                    :key="emoji"
                    class="emoji-item"
                    @click="insertEmoji(emoji)"
                >
                  {{ emoji }}
                </button>
              </div>
            </div>
          </div>

          <div class="message-input-wrapper">
            <el-input
                v-model="message"
                type="textarea"
                :rows="3"
                placeholder="Type your message..."
                @keyup.enter.native="sendMessage"
            />
            <el-button type="primary" @click="sendMessage">Send</el-button>
          </div>
        </div>
      </template>
      <div v-else class="no-chat-selected">
        <p>Select a user to start chatting with</p>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.chat-container {
  display: flex;
  height: calc(100vh - 120px);
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  margin: 20px;
  overflow: hidden;
}

.users-list {
  width: 300px;
  border-right: 1px solid #e6e6e6;
  display: flex;
  flex-direction: column;

  .list-header {
    padding: 20px;
    border-bottom: 1px solid #e6e6e6;

    h3 {
      margin: 0;
      color: #333;
    }
  }

  .users {
    overflow-y: auto;
    flex: 1;

    .user-item {
      display: flex;
      align-items: center;
      padding: 15px 20px;
      cursor: pointer;
      transition: all 0.3s ease;

      &:hover {
        background: #f5f7fa;
      }

      &.active {
        background: #ecf5ff;
      }

      .user-avatar {
        margin-right: 12px;
        position: relative;

        .unread-badge {
          position: absolute;
          top: -8px;
          right: -8px;
        }
      }

      .user-info {
        .username {
          font-weight: 500;
          color: #333;
        }
      }
    }
  }
}

.chat-area {
  flex: 1;
  display: flex;
  flex-direction: column;

  .chat-header {
    padding: 20px;
    border-bottom: 1px solid #e6e6e6;

    .chat-user-info {
      display: flex;
      align-items: center;
      gap: 12px;

      h3 {
        margin: 0;
        color: #333;
      }
    }
  }

  .messages-container {
    flex: 1;
    overflow-y: auto;
    padding: 20px;
    background: #f5f7fa;

    .message {
      margin-bottom: 20px;
      display: flex;
      align-items: flex-start;
      gap: 12px;

      &.message-self {
        flex-direction: row-reverse;

        .message-content {
          background: #409eff;
          color: white;
          margin-left: 0;
          margin-right: 12px;

          .message-time {
            color: rgba(255, 255, 255, 0.8);
          }
        }
      }

      .message-avatar {
        flex-shrink: 0;
      }

      .message-content {
        background: white;
        padding: 12px 16px;
        border-radius: 12px;
        max-width: 70%;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

        p {
          margin: 0;
          word-break: break-word;
        }

        .message-time {
          font-size: 12px;
          color: #999;
          margin-top: 4px;
          display: block;
        }
      }
    }
  }

  .input-area {
    padding: 20px;
    border-top: 1px solid #e6e6e6;
    background: white;

    .emoji-toolbar {
      margin-bottom: 8px;
      position: relative;

      .emoji-button {
        padding: 4px 8px;
        font-size: 20px;
        line-height: 1;
        cursor: pointer;

        &:hover {
          background: #f5f7fa;
          border-radius: 4px;
        }
      }

      .emoji-picker {
        position: absolute;
        bottom: 100%;
        left: 0;
        background: white;
        border-radius: 8px;
        box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
        padding: 8px;
        z-index: 1000;

        .emoji-grid {
          display: grid;
          grid-template-columns: repeat(8, 1fr);
          gap: 4px;
          max-height: 300px; // 增加高度以容纳更多表情
          overflow-y: auto;
          padding-right: 4px; // 为滚动条留出空间

          .emoji-item {
            border: none;
            background: none;
            font-size: 20px;
            padding: 4px;
            cursor: pointer;
            border-radius: 4px;
            width: 36px;
            height: 36px;
            display: flex;
            align-items: center;
            justify-content: center;

            &:hover {
              background: #f5f7fa;
            }
          }
        }
      }
    }

    .message-input-wrapper {
      display: flex;
      gap: 12px;
      align-items: flex-start;

      .el-input {
        flex: 1;
      }

      .el-button {
        height: 80px;
        width: 100px;
      }
    }
  }
}

.no-chat-selected {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #909399;
  font-size: 16px;
}
</style>
