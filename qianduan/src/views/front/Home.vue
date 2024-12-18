<template>
  <div class="home-container">
    <!-- 顶部搜索和导航 -->
    <div class="header-section">
      <div class="search-box">
        <el-input
          v-model="searchQuery"
          placeholder="Search posts..."
          clearable
          @clear="handleSearch"
          @keyup.enter="handleSearch"
          @input="handleSearchInput"
          class="search-input"
        >
          <template #prefix>
            <el-icon><Search /></el-icon>
          </template>
        </el-input>
      </div>

      <div class="nav-tabs">
        <el-tabs v-model="activeCategory" @tab-change="handleCategoryChange">
          <el-tab-pane label="All" name="全部" />
          <el-tab-pane v-for="tag in tags.filter(t => t !== '全部')"
            :key="tag"
            :label="tag"
            :name="tag"
          />
        </el-tabs>
      </div>
    </div>

    <!-- 瀑布流帖子列表 -->
    <div class="posts-container">
      <el-skeleton :loading="loading" animated :count="6" :throttle="500">
        <template #default>
          <div class="waterfall-list">
            <div v-for="post in posts" :key="post.id" class="post-card" @click="goToDetail(post.id)">
              <div class="post-image-wrapper">
                <el-image
                  v-if="post.imageUrl"
                  :src="post.imageUrl"
                  class="post-image"
                  fit="cover"
                  loading="lazy"
                  :style="{ aspectRatio: post.imageRatio || '3/4' }"
                >
                  <template #error>
                    <div class="image-placeholder">
                      <el-icon><Picture /></el-icon>
                    </div>
                  </template>
                </el-image>
                <el-tag v-if="post.isHot" class="hot-tag" type="danger" effect="dark" size="small">
                  <el-icon><Star /></el-icon>
                  <span>Hot</span>
                </el-tag>
              </div>

              <div class="post-info">
                <div class="post-header">
                  <el-avatar :size="24" :src="post.user?.avatar"/>
                  <span class="username">{{ post.user?.username }}</span>
                </div>
                <p class="post-text">{{ post.title }}</p>
                <div class="post-footer">
                  <div class="interaction-info">
                    <span class="like-count">
                      <el-icon><Star /></el-icon>
                      {{ post.likeCount }}
                    </span>
                    <span class="comment-count">
                      <el-icon><ChatDotRound /></el-icon>
                      {{ post.replyCount }}
                    </span>
                  </div>
                  <el-tag :type="getRandomTagType(post.id)" size="small">
                    {{ post.category }}
                  </el-tag>
                </div>
              </div>
            </div>
          </div>
        </template>
      </el-skeleton>

      <!-- 底部加载状态 -->
      <div class="load-more" ref="loadingRef">
        <el-empty v-if="posts.length === 0" description="No data available" />
        <div v-else-if="loading" class="loading-text">
          <el-icon class="loading-icon"><Loading /></el-icon>
          Loading...
        </div>
        <div v-else-if="finished" class="finished-text">
          No more posts
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useDebounceFn } from '@vueuse/core'
import { Search, ChatDotRound, Star, View, Loading, Picture } from '@element-plus/icons-vue'
import request from "@/utils/request"
import { useRouter } from 'vue-router'
import { useIntersectionObserver } from '@vueuse/core'

const activeCategory = ref('全部')
const searchQuery = ref('')
const tags = ref([])
const selectedTags = ref([])
const loading = ref(false)
const finished = ref(false)
const loadingRef = ref(null)

const page = ref({
  pageSize: 10,
  pageNum: 1,
  search: '',
  category: '',
  tags: [],
  total: 0
})

const posts = ref([])
const router = useRouter()

// 定义一组清晰的标签类型
const tagTypes = ['success', 'warning', 'danger', 'info', 'primary']

// 根据帖子ID生成随机但固定的标签类型
const getRandomTagType = (postId) => {
  // 使用帖子ID作为种子来确保同一个帖子总是获得相同的颜色
  const index = Math.abs(hashCode(postId.toString())) % tagTypes.length
  return tagTypes[index]
}

// 简单的字符串哈希函数
const hashCode = (str) => {
  let hash = 0
  for (let i = 0; i < str.length; i++) {
    const char = str.charCodeAt(i)
    hash = ((hash << 5) - hash) + char
    hash = hash & hash
  }
  return hash
}

// 获取帖子列表
const fetchPosts = async () => {
  if (loading.value || finished.value) return
  loading.value = true
  try {
    const res = await request.post('/post/pageFront', {
      ...page.value,
      search: searchQuery.value,
      category: activeCategory.value === '全部' ? '' : activeCategory.value,
      tags: selectedTags.value
    })
    const newPosts = res.data.data.records.map(post => {
      if (post.imageUrl && !post.imageRatio) {
        post.imageRatio = '3/4'
      }
      return post
    })
    posts.value = page.value.pageNum === 1 ? newPosts : [...posts.value, ...newPosts]
    page.value.total = res.data.data.total
    // 判断是否加载完所有数据
    if (posts.value.length >= res.data.data.total) {
      finished.value = true
    }
    page.value.pageNum++
  } catch (error) {
    console.error('获取帖子列表失败:', error)
  } finally {
    loading.value = false
  }
}

// 获取标签列表
const fetchTags = async () => {
  try {
    const res = await request.get('/post/postTag')
    tags.value = ['全部', ...res.data.data]
  } catch (error) {
    console.error('获取标签列表失败:', error)
  }
}

// 切换分类
const handleCategoryChange = (category) => {
  activeCategory.value = category
  page.value.pageNum = 1
  finished.value = false
  fetchPosts()
}

