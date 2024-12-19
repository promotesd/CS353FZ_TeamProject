<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  User,
  UserFilled,
  Lock,
  Message,
  Document,
  Connection,
  Share,
  Plus
} from '@element-plus/icons-vue'
import request from '@/utils/request'

const router = useRouter()
const form = ref({
  username: '',
  password: '',
  email: '',
  nickname: '',
  avatar: '',
  bio: '',
  role:'USER'
})

const handleAvatarSuccess = (response) => {
  form.value.avatar = response[0].url
}

const register = () => {
  request.post('/system/register', form.value).then(() => {
    router.push('/login')
  })
}
</script>

<template>
  <div class="register-container">
    <div class="register-box">
      <!-- 左侧信息 -->
      <div class="info-section">
        <h1 class="title">Join Us</h1>
        <p class="subtitle">Start your learning and social journey here</p>

        <div class="features">
          <div class="feature-item">
            <el-icon><Document /></el-icon>
            <span>Knowledge sharing</span>
          </div>
          <div class="feature-item">
            <el-icon><Connection /></el-icon>
            <span>collaborative learning</span>
          </div>
          <div class="feature-item">
            <el-icon><Share /></el-icon>
            <span>resource sharing</span>
          </div>
        </div>
      </div>

      <!-- 右侧注册表单 -->
      <div class="form-section">
        <h2>Create an account!</h2>
        <p class="form-subtitle">Fill in the information to complete registration</p>

        <el-form class="register-form">
          <!-- 头像上传 -->
          <el-form-item>
            <div class="avatar-upload">
              <el-upload
                class="avatar-uploader"
                action="/api/file/upload"
                :data="{fileTypeEnum:'FILE'}"
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                name="files"
              >
                <div class="upload-box">
                  <el-avatar v-if="form.avatar" :size="80" :src="form.avatar" />
                  <div v-else class="upload-placeholder">
                    <el-icon><Plus /></el-icon>
                    <span>upload</span>
                  </div>
                </div>
              </el-upload>
            </div>
          </el-form-item>

          <el-form-item>
            <el-input
              v-model="form.username"
              placeholder="account"
              :prefix-icon="User"
            />
          </el-form-item>

          <el-form-item>
            <el-input
              v-model="form.nickname"
              placeholder="user name"
              :prefix-icon="UserFilled"
            />
          </el-form-item>

          <el-form-item>
            <el-input
              v-model="form.password"
              type="password"
              placeholder="password"
              show-password
              :prefix-icon="Lock"
            />
          </el-form-item>

          <el-form-item>
            <el-input
              v-model="form.email"
              placeholder="email"
              :prefix-icon="Message"
            />
          </el-form-item>

          <el-form-item>
            <el-input
              v-model="form.bio"
              type="textarea"
              :rows="3"
              placeholder="A brief introduction..."
              resize="none"
            />
          </el-form-item>

          <div class="button-container">
            <el-button
              type="primary"
              class="sign-in-button"
              @click="register"
            >
              Register
            </el-button>
            <el-button
              class="return-button"
              @click="$router.push('/login')"
            >
              Return
            </el-button>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f8f9fa;
}

.register-box {
  display: flex;
  width: 900px;
  background: white;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.info-section {
  flex: 1;
  padding: 40px;
  background: linear-gradient(135deg, #7571f9 0%, #9c91f9 100%);
  color: white;

  .title {
    font-size: 36px;
    font-weight: 600;
    margin-bottom: 16px;
  }

  .subtitle {
    font-size: 16px;
    opacity: 0.9;
    margin-bottom: 40px;
    line-height: 1.6;
  }

  .features {
    .feature-item {
      display: flex;
      align-items: center;
      gap: 12px;
      margin-bottom: 20px;
      font-size: 16px;

      .el-icon {
        font-size: 20px;
      }
    }
  }
}

.form-section {
  flex: 1;
  padding: 40px;

  h2 {
    font-size: 28px;
    color: #333;
    margin-bottom: 8px;
  }

  .form-subtitle {
    color: #666;
    margin-bottom: 32px;
  }

  .register-form {
    .avatar-upload {
      display: flex;
      justify-content: center;
      margin-bottom: 24px;

      .avatar-uploader {
        :deep(.el-upload) {
          display: block;
          cursor: pointer;
        }

        .upload-box {
          width: 100px;
          height: 100px;
          border-radius: 50%;
          overflow: hidden;
          border: 2px dashed #dcdfe6;
          transition: all 0.3s;

          &:hover {
            border-color: #7571f9;

            .upload-placeholder {
              color: #7571f9;
            }
          }

          .el-avatar {
            width: 100%;
            height: 100%;
            object-fit: cover;
          }

          .upload-placeholder {
            width: 100%;
            height: 100%;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            color: #909399;
            transition: all 0.3s;

            .el-icon {
              font-size: 28px;
              margin-bottom: 4px;
            }

            span {
              font-size: 12px;
            }
          }
        }
      }
    }

    .el-form-item {
      margin-bottom: 20px;
    }

    :deep(.el-input__wrapper) {
      box-shadow: none;
      border: 1px solid #dcdfe6;
      border-radius: 8px;
      padding: 8px 12px;

      &.is-focus {
        border-color: #7571f9;
        box-shadow: 0 0 0 2px rgba(117, 113, 249, 0.1);
      }
    }

    :deep(.el-textarea__inner) {
      border-radius: 8px;
      padding: 12px;
      border: 1px solid #dcdfe6;
      box-shadow: none;

      &:focus {
        border-color: #7571f9;
        box-shadow: 0 0 0 2px rgba(117, 113, 249, 0.1);
      }
    }
  }

  .button-container {
    display: flex;
    flex-direction: column;
    gap: 12px;
    margin-top: 24px;

    .el-button {
      width: 100%;
      height: 40px;
      border-radius: 8px;
      font-size: 16px;
    }

    .sign-in-button {
      background: linear-gradient(135deg, #7571f9 0%, #9c91f9 100%);
      border: none;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(117, 113, 249, 0.3);
      }
    }

    .return-button {
      background: #f8f9fa;
      border: none;
      color: #666;

      &:hover {
        background: #f1f3f5;
        color: #7571f9;
      }
    }
  }
}
</style>
