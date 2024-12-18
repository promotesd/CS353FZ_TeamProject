<script setup>
import {onMounted, ref} from 'vue'
import {ElMessageBox} from "element-plus"
import {Delete, Edit, Plus, Search, View} from "@element-plus/icons-vue"
import request from "@/utils/request"

//举报表管理
const reportApi = {
  page(data) {
    return request.post('/report/page', data)
  },
  listAll() {
    return request.post('/report/listAll', {})
  },
  getById(id) {
    return request.get('/report/getById/' + id)
  },
  add(data) {
    return request.post('/report/add', data)
  },
  deleteById(id) {
    return request.delete('/report/deleteById/' + id)
  },
  deleteByIds(ids) {
    return request.delete('/report/deleteByIds', {
      data: ids
    })
  },
  updateById(data) {
    return request.put('/report/updateById', data)
  }, listAllpost() {
    return request.post('/post/listAll', {})
  },
}
const listAllpost = ref([])

onMounted(() => {
  reportApi.listAllpost()
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
  optionName: 'New Report',
  formDisabled: true,
  optionValue: null
})

const form = ref({})

const search = () => {
  reportApi.page(page.value)
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
    dialog.value.optionName = 'Add Report'
    dialog.value.formDisabled = false
  } else if (type === 'update') {
    reportApi.getById(row.id).then((resp) => {
      dialog.value.dialogFormVisible = true
      dialog.value.optionName = 'Edit Report'
      dialog.value.formDisabled = false
      form.value = resp.data.data
    })
  } else if (type === 'detail') {
    reportApi.getById(row.id).then((resp) => {
      dialog.value.dialogFormVisible = true
      dialog.value.optionName = 'Report Details'
      dialog.value.formDisabled = true
      form.value = resp.data.data
    })
  } else if (type === 'delete') {
    ElMessageBox.confirm(
        'Are you sure to delete this report?',
        'Warning', {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning',
        }
    ).then(() => {
      reportApi.deleteById(row.id).then(() => {
        initTableData()
      })
    })
  }

}

const currentChange = (number) => {
  page.value.pageNum = number
  reportApi.page(page.value).then(resp => {
    tableData.value = resp.data.data.records
    page.value.total = resp.data.data.total
  })
}

const formSubmit = () => {
  dialog.value.dialogFormVisible = false
  if (dialog.value.optionValue === 'add') {
    reportApi.add(form.value)
        .then(() => {
          initTableData()
        })
  } else if (dialog.value.optionValue === 'update') {
    reportApi.updateById(form.value)
        .then(() => {
          initTableData()
        })
  }
}

const dialogClose = () => {
  form.value = {}
}

const initTableData = () => {
  reportApi.page(page.value)
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
      'Are you sure to delete these reports?',
      'Warning', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning',
      }
  ).then(() => {
    reportApi.deleteByIds(delArray.value)
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
          <el-button type="primary" :icon="Plus" @click="clickButton('add',null)">New</el-button>
          <el-button type="danger" :icon="Delete" :disabled="delArray.length<1" @click="delBach">Batch Delete
          </el-button>
        </el-col>
        <el-col :span="12">
          <div class="search-box">
            <el-input
                v-model="page.search"
                placeholder="Enter reported post ID"
                clearable
                @clear="initTableData"
                :prefix-icon="Search"
            />
            <el-button type="primary" :icon="Search" @click="search">Search</el-button>
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
        <el-table-column prop="id" label="ID"/>
        <el-table-column prop="postId" label="Reported Post ID"/>
        <el-table-column prop="reason" label="Report Reason"/>
        <el-table-column prop="createTime" label="Create Time"/>
        <el-table-column prop="createBy" label="Reporter"/>

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
        <el-form-item label="Reported Post ID">
          <el-input v-model="form.postId" placeholder="Enter reported post ID"/>
        </el-form-item>
        <el-form-item label="Report Reason">
          <el-input v-model="form.reason" placeholder="Enter report reason"/>
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
