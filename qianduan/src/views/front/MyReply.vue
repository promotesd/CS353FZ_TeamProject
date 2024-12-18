<script setup>
import {onMounted, ref} from 'vue'
import {ElMessageBox} from "element-plus"
import {Delete, Edit, Plus, Search, View} from "@element-plus/icons-vue"
import request from "@/utils/request"

//回复表管理
const replyApi = {
  page(data) {
    return request.post('/reply/pageSended', data)
  },
  listAll() {
    return request.post('/reply/listAll', {})
  },
  getById(id) {
    return request.get('/reply/getById/' + id)
  },
  add(data) {
    return request.post('/reply/add', data)
  },
  deleteById(id) {
    return request.delete('/reply/deleteById/' + id)
  },
  deleteByIds(ids) {
    return request.delete('/reply/deleteByIds', {
      data: ids
    })
  },
  updateById(data) {
    return request.put('/reply/updateById', data)
  }, listAllpost() {
    return request.post('/post/listAll', {})
  }
}
const listAllpost = ref([])


onMounted(() => {
  replyApi.listAllpost()
      .then((resp) => {
        listAllpost.value = resp.data.data
      })

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
  optionName: 'New Reply',
  formDisabled: true,
  optionValue: null
})

const form = ref({})

const search = () => {
  replyApi.page(page.value)
      .then(resp => {
        tableData.value = resp.data.data.records
        page.value.total = resp.data.data.total
      })
}

const handleEndSuccess = (response) => {
  form.value.imgUrl = response[0].url
}


const clickButton = (type, row) => {
  dialog.value.optionValue = type
  if (type === 'add') {
    dialog.value.dialogFormVisible = true
    dialog.value.optionName = 'Add Reply'
    dialog.value.formDisabled = false
  } else if (type === 'update') {
    replyApi.getById(row.id).then((resp) => {
      dialog.value.dialogFormVisible = true
      dialog.value.optionName = 'Edit Reply'
      dialog.value.formDisabled = false
      form.value = resp.data.data
    })
  } else if (type === 'detail') {
    replyApi.getById(row.id).then((resp) => {
      dialog.value.dialogFormVisible = true
      dialog.value.optionName = 'Reply Details'
      dialog.value.formDisabled = true
      form.value = resp.data.data
    })
  } else if (type === 'delete') {
    ElMessageBox.confirm(
        'Are you sure to delete this reply?',
        'Warning', {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning',
        }
    ).then(() => {
      replyApi.deleteById(row.id).then(() => {
        initTableData()
      })
    })
  }

}

const currentChange = (number) => {
  page.value.pageNum = number
  replyApi.page(page.value).then(resp => {
    tableData.value = resp.data.data.records
    page.value.total = resp.data.data.total
  })
}

const formSubmit = () => {
  dialog.value.dialogFormVisible = false
  if (dialog.value.optionValue === 'add') {
    replyApi.add(form.value)
        .then(() => {
          initTableData()
        })
  } else if (dialog.value.optionValue === 'update') {
    replyApi.updateById(form.value)
        .then(() => {
          initTableData()
        })
  }
}

const dialogClose = () => {
  form.value = {}
}

const initTableData = () => {
  replyApi.page(page.value)
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
      'Are you sure to delete these replies?',
      'Warning', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  ).then(() => {
    replyApi.deleteByIds(delArray.value)
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
          <el-button type="danger" :icon="Delete" round :disabled="delArray.length<1" @click="delBach">Batch Delete</el-button>
        </el-col>
        <el-col :span="12">
          <div class="search-box">
            <el-input
                v-model="page.search"
                placeholder="Search reply content..."
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

      >
        <el-table-column type="selection" width="55"/>
        <el-table-column prop="id" label="ID" width="80" align="center"/>
        <el-table-column prop="postId" label="PostId"/>
        <el-table-column prop="content" label="Content" min-width="300"/>
        <el-table-column prop="createTime" label="Reply Time" width="180" align="center"/>
        <el-table-column prop="createBy" label="Replier"/>


        <el-table-column label="Actions" width="180" fixed="right" align="center">
          <template #default="scope">
            <div class="action-buttons">

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
        :title="dialog.optionName"
        width="600px"
        @closed="dialogClose"
    >
      <el-form
          :model="form"
          label-position="right"
          label-width="120px"
          :disabled="dialog.formDisabled"
          class="dialog-form"
      >
        <el-form-item label="Post ID">
          <el-input v-model="form.postId" />
        </el-form-item>
        <el-form-item label="Reply Content">
          <el-input v-model="form.content" />
        </el-form-item>
      </el-form>

      <template #footer>
        <span class="dialog-footer" v-if="!dialog.formDisabled">
          <el-button @click="dialog.dialogFormVisible = false">Cancel</el-button>
          <el-button type="primary" @click="formSubmit">Confirm</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<style lang="scss" scoped>
.page-container {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
  min-height: calc(100vh - 140px);
}

.action-header {
  padding: 16px;
  background: #f8f9fa;
  border-radius: 6px;
  margin-bottom: 20px;

  .action-row {
    .search-box {
      display: flex;
      gap: 10px;
      justify-content: flex-end;
    }
  }
}

.table-container {
  margin: 20px 0;

  .table-image {
    width: 60px;
    height: 60px;
    object-fit: cover;
    border-radius: 4px;
    cursor: pointer;
  }
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  padding: 10px 0;
}

.dialog-form {
  padding: 20px;

  .upload-area {
    width: 70%;

    .upload-image {
      width: 200px;
      height: 200px;
      object-fit: cover;
    }
  }
}

:deep(.el-button-group) {
  .el-button {
    margin: 0 4px;
  }
}

:deep(.el-table) {
  --el-table-border-color: var(--el-border-color-lighter);
  --el-table-header-bg-color: var(--el-fill-color-light);

  th {
    font-weight: bold;
    background-color: var(--el-fill-color-light);
  }
}
</style>
