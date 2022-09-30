<!--岗位信息-->
<template>
  <div class="table6">
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
      <!-- <template v-if="record.certificatename" slot="certificatename" slot-scope="text, record"> -->
      <template slot="certificatename" slot-scope="text, record">
        <!-- <a  href="javascript:;" @click="downRow(record.certificatename,record.certificatepath)"><a-icon type="download" />  {{record.certificatename}}</a> -->
        <upload-file
          name="filezslook"
          :baseId="record.id"
          :isEdit="false"
          refTab="jobtype_zs"
          btnTitle="上传证书附件"
          ></upload-file>
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
        <a-form-model-item label="岗位类型" prop="jobtype">
          <a-select v-model="formData.jobtype" @change="getjobname(formData.jobtype, 'job')" allowClear>
            <a-select-option v-for="item in zidianInfo.jobtype" :value="item.keyy" :key="item.keyy">
              {{item.valuee}}
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="岗位等级" prop="joblevel">
          <a-select v-model="formData.joblevel" @change="getjoblevelname(formData.joblevel, 'joblevel')" allowClear>
            <a-select-option v-for="item in joblevelOptions" :value="item.keyy" :key="item.keyy">
              {{item.valuee}}
            </a-select-option>
          </a-select>
        </a-form-model-item>
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
        <a-form-model-item label="聘任单位" prop="prdw">
          <a-input
            v-model="formData.prdw"
            allowClear
          />
        </a-form-model-item>
        <a-form-model-item label="证书名称" prop="certificatename">
          <!-- <a href="javascript:;" @click="downRow(formData.certificatename,formData.certificatepath)">{{formData.certificatename}}</a> -->
          <upload-file
            name="filezs"
            :baseId="formData.id"
            refTab="jobtype_zs"
            :isCallback="true"
            @fileData="fileZsData"
            btnTitle="上传证书附件"
            ></upload-file>
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
import { mapGetters, mapActions } from 'vuex'
import { getTime, compareTime } from '../../../utils/filter'
import UploadFile from '../../common/UploadFile'
import moment from 'moment'

