<script setup>
import { ref, onMounted } from "vue"
import { captcha, login, verification } from "@/api/api"
import router from "@/router"
import { useUserStore } from "@/stores/userInfo"
import {
  User,
  Lock,
  ChatLineRound,
  Connection,
  Share,
  Document
} from "@element-plus/icons-vue"

const userStore = useUserStore()
const loginDTO = ref({
  username: '',
  password: '',
  role: 'USER'
})

const captchaInfo = ref({
  userCode: ''
})

const initCaptcha = () => {
  captcha().then((resp) => {
    captchaInfo.value = resp.data.data
  })
}

const loginHandle = () => {
  verification(captchaInfo.value)
    .then((resp) => {
      if (resp.data.code === 200) {
        login(loginDTO.value)
          .then((resp) => {
            if (resp.data.code === 200) {
              userStore.setUser(resp.data.data)
              if (resp.data.data.authentication.principal.role !== 'ADMIN') {
                router.push({path: '/front'})
              } else {
                router.push({path: '/end'})
              }
            }
          })
      }
    })
}

onMounted(() => {
  initCaptcha()
})
</script>

<template>
  <div class="login-container">
    <div class="login-box">
      <!-- 左侧信息 -->
      <div class="info-section">
        <h1 class="title">Fzu Helper</h1>
        <p class="subtitle">This is a platform for mutual assistance and communication among college students</p>
        
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

      <!-- 右侧登录表单 -->
      <div class="form-section">
        <h2>Welcome back!</h2>
        <p class="form-subtitle">Log in to your account</p>

        <el-form class="login-form">
          <el-form-item>
            <el-input 
              v-model="loginDTO.username" 
              placeholder="account"
              :prefix-icon="User"
            />
          </el-form-item>

          <el-form-item>
            <el-input 
              v-model="loginDTO.password" 
              type="password" 
              placeholder="password"
              show-password
              :prefix-icon="Lock"
            />
          </el-form-item>

          <el-form-item>
            <div class="captcha-container">
              <el-input 
                v-model="captchaInfo.userCode"
                placeholder="Verification Code"
              />
              <img
                :src="captchaInfo.code"
                class="captcha-img"
                @click="initCaptcha"
                title="Click to refresh"
              >
            </div>
          </el-form-item>

          <div class="role-select">
            <el-radio-group v-model="loginDTO.role">
              <el-radio label="USER">user</el-radio>
              <el-radio label="ADMIN">admin</el-radio>
            </el-radio-group>
          </div>

          <div class="button-container">
            <el-button 
              type="primary" 
              class="login-button" 
              @click="loginHandle"
            >
              Sign in
            </el-button>
            <el-button 
              class="register-button" 
              @click="$router.push('/Register')"
            >
              Register
            </el-button>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f8f9fa;
}

.login-box {
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

  .login-form {
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

    .captcha-container {
      display: flex;
      gap: 10px;

      .captcha-img {
        height: 40px;
        border-radius: 8px;
        cursor: pointer;
      }
    }
  }

  .role-select {
    margin-bottom: 24px;
  }

  .button-container {
    display: flex;
    flex-direction: column;
    gap: 12px;

    .el-button {
      width: 100%;
      height: 40px;
      border-radius: 8px;
      font-size: 16px;
    }

    .login-button {
      background: linear-gradient(135deg, #7571f9 0%, #9c91f9 100%);
      border: none;

      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 12px rgba(117, 113, 249, 0.3);
      }
    }

    .register-button {
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
