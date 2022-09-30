<!--导入社保-->
<template>
  <a-card :bordered="false" class="card-area socialSecurity">
    <a-upload
      :multiple="false"
      :file-list="fileList"
      :before-upload="beforeUpload"
      :customRequest="handleUpload"
      :remove="handleRemove"
    >
      <a-button> <a-icon type="upload" /> 导入</a-button>
    </a-upload>
    <!--<a-upload :file-list="fileList" :remove="handleRemove" :before-upload="beforeUpload">-->
      <!--<a-button> <a-icon type="upload" /> 上传 </a-button>-->
    <!--</a-upload>-->
    <!--<a-button-->
      <!--type="primary"-->
      <!--:disabled="fileList.length === 0"-->
      <!--:loading="uploading"-->
      <!--style="margin-top: 16px"-->
      <!--@click="handleUpload"-->
    <!--&gt;-->
      <!--{{ uploading ? '上传中' : '开始上传' }}-->
    <!--</a-button>-->
    <p style="margin-top: 10px; color: #ccc; cursor: pointer" @click="exportExcel"><span style="color: red"> * </span> 导入之前请先  <span style="color: blue"><a-icon type="download" />下载模板</span></p>
  </a-card>
</template>
<script>
export default {
  name: 'ExportSocialSecurity',
  data () {
    return {
      fileList: [],
      fileInfo: {},
      uploading: false
    }
  },
  watch: {
  },
  created () {
  },
  mounted () {
  },
  methods: {
    exportExcel () {
      let url = this.$baseURL + 'system/sb/download/sb_templates.xlsx'
      let eleLink = document.createElement('a')
      eleLink.href = url
      eleLink.click()
      eleLink.remove()
      this.$message.success('下载成功！')
    },
    handleRemove (file) {
      const index = this.fileList.indexOf(file)
      const newFileList = this.fileList.slice()
      newFileList.splice(index, 1)
      this.fileList = newFileList
    },
    // 导入之前
    beforeUpload (file) {
      let fileName = file.name.split('.')
      // fileName[fileName.length - 1] === 'xls' ||
      // fileName[fileName.length - 1] === 'xltx' ||
      // fileName[fileName.length - 1] === 'xlt' ||
      // fileName[fileName.length - 1] === 'xlsm' ||
      // fileName[fileName.length - 1] === 'xlsb' ||
      // fileName[fileName.length - 1] === 'xltm' ||
      // fileName[fileName.length - 1] === 'csv'
      if (fileName[fileName.length - 1] === 'xlsx') {
        this.fileInfo = file
        return true
      } else {
        // this.fileList = []
        // this.fileInfo = []
        this.$message.warning('请上传正确格式的表格!')
        return false
      }
    },
    // 自定义上传
    handleUpload () {
      const formImgData = new FormData()
      formImgData.append('file', this.fileInfo)
      this.$upload('/system/sb/import', formImgData).then(res => {
        let data = res.data.data.data
        let error = res.data.data.error
        if (data && data.length > 0) {
          this.$message.success('导入成功！')
          this.fileList = [...this.fileList, this.fileInfo]
        }
        if (error && error.length > 0) {
          this.$message.warning('导入文件内容有误！')
        }
      }).catch(err => {
        this.$message.warning(err)
      })
    }
  }
}
</script>
<style scoped lang="less">
  .socialSecurity{
    width: 100%;
    .ant-upload-list-item-card-actions{
      right: -30px;
    }
  }
</style>
<style lang="less">
  @import "../../../static/less/Common";
</style>
