<template>
  <div class="post-detail-container">
    <div class="post-content">
      <!-- 左侧文章区域 -->
      <div class="article-section">
        <!-- 文章头部信息 -->
        <div class="article-header">
          <div class="user-info">
            <div class="user-profile">
              <el-avatar :size="48" :src="postDetail.user?.avatar">
                {{ postDetail.user?.nickname?.charAt(0) }}
              </el-avatar>
              <div class="user-meta">
                <div class="nickname">{{ postDetail.user?.nickname }}</div>
                <div class="bio">{{ postDetail.user?.bio || 'This user is lazy and hasn\'t written anything~' }}</div>
                <div class="post-meta">
                  <span class="time">{{ postDetail.createTime }}</span>
                  <el-tag :type="getCategoryType(postDetail.category)" size="small" effect="light">
                    {{ translateCategory(postDetail.category) }}
                  </el-tag>
                </div>
              </div>
            </div>
            <div class="actions">
              <el-button
                :type="isFollowed ? 'default' : 'primary'"
                size="small"
                class="follow-btn"
                @click="handleFollow"
                :loading="followLoading"
              >
                {{ isFollowed ? 'Following' : 'Follow' }}
              </el-button>
              <el-dropdown trigger="click" @command="handleCommand">
                <el-button type="info" plain size="small">
                  <el-icon><More /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="report">
                      <el-icon><Warning /></el-icon>
                      Report Post
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </div>
        </div>

        <!-- 文章标题和内容 -->
        <div class="article-content">
          <h1 class="article-title">{{ postDetail.title }}</h1>
          <div class="post-text" v-html="postDetail.content"></div>

          <div class="interaction-bar">
            <div class="stats">
              <div class="stat-item" @click="handleLike">
                <el-icon class="like-icon" :class="{ 'is-liked': isLiked }">
                  <Sunny />
                </el-icon>
                <span>{{ postDetail.likeCount }}</span>
              </div>
              <div class="stat-item">
                <el-icon><ChatDotRound /></el-icon>
                <span>{{ postDetail.replyCount }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧评论区 -->
      <div class="comments-section">
        <div class="comments-container">
          <h3 class="section-title">
            Comments
            <span class="comment-count">({{ postDetail.replyCount }})</span>
          </h3>

          <!-- 评论输入框 -->
          <div class="comment-input">
            <el-input
              v-model="replyContent"
              type="textarea"
              :rows="3"
              placeholder="Share your thoughts..."
              resize="none"
            />
            <div class="input-actions">
              <span v-if="replyTo" class="reply-to">
                Reply to @{{ replyTo.createBy }}
                <el-icon class="cancel-reply" @click="cancelReply"><Close /></el-icon>
              </span>
              <el-button type="primary" @click="submitReply" :loading="submitting">
                Post Comment
              </el-button>
            </div>
          </div>

          <!-- 评论列表 -->
          <div class="comments-list">
            <div v-for="reply in topLevelReplies" :key="reply.id" class="comment-item">
              <el-avatar :size="36" :src="reply.sysUser?.avatar">
                {{ reply.sysUser?.nickname?.charAt(0) }}
              </el-avatar>
              <div class="comment-content">
                <div class="comment-header">
                  <span class="username">{{ reply.sysUser?.nickname }}</span>
                  <span class="time">{{ reply.createTime }}</span>
                </div>
                <div class="comment-text">{{ reply.content }}</div>
                <div class="comment-actions">
                  <el-button type="primary" link @click="showReplyInput(reply)">
                    Reply
                  </el-button>
                </div>

                <!-- 子评论 -->
                <div v-if="getChildReplies(reply.id).length > 0" class="sub-comments">
                  <div v-for="childReply in getChildReplies(reply.id)"
                    :key="childReply.id"
                    class="sub-comment-item"
                  >
                    <el-avatar :size="28" :src="childReply.sysUser?.avatar">
                      {{ childReply.sysUser?.nickname?.charAt(0) }}
                    </el-avatar>
                    <div class="comment-content">
                      <div class="comment-header">
                        <span class="username">{{ childReply.sysUser?.nickname }}</span>
                        <span class="time">{{ childReply.createTime }}</span>
                      </div>
                      <div class="comment-text">
                        <span class="reply-to">@{{ getParentReply(childReply.parentId)?.sysUser?.nickname }}</span>
                        {{ childReply.content }}
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 添加举报对话框 -->
    <el-dialog
      v-model="reportDialog.visible"
      title="Report Post"
      width="500px"
      destroy-on-close
    >
      <el-form :model="reportForm" label-width="100px">
        <el-form-item label="Report Reason" required>
          <el-input
            v-model="reportForm.reason"
            type="textarea"
            :rows="4"
            placeholder="Please enter the reason for reporting..."
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="reportDialog.visible = false">Cancel</el-button>
          <el-button type="primary" @click="submitReport" :loading="reportDialog.loading">
            Submit
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { ChatDotRound, Picture, Close, Sunny, Warning, More } from '@element-plus/icons-vue'
import request from "@/utils/request"
import { ElMessage } from 'element-plus'

const route = useRoute()
const postDetail = ref({})
const replyContent = ref('')
const replyTo = ref(null)
const submitting = ref(false)
const isLiked = ref(false)
const isFollowed = ref(false)
const followLoading = ref(false)

// 举报相关数据
const reportDialog = ref({
  visible: false,
  loading: false
})

const reportForm = ref({
  postId: '',
  reason: ''
})

// 处理下拉菜单命令
const handleCommand = (command) => {
  if (command === 'report') {
    reportForm.value.postId = route.params.id
    reportDialog.value.visible = true
  }
}

// 提交举报
const submitReport = async () => {
  if (!reportForm.value.reason.trim()) {
    ElMessage.warning('Please enter the report reason')
    return
  }

  reportDialog.value.loading = true
  try {
    await request.post('/report/add', reportForm.value)
    ElMessage.success('Report submitted successfully')
    reportDialog.value.visible = false
    reportForm.value.reason = ''
  } catch (error) {
    ElMessage.error('Failed to submit report')
  } finally {
    reportDialog.value.loading = false
  }
}

// 获取帖子详情
const getPostDetail = async () => {
  try {
    const res = await request.get('/post/getById/' + route.params.id)
    postDetail.value = res.data.data
    await checkFollowStatus()
  } catch (error) {
    console.error('获取帖子详情失败:', error)
  }
}

// 获取父回复信息
const getParentReply = (parentId) => {
  return postDetail.value.replies?.find(reply => reply.id === parentId)
}

// 分类翻译函数
const translateCategory = (category) => {
  const translations = {
    '寻物启事': 'Lost & Found',
    '技能咨询': 'Skills & Consulting',
    '资源共享': 'Resource Sharing',
    '学习交流': 'Study Exchange',
    '校园生活': 'Campus Life'
  }
  return translations[category] || category
}

// 获取分类标签类型
const getCategoryType = (category) => {
  const typeMap = {
    'Lost & Found': 'warning',
    'Skills & Consulting': 'success',
    'Resource Sharing': 'primary',
    'Study Exchange': 'info',
    'Campus Life': ''
  }
  return typeMap[translateCategory(category)] || ''
}

// 显示回复输入框
const showReplyInput = (reply) => {
  // 如果是二级回复，不允许继续回复
  if (reply.parentId) {
    ElMessage.warning('Multi-level replies are not supported')
    return
  }
  replyTo.value = reply
  // 滚动到输入框
  document.querySelector('.reply-input').scrollIntoView({ behavior: 'smooth' })
}

// Cancel回复
const cancelReply = () => {
  replyTo.value = null
}

// 提交回复
const submitReply = async () => {
  if (!replyContent.value.trim()) {
    ElMessage.warning('Please enter your reply')
    return
  }

  // 检查是否是二级回复的回复
  if (replyTo.value?.parentId) {
    ElMessage.warning('Multi-level replies are not supported')
    return
  }

  submitting.value = true
  try {
    await request.post('/reply/add', {
      postId: postDetail.value.id,
      content: replyContent.value,
      parentId: replyTo.value?.id || null
    })

    ElMessage.success('Reply posted successfully')
    replyContent.value = ''
    replyTo.value = null
    // 重新获取帖子详情以更新回复列表
    await getPostDetail()
  } catch (error) {
    ElMessage.error('Failed to post reply')
  } finally {
    submitting.value = false
  }
}

// 获顶层回复
const topLevelReplies = computed(() => {
  return postDetail.value.replies?.filter(reply => !reply.parentId) || []
})

// 获取子回复
const getChildReplies = (parentId) => {
  return postDetail.value.replies?.filter(reply => reply.parentId === parentId) || []
}

// 处理点赞
const handleLike = async () => {
  try {
    await request.put('/post/updateById', {
      id: postDetail.value.id,
      likeCount: postDetail.value.likeCount + 1
    })

    isLiked.value = true
    postDetail.value.likeCount++
    ElMessage.success('Liked successfully')
  } catch (error) {
    ElMessage.error('Operation failed')
  }
}

// 检查是否已关注
const checkFollowStatus = async () => {
  try {
    const res = await request.post('/userFollow/followed', {
      followeeId: postDetail.value.user?.id
    })
    isFollowed.value = res.data.data
  } catch (error) {
    console.error('获取关注状态失败:', error)
  }
}

// 处理关注/Cancel关注
const handleFollow = async () => {
  if (!postDetail.value.user?.id) return

  followLoading.value = true
  try {
    await request.post('/userFollow/follow', {
      followeeId: postDetail.value.user.id
    })

    isFollowed.value = !isFollowed.value
    ElMessage.success(isFollowed.value ? 'Followed successfully' : 'Unfollowed successfully')
  } catch (error) {
    ElMessage.error('Operation failed')
  } finally {
    followLoading.value = false
  }
}

onMounted(() => {
  getPostDetail()
})
</script>

<style lang="scss" scoped>
.post-detail-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 24px;
}

