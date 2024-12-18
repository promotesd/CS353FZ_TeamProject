<script setup>
import {onMounted, ref} from 'vue'
import {ElMessageBox} from "element-plus"
import {ChatDotRound, Delete, Document, Edit, Plus, Search, Star, UserFilled, View} from "@element-plus/icons-vue"
import request from "@/utils/request"

//用户表管理
const sysUserApi = {
  page(data) {
    return request.post('/sysUser/page', data)
  },
  listAll() {
    return request.post('/sysUser/listAll', {})
  },
  getById(id) {
    return request.get('/sysUser/getById/' + id)
  },
  add(data) {
    return request.post('/sysUser/add', data)
  },
  deleteById(id) {
    return request.delete('/sysUser/deleteById/' + id)
  },
  deleteByIds(ids) {
    return request.delete('/sysUser/deleteByIds', {
      data: ids
    })
  },
  updateById(data) {
    return request.put('/sysUser/updateById', data)
  },
}

onMounted(() => {
})


const page = ref({
  pageSize: 10,
  pageNum: 1,
  search: '',
  total: 0,
  role: 'USER'
})

const tableData = ref([])
const dialog = ref({
  dialogFormVisible: false,
  optionName: '新增',
  formDisabled: true,
  optionValue: null
})

const form = ref({})

const search = () => {
  sysUserApi.page(page.value)
      .then(resp => {
        tableData.value = resp.data.data.records
        page.value.total = resp.data.data.total
      })
}

const handleEndSuccess = (response) => {
  form.value.avatar = response[0].url
}


const clickButton = (type, row) => {
  dialog.value.optionValue = type
  if (type === 'add') {
    dialog.value.dialogFormVisible = true
    dialog.value.optionName = '用户表新增'
    dialog.value.formDisabled = false
  } else if (type === 'update') {
    sysUserApi.getById(row.id).then((resp) => {
      dialog.value.dialogFormVisible = true
      dialog.value.optionName = '用户表修改'
      dialog.value.formDisabled = false
      form.value = resp.data.data
    })
  } else if (type === 'detail') {
    sysUserApi.getById(row.id).then((resp) => {
      dialog.value.dialogFormVisible = true
      dialog.value.optionName = '用户表详情'
      dialog.value.formDisabled = true
      form.value = resp.data.data
    })
  } else if (type === 'delete') {
    ElMessageBox.confirm(
        'Are you sure to delete this user?',
        'Warning', {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning',
        }
    ).then(() => {
      sysUserApi.deleteById(row.id).then(() => {
        initTableData()
      })
    })
  }

}

const currentChange = (number) => {
  page.value.pageNum = number
  sysUserApi.page(page.value).then(resp => {
    tableData.value = resp.data.data.records
    page.value.total = resp.data.data.total
  })
}

const formSubmit = () => {
  dialog.value.dialogFormVisible = false
  if (dialog.value.optionValue === 'add') {
    sysUserApi.add(form.value)
        .then(() => {
          initTableData()
        })
  } else if (dialog.value.optionValue === 'update') {
    sysUserApi.updateById(form.value)
        .then(() => {
          initTableData()
        })
  }
}

const dialogClose = () => {
  form.value = {}
}

const initTableData = () => {
  sysUserApi.page(page.value)
      .then(resp => {
        tableData.value = resp.data.data.records
        page.value.total = resp.data.data.total
      })
}

const delArray = ref([])
const handleSelectionChange = (val) => {
  delArray.value = val.map(item => item.id)
}
const delBach = () => {
  ElMessageBox.confirm(
      'Are you sure to delete these users?',
      'Warning', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  ).then(() => {
    sysUserApi.deleteByIds(delArray.value)
        .then(() => {
          initTableData()
        })
  })
}

onMounted(() => {
  initTableData()
})


</script>

