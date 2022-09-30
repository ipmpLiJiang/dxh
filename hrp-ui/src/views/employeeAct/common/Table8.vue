<!--职务-->
<template>
  <div class="table8">
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
      <template v-if="record.appointmentdocumentspath" slot="appointmentdocumentspth" slot-scope="text, record">
        <a href="javascript:;" @click="downRow">
          <a-icon type="download" />  {{record.appointmentdocumentspth}}
        </a>
      </template>
      <template v-if="record.removaldocumentspath" slot="removaldocumentspath" slot-scope="text, record">
        <a href="javascript:;" @click="downRow">
          <a-icon type="download" />  {{record.removaldocumentspath}}
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
          />
        </a-form-model-item>
        <a-form-model-item label="结束时间" prop="enddate">
          <a-date-picker
            :value="getTime(formData.enddate)"
            :format="dateFormat"
            :valueFormat="dateFormat"
            @change="enddateChange"
          />
        </a-form-model-item>
        <a-form-model-item label="科室类型" prop="depttype">
          <a-select v-model="formData.depttype" allowClear>
            <a-select-option v-for="item in zidianInfo.depttype" :value="item.keyy" :key="item.keyy">
              {{item.valuee}}
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="行政职务科室" prop="dept">
          <a-input
            v-model="formData.dept"
            allowClear
          />
        </a-form-model-item>
        <a-form-model-item label="行政职务" prop="job">
          <a-input
            v-model="formData.job"
            allowClear
          />
        </a-form-model-item>
        <a-form-model-item label="任命文件上传" prop="appointmentdocumentspath">
          <a-upload
            :multiple="false"
            :file-list="formData.fileList1"
            @change="handleChange"
            :before-upload="beforeUpload"
            :customRequest="upLoad"
            :remove="removeFile"
          >
            <a-button @click="eventdate('appointment')"> <a-icon type="upload" /> 上传 </a-button>
          </a-upload>
        </a-form-model-item>
        <a-form-model-item label="免职文件上传" prop="removaldocumentspath">
          <a-upload
            :multiple="false"
            :file-list="formData.fileList2"
            @change="handleChange"
            :before-upload="beforeUpload"
            :customRequest="upLoad"
            :remove="removeFile"
          >
            <a-button @click="eventdate('removal')"> <a-icon type="upload" /> 上传 </a-button>
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
      picFile: {},
      btnType: 'addBtn',
      startdateValue: 0,
      enddateValue: 0,
      formData: {
        employeeid: null,
        startdate: '',
        enddate: '9999-12-31',
        depttype: '',
        dept: '',
        job: '',
        fileList1: [],
        fileList2: [],
        column: '',
        appointmentdocuments: '',
        appointmentdocumentspath: '',
        removaldocuments: '',
        removaldocumentspath: ''
      },
      formRules: {
        startdate: [
          { required: true, message: '请输入开始时间', trigger: 'change' }
        ],
        enddate: [
          { required: true, message: '请输入结束时间', trigger: 'change' }
        ]
        // fileList1: [{ required: true, message: '请上传任命文件', trigger: 'change' }],
        // fileList2: [{ required: true, message: '请上传免职文件', trigger: 'change' }]
      }
    }
  },
  computed: {
    ...mapGetters(
      ['administrativePostInfo', 'queryData', 'zidianInfo']
    ),
    columns () {
      let { depttype } = this.zidianInfo
      return [
        { title: '开始时间', dataIndex: 'startdate', key: '1' },
        { title: '结束时间', dataIndex: 'enddate', key: '2' },
        { title: '科室类型',
          dataIndex: 'depttype',
          width: 100,
          key: '3',
          customRender: (text, row, index) => {
            let option = depttype.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '行政职务科室', dataIndex: 'dept', key: '4' },
        { title: '行政职务', dataIndex: 'job', key: '5' },
        {
          title: '任命文件',
          dataIndex: 'appointmentdocumentspth',
          width: 150,
          scopedSlots: { customRender: 'appointmentdocumentspath' }
        },
        {
          title: '免职文件',
          dataIndex: 'removaldocumentspath',
          width: 150,
          scopedSlots: { customRender: 'removaldocumentspath' }
        },
        {
          title: '操作',
          key: 'operation',
          fixed: 'right',
          width: 100,
          scopedSlots: { customRender: 'operation' }
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
    administrativePostInfo: {
      handler: function (data) {
        if (data) {
          this.tableData = data
        } else {
          this.tableData = []
        }
      },
      immediate: true
    },
    'formData.startdate': {
      handler: function (data) {
        if (data) {
          this.startdateValue = moment(data).valueOf()
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
    // 初始化时间
    getTime,
    startdateChange (date) {
      this.formData.startdate = date
    },
    enddateChange (date) {
      this.formData.enddate = date
    },
    clickAdd (type) {
      this.btnType = type
      this.coreVisible = true
      this.formData.id = null
      this.resetForm()
      this.formData.enddate = '9999-12-31'
    },
    editRow (record, type) {
      this.btnType = type
      this.coreVisible = true
      this.formData = {...this.formData, ...record}
      if (record.appointmentdocumentspath) {
        this.formData.fileList1 = [{name: record.appointmentdocumentspath, url: record.appointmentdocumentspath, status: 'done', uid: this.formData.id}]
        this.formData.appointmentdocuments = record.appointmentdocuments ? record.appointmentdocuments : null
        this.formData.appointmentdocumentspath = record.appointmentdocumentspath
      } else {
        this.formData.fileList1 = []
        this.formData.appointmentdocuments = null
        this.formData.appointmentdocumentspath = null
      }
      if (record.removaldocumentspath) {
        this.formData.fileList2 = [{name: record.removaldocuments, url: record.removaldocumentspath, status: 'done', uid: this.formData.id}]
        this.formData.removaldocuments = record.removaldocumentspath ? record.removaldocuments : null
        this.formData.removaldocumentspath = record.removaldocumentspath
      } else {
        this.formData.fileList2 = []
        this.formData.removaldocuments = null
        this.formData.removaldocumentspath = null
      }
    },
    onSelectChange (selectedRowKeys) {
      console.log('selectedRowKeys changed: ', selectedRowKeys)
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
            that.$delete('system/administrative/post/delete/' + that.selectedRowKeys.toString()).then(res => {
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
    eventdate (column) {
      this.formData.column = column
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
      this.picFile = file
      // return isJpgOrPng && isLt2M
    },
    // 自定义上传
    upLoad () {
      const formImgData = new FormData()
      formImgData.append('file', this.picFile)
      // formImgData.append('data', JSON.stringify(this.formData))
      this.$upload('system/employee/picture/upload/electronic', formImgData).then(res => {
        if (res.data) {
          if (this.formData.column === 'appointment') {
            this.formData.fileList1 = [{name: res.data.path, url: res.data.path, status: 'done', uid: this.formData.id}]
            this.formData.appointmentdocuments = res.data.fileName
            this.formData.appointmentdocumentspath = res.data.path
          } else {
            this.formData.fileList2 = [{name: res.data.path, url: res.data.path, status: 'done', uid: this.formData.id}]
            this.formData.removaldocuments = res.data.path
            this.formData.removaldocumentspath = res.data.path
          }
          this.$message.success('上传成功！')
        }
      })
    },
    // 移除文件
    removeFile (info) {
      console.log(info, 55555)
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
          params.depttype = this.formData.depttype
          params.dept = this.formData.dept
          params.job = this.formData.job
          params.appointmentdocuments = this.formData.appointmentdocuments
          params.appointmentdocumentspath = this.formData.appointmentdocumentspath
          params.removaldocuments = this.formData.removaldocuments
          params.removaldocumentspath = this.formData.removaldocumentspath
          let http = this.btnType === 'addBtn' ? this.$post : this.$put
          http('system/administrative/post', {...params}).then(res => {
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
      this.formData.depttype = null
      this.formData.dept = null
      this.formData.job = null
      this.formData.fileList1 = []
      this.formData.fileList2 = []
      this.formData.column = null
      this.formData.appointmentdocuments = null
      this.formData.appointmentdocumentspath = null
      this.formData.removaldocuments = null
      this.formData.removaldocumentspath = null
    }
  }
}
</script>
<style lang="less" scoped>
  .table8{
    margin-top: 15px;
  }
  .actionBtn {
    margin-bottom: 10px;
    text-align: right;
  }
</style>