.post-content {
  display: flex;
  gap: 24px;
}

.article-section {
  flex: 1;
  min-width: 0; // 防止内容溢出
}

.article-header {
  background: white;
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);

  .user-info {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
  }

  .user-profile {
    display: flex;
    gap: 16px;
  }

  .user-meta {
    .nickname {
      font-size: 18px;
      font-weight: 600;
      color: #2c3e50;
      margin-bottom: 4px;
    }

    .bio {
      color: #64748b;
      font-size: 14px;
      margin-bottom: 8px;
      line-height: 1.4;
    }

    .post-meta {
      display: flex;
      align-items: center;
      gap: 12px;
      color: #94a3b8;
      font-size: 14px;
    }
  }
}

.article-content {
  background: white;
  border-radius: 16px;
  padding: 32px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);

  .article-title {
    font-size: 28px;
    font-weight: 700;
    color: #1a1a1a;
    margin: 0 0 24px;
    line-height: 1.4;
  }

  .post-text {
    font-size: 16px;
    line-height: 1.8;
    color: #333;

    :deep(p) {
      margin-bottom: 1em;
    }

    :deep(img) {
      max-width: 100%;
      height: auto;
      border-radius: 8px;
      margin: 1em 0;
      display: block;
    }

    :deep(a) {
      color: #3498db;
      text-decoration: none;
      transition: color 0.3s ease;

      &:hover {
        color: #2980b9;
        text-decoration: underline;
      }
    }

    :deep(ul), :deep(ol) {
      padding-left: 2em;
      margin: 1em 0;
    }

    :deep(li) {
      margin-bottom: 0.5em;
    }

    :deep(blockquote) {
      margin: 1em 0;
      padding: 1em;
      border-left: 4px solid #e5e7eb;
      background: #f8fafc;
      color: #4b5563;
    }

    :deep(pre) {
      background: #f8fafc;
      padding: 1em;
      border-radius: 8px;
      overflow-x: auto;
      margin: 1em 0;

      code {
        background: transparent;
        padding: 0;
        border-radius: 0;
      }
    }

    :deep(code) {
      background: #f1f5f9;
      padding: 0.2em 0.4em;
      border-radius: 4px;
      font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
      font-size: 0.9em;
      color: #2563eb;
    }

    :deep(table) {
      width: 100%;
      border-collapse: collapse;
      margin: 1em 0;

      th, td {
        border: 1px solid #e5e7eb;
        padding: 0.75em;
        text-align: left;
      }

      th {
        background: #f8fafc;
        font-weight: 600;
      }

      tr:nth-child(even) {
        background: #f8fafc;
      }
    }

    :deep(h1), :deep(h2), :deep(h3), :deep(h4), :deep(h5), :deep(h6) {
      margin: 1.5em 0 0.5em;
      color: #1a1a1a;
      font-weight: 600;
      line-height: 1.4;
    }

    :deep(h1) { font-size: 2em; }
    :deep(h2) { font-size: 1.75em; }
    :deep(h3) { font-size: 1.5em; }
    :deep(h4) { font-size: 1.25em; }
    :deep(h5) { font-size: 1.1em; }
    :deep(h6) { font-size: 1em; }

    :deep(hr) {
      border: none;
      border-top: 1px solid #e5e7eb;
      margin: 2em 0;
    }

    :deep(strong) {
      font-weight: 600;
      color: #1a1a1a;
    }

    :deep(em) {
      font-style: italic;
    }

    :deep(.w-e-text-container) {
      min-height: auto !important;
      height: auto !important;
    }
  }
}

