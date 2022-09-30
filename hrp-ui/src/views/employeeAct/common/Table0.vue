<!--员工工号信息-->
<template>
  <div class="table0">
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
      :scroll="{ x: 1200 }"
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
            :disabled="disEdit"
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
        <a-form-model-item label="员工工号" prop="employeecode">
          <a-input
            v-model="formData.employeecode"
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
  { title: '员工工号', dataIndex: 'employeecode', key: '3' },
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
      disEdit: false,
      query: {},
      formData: {
        startdate: '',
        enddate: '9999-12-31',
        employeecode: '',
        employeeid: null
      },
      startdateValue: 0,
      enddateValue: 0,
      lastdateValue: 0,
      lastdate: '',
      coreRules: {
        startdate: [
          { required: true, message: '请输入开始时间', trigger: 'change' }
        ],
        enddate: [
          { required: true, message: '请输入结束时间', trigger: 'change' }
        ],
        employeecode: [
          { required: true, message: '请输入人员编号', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    ...mapGetters(['codeInfo', 'queryData'])
  },
  watch: {
    queryData: {
      handler: function (data) {
        if (data) {
          this.query = data
          this.formData.employeeid = data.employeeid
          this.formData.startdate = data.startdate
        }
      },
      immediate: true
    },
    codeInfo: {
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
          this.formData.startdate = this.query.startdate
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
          if (this.btnType === 'addBtn' && this.lastdate && this.startdateValue <= this.lastdateValue) {
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
          if (this.btnType === 'addBtn' && this.lastdate && this.startdateValue && this.enddateValue && this.startdateValue >= this.enddateValue) {
            this.$message.warning('结束时间不能小于或等于开始时间')
            this.formData.enddate = '9999-12-31'
          }
        }
      },
      immediate: true
    }
  },
  mounted () {

  },
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
    clickAdd (type) {
      this.btnType = type
      this.coreVisible = true
      this.disEdit = false
      this.formData.id = null
      this.resetForm()
      this.formData.enddate = '9999-12-31'
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
            that.$delete('system/code/delete/' + that.selectedRowKeys.toString()).then(res => {
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
          params.startdate = moment(this.formData.startdate).format('YYYY-MM-DD')
          params.enddate = this.formData.enddate
          params.employeecode = this.formData.employeecode
          params.employeeid = this.formData.employeeid
          params.id = this.btnType === 'addBtn' ? null : this.formData.id
          let http = this.btnType === 'addBtn' ? this.$post : this.$put
          http('system/code', {...params}).then(res => {
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
      this.disEdit = true
      this.formData = {...this.formData, ...record}
    },
    resetForm () {
      this.formData.employeecode = null
    }
  }
}
</script>
<style lang="less" scoped>
  .table0 {
    th.column-money,
    td.column-money {
      text-align: right !important;
    }
    .actionBtn {
      margin-bottom: 10px;
      text-align: right;
    }
    .ant-calendar-picker {
      width: 100%;
    }
  }
</style>
<style lang="less">
  @import "../../../../static/less/Common";
</style>
