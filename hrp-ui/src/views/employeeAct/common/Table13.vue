<!--荣誉-->
<template>
  <div class="table13">
    <div class="actionBtn">
      <a-button v-if="formData.employeeid" type="primary" class="editable-add-btn editable-margin-right" @click="clickAdd('addBtn')">
        新增
      </a-button>
      <a-button v-if="formData.employeeid" class="editable-add-btn" @click="deleteRow">
        删除
      </a-button>
    </div>
    <a-table
      :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
      :columns="columns"
      :data-source="tableData"
      :rowKey="record => record.id"
      bordered
      :pagination="false"
    >
      <template v-if="record.filepath" slot="filepath" slot-scope="text, record">
        <a href="javascript:;" @click="downRow">
          <a-icon type="download" />  {{record.filepath}}
        </a>
      </template>
      <!--<template slot="operation" slot-scope="text, record">-->
        <!--<a-button class="editable-add-btn" @click="editRow(record, 'editBtn')">-->
          <!--编辑-->
        <!--</a-button>-->
      <!--</template>-->
      <a slot="operation" slot-scope="text, record" href="javascript:;" @click="editRow(record, 'editBtn')">编辑</a>
    </a-table>
    <a-modal class="ant-modal-nofooter" v-model="coreVisible" :title="btnType === 'addBtn' ? '新增' : '修改'"  @ok="handleOk">
      <a-form-model
        ref="formData"
        :model="formData"
        :rules="formRules"
        :label-col="labelCol"
        :wrapper-col="wrapperCol"
      >
        <a-form-model-item label="称号" prop="title">
          <a-input
            v-model="formData.title"
            allowClear
          />
        </a-form-model-item>
        <a-form-model-item label="时间" prop="getdate">
          <a-date-picker
            :value="getTime(formData.getdate)"
            :format="dateFormat"
            :valueFormat="dateFormat"
            @change="getdateChange"
          />
        </a-form-model-item>
        <a-form-model-item label="备注" prop="memo">
          <a-input
            type="textarea"
            v-model="formData.memo"
            allowClear
          />
        </a-form-model-item>
        <a-form-model-item label="附件上传" prop="filepath">
          <a-upload
            :multiple="false"
            :file-list="formData.fileList"
            :before-upload="beforeUpload"
            :customRequest="upLoad"
            @change="handleChange"
            @remove="removeFile"
          >
            <a-button> <a-icon type="upload" /> 上传 </a-button>
          </a-upload>
        </a-form-model-item>
        <a-form-model-item :wrapper-col="{ span: 14, offset: 4 }">
          <a-button @click="resetForm">
            重置
          </a-button>
          <a-button style="margin-left: 20px;" type="primary" @click="onSubmit">
            确认
          </a-button>
        </a-form-model-item>
      </a-form-model>
    </a-modal>
  </div>
</template>
<script>
import { mapGetters } from 'vuex'
import { getTime } from '../../../utils/filter'

