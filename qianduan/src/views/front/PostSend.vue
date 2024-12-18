<template>
  <div class="post-send">
    <div class="post-card">
      <h2 class="post-title">Create New Post</h2>

      <el-form
        :model="postForm"
        :rules="rules"
        ref="formRef"
        label-width="0"
        @submit.prevent
        class="post-form"
      >
        <div class="form-content">
          <!-- Left Edit Section -->
          <div class="left-section">
            <el-form-item prop="title" class="title-item">
              <el-input
                v-model.trim="postForm.title"
                placeholder="Enter title..."
                maxlength="100"
                show-word-limit
                class="title-input"
              ></el-input>
            </el-form-item>

            <el-form-item prop="content" class="content-item">
              <div class="editor-container">
                <Toolbar
                    style="border-bottom: 1px solid #e4e7ed"
                    :editor="editorRef"
                    :defaultConfig="toolbarConfig"
                    mode="default"
                />
                <Editor
                    style="height: 520px"
                    v-model="postForm.content"
                    :defaultConfig="editorConfig"
                    mode="default"
                    @onCreated="handleCreated"
                />
              </div>
            </el-form-item>
          </div>

          <!-- Right Settings Section -->
          <div class="right-section">
            <div class="section-card">
              <h3 class="section-title">Cover Image</h3>
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
                    v-if="postForm.imageUrl"
                    :src="postForm.imageUrl"
                    class="upload-image"
                    fit="cover"
                />
                <div v-else class="upload-placeholder">
                  <el-icon class="upload-icon"><Picture /></el-icon>
                  <div class="upload-text">
                    <span>Click or drag to upload</span>
                    <span class="upload-tip">Recommended ratio 16:9</span>
                  </div>
                </div>
              </el-upload>
            </div>

            <div class="section-card">
              <h3 class="section-title">Category</h3>
              <el-form-item prop="category">
                <el-input
                  v-model.trim="postForm.category"
                  placeholder="Enter category name"
                  @input="validateCategory"
                  maxlength="30"
                  @blur="() => formRef?.validateField('category')"
                  class="category-input"
                >
                  <template #prefix>
                    <el-icon><Collection /></el-icon>
                  </template>
                </el-input>
                <div class="input-tip">Max 6 Chinese characters or 30 English letters</div>
              </el-form-item>
            </div>

            <el-button
              type="primary"
              @click="submitPost"
              size="large"
              class="publish-btn"
              :disabled="!postForm.imageUrl"
            >
              Publish Post
            </el-button>
          </div>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import {onBeforeUnmount, ref, shallowRef} from 'vue'
import {ElMessage} from 'element-plus'
import {useRouter} from 'vue-router'
import '@wangeditor/editor/dist/css/style.css'
import {Editor, Toolbar} from '@wangeditor/editor-for-vue'
import request from '@/utils/request'
import {Plus, UploadFilled, Picture, Collection} from '@element-plus/icons-vue'
import {i18nAddResources, i18nChangeLanguage} from "@wangeditor/editor";

i18nChangeLanguage('en')

const router = useRouter()
const editorRef = shallowRef()

// 创建一个自定义的多语言配置
const customLanguage = {

}

// 编辑器配置
const toolbarConfig = {
  excludeKeys: [
    'uploadVideo',
    'insertVideo',
    'videoUpload',
    'insertTable',
    'code',
    'codeBlock',
    'todo',
    'group-video',
    'group-more'
  ]
}




const editorConfig = {
  placeholder: 'Enter your content here...',
  MENU_CONF: {},
  lang: 'en',
}

// 修改上传图片的配置
editorConfig.MENU_CONF['uploadImage'] = {
  server: '/api/file/upload',
  fieldName: 'files',
  formData: {
    fileTypeEnum: 'FILE'
  },
  maxFileSize: 5 * 1024 * 1024,
  allowedFileTypes: ['image/jpeg', 'image/png', 'image/gif', 'image/webp'],
}

const postForm = ref({
  title: '',
  category: '',
  content: '',
  imageUrl: ''
})

const formRef = ref(null)

