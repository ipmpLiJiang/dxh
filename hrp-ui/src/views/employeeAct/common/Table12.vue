<!--进修-->
<template>
  <div class="table12">
    <div class="actionBtn">
      <a-button v-if="formData.employeeid" type="primary" class="editable-add-btn editable-margin-right" @click="clickAdd('addBtn')" :disabled="disAdd">
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
        :rules="coreRules"
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
        <a-form-model-item label="单位" prop="unit">
          <a-input
            v-model="formData.unit"
            allowClear
          />
        </a-form-model-item>
        <a-form-model-item label="专业" prop="profession">
          <a-input
            v-model="formData.profession"
            allowClear
          />
        </a-form-model-item>
        <a-form-model-item label="登记时间" prop="djdate">
          <a-date-picker
            :value="getTime(formData.djdate)"
            :format="dateFormat"
            :valueFormat="dateFormat"
            @change="djdateChange"
          />
        </a-form-model-item>
        <a-form-model-item label="实际结束时间" prop="realenddate">
          <a-date-picker
            :value="getTime(formData.realenddate)"
            :format="dateFormat"
            :valueFormat="dateFormat"
            @change="realenddateChange"
          />
        </a-form-model-item>
        <a-form-model-item label="备注" prop="memo">
          <a-input
            type="textarea"
            v-model="formData.memo"
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
import { getTime } from '../../../utils/filter'
import moment from 'moment'

const columns = [
  { title: '开始时间', dataIndex: 'startdate', key: '1' },
  { title: '结束时间', dataIndex: 'enddate', key: '2' },
  { title: '单位', dataIndex: 'unit', key: '3' },
  { title: '专业', dataIndex: 'profession', key: '4' },
  { title: '登记时间', dataIndex: 'djdate', key: '5' },
  { title: '实际结束时间', dataIndex: 'realenddate', key: '6' },
  { title: '备注', dataIndex: 'memo', key: '7' },
  {
    title: '操作',
    key: 'operation',
    width: 100,
    scopedSlots: { customRender: 'operation' }
  }
]

export default {
  data () {
    return {
      dateFormat: 'YYYY-MM-DD',
      disAdd: false,
      btnType: 'addBtn',
      coreVisible: false,
      tableData: [],
      columns,
      selectedRowKeys: [],
      labelCol: { span: 6 },
      wrapperCol: { span: 14 },
      query: {},
      startdateValue: 0,
      enddateValue: 0,
      formData: {
        startdate: '',
        enddate: '',
        unit: '',
        profession: '',
        djdate: '',
        realenddate: '',
        employeeid: null,
        memo: ''
      },
      coreRules: {
        // startdate: [
        //   { required: true, message: '请输入开始时间', trigger: 'change' }
        // ],
        // enddate: [
        //   { required: true, message: '请输入结束时间', trigger: 'change' }
        // ],
        // employeecode: [
        //   { required: true, message: '请输入人员编号', trigger: 'change' }
        // ]
      }
    }
  },
  computed: {
    ...mapGetters(['continueEducation', 'queryData', 'baseInfo'])
  },
  watch: {
    queryData: {
      handler: function (data) {
        if (data) {
          this.query = data
          this.formData.employeeid = data.employeeid
        }
      },
      immediate: true
    },
    continueEducation: {
      handler: function (data) {
        if (data && data.length > 0) {
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
            this.formData.startdate = ''
            this.startdateValue = 0
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
            this.formData.enddate = ''
            this.enddateValue = 0
          }
        }
      },
      immediate: true
    }
  },
  mounted () {},
  methods: {
    ...mapActions(['getSearch']),
    // 初始化开始时间
    getTime,
    // 入党时间
    startdateChange (date) {
      this.formData.startdate = date
    },
    enddateChange (date) {
      this.formData.enddate = date
    },
    djdateChange (date) {
      this.formData.djdate = date
    },
    realenddateChange (date) {
      this.formData.realenddate = date
    },
    clickAdd (type) {
      this.btnType = type
      this.coreVisible = true
      this.formData.id = null
      this.resetForm()
    },
    // 选中
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
            that.$delete('system/continue/education/delete/' + that.selectedRowKeys.toString()).then(res => {
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
    onSubmit () {
      this.$refs.formData.validate(valid => {
        if (valid) {
          let params = {}
          params.id = this.btnType === 'addBtn' ? null : this.formData.id
          params.employeeid = this.formData.employeeid
          params.startdate = moment(this.formData.startdate).format('YYYY-MM-DD')
          params.enddate = this.formData.enddate
          params.unit = this.formData.unit
          params.profession = this.formData.profession
          params.djdate = this.formData.djdate
          params.realenddate = this.formData.realenddate
          params.memo = this.formData.memo
          let http = this.btnType === 'addBtn' ? this.$post : this.$put
          http('system/continue/education', {...params}).then(res => {
            if (res.data.status === 'OK') {
              this.$store.dispatch('getSearch', {vm: this, id: this.formData.employeeid})
              this.coreVisible = false
              this.$message.success(this.btnType === 'addBtn' ? '新增成功！' : '修改成功！')
              this.resetForm()
            }
          }).catch(err => {
            this.$message.warning(err)
          })
        } else {
          return false
        }
      })
    },
    editRow (record, type) {
      this.btnType = type
      this.coreVisible = true
      this.formData = {...this.formData, ...record}
    },
    resetForm () {
      this.formData.startdate = ''
      this.formData.enddate = ''
      this.formData.unit = ''
      this.formData.profession = ''
      this.formData.djdate = ''
      this.formData.realenddate = ''
    }
  }
}
</script>
<style lang="less" scoped>
  .table12 {
    .actionBtn {
      margin-bottom: 10px;
      text-align: right;
    }
    .ant-calendar-picker {
      width: 100%;
    }
  }
</style>