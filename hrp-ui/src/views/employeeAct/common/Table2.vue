<!--教育及培训-->
<template>
  <div>
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
      :rowKey="record => record.id"
      :data-source="tableData"
      bordered
      :pagination="false"
      :scroll="{ x: 1400 }"
    >
      <template slot="academicname"  slot-scope="text, record">
        <upload-file
          name="filexlzslook"
          :baseId="record.id"
          refTab="education_xlzs"
          :isEdit="false"
          btnTitle="上传学历证书附件"
          ></upload-file>
      </template>
      <template slot="degreesname"  slot-scope="text, record">
        <upload-file
          name="filexwzslook"
          :baseId="record.id"
          refTab="education_xwzs"
          :isEdit="false"
          btnTitle="上传学位证书附件"
          ></upload-file>
      </template>
      <template slot="xlzsdzbaname"  slot-scope="text, record">
        <upload-file
          name="filedzbablook"
          :baseId="record.id"
          refTab="education_dzbab"
          :isEdit="false"
          btnTitle="上传电子备案表附件"
          ></upload-file>
      </template>
      <!-- <template v-if="record.academicname" slot="academicname"  slot-scope="text, record">
        <a href="javascript:;" @click="downRow(record.academicname,record.academicpath)"><a-icon type="download" />   {{record.academicname}}</a>
      </template>
      <template v-if="record.degreesname" slot="degreesname"  slot-scope="text, record">
        <a href="javascript:;" @click="downRow(record.degreesname,record.degreespath)"><a-icon type="download" />   {{record.degreesname}}</a>
      </template>
      <template v-if="record.xlzsdzbaname" slot="xlzsdzbaname"  slot-scope="text, record">
        <a href="javascript:;" @click="downRow(record.xlzsdzbaname,record.xlzsdzbapath)"><a-icon type="download" />   {{record.xlzsdzbaname}}</a>
      </template> -->
      <!--<template slot="operation" slot-scope="text, record">-->
        <!--<a-button class="editable-add-btn" @click="editRow(record, 'editBtn')">-->
          <!--编辑-->
        <!--</a-button>-->
      <!--</template>-->
      <a slot="operation" slot-scope="text, record" href="javascript:;" @click="editRow(record, 'editBtn')">编辑</a>
    </a-table>
    <a-modal class="ant-modal-nofooter" v-model="coreVisible" :title="btnType === 'addBtn' ? '新增' : '修改'" :width="650" @ok="handleOk">
      <a-form-model
        ref="formRules"
        :model="formData"
        :rules="formRules"
        :label-col="labelCol"
        :wrapper-col="wrapperCol"
      >
        <a-row>
          <a-col :span="12">
            <!--<a-form-model-item label="开始时间" prop="startdate">-->
              <!--<a-date-picker-->
                <!--:value="getTime(formData.startdate)"-->
                <!--:format="dateFormat"-->
                <!--:valueFormat="dateFormat"-->
                <!--@change="startdateChange"-->
              <!--/>-->
            <!--</a-form-model-item>-->
            <a-form-model-item label="毕业时间" prop="enddate">
              <a-date-picker
                :value="getTime(formData.enddate)"
                :format="dateFormat"
                :valueFormat="dateFormat"
                @change="enddateChange"
              />
            </a-form-model-item>
            <a-form-model-item label="学位" prop="degrees">
              <a-select v-model="formData.degrees"  allowClear>
                <a-select-option v-for="item in zidianInfo.degrees" :value="item.keyy" :key="item.keyy">
                  {{item.valuee}}
                </a-select-option>
              </a-select>
            </a-form-model-item>
            <a-form-model-item label="专业" prop="profession">
              <a-input
                v-model="formData.profession"
                allowClear
              />
            </a-form-model-item>
            <a-form-model-item label="导师" prop="tutor">
              <a-input
                v-model="formData.tutor"
                allowClear
              />
            </a-form-model-item>

            <a-form-model-item label="学历证书" prop="academicname">
              <!-- <a href="javascript:;" @click="downRow(formData.academicname,formData.academicpath)">{{formData.academicname}}</a> -->
              <upload-file
                name="filexlzs"
                :baseId="formData.id"
                refTab="education_xlzs"
                :isCallback="true"
                @fileData="fileXlzsData"
                btnTitle="上传学历证书附件"
                ></upload-file>
            </a-form-model-item>
            <a-form-model-item label="电子备案表" prop="xlzsdzbaname">
              <!-- <a href="javascript:;" @click="downRow(formData.xlzsdzbaname,formData.xlzsdzbapath)">{{formData.xlzsdzbaname}}</a> -->
              <upload-file
                name="filedzbab"
                :baseId="formData.id"
                refTab="education_dzbab"
                :isCallback="true"
                @fileData="fileDzbabData"
                btnTitle="上传电子备案表附件"
                ></upload-file>
            </a-form-model-item>
            <a-form-model-item label="备注" prop="memo">
              <a-input
                type="textarea"
                v-model="formData.memo"
                allowClear
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item label="学习形式" prop="studymethod">
              <a-select v-model="formData.studymethod"  allowClear>
                <a-select-option v-for="item in zidianInfo.studymethod" :value="item.keyy" :key="item.keyy">
                  {{item.valuee}}
                </a-select-option>
              </a-select>
            </a-form-model-item>
            <a-form-model-item label="学历" prop="academic">
              <a-select v-model="formData.academic"  allowClear>
                <a-select-option v-for="item in zidianInfo.academic" :value="item.keyy" :key="item.keyy">
                  {{item.valuee}}
                </a-select-option>
              </a-select>
            </a-form-model-item>
            <a-form-model-item label="毕业学校" prop="school">
              <a-input
                v-model="formData.school"
                allowClear
              />
            </a-form-model-item>
            <a-form-model-item label="是否职前" prop="tutor">
              <a-radio-group v-model="formData.zqflag">
                <a-radio :value="1">是</a-radio>
                <a-radio :value="0">否</a-radio>
              </a-radio-group>
            </a-form-model-item>

            <a-form-model-item label="学位证书" prop="degreesname">
              <!-- <a @click="downRow(formData.degreesname,formData.degreespath)">{{formData.degreesname}}</a> -->
              <upload-file
                name="filexwzs"
                :baseId="formData.id"
                refTab="education_xwzs"
                :isCallback="true"
                @fileData="fileXwzsData"
                btnTitle="上传学位证书附件"
                ></upload-file>
            </a-form-model-item>
          </a-col>
        </a-row>
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
import { getTime } from '../../../utils/filter'
import UploadFile from '../../common/UploadFile'
import moment from 'moment'

