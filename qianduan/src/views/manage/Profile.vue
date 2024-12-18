<script setup>
import {onMounted, ref} from 'vue'
import {curryUser} from '@/api/api'
import {ElMessage} from 'element-plus'
import {
  Plus,
  Edit,
  Check,
  Document,
  ChatDotRound,
  UserFilled,
  Star,
  User,
  Message,
  InfoFilled
} from '@element-plus/icons-vue'
import request from "@/utils/request";

const userInfo = ref({
  username: '',
  password: '',
  email: '',
  nickname: '',
  avatar: '',
  bio: '',
  role: '',
  // 不可修改的统计数据
  postCount: 0,
  replyCount: 0,
  followerCount: 0,
  followingCount: 0
})

const loading = ref(false)
const isEditing = ref(false)

// 获取用户信息
const getUserInfo = async () => {
  try {
    loading.value = true
    const res = await curryUser()
    if (res.data.code === 200) {
      userInfo.value = res.data.data
    }
  } catch (error) {
    ElMessage.error('Failed to get user information')
  } finally {
    loading.value = false
  }
}

// 处理头像上传成功
const handleAvatarSuccess = (response) => {
  userInfo.value.avatar = response[0].url
}

// 保存用户信息
const saveUserInfo = () => {
  loading.value = true
  request.put('/sysUser/updateById', {...userInfo.value})
      .then(() => {
        isEditing.value = false
        getUserInfo()
        ElMessage.success('Saved successfully')
      })
      .catch(() => {
        ElMessage.error('Save failed')
      })
      .finally(() => {
        loading.value = false
      })
}

onMounted(() => {
  getUserInfo()
})
</script>

<template>
  <div class="profile-container">
    <el-card class="profile-card" :body-style="{ padding: '0' }">
      <!-- 顶部背景区域 -->
      <div class="profile-header">
        <div class="header-content">
          <h2>Personal Information</h2>
          <div class="header-actions">
            <el-button v-if="!isEditing" type="primary" round @click="isEditing = true">
              <el-icon>
                <Edit/>
              </el-icon>
              Edit Profile
            </el-button>
            <template v-else>
              <el-button round @click="isEditing = false">Cancel</el-button>
              <el-button type="primary" round @click="saveUserInfo">
                <el-icon>
                  <Check/>
                </el-icon>
                Save
              </el-button>
            </template>
          </div>
        </div>
      </div>

      <el-skeleton :loading="loading" animated>
        <template #default>
          <div class="profile-content">
            <!-- 左侧信息区 -->
            <div class="left-section">
              <div class="avatar-section">
                <el-upload
                    v-if="isEditing"
                    class="avatar-uploader"
                    action="/api/file/upload"
                    :data="{fileTypeEnum:'FILE'}"
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess"
                    name="files"
                >
                  <div class="avatar-wrapper">
                    <el-image
                        v-if="userInfo.avatar"
                        :src="userInfo.avatar"
                        class="avatar-image"
                        fit="cover"
                    />
                    <div v-else class="upload-placeholder">
                      <el-icon class="upload-icon">
                        <Plus/>
                      </el-icon>
                      <span>Click to upload</span>
                    </div>
                  </div>
                </el-upload>
                <el-image
                    v-else
                    :src="userInfo.avatar"
                    class="avatar-display"
                    fit="cover"
                />
                <h3 class="username">{{ userInfo.nickname || userInfo.username }}</h3>
                <p class="role-tag">
                  {{ userInfo.role === 'ADMIN' ? 'Admin' : 'User' }}
                </p>
              </div>

              <!-- 统计卡片 -->
              <div class="stats-grid">
                <div class="stat-card">
                  <div class="stat-icon posts">
                    <el-icon>
                      <Document/>
                    </el-icon>
                  </div>
                  <div class="stat-info">
                    <div class="stat-value">{{ userInfo.postCount }}</div>
                    <div class="stat-label">Posts</div>
                  </div>
                </div>
                <div class="stat-card">
                  <div class="stat-icon replies">
                    <el-icon>
                      <ChatDotRound/>
                    </el-icon>
                  </div>
                  <div class="stat-info">
                    <div class="stat-value">{{ userInfo.replyCount }}</div>
                    <div class="stat-label">Replies</div>
                  </div>
                </div>
                <div class="stat-card">
                  <div class="stat-icon followers">
                    <el-icon>
                      <UserFilled/>
                    </el-icon>
                  </div>
                  <div class="stat-info">
                    <div class="stat-value">{{ userInfo.followerCount }}</div>
                    <div class="stat-label">Followers</div>
                  </div>
                </div>
                <div class="stat-card">
                  <div class="stat-icon following">
                    <el-icon>
                      <Star/>
                    </el-icon>
                  </div>
                  <div class="stat-info">
                    <div class="stat-value">{{ userInfo.followingCount }}</div>
                    <div class="stat-label">Following</div>
                  </div>
                </div>
              </div>
            </div>

            <!-- 右侧表单区 -->
            <div class="right-section">
              <el-form label-width="80px" label-position="left" class="info-form">
                <el-form-item label="Username">
                  <div class="info-item">
                    <el-icon>
                      <User/>
                    </el-icon>
                    <span>{{ userInfo.username }}</span>
                  </div>
                </el-form-item>

                <el-form-item label="Nickname">
                  <template v-if="isEditing">
                    <el-input
                        v-model="userInfo.nickname"
                        placeholder="Enter nickname"
                    />
                  </template>
                  <div v-else class="info-item">
                    <el-icon>
                      <UserFilled/>
                    </el-icon>
                    <span>{{ userInfo.nickname }}</span>
                  </div>
                </el-form-item>

                <el-form-item label="Email">
                  <template v-if="isEditing">
                    <el-input
                        v-model="userInfo.email"
                        placeholder="Enter email"
                    />
                  </template>
                  <div v-else class="info-item">
                    <el-icon>
                      <Message/>
                    </el-icon>
                    <span>{{ userInfo.email }}</span>
                  </div>
                </el-form-item>

                <el-form-item label="Bio">
                  <template v-if="isEditing">
                    <el-input
                        v-model="userInfo.bio"
                        type="textarea"
                        :rows="4"
                        placeholder="Write something about yourself..."
                    />
                  </template>
                  <div v-else class="info-item bio">
                    <el-icon>
                      <InfoFilled/>
                    </el-icon>
                    <span>{{ userInfo.bio || 'No introduction yet' }}</span>
                  </div>
                </el-form-item>

                <el-form-item label="Password" v-if="isEditing">
                  <el-input
                      v-model="userInfo.password"
                      type="password"
                      show-password
                      placeholder="Leave blank if no change"
                  />
                </el-form-item>
              </el-form>
            </div>
          </div>
        </template>
      </el-skeleton>
    </el-card>
  </div>