export default {
  components: { UploadFile },
  data () {
    return {
      dateFormat: 'YYYY-MM-DD',
      coreVisible: false,
      tableData: [],
      jobTypeList: [],
      selectedRowKeys: [],
      labelCol: { span: 6 },
      wrapperCol: { span: 14 },
      other: '',
      btnType: 'addBtn',
      startdateValue: 0,
      enddateValue: 0,
      lastdateValue: 0,
      lastdate: '',
      disEdit: false,
      formData: {
        startdate: new Date().toLocaleDateString(),
        enddate: '9999-12-31',
        jobtype: '',
        joblevel: '',
        joblevelname: '',
        employeeid: null,
        certificatepath: null,
        certificatename: null,
        zsfileid:  null,
        prdw: '',
        fileList: []
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
      ['jobTypeInfo', 'queryData', 'zidianInfo', 'joblevelOptions']
    ),
    columns () {
      let { jobtype, joblevel } = this.zidianInfo
      return [
        { title: '开始时间', dataIndex: 'startdate', key: '1' },
        { title: '结束时间', dataIndex: 'enddate', key: '2' },
        { title: '任职年限', dataIndex: 'appointmentdate', key: '3' },
        {
          title: '岗位类型',
          dataIndex: 'jobtype',
          key: '4',
          customRender: (text, row, index) => {
            let option = jobtype.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: '岗位等级',
          dataIndex: 'joblevel',
          key: '5',
          customRender: (text, row, index) => {
            let option = joblevel.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '聘任单位', dataIndex: 'prdw', key: '6' },
        {
          title: '证书名称',
          dataIndex: 'certificatename',
          width: 150,
          scopedSlots: { customRender: 'certificatename' }
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
    jobTypeInfo: {
      handler: function (data) {
        if (data && data.length > 0) {
          data.forEach(item => {
            let startdatevalue = moment(item.startdate).format('YYYY')
            let enddatevalue = moment(item.enddate).format('YYYY')
            let appointmentdatevalue
            let currentdatevalue = moment(new Date()).format('YYYY')
            if (enddatevalue == 9999) {
              appointmentdatevalue = currentdatevalue - startdatevalue
            } else {
              appointmentdatevalue = enddatevalue - startdatevalue
            }
            item['appointmentdate'] = appointmentdatevalue + ' 年'
          })
          this.tableData = data
        } else {
          this.tableData = []
        }
      },
      immediate: true
    },
    jobTypeList: {
      handler: function (data) {
        if (data && data.length > 0) {
          if (this.btnType === 'addBtn') {
            this.lastdateValue = moment(data[0].startdate).valueOf()
            this.lastdate = data[0].startdate
            this.formData.startdate = moment(this.lastdate, 'YYYY-MM-DD').add(1, 'days')
          }
        } else {
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
          if (this.btnType === 'addBtn' && this.lastdateValue && this.startdateValue <= this.lastdateValue) {
            this.$message.warning('开始时间不能小于或等于上一次开始时间！')
            this.formData.startdate = moment(this.lastdate, 'YYYY-MM-DD').add(1, 'days')
          } else {
            this.formData.startdate = data
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
    ...mapActions(['getjoblevel']),

    // 初始化时间
    getTime,
    startdateChange (date) {
      this.formData.startdate = date
    },
    enddateChange (date) {
      this.formData.enddate = date
    },
    fileZsData (filelist) {
      if (this.formData.id == null) {
        if (filelist.length == 0) {
          this.formData.zsfileid = null
        } else {
          this.formData.zsfileid = filelist[0].uid
        }
      }
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
      this.formData.jobtype = this.formData.jobtype ? this.formData.jobtype.toString() : ''
      if (record.certificatepath) {
        this.formData.fileList = [{name: record.certificatepath, url: record.certificatepath, status: 'done', uid: record.id}]
        this.formData.certificatepath = record.certificatepath
        this.formData.certificatename = record.certificatename ? record.certificatename : null
      } else {
        this.formData.fileList = []
        this.formData.certificatepath = null
        this.formData.certificatename = null
      }
      if (this.formData.jobtype) {
        this.$store.dispatch('getjoblevel', {vm: this, id: this.formData.jobtype})
      }
    },
    getjobname (value, name) {
      if (value) {
        this.$store.dispatch('getjoblevel', {vm: this, id: value})
        this.formData.joblevel = ''
      }
      this.jobTypeList = this.jobTypeInfo.filter(item => item.jobtype === value)
      this.jobTypeList = this.jobTypeList.sort(compareTime('startdate', false))
    },
    getjoblevelname (value) {
      if (value && !this.formData.jobtype) {
        this.formData.joblevel = ''
        this.$message.warning('请选择岗位类型！')
      }
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
            that.$delete('system/job/type/delete/' + that.selectedRowKeys.toString()).then(res => {
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
    downRow (filename, path) {
      let url = this.$baseURL + 'system/employee/download/' + filename + '/' + path
      let eleLink = document.createElement('a')
      eleLink.href = url
      eleLink.click()
      eleLink.remove()
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
      this.$upload('system/employee/picture/upload/title', formImgData).then(res => {
        if (res.data) {
          this.formData.fileList = [{name: res.data.certificatepath, url: res.data.certificatepath, status: 'done', uid: this.formData.id}]
          this.$message.success('上传成功！')
          this.formData.certificatepath = res.data.certificatepath
          this.formData.certificatename = res.data.fileName
        }
      }).catch(err => {
        this.$message.warning(err)
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
          params.jobtype = this.formData.jobtype
          params.joblevel = this.formData.joblevel
          params.joblevelname = this.formData.joblevelname
          params.certificatepath = this.formData.certificatepath
          params.certificatename = this.formData.certificatename
          params.prdw = this.formData.prdw
          params.zsfileid = this.formData.zsfileid
          let http = this.btnType === 'addBtn' ? this.$post : this.$put
          http('system/job/type', {...params}).then(res => {
            if (res.data.status === 'OK') {
              this.tableData = []
              this.$store.dispatch('getSearch', {vm: this, id: this.formData.employeeid})
              this.resetForm()
              this.coreVisible = false
              this.$message.success(this.btnType === 'addBtn' ? '新增成功！' : '修改成功！')
            }
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    resetForm () {
      this.formData.jobtype = null
      this.formData.joblevelname = null
      this.formData.joblevel = null
      this.formData.fileList = []
      this.formData.certificatepath = null
      this.formData.certificatename = null
      this.formData.prdw = null
      this.formData.zsfileid = null
    }
  }
}
</script>
<style lang="less" scoped>
  .table6{
    margin-top: 15px;
  }
  .actionBtn {
    margin-bottom: 10px;
    text-align: right;
  }
</style>
