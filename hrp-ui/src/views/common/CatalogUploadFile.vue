<template>
  <a-modal
    title="附件上传"
    :visible="fileVisiable"
    :footer="null"
    @cancel="cancelAudit"
    :maskClosable="false"
  >
    <a-upload
      accept=".pdf"
      :fileList="fileList"
      :remove="handleRemove"
      :beforeUpload="beforeUpload"
    >
      <a-button v-if="fileList.length === 0">
        <a-icon type="upload"  /> 选择文件
      </a-button>
    </a-upload>
  </a-modal>

</template>
  
  <script>
export default {
  name: "file",
  data () {
    return {
      tstyle: { "color": "#0785fd", "font-weight": "bold", "background-color": "#ececec" },
      fileList: [],
      uploading: false,
      baseId: '',
      fileUrl: '',
      fileName: '',
      serName: '',
      // code: '',
      // parentid: '',
      gparName:'catalog'
    }
  },
  props: {
    fileVisiable: {
      default: false
    },
  },
  methods: {
    handleRemove (file) {
      // const index = this.fileList.indexOf(file)
      // const newFileList = this.fileList.slice()
      // newFileList.splice(index, 1)
      this.fileList = []
      this.fileName = ''// 空 清空
      this.serName = ''
      this.fileUrl = ''
      this.$emit("setFileId", this.fileName,this.serName, this.fileUrl,2)
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
      formData.append('gparname', this.gparName)
      // formData.append('parent', this.parentid)
      // formData.append('code', this.code)
      this.$upload('system/catalog/uploadFile', formData).then((r) => {
        if (r.data.data.success === 1) {
          r.data.data.uid = this.baseId
          this.serName = r.data.data.url
          this.fileUrl = this.$baseURL + r.data.data.url
          this.fileName = r.data.data.name
          r.data.data.url = this.fileUrl
          this.fileList.push(r.data.data)
          this.$message.success('上传成功.')
          this.$emit("setFileId", this.fileName,this.serName, this.fileUrl,1)
          
        } else {
          this.$message.error(r.data.data.message)
        }
      }).catch(() => {
        this.$message.error('上传失败.')
      })
    },
    fetch (id,fname,sname,gparName) {
      if (sname != null && sname != undefined && sname != '') {
        this.fileList=[{
          uid: id,
          name: fname,
          status: 'done',
          url: this.$baseURL + sname
        }]
      } else {
        this.fileList = []
      }
      this.baseId = id
      // this.parentid = parentid
      // this.code = code
      this.gparName = gparName
    },
    cancelAudit () {
      this.fileList = []
      this.fileName = ''// 空 清空
      this.serName = ''
      this.fileUrl = ''
      this.gparName=''
      // this.parentid = ''
      // this.code = ''
      this.$emit("setFileId", this.fileName,this.serName, this.fileUrl,0)
    },

  }
}
  </script>
  
  <style>
</style>