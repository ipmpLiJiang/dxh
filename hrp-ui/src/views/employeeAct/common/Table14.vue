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
        <a-form-model-item label="考核年度" prop="startdate">
          <a-date-picker
            mode="year"
            :format="dateFormat"
            :valueFormat="dateFormat"
            :value="formData.year"
            :open="yyyyOpen"
            @panelChange="startPanelChange"
            @openChange="startOpenChange"
          />
        </a-form-model-item>
        <a-form-model-item label="考核结果" prop="result">
          <a-select v-model="formData.result" allowClear>
            <a-select-option v-for="item in zidianInfo.checkresult" :value="item.keyy" :key="item.keyy">
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
import moment from 'moment'

export default {
  data () {
    return {
      dateFormat: 'YYYY',
      disAdd: false,
      btnType: 'addBtn',
      coreVisible: false,
      tableData: [],
      yyyyOpen: false, 
      selectedRowKeys: [],
      labelCol: { span: 6 },
      wrapperCol: { span: 14 },
      query: {},
      formData: {
        year: '',
        result: '',
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
    ...mapGetters(['checkInfo', 'queryData', 'zidianInfo']),
    columns () {
      let { checkresult } = this.zidianInfo
      return [
        {
          title: '考核年度',
          dataIndex: 'year'
        },
        {
          title: '考核结果',
          dataIndex: 'result',
          customRender: (text, row, index) => {
            let option = checkresult.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: '备注',
          dataIndex: 'memo'
        },
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
          this.query = data
          this.formData.employeeid = data.employeeid
        }
      },
      immediate: true
    },
    checkInfo: {
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
    ...mapActions(['getSearch']),
    // 入党时间
    startPanelChange (date) {
      this.formData.year = date
      this.yyyyOpen = false
    },
    startOpenChange (status) {
      if(status){
        this.yyyyOpen = true
      } else {
        this.yyyyOpen = false
      }
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
            that.$delete('system/check/delete/' + that.selectedRowKeys.toString()).then(res => {
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
          params.year = moment(this.formData.year).format('YYYY-MM')
          params.memo = this.formData.memo
          params.result = this.formData.result
          params.employeeid = this.formData.employeeid
          params.id = this.btnType === 'addBtn' ? null : this.formData.id
          let http = this.btnType === 'addBtn' ? this.$post : this.$put
          http('system/check', {...params}).then(res => {
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
      this.yyyyOpen = false
      this.formData = {...this.formData, ...record}
      this.formData.result = record.result.toString()
    },
    resetForm () {
      this.formData.year = null
      this.formData.result = null
      this.formData.memo = null
      this.yyyyOpen = false
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