// 定义表单验证规则
const rules = {
  title: [
    { required: true, message: 'Please enter title', trigger: 'blur' },
    { min: 2, max: 100, message: 'Length should be 2 to 100 characters', trigger: 'blur' }
  ],
  content: [
    { required: true, message: 'Please enter content', trigger: 'blur' }
  ],
  category: [
    { required: true, message: 'Please enter category', trigger: ['blur', 'change'] },
    {
      validator: (rule, value, callback) => {
        if (value && value.length > 30) {
          callback(new Error('Category name is too long'))
        } else {
          callback()
        }
      },
      trigger: ['blur', 'change']
    }
  ]
}

// 创建编辑器时的处理函数
const handleCreated = (editor) => {
  editorRef.value = editor
}

const fileList = ref([])


const handleEndSuccess = (response) => {
  if (response && response.length > 0) {
    postForm.value.imageUrl = response[0].url
    ElMessage.success('Image uploaded successfully')
  }
}


const handleRemove = () => {
  postForm.value.imageUrl = ''
  fileList.value = []
}

const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  if (!isImage) {
    ElMessage.error('Only image files are allowed!')
    return false
  }
  return true
}

// 修改 getStringLength 函数，添加计算汉字数量的功能
const getStringLength = (str) => {
  let len = 0
  let hanCount = 0
  for (let i = 0; i < str.length; i++) {
    if (str.charCodeAt(i) > 127 || str.charCodeAt(i) === 94) {
      len += 2
      hanCount++
    } else {
      len += 1
    }
  }
  return { len, hanCount }
}

// 修改分类验证函数
const validateCategory = (value) => {
  if (!value) return

  const { len, hanCount } = getStringLength(value)
  if (len > 30 || hanCount > 6) {
    // Truncate excess content
    let newValue = ''
    let currentLen = 0
    let currentHanCount = 0
    for (let i = 0; i < value.length; i++) {
      const isHan = value.charCodeAt(i) > 127 || value.charCodeAt(i) === 94
      if (isHan) {
        if (currentHanCount >= 6) break
        currentHanCount++
        currentLen += 2
      } else {
        if (currentLen >= 30) break
        currentLen += 1
      }
      if (currentLen <= 30 && currentHanCount <= 6) {
        newValue += value[i]
      } else {
        break
      }
    }
    postForm.value.category = newValue
    ElMessage.warning('Category can contain up to 6 Chinese characters or 30 English letters')
    formRef.value?.validateField('category')
  }
}

const submitPost = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid, fields) => {
    if (!valid) {
      ElMessage.error('Please check your input')
      return
    }

    if (!postForm.value.imageUrl) {
      ElMessage.warning('Please upload a cover image')
      return
    }

    try {
      await request.post('/post/add', postForm.value)
      ElMessage.success('Post published successfully')
      router.push('/front/Home')
    } catch (error) {
      ElMessage.error('Failed to publish post')
    }
  })
}

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})
</script>

