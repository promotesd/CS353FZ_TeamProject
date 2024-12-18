<script setup>
import {onMounted, ref} from 'vue'
import {ElMessageBox} from "element-plus"
import {ChatDotRound, Delete, Edit, Picture, Plus, Search, Star, UploadFilled, View} from "@element-plus/icons-vue"
import request from "@/utils/request"
import router from "@/router";

//帖子表管理
const postApi = {
  page(data) {
    return request.post('/post/pageSended', data)
  },
  listAll() {
    return request.post('/post/listAll', {})
  },
  getById(id) {
    return request.get('/post/getById/' + id)
  },
  add(data) {
    return request.post('/post/add', data)
  },
  deleteById(id) {
    return request.delete('/post/deleteById/' + id)
  },
  deleteByIds(ids) {
    return request.delete('/post/deleteByIds', {
      data: ids
    })
  },
  updateById(data) {
    return request.put('/post/updateById', data)
  },
}

onMounted(() => {
})


const page = ref({
  pageSize: 10,
  pageNum: 1,
  search: '',
  total: 0
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
  postApi.page(page.value)
      .then(resp => {
        tableData.value = resp.data.data.records
        page.value.total = resp.data.data.total
      })
}

const handleEndSuccess = (response) => {
  form.value.imageUrl = response[0].url
}


const clickButton = (type, row) => {
  dialog.value.optionValue = type
  if (type === 'add') {
    dialog.value.dialogFormVisible = true
    dialog.value.optionName = '帖子表新增'
    dialog.value.formDisabled = false
  } else if (type === 'update') {
    postApi.getById(row.id).then((resp) => {
      dialog.value.dialogFormVisible = true
      dialog.value.optionName = '帖子表修改'
      dialog.value.formDisabled = false
      form.value = resp.data.data
    })
  } else if (type === 'detail') {
    postApi.getById(row.id).then((resp) => {
      dialog.value.dialogFormVisible = true
      dialog.value.optionName = '帖子表详情'
      dialog.value.formDisabled = true
      form.value = resp.data.data
    })
  } else if (type === 'delete') {
    ElMessageBox.confirm(
        'Are you sure to delete this message?',
        'Warn', {
          confirmButtonText: 'Sure',
          cancelButtonText: 'Cancel',
          type: 'warning',
        }
    ).then(() => {
      postApi.deleteById(row.id).then(() => {
        initTableData()
      })
    })
  }

}

const currentChange = (number) => {
  page.value.pageNum = number
  postApi.page(page.value).then(resp => {
    tableData.value = resp.data.data.records
    page.value.total = resp.data.data.total
  })
}

const formSubmit = () => {
  dialog.value.dialogFormVisible = false
  if (dialog.value.optionValue === 'add') {
    postApi.add(form.value)
        .then(() => {
          initTableData()
        })
  } else if (dialog.value.optionValue === 'update') {
    postApi.updateById(form.value)
        .then(() => {
          initTableData()
        })
  }
}

const dialogClose = () => {
  form.value = {}
}

const initTableData = () => {
  postApi.page(page.value)
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
      'Are you sure to delete this information in bulk?',
      'Warn', {
        confirmButtonText: 'Sure',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  ).then(() => {
    postApi.deleteByIds(delArray.value)
        .then(() => {
          initTableData()
        })
  })
}

onMounted(() => {
  initTableData()
})

// 添加分类标签颜色映射函数
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

</script>

<template>
  <div class="page-container">
    <!-- 顶部操作栏 -->
    <div class="action-header">
      <el-row align="middle" class="action-row">
        <el-col :span="12">
          <el-button type="primary" :icon="Plus" round @click="router.push('/front/PostSend')">New Post</el-button>
          <el-button type="danger" :icon="Delete" round :disabled="delArray.length<1" @click="delBach">Batch Delete</el-button>
        </el-col>
        <el-col :span="12">
          <div class="search-box">
            <el-input
                v-model="page.search"
                placeholder="Search post title..."
                clearable
                @clear="initTableData"
                :prefix-icon="Search"
            />
            <el-button type="primary" :icon="Search" round @click="search">Search</el-button>
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
        <el-table-column prop="title" label="Title" min-width="200">
          <template #default="scope">
            <div class="title-cell">
              <span class="title-text">{{ scope.row.title }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="content" label="Content" show-overflow-tooltip min-width="300"/>
        <el-table-column prop="category" label="Category" width="120">
          <template #default="scope">
            <el-tag :type="getCategoryType(scope.row.category)">{{ scope.row.category }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="imageUrl" label="Image" width="100" align="center">
          <template #default="scope">
            <el-image
                v-if="scope.row.imageUrl"
                :src="scope.row.imageUrl"
                :preview-src-list="[scope.row.imageUrl]"
                preview-teleported
                class="table-image"
            >
              <template #error>
                <div class="image-error">
                  <el-icon>
                    <Picture/>
                  </el-icon>
                </div>
              </template>
            </el-image>
            <span v-else class="no-image">No Image</span>
          </template>
        </el-table-column>
        <el-table-column label="Interactions" width="200" align="center">
          <template #default="scope">
            <div class="interaction-data">
              <div class="data-item">
                <el-icon>
                  <Star/>
                </el-icon>
                <span>{{ scope.row.likeCount }}</span>
              </div>
              <div class="data-item">
                <el-icon>
                  <ChatDotRound/>
                </el-icon>
                <span>{{ scope.row.replyCount }}</span>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="Status" width="150" align="center">
          <template #default="scope">
            <div class="status-tags">
              <el-tag v-if="scope.row.isHot" type="danger" effect="dark" size="small">Hot</el-tag>
              <el-tag v-if="scope.row.isReported" type="warning" effect="dark" size="small">Reported</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="Post Time" width="180" align="center"/>
        <el-table-column prop="createBy" label="Author" width="120" align="center"/>

        <el-table-column label="Actions" width="180" fixed="right" align="center">
          <template #default="scope">
            <div class="action-buttons">
              <el-button
                  type="info"
                  :icon="View"
                  @click="router.push('/front/post/'+scope.row.id)"
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
        :title="dialog.optionName"
        width="700px"
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
        <el-form-item label="Post Title">
          <el-input v-model="form.title" placeholder="Enter post title"/>
        </el-form-item>

        <el-form-item label="Category">
          <el-input v-model="form.category" placeholder="Enter category"/>
        </el-form-item>

        <el-form-item label="Content">
          <el-input
              v-model="form.content"
              type="textarea"
              :rows="6"
              placeholder="Enter post content"
          />
        </el-form-item>

        <el-form-item label="Image">
          <div class="upload-container">
            <el-upload
                class="upload-area"
                drag
                action="/api/file/upload"
                :data="{fileTypeEnum:'FILE'}"
                :show-file-list="false"
                :on-success="handleEndSuccess"
                name="files"
            >
              <el-image
                  v-if="form.imageUrl"
                  :src="form.imageUrl"
                  class="upload-image"
                  fit="cover"
              />
              <div v-else class="upload-placeholder">
                <el-icon class="upload-icon">
                  <UploadFilled/>
                </el-icon>
                <div class="upload-text">
                  <span>Click or drag image to upload</span>
                  <span class="upload-tip">Supports jpg/png format</span>
                </div>
              </div>
            </el-upload>
          </div>
        </el-form-item>

        <el-form-item label="Status Settings" v-if="!dialog.formDisabled">
          <div class="status-settings">
            <el-switch
                v-model="form.isHot"
                active-text="Hot"
                inactive-text="Normal"
            />
          </div>
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

  .title-cell {
    .title-text {
      font-weight: 500;
      color: #1e293b;
    }
  }

  .table-image {
    width: 60px;
    height: 60px;
    border-radius: 8px;
    object-fit: cover;
    cursor: pointer;
    transition: transform 0.3s;

    &:hover {
      transform: scale(1.05);
    }
  }

  .image-error {
    width: 60px;
    height: 60px;
    display: flex;
    align-items: center;
    justify-content: center;
    background: #f1f5f9;
    border-radius: 8px;
    color: #94a3b8;
  }

  .no-image {
    color: #94a3b8;
    font-size: 12px;
  }

  .interaction-data {
    display: flex;
    justify-content: center;
    gap: 20px;

    .data-item {
      display: flex;
      align-items: center;
      gap: 4px;
      color: #64748b;

      .el-icon {
        font-size: 16px;
      }
    }
  }

  .status-tags {
    display: flex;
    gap: 8px;
    justify-content: center;
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

  .upload-container {
    width: 100%;

    .upload-area {
      width: 100%;
      border: 2px dashed #e2e8f0;
      border-radius: 12px;
      overflow: hidden;
      transition: all 0.3s;

      &:hover {
        border-color: var(--el-color-primary);
      }

      .upload-image {
        width: 100%;
        height: 200px;
        object-fit: cover;
      }

      .upload-placeholder {
        padding: 40px 0;
        text-align: center;
        color: #64748b;

        .upload-icon {
          font-size: 48px;
          margin-bottom: 16px;
          color: #94a3b8;
        }

        .upload-text {
          display: flex;
          flex-direction: column;
          gap: 8px;

          .upload-tip {
            font-size: 12px;
            color: #94a3b8;
          }
        }
      }
    }
  }

  .status-settings {
    display: flex;
    gap: 20px;
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
