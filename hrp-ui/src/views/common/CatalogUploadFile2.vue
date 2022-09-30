<template>
  <a-upload
    :accept="suffix"
    :disabled="!isEdit"
    :file-list="fileList"
    :multiple="false"
    :remove="handleImageRemove"
    :beforeUpload="beforeUpload"
  >
    <a-button size="small" v-if="isUpload"> <a-icon type="upload" /> 上传文件 </a-button>
  </a-upload>
</template>

<script>
export default {
  name: 'file',
  data () {
    return {
      fileList: [],
      uploading: false,
      isUpload: true
    }
  },
  props: {
    filename: {
      default: '',
      required:true
    },
    sername: {
      default: '',
      required:true
    },
    baseId: {
      default: '',
      required:true
    },
    size: {
      default: 1
    },
    suffix: {
      default: '.pdf'
    },
    isEdit: {
      default: true
    },
    isCallback: {
      default: false
    }
  },
  watch: {
    filename: function () {
      if (this.filename !== null && this.filename !== '') {
        var _this = this
        setTimeout(() => {
          _this.findFileList(_this.filename,_this.sername)
        }, 100)
      } else {
        if (!this.isEdit){
          this.isUpload = false
        } else {
        this.isUpload = true
        }
        this.fileList = []
      }
    },
    isEdit: function () {
      if (this.filename !== null && this.filename !== '') {
        var _this = this
        setTimeout(() => {
          _this.findFileList(_this.filename,this.sername)
        }, 100)
      } else {
        if (!this.isEdit){
          this.isUpload = false
        } else {
        this.isUpload = true
        }
        this.fileList = []
      }
    },
    immediate: true, // watch侦听操作内的函数会立刻被执行
    deep: true
  },
  mounted () {
    setTimeout(() => {
      if (this.filename !== null && this.filename !== '') {
        this.findFileList(this.filename,this.sername)
      } else {
        if (!this.isEdit){
          this.isUpload = false
        } else {
        this.isUpload = true
        }
        this.fileList = []
      }
    }, 100)
  },
  methods: {
    reset() {
      this.fileList = []
      this.isUpload = false
    },
    beforeUpload (file) {
      let isType = true;
      if (this.suffix === ".pdf") {
        isType = file.type === 'application/pdf'
        if (!(isType)) {
          this.$error({
            title: '只能上传pdf 格式~'
          })
          return
        }
      }
      const isLt3M = file.size / 1024 < 3001
      if (!isLt3M) {
        this.$error({
          title: '文件超3MB限制，不允许上传~'
        })
        return
      }
      return isType && isLt3M && this.customRequest(file)
    },
    customRequest (file) {
      const formData = new FormData()
      formData.append('file', file)
      formData.append('baseId', this.baseId)
      this.uploading = true
      let that = this
      this.$upload('system/catalog/uploadFile', formData).then((r) => {
        if (r.data.data.success === 1) {
          r.data.data.uid = this.baseId
          r.data.data.url = that.$baseURL + r.data.data.url
          r.data.data.thumbUrl = that.$baseURL + r.data.data.thumbUrl
          that.fileList.push(r.data.data)
          this.uploading = false
          this.$message.success('上传成功.')
          this.lableErr = ''
          if (this.isCallback === true) {
            this.$emit("fileData",that.fileList)
          }
          that.setBtnVisible()
        } else {
          this.$message.error(r.data.data.message)
        }
      }).catch(() => {
        this.uploading = false
        this.$message.error('上传失败.')
      })
    },
    handleImageRemove (file) {
      let that = this
      this.$confirm({
        title: '确定删除所选中的文件?',
        content: '当您点击确定按钮后，这些文件将会被彻底删除',
        centered: true,
        onOk () {
            that.uploading = false
            that.$message.success('删除文件成功')
            const index = that.fileList.indexOf(file)
            const newFileList = that.fileList.slice()
            newFileList.splice(index, 1)
            that.fileList = newFileList
            if (that.isCallback === true) {
              that.$emit("fileData",that.fileList)
            }
            that.setBtnVisible()
        },
        onCancel () {
        }
      })
    },
    setBtnVisible () {
      if (!this.isEdit) {
        this.isUpload = false
      } else {
        if (this.fileList.length === this.size) {
          this.isUpload = false
        } else {
          this.isUpload = true
        }
      }
    },
    findFileList (filename,sername) {
      this.fileList = []
      if(filename !== null && filename !== "") {
        this.fileList.push({
          uid: this.baseId,
          name: filename,
          status: 'done',
          url: this.$baseURL + sername,
          thumbUrl: this.$baseURL + sername
        })
      }
      this.setBtnVisible()
    }
  }
}
</script>
<style scoped>
</style>
