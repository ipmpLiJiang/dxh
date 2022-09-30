<!--行政级别-->
<template>
  <div  class="table7">
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
      <template slot="documentpath" slot-scope="text, record">
        <a v-if="record.documentpath" href="javascript:;" @click="downRow">
          <a-icon type="download" />  {{record.documentpath}}
        </a>
      </template>
      <!--<template slot="operation" slot-scope="text, record">-->
        <!--<a-button class="editable-add-btn" @click="editRow(record, 'editBtn')">-->
          <!--编辑-->
        <!--</a-button>-->
      <!--</template>-->
      <a slot="operation" slot-scope="text, record" href="javascript:;" @click="editRow(record, 'editBtn')">编辑</a>
    </a-table>
    <a-modal class="ant-modal-nofooter" v-model="coreVisible" :title="btnType === 'addBtn' ? '新增' : '修改'" @ok="handleOk">
      <a-form-model
        ref="formData"
        :model="formData"
        :rules="formRules"
        :label-col="labelCol"
        :wrapper-col="wrapperCol"
      >
        <a-form-model-item label="开始时间" prop="startdate">
          <a-date-picker
            :value="getTime(formData.startdate)"
            :format="dateFormat"
            :valueFormat="dateFormat"
            @change="startdateChange"
            :disabled="disEdit"
          />
        </a-form-model-item>
        <a-form-model-item label="结束时间" prop="enddate">
          <a-date-picker
            :value="getTime(formData.enddate)"
            :format="dateFormat"
            :valueFormat="dateFormat"
            @change="enddateChange"
            :disabled="disEdit"
          />
        </a-form-model-item>
        <a-form-model-item label="行政级别" prop="level">
          <a-select v-model="formData.level" @change="getlevelname(formData.level, 'level')" allowClear>
            <a-select-option v-for="item in zidianInfo.level" :value="item.keyy" :key="item.keyy">
              {{item.valuee}}
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="任命时间" prop="appointedtime">
          <a-date-picker
            :value="getTime(formData.appointedtime)"
            :format="dateFormat"
            :valueFormat="dateFormat"
            @change="appointedtimeChange"
          />
        </a-form-model-item>
        <a-form-model-item label="任命文件上传" prop="documentpath">
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
import moment from 'moment'