export default {
  components: { UploadFile },
  data () {
    return {
      dateFormat: 'YYYY-MM-DD',
      pageStatus: '',
      coreVisible: false,
      tableData: [],
      selectedRowKeys: [],
      labelCol: { span: 6 },
      wrapperCol: { span: 12 },
      other: '',
      fileInfo: {},
      btnType: 'addBtn',
      startdateValue: 0,
      enddateValue: 0,
      lastdateValue: 0,
      lastdate: '',
      disEdit: false,
      formData: {
        employeeid: null,
        // startdate: '',
        enddate: '',
        studymethod: '',
        academic: '',
        school: '',
        profession: '',
        degrees: '',
        tutor: '',
        memo: '',
        fileList1: [],
        fileList2: [],
        academicpath: '',
        degreespath: '',
        academicname: '',
        degreesname: '',
        xlzsdzbaname: '',
        xlzsdzbapath: '',
        column: '',
        zqflag: '',
        xlzsfileid: '',
        dzbabfileid: '',
        xwzsfileid: ''
      },
      zqflagOptions: [
        {keyy: 0, valuee: '否'},
        {keyy: 1, valuee: '是'},
      ],
      formRules: {
        // startdate: [
        //   { required: true, message: '请输入开始时间', trigger: 'change' }
        // ],
        enddate: [
          { required: true, message: '请输入结束时间', trigger: 'change' }
        ],
        // academic: [
        //   { required: true, message: '请输入学历', trigger: 'change' }
        // ],
        // degrees: [{ required: true, message: '请输入学位', trigger: 'change' }]
      }
    }
  },
  computed: {
    ...mapGetters(
      ['educationInfo', 'queryData', 'baseInfo', 'zidianInfo']
    ),
    columns () {
      let { studymethod, academic, degrees } = this.zidianInfo
      return [
        // { title: '开始时间', dataIndex: 'startdate', width: 120 },
        { title: '结束时间', dataIndex: 'enddate', width: 120 },
        { title: '学习形式',
          dataIndex: 'studymethod',
          width: 100,
          customRender: (text, row, index) => {
            let option = studymethod.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '学历',
          dataIndex: 'academic',
          width: 100,
          customRender: (text, row, index) => {
            let option = academic.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '学位',
          dataIndex: 'degrees',
          width: 100,
          customRender: (text, row, index) => {
            let option = degrees.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '毕业学校', dataIndex: 'school', width: 100 },
        { title: '专业', dataIndex: 'profession', width: 100 },
        { title: '导师', dataIndex: 'tutor', width: 100 },
        { title: '备注', dataIndex: 'memo', width: 100 },
        {
          title: '学历证书附件',
          dataIndex: 'academicname',
          width: 120,
          scopedSlots: { customRender: 'academicname' }
        },
        {
          title: '学位证书附件',
          dataIndex: 'degreesname',
          width: 120,
          scopedSlots: { customRender: 'degreesname' }
        },
        {
          title: '电子备案表',
          dataIndex: 'xlzsdzbaname',
          width: 120,
          scopedSlots: { customRender: 'xlzsdzbaname' }
        },
        { title: '是否职前',
          dataIndex: 'zqflag',
          width: 100,
          customRender: (text, row, index) => {
            let option = this.zqflagOptions.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: '操作',
          key: 'operation',
          fixed: 'right',
          width: 90,
          scopedSlots: { customRender: 'operation' }
        }
      ]
    }
  },
  watch: {
    queryData: {
      handler: function (data) {
        if (data) {
          this.pageStatus = data.pageStatus
          this.formData.employeeid = data.employeeid
        }
      },
      immediate: true
    },
    educationInfo: {
      handler: function (data) {
        if (data && data.length > 0) {
          this.tableData = data
        } else {
          this.tableData = []
        }
      },
      immediate: true
    },
    // 'formData.startdate': {
    //   handler: function (data) {
    //     if (data) {
    //       this.startdateValue = moment(data).valueOf()
    //       if (this.btnType === 'addBtn' && this.startdateValue && this.enddateValue && this.startdateValue >= this.enddateValue) {
    //         this.$message.warning('开始时间不能大于或等于结束时间')
    //         this.formData.startdate = ''
    //         this.startdateValue = 0
    //       }
    //     }
    //   },
    //   immediate: true
    // },
    // 'formData.enddate': {
    //   handler: function (data) {
    //     if (data) {
    //       this.enddateValue = moment(data).valueOf()
    //       if (this.btnType === 'addBtn' && this.startdateValue && this.enddateValue && this.startdateValue >= this.enddateValue) {
    //         this.$message.warning('结束时间不能小于或等于开始时间')
    //         this.formData.enddate = ''
    //         this.enddateValue = 0
    //       }
    //     }
    //   },
    //   immediate: true
    // }
  },
  mounted () {},
  methods: {
    ...mapActions(['getSearch']),
    // 初始化时间
    getTime,
    startdateChange (date) {
      this.formData.startdate = date
    },
    enddateChange (date) {
      this.formData.enddate = date
    },
    fileXwzsData (filelist) {
      if (this.formData.id == null) {
        if (filelist.length == 0) {
          this.formData.xwzsfileid = null
        } else {
          this.formData.xwzsfileid = filelist[0].uid
        }
      }
    },
    fileXlzsData (filelist) {
      debugger
      if (this.formData.id == null) {
        if (filelist.length == 0) {
          this.formData.xlzsfileid = null
        } else {
          this.formData.xlzsfileid = filelist[0].uid
        }
      }
    },
    fileDzbabData (filelist) {
      if (this.formData.id == null) {
        if (filelist.length == 0) {
          this.formData.dzbabfileid = null
        } else {
          this.formData.dzbabfileid = filelist[0].uid
        }
      }
    },
    // 新增
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
      this.formData.studymethod = this.formData.studymethod ? this.formData.studymethod.toString() : ''
      this.formData.academic = this.formData.academic ? this.formData.academic.toString() : ''
      this.formData.degrees = this.formData.degrees ? this.formData.degrees.toString() : ''
      if (record.academicpath) {
        this.formData.fileList1 = [{name: record.academicpath || '', url: record.academicpath || '', status: 'done', uid: this.formData.id}]
        this.formData.academicpath = record.academicpath
        this.formData.academicname = record.academicname
      } else {
        this.formData.fileList1 = []
        this.formData.academicpath = null
        this.formData.academicname = null
      }
      if (record.degreespath) {
        this.formData.fileList2 = [{name: record.degreespath || '', url: record.degreespath || '', status: 'done', uid: this.formData.id}]
        this.formData.degreespath = record.degreespath
        this.formData.degreesname = record.degreesname
      } else {
        this.formData.fileList2 = []
        this.formData.degreespath = null
        this.formData.degreesname = null
      }
      if (record.xlzsdzbapath) {
        this.formData.fileList2 = [{name: record.xlzsdzbapath || '', url: record.xlzsdzbapath || '', status: 'done', uid: this.formData.id}]
        this.formData.xlzsdzbapath = record.xlzsdzbapath
        this.formData.xlzsdzbaname = record.xlzsdzbaname
      } else {
        this.formData.fileList2 = []
        this.formData.xlzsdzbapath = null
        this.formData.xlzsdzbaname = null
      }
    },
    getname (value, name) {
    },
    // 选中
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    // 删除
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
            that.$delete('system/education/delete/' + that.selectedRowKeys.toString()).then(res => {
              if (res.data.status === 'OK') {
                that.$message.success('删除成功！')
                that.$store.dispatch('getSearch', {vm: that, id: that.formData.employeeid})
                // 需要在回调查询解救
              }
            }).catch(err => {
              that.$message.warning(err)
            })
          },
          onCancel () {
            that.$message.warning('取消删除！')
          }
        })
      }
    },
    // 新增确认弹框
    handleOk (e) {
      this.coreVisible = false
    },
    upLoadFile (type) {
      this.formData.column = type
    },
    // 下载
    downRow (filename, path) {
      let url = this.$baseURL + 'system/employee/download/' + filename + '/' + path
      let eleLink = document.createElement('a')
      eleLink.href = url
      eleLink.click()
      eleLink.remove()
    },
    // 上传文件改变触发
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
      let that = this
      this.$confirm({
        title: '确定删除选中记录吗?',
        content: '',
        onOk () {
          that.$delete('system/employee/delete/').then(res => {
            if (res.data.status === 'OK') {
              that.$message.success('删除成功！')
              that.$store.dispatch('getSearch', {vm: that, id: that.formData.employeeid})
              // 需要在回调查询解救
            }
          }).catch(err => {
            that.$message.warning(err)
          })
        },
        onCancel () {
          that.$message.warning('取消删除！')
        }
      })
    },
    // 图片上传之前的校验
    beforeUpload (file) {
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
      // formImgData.append('data', JSON.stringify(this.formData))
      this.$upload('system/employee/picture/upload/education', formImgData).then(res => {
        if (res.data) {
          if (this.formData.column === 'academic') {
            this.formData.fileList1 = [{name: res.data.path, url: res.data.path, status: 'done', uid: this.formData.id}]
            this.formData.academicpath = res.data.path
            this.formData.academicname = res.data.fileName
          } else {
            this.formData.fileList2 = [{name: res.data.path, url: res.data.path, status: 'done', uid: this.formData.id}]
            this.formData.degreespath = res.data.path
            this.formData.degreesname = res.data.fileName
          }
          this.$message.success('上传成功！')
        }
      }).catch(err => {
        this.$message.success(err)
      })
    },
    // 新增保存
    onSubmit () {
      this.$refs.formRules.validate(valid => {
        if (valid) {
          let params = {}
          params.id = this.btnType === 'addBtn' ? null : this.formData.id
          params.employeeid = this.formData.employeeid
          // params.startdate = this.formData.startdate
          params.enddate = this.formData.enddate
          params.studymethod = this.formData.studymethod
          params.academic = this.formData.academic
          params.school = this.formData.school
          params.profession = this.formData.profession
          params.degrees = this.formData.degrees
          params.tutor = this.formData.tutor
          params.memo = this.formData.memo
          params.academicpath = this.formData.academicpath
          params.academicname = this.formData.academicname
          params.academic = this.formData.academic
          params.degreespath = this.formData.degreespath
          params.degrees = this.formData.degrees
          params.degreesname = this.formData.degreesname
          params.xlzsdzbapath = this.formData.xlzsdzbapath
          params.xlzsdzbaname = this.formData.xlzsdzbaname
          params.zqflag = this.formData.zqflag
          params.xlzsfileid = this.formData.xlzsfileid
          params.dzbabfileid = this.formData.dzbabfileid
          params.xwzsfileid = this.formData.xwzsfileid
          let http = this.btnType === 'addBtn' ? this.$post : this.$put
          http('system/education', {...params}).then(res => {
            if (res.data.status === 'OK') {
              this.tableData = []
              this.$store.dispatch('getSearch', {vm: this, id: this.formData.employeeid})
              this.resetForm()
              this.coreVisible = false
              this.$message.success(this.btnType === 'addBtn' ? '新增成功！' : '修改成功！')
              this.$refs.filexlzs.reset()
              this.$refs.filexwzs.reset()
              this.$refs.dzbab.reset()
            }
          }).catch(err => {
            this.$message.warning(err)
          })
        } else {
          return false
        }
      })
    },
    // 重置
    resetForm () {
      // this.formData.startdate = null
      this.formData.enddate = null
      this.formData.degrees = null
      this.formData.school = null
      this.formData.profession = null
      this.formData.academic = null
      this.formData.tutor = null
      this.formData.memo = null
      this.formData.fileList1 = []
      this.formData.fileList2 = []
      this.formData.academicpath = null
      this.formData.academicname = null
      this.formData.degreespath = null
      this.formData.xlzsdzbapath = null
      this.formData.xlzsdzbaname = null
      this.formData.degreesname = null
      this.formData.column = null
      this.formData.xlzsfileid = null
      this.formData.dzbabfileid = null
      this.formData.xwzsfileid = null
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