.comments-section {
  width: 400px;
  position: sticky;
  top: 24px;
  height: calc(100vh - 48px);

  .comments-container {
    background: white;
    border-radius: 16px;
    height: 100%;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
    display: flex;
    flex-direction: column;
  }

  .section-title {
    padding: 24px;
    margin: 0;
    border-bottom: 1px solid #e5e7eb;
  }

  .comment-input {
    padding: 24px;
    border-bottom: 1px solid #e5e7eb;
  }

  .comments-list {
    flex: 1;
    overflow-y: auto;
    padding: 0 24px;
  }
}

.interaction-bar {
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid #e5e7eb;

  .stats {
    display: flex;
    gap: 24px;
  }

  .stat-item {
    display: flex;
    align-items: center;
    gap: 8px;
    color: #64748b;
    cursor: pointer;
    transition: all 0.3s ease;

    &:hover {
      color: #3498db;
    }

    .like-icon {
      font-size: 20px;

      &.is-liked {
        color: #f1c40f;
      }
    }
  }
}

.comment-item {
  display: flex;
  gap: 16px;
  padding: 20px 0;
  border-bottom: 1px solid #e5e7eb;

  &:last-child {
    border-bottom: none;
  }
}

.comment-content {
  flex: 1;
}

.comment-header {
  margin-bottom: 8px;

  .username {
    font-weight: 600;
    color: #2c3e50;
  }

  .time {
    font-size: 14px;
    color: #94a3b8;
    margin-left: 12px;
  }
}