// 处理标签选择
const handleTagChange = () => {
  page.value.pageNum = 1
  fetchPosts()
}

// 搜索
const handleSearch = () => {
  page.value.pageNum = 1
  finished.value = false
  fetchPosts()
}

// 获取分类标签类型
const getCategoryType = (category) => {
  const typeMap = {
    '寻物启事': 'warning',
    '技能咨询': 'success',
    '资源共享': 'primary',
    '学习交流': 'info',
    '校园生活': ''
  }
  return typeMap[category] || ''
}

// 设置滚动加载
useIntersectionObserver(
  loadingRef,
  ([{ isIntersecting }]) => {
    if (isIntersecting) {
      fetchPosts()
    }
  },
  { threshold: 0 }
)

// 添加跳转到详情页方法
const goToDetail = (postId) => {
  router.push(`/front/post/${postId}`)
}

// 使用防抖包装搜索函数
const debouncedSearch = useDebounceFn(() => {
  page.value.pageNum = 1
  finished.value = false
  fetchPosts()
}, 300)

// 监听输入变化
const handleSearchInput = () => {
  debouncedSearch()
}

onMounted(() => {
  fetchTags()
  fetchPosts()
})
</script>

<style lang="scss" scoped>
.home-container {
  max-width: 1440px;
  margin: 0 auto;
  padding: 20px;
}

.header-section {
  position: sticky;
  top: 0;
  z-index: 100;
  background: white;
  padding: 16px 0;
  margin-bottom: 24px;
  border-bottom: 1px solid #f1f1f1;

  .search-box {
    max-width: 360px;
    margin: 0 auto 16px;

    .search-input {
      :deep(.el-input__wrapper) {
        border-radius: 4px;
        box-shadow: none;
        background: #f6f6f6;
        padding-right: 0;
        height: 32px;

        &:hover {
          background: #f6f6f6;
        }

        &.is-focus {
          background: #f6f6f6;
        }
      }

      :deep(.el-input__inner) {
        height: 32px;
        line-height: 32px;
        background: transparent;
        font-size: 14px;
      }

      :deep(.el-input__prefix) {
        left: 12px;
        color: #666;

        .el-icon {
          font-size: 16px;
        }
      }
    }
  }

  .nav-tabs {
    :deep(.el-tabs__header) {
      margin: 0;
    }

    :deep(.el-tabs__nav-wrap::after) {
      display: none;
    }

    :deep(.el-tabs__active-bar) {
      background-color: #ff2442;
    }

    :deep(.el-tabs__item) {
      font-size: 16px;
      padding: 0 24px;

      &.is-active {
        color: #ff2442;
        font-weight: 600;
      }

      &:hover {
        color: #ff2442;
      }
    }
  }
}

.waterfall-list {
  column-count: 5;
  column-gap: 16px;
  margin: 0 auto;
  padding: 16px;

  @media screen and (max-width: 1600px) {
    column-count: 4;
  }
  @media screen and (max-width: 1200px) {
    column-count: 3;
  }
  @media screen and (max-width: 768px) {
    column-count: 2;
  }
}

.post-card {
  break-inside: avoid;
  margin-bottom: 16px;
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  cursor: pointer;

  .post-image-wrapper {
    position: relative;
    width: 100%;

    .post-image {
      width: 100%;
      display: block;
      vertical-align: middle;
    }

    .image-placeholder {
      width: 100%;
      aspect-ratio: 3/4;
      display: flex;
      align-items: center;
      justify-content: center;
      background: #f5f5f5;
      color: #909399;
    }

    .hot-tag {
      position: absolute;
      top: 12px;
      right: 12px;
      z-index: 1;
    }
  }

  .post-info {
    padding: 12px;

    .post-header {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 8px;

      .username {
        font-size: 14px;
        color: #333;
        font-weight: 500;
      }
    }

    .post-text {
      font-size: 14px;
      color: #666;
      margin-bottom: 12px;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }

    .post-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .interaction-info {
        display: flex;
        gap: 12px;
        color: #999;
        font-size: 12px;

        span {
          display: flex;
          align-items: center;
          gap: 4px;
        }
      }
    }
  }
}

.load-more {
  margin-top: 32px;
  display: flex;
  justify-content: center;
  padding: 20px 0;

  .loading-text,
  .finished-text {
    color: #909399;
    font-size: 14px;
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .loading-icon {
    animation: rotating 2s linear infinite;
  }
}

@keyframes rotating {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

// 优化标签样式，使颜色更清晰
:deep(.el-tag) {
  font-weight: 500;

  &.el-tag--success {
    background-color: rgba(103, 194, 58, 0.15);
    border-color: rgba(103, 194, 58, 0.3);
    color: #67c23a;
  }

  &.el-tag--warning {
    background-color: rgba(230, 162, 60, 0.15);
    border-color: rgba(230, 162, 60, 0.3);
    color: #e6a23c;
  }

  &.el-tag--danger {
    background-color: rgba(245, 108, 108, 0.15);
    border-color: rgba(245, 108, 108, 0.3);
    color: #f56c6c;
  }

  &.el-tag--info {
    background-color: rgba(144, 147, 153, 0.15);
    border-color: rgba(144, 147, 153, 0.3);
    color: #909399;
  }

  &.el-tag--primary {
    background-color: rgba(64, 158, 255, 0.15);
    border-color: rgba(64, 158, 255, 0.3);
    color: #409eff;
  }
}

// 架屏样式优化
:deep(.el-skeleton) {
  .el-skeleton__item {
    border-radius: 12px;
  }
}
</style>
