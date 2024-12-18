import { defineStore } from 'pinia'
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

export const useChatStore = defineStore('chat', () => {
  const ws = ref(null)
  const onlineUsers = ref([])
  const chatMessages = ref({})
  const unreadMessages = ref({})
  const currentUser = ref(null)

  // Initialize WebSocket connection
  const initWebSocket = (user) => {
    if (ws.value?.readyState === WebSocket.OPEN) {
      return // Already connected
    }

    currentUser.value = user
    ws.value = new WebSocket('ws://47.120.13.131:9001/websocket')

    ws.value.onopen = () => {
      console.log('WebSocket connected')
      ws.value.send(JSON.stringify({
        type: 'connect',
        username: currentUser.value.username,
        avatar: currentUser.value.avatar
      }))
    }

    ws.value.onmessage = (event) => {
      const data = JSON.parse(event.data)

      if (data.type === 'userList') {
        onlineUsers.value = data.users
        return
      }

      // Handle regular messages
      if (!chatMessages.value[data.from]) {
        chatMessages.value[data.from] = []
      }
      chatMessages.value[data.from].push({
        content: data.content,
        from: data.from,
        timestamp: new Date().toLocaleTimeString(),
        avatar: onlineUsers.value.find(u => u.username === data.from)?.avatar
      })

      // 修改未读消息计数逻辑
      const isInChatPage = window.location.pathname.includes('OnlineChat')
      const isCurrentChat = window.activeChatUser === data.from
      
      // 如果不在聊天页面，或者在聊天页面但不是当前聊天的用户发来的消息
      if (!isInChatPage || (isInChatPage && !isCurrentChat)) {
        if (!unreadMessages.value[data.from]) {
          unreadMessages.value[data.from] = 0
        }
        unreadMessages.value[data.from]++
      }

      saveToLocalStorage()
    }

    ws.value.onclose = () => {
      console.log('WebSocket disconnected')
      // Try to reconnect after 5 seconds
      setTimeout(() => {
        if (currentUser.value) {
          initWebSocket(currentUser.value)
        }
      }, 5000)
    }
  }

  // Send message
  const sendMessage = (to, content) => {
    if (!ws.value || ws.value.readyState !== WebSocket.OPEN) {
      ElMessage.error('Connection lost, trying to reconnect...')
      return
    }

    const messageData = {
      type: 'message',
      to: to,
      from: currentUser.value.username,
      content: content,
      timestamp: new Date().toLocaleTimeString()
    }

    ws.value.send(JSON.stringify(messageData))

    if (!chatMessages.value[to]) {
      chatMessages.value[to] = []
    }

    chatMessages.value[to].push({
      ...messageData,
      isSelf: true,
      avatar: currentUser.value.avatar
    })

    saveToLocalStorage()
  }

  // Load chat history from localStorage
  const loadFromLocalStorage = () => {
    const saved = localStorage.getItem('chatMessages')
    const savedUnread = localStorage.getItem('unreadMessages')
    if (saved) {
      chatMessages.value = JSON.parse(saved)
    }
    if (savedUnread) {
      unreadMessages.value = JSON.parse(savedUnread)
    }
  }

  // Save chat history to localStorage
  const saveToLocalStorage = () => {
    localStorage.setItem('chatMessages', JSON.stringify(chatMessages.value))
    localStorage.setItem('unreadMessages', JSON.stringify(unreadMessages.value))
  }

  return {
    ws,
    onlineUsers,
    chatMessages,
    unreadMessages,
    currentUser,
    initWebSocket,
    sendMessage,
    loadFromLocalStorage
  }
})
