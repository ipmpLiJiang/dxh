<!--取得职称等级信息-->
<template>
  <div class="table9">
    <div class="actionBtn">
      <a-button type="primary" class="editable-add-btn editable-margin-right" @click="clickAdd('addBtn')">
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
        <!-- <a  href="javascript:;" @click="downRow(record.certificatename,record.certificatepath)">
          <a-icon type="download" />  {{record.certificatename}}
        </a> -->
        <upload-file
          name="filezslook"
          :baseId="record.id"
          :isEdit="false"
          refTab="titleget_zs"
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
        <a-form-model-item label="获得时间" prop="startdate">
          <a-date-picker
            v-model="formData.startdate"
            :format="dateFormat"
            :valueFormat="dateFormat"
            @change="startdateChange"
          />
        </a-form-model-item>
        <a-form-model-item label="职称" prop="title">
          <a-select v-model="formData.title" @change="getname(formData.title, 'title')" allowClear>
            <a-select-option v-for="item in zidianInfo.title" :value="item.keyy" :key="item.keyy">
              {{item.valuee}}
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="专业技术职务" prop="duty">
          <a-select v-model="formData.duty" @change="getdutyname(formData.duty, 'duty')" allowClear>
            <a-select-option v-for="item in dutyOptions" :value="item.keyy" :key="item.keyy">
              {{item.valuee}}
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="证书编号" prop="number">
          <a-input
            v-model="formData.number"
            allowClear
          />
        </a-form-model-item>
        <a-form-model-item label="证书名称" prop="certificatename">
          <!-- <a href="javascript:;" @click="downRow(formData.certificatename,formData.certificatepath)">{{formData.certificatename}}</a> -->
          <upload-file
            name="filezs"
            :baseId="formData.id"
            refTab="titleget_zs"
            :isCallback="true"
            @fileData="fileZsData"
            btnTitle="上传证书附件"
            ></upload-file>
        </a-form-model-item>
        <a-form-model-item label="专业名称" prop="zyname">
          <a-input
            v-model="formData.zyname"
            allowClear
          />
        </a-form-model-item>
        <a-form-model-item label="批准单位" prop="pzdw">
          <a-input
            v-model="formData.pzdw"
            allowClear
          />
        </a-form-model-item>
        <a-form-model-item label="批准文号" prop="pzwh">
          <a-input
            v-model="formData.pzwh"
            allowClear
          />
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
import UploadFile from '../../common/UploadFile'
import { getTime } from '../../../utils/filter'
import moment from 'moment'

export default {
  components: { UploadFile },
  data () {
    return {
      dateFormat: 'YYYY-MM-DD',
      coreVisible: false,
      tableData: [],
      selectedRowKeys: [],
      labelCol: { span: 6 },
      wrapperCol: { span: 14 },
      fileInfo: {},
      btnType: 'addBtn',
      lastdate: '',
      formData: {
        title: '',
        number: '',
        duty: '',
        dutyname: '',
        employeeid: null,
        certificatepath: null,
        certificatename: null,
        zyname: '',
        pzdw: '',
        pzwh: '',
        zsfileid: '',
        fileList: []
      },
      formRules: {
        // startdate: [
        //   { required: true, message: '请输入开始时间', trigger: 'change' }
        // ],
      }
    }
  },
  computed: {
    ...mapGetters(
      ['getTitleInfo', 'queryData', 'zidianInfo', 'dutyOptions']
    ),
    columns () {
      let { title, duty } = this.zidianInfo
      return [
        { title: '获得时间', dataIndex: 'startdate', key: '1' },
        { title: '职称',
          dataIndex: 'title',
          key: '2',
          customRender: (text, row, index) => {
            let option = title.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '专业技术职务',
          dataIndex: 'duty',
          key: '3',
          customRender: (text, row, index) => {
            let option = duty.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '专业名称', dataIndex: 'zyname', key: '4' },
        { title: '证书编号', dataIndex: 'number', key: '5' },
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
    getTitleInfo: {
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
    ...mapActions(['getduty']),
    // 初始化开始时间
    getTime,
    startdateChange (date) {
      this.formData.startdate = date
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
      this.formData.id = null
      this.resetForm()
    },
    editRow (record, type) {
      this.btnType = type
      this.coreVisible = true
      this.formData = {...this.formData, ...record}
      this.formData.fileList = [{name: record.certificatename, url: record.certificatepath, status: 'done', uid: this.formData.id}]
      if (record.certificatepath) {
        this.formData.fileList = [{name: record.certificatepath, url: record.certificatepath, status: 'done', uid: this.formData.id}]
        this.formData.certificatepath = record.certificatepath
        this.formData.certificatename = record.certificatename ? record.certificatename : null
      } else {
        this.formData.fileList = []
        this.formData.certificatepath = null
        this.formData.certificatename = null
      }
      if (this.formData.title) {
        this.$store.dispatch('getduty', {vm: this, id: this.formData.title})
      }
      if (this.formData.duty) {
        this.getdutyname(this.formData.duty, 'duty')
      }
    },
    getname (value, name) {
      if (value) {
        this.formData.duty = null
        this.formData.dutyname = null
        this.$store.dispatch('getduty', {vm: this, id: value})
      }
    },
    getdutyname (value, name) {
      if (!value) {
        this.formData[name + 'name'] = null
        return
      }
      if (!this.formData.title) {
        this.$message.warning('请选择职称！')
        this.formData[name + 'name'] = null
        this.formData[name] = null
        return
      }
      if (this.dutyOptions && this.dutyOptions.length > 0) {
        this.formData[name + 'name'] = this.dutyOptions.filter(item => item.keyy === value)[0].valuee
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
            that.$delete('system/titleget/delete/' + that.selectedRowKeys.toString()).then(res => {
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
      formImgData.append('data', JSON.stringify(this.formData))
      this.$upload('system/employee/picture/upload/title', formImgData).then(res => {
        if (res.data) {
          this.formData.fileList = [{name: res.data.path, url: res.data.path, status: 'done', uid: this.formData.id}]
          this.formData.certificatepath = res.data.path
          this.formData.certificatename = res.data.fileName
          this.$message.success('上传成功！')
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
          params.title = this.formData.title
          params.number = this.formData.number
          params.duty = this.formData.duty
          params.dutyname = this.formData.dutyname
          params.zyname = this.formData.zyname
          params.pzdw = this.formData.pzdw
          params.pzwh = this.formData.pzwh
          params.certificatepath = this.formData.certificatepath
          params.zsfileid = this.formData.zsfileid
          let http = this.btnType === 'addBtn' ? this.$post : this.$put
          http('/system/titleget', {...params}).then(res => {
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
      this.formData.title = null
      this.formData.number = null
      this.formData.duty = null
      this.formData.dutyname = null
      this.formData.certificatepath = null
      this.zyname = null
      this.pzdw = null
      this.pzwh = null
      this.formData.zsfileid = null
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