.comment-text {
  color: #4b5563;
  line-height: 1.6;
}

.sub-comments {
  margin-top: 16px;
  margin-left: 24px;
  padding-left: 24px;
  border-left: 2px solid #e5e7eb;
}

.sub-comment-item {
  display: flex;
  gap: 12px;
  padding: 16px 0;

  .reply-to {
    color: #3498db;
    margin-right: 8px;
  }
}

.input-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 12px;
}

.reply-to {
  color: #3498db;
  margin-right: 8px;
}

.cancel-reply {
  cursor: pointer;
  transition: all 0.3s ease;

  &:hover {
    color: #3498db;
  }
}

.sidebar {
  width: 300px;
  position: sticky;
  top: 24px;
  height: fit-content;

  .author-card {
    background: white;
    border-radius: 16px;
    overflow: hidden;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);

    .author-header {
      padding: 20px;
      background: linear-gradient(135deg, #3498db, #2980b9);

      h3 {
        color: white;
        margin: 0;
        font-size: 18px;
      }
    }

    .author-info {
      padding: 24px;
      text-align: center;

      h4 {
        margin: 16px 0 8px;
        color: #2c3e50;
        font-size: 16px;
      }

      p {
        color: #64748b;
        font-size: 14px;
        line-height: 1.6;
        margin: 0;
      }
    }
  }
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

:deep(.el-dialog) {
  border-radius: 12px;
  overflow: hidden;

  .el-dialog__header {
    margin: 0;
    padding: 20px 24px;
    border-bottom: 1px solid #e5e7eb;

    .el-dialog__title {
      font-size: 18px;
      font-weight: 600;
      color: #2c3e50;
    }
  }

  .el-dialog__body {
    padding: 24px;
  }

  .el-dialog__footer {
    padding: 16px 24px;
    border-top: 1px solid #e5e7eb;
  }
}
</style>
