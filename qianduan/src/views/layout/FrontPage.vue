<template>
  <el-container class="main-container">
    <el-header class="header">
      <el-affix>
        <el-row :justify="'space-between'" class="header-row">
          <el-col :span="22">
            <el-menu
                class="my-menu"
                mode="horizontal"
                :ellipsis="false"
                router
                :default-active="$route.path"
            >
              <div class="menu-brand">
                <el-icon class="blog-logo"><House /></el-icon>
                <span class="blog-title">Campus Social Platform</span>
              </div>
              <el-menu-item index="/front/Home">
                <el-icon><HomeFilled /></el-icon>
                Home
              </el-menu-item>


              <el-menu-item index="/front/PostSend">
                <el-icon><EditPen /></el-icon>
                Send Post
              </el-menu-item>

              <el-menu-item index="/front/OnlineChat">
                <el-icon><Promotion /></el-icon>
                OnlineChat
              </el-menu-item>

              <el-menu-item index="/front/Profile">
                <el-icon><UserFilled /></el-icon>
                Profile
              </el-menu-item>


            </el-menu>
          </el-col>
          <el-col :span="2" class="user-profile">
            <el-dropdown trigger="click">
              <div class="avatar-with-name">
                <el-avatar
                  :size="45"
                  :src="userStore.user.authentication.principal.avatar"
                  class="user-avatar"
                ></el-avatar>
                <span class="username">{{ userStore.user.authentication.principal.username }}</span>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="logout">
                    <el-icon><SwitchButton /></el-icon>Logout
                  </el-dropdown-item>
                  <el-dropdown-item @click="endPage" v-if="authShow(['ADMIN'])">
                    <el-icon><Setting /></el-icon>Admin Panel
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </el-col>
        </el-row>
      </el-affix>
    </el-header>
    <el-main class="main-content">
      <router-view/>
    </el-main>
  </el-container>
</template>

<script setup>
import router from "@/router";
import {useUserStore} from "@/stores/userInfo";
import {onMounted} from 'vue';
import {authShow} from "@/utils/auth";
import {
  Calendar,
  ChatDotSquare,
  HomeFilled,
  House,
  Notebook,
  Setting,
  Shop,
  SwitchButton,
  User,
  EditPen,
  UserFilled, Promotion
} from '@element-plus/icons-vue'

const userStore = useUserStore();

const endPage = () => {
  router.push({ path: '/end' });
};

const logout = () => {
  router.push({ path: '/login' });
  userStore.removeUser()
}

onMounted(() => {
  // 初始化操作
});
</script>

<style scoped lang="scss">
.main-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f7fa;
}

.header {
  padding: 0;
  box-shadow: 0 2px 12px rgba(101, 71, 148, 0.15);
}

.header-row {
  background: linear-gradient(135deg, #d4e6fd 0%, #654794 100%);
}

.my-menu {
  border: none;
  background: transparent;
  height: 60px;
  line-height: 60px;

  .menu-brand {
    float: left;
    padding: 0 20px;
    display: flex;
    align-items: center;
    gap: 10px;

    .blog-logo {
      font-size: 28px;
      color: #2c3e50;
      margin-top: 2px;
      filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.08));
    }

    .blog-title {
      font-size: 20px;
      font-weight: 600;
      color: #2c3e50;
      text-shadow: 0 2px 4px rgba(255, 255, 255, 0.2);
    }
  }
}

.el-menu--horizontal > .el-menu-item {
  height: 60px;
  line-height: 60px;
  padding: 0 25px;
  font-size: 16px;
  color: #2c3e50;
  font-weight: 500;
  transition: all 0.3s ease;

  &:hover {
    background-color: rgba(255, 255, 255, 0.2);
    color: #654794;
  }

  &.is-active {
    background-color: rgba(255, 255, 255, 0.25);
    color: #654794;
    font-weight: 600;
    box-shadow: inset 0 -3px 0 #654794;
  }

  .el-icon {
    margin-right: 8px;
    font-size: 18px;
    color: inherit;
  }
}

.user-profile {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 20px;
}

.avatar-with-name {
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 5px;
  border-radius: 25px;
  transition: all 0.3s ease;

  &:hover {
    background-color: rgba(255, 255, 255, 0.2);
  }
}

.user-avatar {
  border: 2px solid rgba(255, 255, 255, 0.9);
  transition: transform 0.3s ease;
  box-shadow: 0 2px 8px rgba(101, 71, 148, 0.15);

  &:hover {
    transform: scale(1.05);
  }
}

.username {
  font-size: 14px;
  color: #2c3e50;
  font-weight: 500;
  text-shadow: 0 1px 2px rgba(255, 255, 255, 0.2);
}

.main-content {
  flex: 1;
  padding: 24px;
}

// 路由过渡动画
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

:deep(.el-dropdown-menu__item) {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;

  .el-icon {
    font-size: 16px;
    color: #654794;
  }

  &:hover {
    color: #654794;
    background-color: rgba(212, 230, 253, 0.1);
  }
}
</style>