const columns = [
  { title: '称号', dataIndex: 'title', key: '1' },
  { title: '时间', dataIndex: 'getdate', key: '2', width: 150 },
  { title: '备注', dataIndex: 'memo', key: '3' },
  {
    title: '附件',
    dataIndex: 'filepath',
    width: 150,
    scopedSlots: { customRender: 'filepath' }
  },
  {
    title: '操作',
    key: 'operation',
    fixed: 'right',
    width: 100,
    scopedSlots: { customRender: 'operation' }
  }
]
export default {
  data () {
    return {
      dateFormat: 'YYYY-MM-DD',
      coreVisible: false,
      tableData: [],
      columns,
      selectedRowKeys: [],
      labelCol: { span: 6 },
      wrapperCol: { span: 14 },
      other: '',
      fileInfo: {},
      btnType: 'addBtn',
      formData: {
        fileList: [],
        filepath: '',
        pathname: '',
        employeeid: null,
        getdate: '',
        title: '',
        memo: ''
      },
      formRules: {
        // eventdate: [
        //   { required: true, message: '请输入时间', trigger: 'change' }
        // ],
        // event: [
        //   { required: true, message: '请输入称号', trigger: 'change' }
        // ]
      }
    }
  },
  computed: {
    ...mapGetters(['honorInfo', 'queryData'])
  },
  watch: {
    queryData: {
      handler: function (data) {
        if (data) {
          this.formData.employeeid = data.employeeid
        }
        this.tableData = []
      },
      immediate: true
    },
    honorInfo: {
      handler: function (data) {
        if (data && data.length > 0) {
          this.tableData = data
        } else {
          this.tableData = []
        }
      },
      immediate: true
    }
  },
  mounted () {},
  methods: {
    getTime,
    // 时间
    getdateChange (date) {
      this.formData.getdate = date
    },
    clickAdd (type) {
      this.btnType = type
      this.coreVisible = true
      this.formData.id = null
      this.resetForm()
    },
    editRow (record, type) {
      this.btnType = type
      this.coreVisible = true
      this.formData = {...this.formData, ...record}
      this.formData.fileList = [{name: record.filepath, url: record.filepath, status: 'done', uid: this.formData.id}]
      this.formData.filepath = record.filepath
      this.formData.pathname = record.pathname ? record.pathname : null
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    deleteRow () {
      if (this.selectedRowKeys.length === 0) {
        this.$message.warning('请勾选您要删除的记录！')
        return false
      } else {
        let that = this
        this.$confirm({
          title: '确定删除选中记录吗?',
          content: '',
          onOk () {
            console.log(this, 66666)
            that.$delete('system/honor/delete/' + that.selectedRowKeys.toString()).then(res => {
              if (res.data.status === 'OK') {
                that.$message.success('删除成功！')
                that.selectedRowKeys = []
                that.$store.dispatch('getSearch', {vm: that, id: that.formData.employeeid})
                // 需要在回调查询解救
              }
            }).catch(err => {
              that.$message.warning(err)
            })
          },
          onCancel () {
            that.selectedRowKeys = []
            that.$message.warning('取消删除！')
          }
        })
      }
    },
    handleOk (e) {
      this.coreVisible = false
    },
    downRow () {
      this.$get('/system/employee/download/test.docx', {
        responseType: 'blob'
      }).then(res => {
        let blob = new Blob([res.data], {
          type: 'application/vnd.ms-pdf'
        })
        let url = window.URL.createObjectURL(blob)
        let a = document.createElement('a')
        a.href = url
        // a.download = this.exportLabel + '.pdf'
        a.download = 'XXX' + '.pdf'
        a.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('下载成功！')
      }).catch(err => {
        this.$message.warning(err)
      })
    },
    handleChange (info) {
      // let fileList = [...info.fileList]
      // fileList = fileList.slice(-2)
      //
      // fileList = fileList.map(file => {
      //   if (file.response) {
      //     // Component will show file.url as link
      //     file.url = file.response.url
      //   }
      //   return file
      // })
      //
      // this.fileList = fileList
    },
    // 移除文件
    removeFile (info) {
      console.log(info, 55555)
    },
    // 图片上传之前的校验
    beforeUpload (file) {
      console.log(file, 444444)
      // if (this.fileList.length === 0) {
      //   this.$message.warning('文件已存在，请删除后在上传！')
      //   return false
      // }
      // const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png'
      // if (!isJpgOrPng) {
      //   this.$message.error('请上传正确的照片格式！')
      // }
      // const isLt2M = file.size / 1024 / 1024 < 10
      // if (!isLt2M) {
      //   this.$message.error('上传的照片不能超过10MB!')
      // }
      this.fileInfo = file
      // return isJpgOrPng && isLt2M
    },
    // 自定义上传
    upLoad () {
      const formImgData = new FormData()
      formImgData.append('file', this.fileInfo)
      this.$upload('system/employee/picture/upload/honor', formImgData).then(res => {
        if (res.data) {
          this.formData.fileList = [{name: res.data.path, url: res.data.path, status: 'done', uid: this.formData.id}]
          this.formData.filepath = res.data.path
          this.formData.pathname = res.data.fileName
          this.$message.success('上传成功！')
          // this.$store.dispatch('getSearch', {vm: this, id: this.formData.employeeid})
        }
      }).catch(err => {
        this.$message.success(err)
      })
    },
    onSubmit () {
      this.$refs.formData.validate(valid => {
        if (valid) {
          let params = {}
          params.id = this.btnType === 'addBtn' ? null : this.formData.id
          params.employeeid = this.formData.employeeid
          params.getdate = this.formData.getdate
          params.title = this.formData.title
          params.memo = this.formData.memo
          params.filepath = this.formData.filepath
          params.filename = this.formData.pathname
          let http = this.btnType === 'addBtn' ? this.$post : this.$put
          http('system/honor', {...params}).then(res => {
            if (res.data.status === 'OK') {
              this.$store.dispatch('getSearch', {vm: this, id: this.formData.employeeid})
              this.resetForm()
              this.coreVisible = false
              this.$message.success(this.btnType === 'addBtn' ? '新增成功！' : '修改成功！')
            }
          })
        } else {
          return false
        }
      })
    },
    resetForm () {
      this.formData.getdate = null
      this.formData.title = null
      this.formData.memo = null
      this.formData.filepath = null
      this.formData.filename = null
      this.formData.fileList = []
    }
  }
}
</script>
<style lang="less" scoped>
  .actionBtn {
    margin-bottom: 10px;
    text-align: right;
  }
</style>