<script setup>
import {
  HomeFilled,
  UserFilled,
  Document,
  ChatDotRound,
  Warning,
  Folder,
  SwitchButton,
  Setting,
  School,
  User,
  House,
  OfficeBuilding
} from "@element-plus/icons-vue";
import {useUserStore} from "@/stores/userInfo";
import router from "@/router";
import {logout} from "@/api/api";
import {authShow} from "@/utils/auth";

const userStore = useUserStore()
const logoutHandle = () => {
  logout().then(() => {
    userStore.removeUser()
    window.location = "/"
  })
}
const frontHandle = () => {
  router.push({path: '/front'})
}
</script>

<template>
  <el-container class="main-container">
    <el-aside width="250px" class="sidebar">
      <div class="logo">
        <div class="logo-content">
          <el-icon><OfficeBuilding /></el-icon>
          <h2>Fzu Helper</h2>
        </div>
      </div>
      <el-menu
          class="my-menu"
          active-text-color="#ffffff"
          background-color="#1e1e2d"
          text-color="#a2a3b7"
          router
          :default-active="$route.path"
      >
        <el-menu-item index="/end/Profile" class="menu-item">
          <el-icon><User /></el-icon>
          <span>Personal Center</span>
        </el-menu-item>
        <el-menu-item v-if="authShow(['ADMIN'])" index="/end/UserManage" class="menu-item">
          <el-icon><UserFilled /></el-icon>
          <span>User Management</span>
        </el-menu-item>
        <el-menu-item v-if="authShow(['ADMIN'])" index="/end/Post" class="menu-item">
          <el-icon><Document /></el-icon>
          <span>Post Management</span>
        </el-menu-item>
        <el-menu-item v-if="authShow(['ADMIN'])" index="/end/Reply" class="menu-item">
          <el-icon><ChatDotRound /></el-icon>
          <span>Reply Management</span>
        </el-menu-item>
        <el-menu-item v-if="authShow(['ADMIN'])" index="/end/Report" class="menu-item">
          <el-icon><Warning /></el-icon>
          <span>Report Management</span>
        </el-menu-item>

        <div class="menu-divider"></div>

        <el-menu-item index="/front/Home" class="menu-item">
          <el-icon><House /></el-icon>
          <span>Home Page</span>
        </el-menu-item>
        <el-menu-item index="#" @click="logoutHandle" class="menu-item">
          <el-icon><SwitchButton /></el-icon>
          <span>Log Out</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <el-row justify="end" align="middle" class="header-row">
          <el-col :span="16" class="breadcrumb-col">
            <div class="page-title">{{ $route.meta.name || 'Console' }}</div>
          </el-col>
          <el-col :span="8" class="user-col">
            <div class="user-info">
              <span class="welcome-text">Welcome back，</span>
              <span class="username">{{ userStore.user.authentication.name }}</span>
              <el-avatar
                :size="40"
                :src="userStore.user.authentication.principal.avatar"
                class="user-avatar"
              ></el-avatar>
            </div>
          </el-col>
        </el-row>
      </el-header>
      <el-main class="content">
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped lang="scss">
.main-container {
  height: 100vh;
}

.sidebar {
  background-color: #1e1e2d;
  color: #ffffff;
  display: flex;
  flex-direction: column;
  box-shadow: 0 0 10px rgba(0,0,0,0.1);
}

.logo {
  width: 100%;
  height: 70px;
  background-color: #1b1b28;
  display: flex;
  align-items: center;
  padding-left: 24px;
  border-bottom: 1px solid rgba(255,255,255,0.1);

  .logo-content {
    display: flex;
    align-items: center;
    gap: 8px;

    .el-icon {
      font-size: 24px;
      color: #7571f9;
    }

    h2 {
      color: white;
      margin: 0;
      font-size: 20px;
    }
  }
}

.my-menu {
  width: 100%;
  border-right: none;

  .el-menu-item {
    height: 50px;
    line-height: 50px;
    margin: 4px 0;
    border-radius: 4px;
    margin: 4px 12px;
    width: calc(100% - 24px);

    &:hover {
      background-color: rgba(117, 113, 249, 0.1);

      // 修改hover状态下的文字颜色
      span, .el-icon {
        color: #7571f9 !important;
      }
    }

    &.is-active {
      background: linear-gradient(90deg, #7571f9 0%, #6f6af8 100%);
      box-shadow: 0 2px 6px rgba(117, 113, 249, 0.4);

      // 确保激活状态下文字为白色
      span, .el-icon {
        color: #ffffff !important;
      }
    }

    .el-icon {
      margin-right: 12px;
      font-size: 18px;
    }
  }
}

.menu-divider {
  height: 1px;
  background-color: rgba(255,255,255,0.1);
  margin: 12px;
}

.header {
  background-color: #ffffff;
  border-bottom: 1px solid #ebeef5;
  padding: 0;
  height: 70px;
  line-height: 70px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
}

.header-row {
  height: 100%;
}

.page-title {
  font-size: 20px;
  font-weight: 600;
  color: #1e1e2d;
  padding-left: 24px;
}

.user-col {
  display: flex;
  justify-content: flex-end;
  padding-right: 24px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;

  .welcome-text {
    color: #6c757d;
  }

  .username {
    color: #1e1e2d;
    font-weight: 600;
  }

  .user-avatar {
    border: 2px solid #7571f9;
    cursor: pointer;
    transition: transform 0.2s;

    &:hover {
      transform: scale(1.05);
      box-shadow: 0 2px 8px rgba(117, 113, 249, 0.3);
    }
  }
}

.content {
  padding: 24px;
  background-color: #f5f7fa;
}
</style>