<template>
  <div class="page-container">
    <!-- 顶部操作栏 -->
    <div class="action-header">
      <el-row align="middle" class="action-row">
        <el-col :span="12">
          <el-button type="primary" :icon="Plus" round @click="clickButton('add',null)">Add User</el-button>
          <el-button type="danger" :icon="Delete" round :disabled="delArray.length<1" @click="delBach">Batch Delete
          </el-button>
        </el-col>
        <el-col :span="12">
          <div class="search-box">
            <el-input
                v-model="page.search"
                placeholder="Search username..."
                @keyup.enter="search"
                :prefix-icon="Search"
            >
              <template #append>
                <el-button :icon="Search" @click="search">Search</el-button>
              </template>
            </el-input>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 表格内容 -->
    <div class="table-container">
      <el-table
          :data="tableData"
          style="width: 100%"
          border
          stripe
          @selection-change="handleSelectionChange"
          class="custom-table"
      >
        <el-table-column type="selection" width="55"/>
        <el-table-column prop="id" label="ID" width="80" align="center"/>
        <el-table-column label="User Info" min-width="200">
          <template #default="scope">
            <div class="user-info">
              <el-avatar :size="40" :src="scope.row.avatar"/>
              <div class="user-details">
                <span class="username">{{ scope.row.username }}</span>
                <span class="nickname">{{ scope.row.nickname }}</span>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="email" label="Email" min-width="180"/>
        <el-table-column label="Role" width="120" align="center">
          <template #default="scope">
            <el-tag :type="scope.row.role === 'ADMIN' ? 'danger' : 'info'">
              {{ scope.row.role === 'ADMIN' ? 'Admin' : 'User' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Statistics" width="280" align="center">
          <template #default="scope">
            <div class="stats-info">
              <div class="stat-item">
                <el-icon><Document/></el-icon>
                <span>{{ scope.row.postCount || 0 }}</span>
              </div>
              <div class="stat-item">
                <el-icon><ChatDotRound/></el-icon>
                <span>{{ scope.row.replyCount || 0 }}</span>
              </div>
              <div class="stat-item">
                <el-icon><UserFilled/></el-icon>
                <span>{{ scope.row.followerCount || 0 }}</span>
              </div>
              <div class="stat-item">
                <el-icon><Star/></el-icon>
                <span>{{ scope.row.followingCount || 0 }}</span>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="Register Time" width="180" align="center"/>

        <el-table-column label="Actions" width="180" fixed="right" align="center">
          <template #default="scope">
            <div class="action-buttons">
              <el-button
                  type="primary"
                  :icon="Edit"
                  @click="clickButton('update', scope.row)"
                  circle
              />
              <el-button
                  type="info"
                  :icon="View"
                  @click="clickButton('detail', scope.row)"
                  circle
              />
              <el-button
                  type="danger"
                  :icon="Delete"
                  @click="clickButton('delete', scope.row)"
                  circle
              />
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
          v-model:current-page="page.pageNum"
          v-model:page-size="page.pageSize"
          :total="page.total"
          :page-sizes="[10, 20, 30, 50]"
          @current-change="currentChange"
          layout="total, sizes, prev, pager, next, jumper"
          background
      />
    </div>

    <!-- 表单对话框 -->
    <el-dialog
        v-model="dialog.dialogFormVisible"
        :title="dialog.optionValue === 'add' ? 'Add User' : dialog.optionValue === 'update' ? 'Edit User' : 'User Details'"
        width="600px"
        @closed="dialogClose"
        destroy-on-close
        class="custom-dialog"
    >
      <el-form
          :model="form"
          label-position="right"
          label-width="100px"
          :disabled="dialog.formDisabled"
          class="dialog-form"
      >
        <el-form-item label="Avatar">
          <div class="avatar-upload">
            <el-upload
                class="avatar-uploader"
                action="/api/file/upload"
                :data="{fileTypeEnum:'FILE'}"
                :show-file-list="false"
                :on-success="handleEndSuccess"
                drag
                name="files"
            >
              <el-image
                  v-if="form.avatar"
                  :src="form.avatar"
                  class="avatar-image"
                  fit="cover"
              />
              <div v-else class="upload-placeholder">
                <el-icon class="upload-icon"><Plus/></el-icon>
                <span>Click to upload avatar</span>
              </div>
            </el-upload>
          </div>
        </el-form-item>

        <el-form-item label="Username">
          <el-input v-model="form.username" placeholder="Enter username"/>
        </el-form-item>

        <el-form-item label="Nickname">
          <el-input v-model="form.nickname" placeholder="Enter nickname"/>
        </el-form-item>

        <el-form-item label="Email">
          <el-input v-model="form.email" placeholder="Enter email"/>
        </el-form-item>

        <el-form-item label="Password" v-if="dialog.optionValue === 'add'">
          <el-input
              v-model="form.password"
              type="password"
              show-password
              placeholder="Enter password"
          />
        </el-form-item>

        <el-form-item label="Role">
          <el-radio-group v-model="form.role">
            <el-radio label="USER">User</el-radio>
            <el-radio label="ADMIN">Admin</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="Bio">
          <el-input
              v-model="form.bio"
              type="textarea"
              :rows="4"
              placeholder="Write something about yourself..."
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer" v-if="!dialog.formDisabled">
          <el-button @click="dialog.dialogFormVisible = false" round>Cancel</el-button>
          <el-button type="primary" @click="formSubmit" round>Confirm</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
// 复用 Post.vue 的基础样式
.page-container {
  padding: 24px;
  background: #f8fafc;
  min-height: calc(100vh - 84px);
}

.action-header {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  margin-bottom: 24px;

  .search-box {
    display: flex;
    gap: 12px;
    justify-content: flex-end;

    .el-input {
      max-width: 300px;
    }
  }
}

.custom-table {
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);

  .user-info {
    display: flex;
    align-items: center;
    gap: 12px;

    .user-details {
      display: flex;
      flex-direction: column;
      gap: 4px;

      .username {
        font-weight: 500;
        color: #1e293b;
      }

      .nickname {
        font-size: 12px;
        color: #64748b;
      }
    }
  }

  .stats-info {
    display: flex;
    justify-content: center;
    gap: 20px;

    .stat-item {
      display: flex;
      align-items: center;
      gap: 4px;
      color: #64748b;

      .el-icon {
        font-size: 16px;
      }
    }
  }

  .action-buttons {
    display: flex;
    justify-content: center;
    gap: 8px;
  }
}

.custom-dialog {
  .dialog-form {
    padding: 20px;
  }

  .avatar-upload {
    width: 100%;
    display: flex;
    justify-content: center;

    .avatar-uploader {
      .avatar-image {
        width: 100px;
        height: 100px;
        border-radius: 50%;
        object-fit: cover;
      }

      .upload-placeholder {
        width: 100px;
        height: 100px;
        border: 2px dashed #e2e8f0;
        border-radius: 50%;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        color: #64748b;
        cursor: pointer;
        transition: all 0.3s;

        &:hover {
          border-color: var(--el-color-primary);
          color: var(--el-color-primary);
        }

        .upload-icon {
          font-size: 24px;
          margin-bottom: 4px;
        }

        span {
          font-size: 12px;
        }
      }
    }
  }
}

:deep(.el-table) {
  --el-table-header-bg-color: #f8fafc;
  --el-table-row-hover-bg-color: #f1f5f9;

  th {
    font-weight: 600;
    color: #1e293b;
  }
}

.pagination-container {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}
</style>