export default {
  data () {
    return {
      dateFormat: 'YYYY-MM-DD',
      coreVisible: false,
      tableData: [],
      selectedRowKeys: [],
      labelCol: { span: 6 },
      wrapperCol: { span: 14 },
      other: '',
      fileInfo: {},
      btnType: 'addBtn',
      startdateValue: 0,
      enddateValue: 0,
      lastdateValue: 0,
      lastdate: '',
      disEdit: false,
      formData: {
        startdate: new Date().toLocaleDateString(),
        enddate: '9999-12-31',
        appointedtime: '',
        level: '',
        employeeid: null,
        fileList: [],
        id: '',
        documentpath: '',
        document: ''
      },
      formRules: {
        startdate: [
          { required: true, message: '请输入开始时间', trigger: 'change' }
        ],
        enddate: [
          { required: true, message: '请输入结束时间', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    ...mapGetters(
      ['administrativeLevelInfo', 'queryData', 'zidianInfo']
    ),
    columns () {
      let { level } = this.zidianInfo
      return [
        {title: '开始时间', dataIndex: 'startdate', key: '1'},
        {title: '结束时间', dataIndex: 'enddate', key: '2'},
        {title: '行政级别',
          dataIndex: 'level',
          key: '3',
          customRender: (text, row, index) => {
            let option = level.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {title: '任命时间', dataIndex: 'appointedtime', key: '4'},
        {
          title: '任命文件',
          dataIndex: 'documentpath',
          width: 150,
          scopedSlots: {customRender: 'documentpath'}
        },
        {
          title: '操作',
          key: 'operation',
          width: 100,
          fixed: 'right',
          scopedSlots: {customRender: 'operation'}
        }
      ]
    }
  },
  watch: {
    queryData: {
      handler: function (data) {
        if (data) {
          this.formData.employeeid = data.employeeid
        }
      },
      immediate: true
    },
    administrativeLevelInfo: {
      handler: function (data) {
        if (data && data.length > 0) {
          if (this.btnType === 'addBtn') {
            this.lastdateValue = moment(data[0].startdate).valueOf()
            this.lastdate = data[0].startdate
            this.formData.startdate = moment(this.lastdate, 'YYYY-MM-DD').add(1, 'days')
          }
          this.tableData = data
        } else {
          this.tableData = []
          this.formData.startdate = new Date().toLocaleDateString()
          this.lastdate = ''
          this.lastdateValue = 0
        }
      },
      immediate: true
    },
    'formData.startdate': {
      handler: function (data) {
        if (data) {
          this.startdateValue = moment(data).valueOf()
          if (this.btnType === 'addBtn' && this.startdateValue <= this.lastdateValue) {
            this.$message.warning('开始时间不能小于或等于上一次开始时间！')
            this.formData.startdate = moment(this.lastdate, 'YYYY-MM-DD').add(1, 'days')
          }
          if (this.btnType === 'addBtn' && this.startdateValue && this.enddateValue && this.startdateValue >= this.enddateValue) {
            this.$message.warning('开始时间不能大于或等于结束时间')
          }
        }
      },
      immediate: true
    },
    'formData.enddate': {
      handler: function (data) {
        if (data) {
          this.enddateValue = moment(data).valueOf()
          if (this.btnType === 'addBtn' && this.startdateValue && this.enddateValue && this.startdateValue >= this.enddateValue) {
            this.$message.warning('结束时间不能小于或等于开始时间')
            this.formData.enddate = '9999-12-31'
          }
        }
      },
      immediate: true
    }
  },
  mounted () {},
  methods: {
    // 初始化开始时间
    getTime,
    startdateChange (date) {
      this.formData.startdate = date
    },
    enddateChange (date) {
      this.formData.enddate = date
    },
    appointedtimeChange (date) {
      this.formData.appointedtime = date
    },
    clickAdd (type) {
      this.btnType = type
      this.coreVisible = true
      this.disEdit = false
      this.formData.id = null
      this.resetForm()
      this.formData.enddate = '9999-12-31'
    },
    editRow (record, type) {
      this.btnType = type
      this.coreVisible = true
      this.disEdit = true
      this.formData = {...this.formData, ...record}
      this.formData.level = this.formData.level ? this.formData.level.toString() : ''
      if (record.documentpath) {
        this.formData.fileList = [{name: record.documentpath, url: record.documentpath, status: 'done', uid: record.id}]
        this.formData.documentpath = record.documentpath
        this.formData.document = record.document ? record.document : null
      } else {
        this.formData.fileList = []
        this.formData.documentpath = null
        this.formData.document = null
      }
    },
    getlevelname (value, name) {},
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
            that.$delete('system/administrative/level/delete/' + that.selectedRowKeys.toString()).then(res => {
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
      this.$get('/system/employee/download/test.docx', {responseType: 'blob'}).then(res => {
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
      formImgData.append('data', JSON.stringify(this.formData))
      this.$upload('system/employee/picture/upload/administrativeLevel', formImgData).then(res => {
        if (res.data) {
          this.formData.fileList = [{name: res.data.path, url: res.data.path, status: 'done', uid: this.formData.id}]
          this.formData.documentpath = res.data.path
          this.formData.document = res.date.fileName
          this.$message.success('上传成功!')
        }
      }).catch(err => {
        this.$message.warning(err)
      })
    },
    // 移除文件
    removeFile (info) {
    },
    handleChange (info) {
      let fileList = [...info.fileList]
      fileList = fileList.slice(-2)

      fileList = fileList.map(file => {
        if (file.response) {
          file.url = file.response.url
        }
        return file
      })

      this.fileList = fileList
    },
    onSubmit () {
      this.$refs.formData.validate(valid => {
        if (valid) {
          let params = {}
          params.id = this.btnType === 'addBtn' ? null : this.formData.id
          params.employeeid = this.formData.employeeid
          params.startdate = moment(this.formData.startdate).format('YYYY-MM-DD')
          params.enddate = this.formData.enddate
          params.appointedtime = this.formData.appointedtime
          params.level = this.formData.level
          params.documentpath = this.formData.documentpath
          params.document = this.formData.document
          let http = this.btnType === 'addBtn' ? this.$post : this.$put
          http('system/administrative/level', {...params}).then(res => {
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
      this.formData.appointedtime = null
      this.formData.level = null
      this.formData.fileList = []
      this.formData.documentpath = null
      this.formData.document = null
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