</template>

<style scoped lang="scss">
.profile-container {
  padding: 24px;
  background: #f8fafc;
  min-height: calc(100vh - 60px);
}

.profile-card {
  max-width: 1000px;
  margin: 0 auto;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.profile-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 30px;
  color: white;

  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
    max-width: 1000px;
    margin: 0 auto;

    h2 {
      font-size: 24px;
      font-weight: 600;
      margin: 0;
    }
  }

  .header-actions {
    display: flex;
    gap: 12px;

    .el-button {
      display: flex;
      align-items: center;
      gap: 6px;
    }
  }
}

.profile-content {
  display: grid;
  grid-template-columns: 320px 1fr;
  gap: 30px;
  padding: 30px;
  background: white;
}

.left-section {
  .avatar-section {
    text-align: center;
    padding: 20px;
    background: #f8fafc;
    border-radius: 16px;
    border: 1px solid #e2e8f0;
    margin-bottom: 24px;
  }

  .avatar-wrapper, .avatar-display {
    width: 140px;
    height: 140px;
    border-radius: 50%;
    overflow: hidden;
    margin: 0 auto;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  }

  .username {
    font-size: 20px;
    font-weight: 600;
    color: #1e293b;
    margin: 16px 0 8px;
  }

  .role-tag {
    display: inline-block;
    padding: 6px 16px;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    border-radius: 20px;
    font-size: 14px;
    font-weight: 500;
  }
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.stat-card {
  background: white;
  border-radius: 16px;
  padding: 16px;
  display: flex;
  align-items: center;
  gap: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  transition: transform 0.3s ease;

  &:hover {
    transform: translateY(-2px);
  }

  .stat-icon {
    width: 40px;
    height: 40px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 20px;
    color: white;

    &.posts {
      background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
    }

    &.replies {
      background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
    }

    &.followers {
      background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
    }

    &.following {
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    }
  }

  .stat-info {
    flex: 1;

    .stat-value {
      font-size: 20px;
      font-weight: 600;
      color: #1e293b;
      line-height: 1;
    }

    .stat-label {
      font-size: 12px;
      color: #64748b;
      margin-top: 4px;
    }
  }
}

.right-section {
  background: white;
  border-radius: 16px;
  padding: 24px;

  .info-form {
    max-width: 500px;
  }

  .info-item {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 8px 12px;
    background: #f8fafc;
    border-radius: 8px;

    .el-icon {
      font-size: 18px;
      color: #64748b;
    }

    span {
      color: #1e293b;
      font-size: 14px;
    }

    &.bio {
      align-items: flex-start;
      padding: 12px;

      .el-icon {
        margin-top: 2px;
      }
    }
  }
}

:deep(.el-input__wrapper) {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  border-radius: 8px;
  padding: 8px 12px;
}

:deep(.el-textarea__inner) {
  border-radius: 8px;
  padding: 12px;
}
</style>
