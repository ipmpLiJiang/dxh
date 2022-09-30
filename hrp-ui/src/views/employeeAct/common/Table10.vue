<!--合同管理信息-->
<template>
  <div class="table10">
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
            v-model="formData.startdate"
            :format="dateFormat"
            :valueFormat="dateFormat"
            @change="startdateChange"
            :disabled="disEdit"
          />
        </a-form-model-item>
        <a-form-model-item label="结束时间" prop="enddate">
          <a-date-picker
            v-model="formData.enddate"
            :format="dateFormat"
            :valueFormat="dateFormat"
            @change="enddateChange"
            :disabled="disEdit"
          />
        </a-form-model-item>
        <!--<a-form-model-item label="试用期" prop="probation">-->
          <!--<a-date-picker-->
            <!--v-model="formData.probation"-->
            <!--:format="dateFormat"-->
            <!--:valueFormat="dateFormat"-->
            <!--@change="probationChange"-->
          <!--/>-->
        <!--</a-form-model-item>-->
        <a-form-model-item label="合同类型" prop="contracttype">
          <a-select v-model="formData.contracttype" @select="getname(formData.contracttype, 'contracttype')" allowClear>
            <a-select-option v-for="item in zidianInfo.contracttype" :value="item.keyy" :key="item.keyy">
              {{item.valuee}}
            </a-select-option>
          </a-select>
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

export default {
  data () {
    return {
      dateFormat: 'YYYY-MM-DD',
      flag: null,
      btnType: 'addBtn',
      coreVisible: false,
      tableData: [],
      selectedRowKeys: [],
      labelCol: { span: 6 },
      wrapperCol: { span: 14 },
      other: '',
      startdateValue: 0,
      enddateValue: 0,
      lastdateValue: 0,
      lastdate: '',
      disEdit: false,
      formData: {
        startdate: moment(new Date().toLocaleDateString()).format('YYYY-MM-DD'),
        enddate: '',
        contracttypename: '',
        contracttype: '',
        employeeid: null,
        // probation: '',
        memo: ''
      },
      coreRules: {
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
    ...mapGetters(['queryData', 'contractInfo', 'zidianInfo']),
    columns () {
      let { contracttype } = this.zidianInfo
      return [
        {title: '开始时间', dataIndex: 'startdate', key: '1'},
        {title: '结束时间', dataIndex: 'enddate', key: '2'},
        {
          title: '合同类型',
          dataIndex: 'contracttype',
          width: 150,
          key: '3',
          customRender: (text, row, index) => {
            let option = contracttype.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        // {title: '试用期', dataIndex: 'probation', key: '4'},
        {title: '备注', dataIndex: 'memo', key: '5'},
        {
          title: '操作',
          key: 'operation',
          width: 100,
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
    contractInfo: {
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
          this.formData.startdate = moment(new Date().toLocaleDateString()).format('YYYY-MM-DD')
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
    ...mapActions(['getSearch']),
    // 初始化开始时间
    getTime,
    startdateChange (date) {
      this.formData.startdate = date
    },
    enddateChange (date) {
      this.formData.enddate = date
    },
    // probationChange (date) {
    //   this.formData.probation = date
    // },
    clickAdd (type) {
      this.btnType = type
      this.coreVisible = true
      this.disEdit = false
      this.formData.id = null
      this.resetForm()
      this.formData.enddate = '9999-12-31'
    },
    getname () {},
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
            that.$delete('system/contract/delete/' + that.selectedRowKeys.toString()).then(res => {
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
          params.startdate = this.formData.startdate
          params.enddate = this.formData.enddate
          params.contracttype = this.formData.contracttype
          // params.probation = this.formData.probation
          params.memo = this.formData.memo
          let http = this.btnType === 'addBtn' ? this.$post : this.$put
          http('system/contract', {...this.formData}).then(res => {
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
      this.formData.contracttype = this.formData.contracttype ? this.formData.contracttype.toString() : ''
    },
    resetForm () {
      // this.formData.probation = null
      this.formData.contracttypename = null
      this.formData.contracttype = null
      this.formData.memo = null
    }
  }
}
</script>
<style lang="less" scoped>
  .table0 {
    .actionBtn {
      margin-bottom: 10px;
      text-align: right;
    }
    .ant-calendar-picker {
      width: 100%;
    }
  }
</style>