<style lang="scss" scoped>
.post-send {
  max-width: 1400px;
  margin: 0 auto;
  padding: 24px;
  min-height: calc(100vh - 48px);
  background: linear-gradient(135deg, #f6f8fc 0%, #f0f4f8 100%);
  display: flex;
  flex-direction: column;

  .post-card {
    background: transparent;
    flex: 1;
    display: flex;
    flex-direction: column;
  }

  .post-title {
    text-align: left;
    margin-bottom: 24px;
    color: #2c3e50;
    font-size: 28px;
    font-weight: 600;
    background: linear-gradient(45deg, #2c3e50, #3498db);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
  }

  .post-form {
    flex: 1;
    display: flex;
    flex-direction: column;
  }

  .form-content {
    display: flex;
    gap: 24px;
    flex: 1;
    min-height: 0;
  }

  .left-section {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 16px;
    min-height: 0;

    .title-item {
      margin-bottom: 0;
    }

    .title-input {
      :deep(.el-input__wrapper) {
        padding: 12px 16px;
        background: #fff;
        box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
        border-radius: 12px;
        border: 1px solid #e8eef7;
        transition: all 0.3s ease;

        &:hover {
          border-color: #3498db;
          box-shadow: 0 4px 16px rgba(52, 152, 219, 0.1);
        }

        &.is-focus {
          border-color: #3498db;
          box-shadow: 0 4px 16px rgba(52, 152, 219, 0.15);
        }

        .el-input__inner {
          font-size: 18px;
          font-weight: 500;
          color: #2c3e50;

          &::placeholder {
            color: #94a3b8;
            font-weight: normal;
          }
        }
      }
    }

    .content-item {
      flex: 1;
      display: flex;
      flex-direction: column;
      min-height: 0;
    }

    .editor-container {
      flex: 1;
      display: flex;
      flex-direction: column;
      min-height: 0;
      background: #fff;
      border-radius: 12px;
      overflow: hidden;
      height: 100%;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
      border: 1px solid #e8eef7;

      :deep(.w-e-toolbar) {
        border-top: none;
        border-left: none;
        border-right: none;
        background: #fff;
        border-bottom: 1px solid #e8eef7;
      }

      :deep(.w-e-text-container) {
        flex: 1;
        min-height: 0;
        background: #fff;
        border: none;
      }
    }
  }

  .right-section {
    width: 320px;
    display: flex;
    flex-direction: column;
    gap: 16px;
    height: fit-content;

    .section-card {
      background: #fff;
      border-radius: 12px;
      padding: 20px;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
      border: 1px solid #e8eef7;

      .section-title {
        font-size: 16px;
        font-weight: 600;
        color: #2c3e50;
        margin-bottom: 16px;
        display: flex;
        align-items: center;
        gap: 8px;

        &::before {
          content: '';
          display: block;
          width: 4px;
          height: 16px;
          background: linear-gradient(to bottom, #3498db, #2980b9);
          border-radius: 2px;
        }
      }
    }

    .upload-area {
      width: 100%;
      border: 2px dashed #e8eef7;
      border-radius: 12px;
      transition: all 0.3s ease;
      background: #f8fafc;

      &:hover {
        border-color: #3498db;
        background: #f0f7ff;
      }

      .upload-image {
        width: 100%;
        height: 180px;
        object-fit: cover;
        border-radius: 8px;
      }

      .upload-placeholder {
        padding: 32px 0;
        text-align: center;

        .upload-icon {
          font-size: 36px;
          color: #3498db;
          margin-bottom: 12px;
          filter: drop-shadow(0 2px 4px rgba(52, 152, 219, 0.2));
        }

        .upload-text {
          display: flex;
          flex-direction: column;
          gap: 6px;
          color: #2c3e50;

          .upload-tip {
            font-size: 12px;
            color: #94a3b8;
          }
        }
      }
    }

    .category-input {
      :deep(.el-input__wrapper) {
        background: #f8fafc;
        box-shadow: none;
        border: 1px solid #e8eef7;
        border-radius: 8px;
        transition: all 0.3s ease;

        .el-input__prefix {
          color: #3498db;
        }

        &:hover, &.is-focus {
          border-color: #3498db;
          background: #fff;
          box-shadow: 0 2px 8px rgba(52, 152, 219, 0.1);
        }
      }
    }

    .input-tip {
      font-size: 12px;
      color: #94a3b8;
      margin-top: 6px;
      padding-left: 4px;
    }

    .publish-btn {
      position: sticky;
      bottom: 0;
      margin-top: 20px;
      height: 48px;
      font-size: 16px;
      font-weight: 500;
      background: linear-gradient(45deg, #3498db, #2980b9);
      border: none;
      border-radius: 12px;
      transition: all 0.3s ease;
      box-shadow: 0 4px 12px rgba(52, 152, 219, 0.2);
      z-index: 10;

      &:hover {
        background: linear-gradient(45deg, #2980b9, #2573a7);
        transform: translateY(-1px);
        box-shadow: 0 6px 16px rgba(52, 152, 219, 0.25);
      }

      &:active {
        transform: translateY(0);
        box-shadow: 0 2px 8px rgba(52, 152, 219, 0.2);
      }

      &:disabled {
        background: #bdc3c7;
        box-shadow: none;
      }
    }
  }
}
</style>

