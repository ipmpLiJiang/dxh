<template>
  <a-card :bordered="false" class="card-area payMgt">
    <div :class="advanced ? 'search' : null">
      <a-form-model class="ruleForm" ref="formDataaddPay" :model="formData" v-bind="layout1">
        <div :class="advanced ? null: 'fold'">
          <a-row >
            <a-col :md="8" :sm="24" >
              <a-form-model-item
                label="员工姓名"
                prop="keyword"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}"
              >
                <Select :keyword="formData.keyword" url="system/employee?search=" @getSearchValue="getSearchValue"></Select>
              </a-form-model-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-model-item
                label="科室"
                prop="dept"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-select v-model="formData.deptids" mode="multiple" allowClear>
                  <a-select-option v-for="item in zidianInfo.dept" :value="item.keyy" :key="item.keyy">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-model-item
                label="人员类型"
                prop="employeetype"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-select v-model="formData.employeetype" mode="multiple" allowClear>
                  <a-select-option v-for="item in zidianInfo.employeetype" :value="item.keyy" :key="item.keyy">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
          </a-row>
          <a-row v-if="advanced">
            <a-col :md="8" :sm="24" >
              <a-form-model-item
                label="人员状态"
                prop="status"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-select v-model="formData.states" mode="multiple" allowClear>
                  <a-select-option v-for="item in zidianInfo.status" :value="item.keyy" :key="item.keyy">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-model-item
                label="人事子范围"
                prop="rszfwname"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-select v-model="formData.rszfws" mode="multiple" allowClear>
                  <a-select-option v-for="item in zidianInfo.rszfw" :value="item.keyy" :key="item.keyy">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
          </a-row>
        </div>
        <span style="float: right; margin-top: 3px; margin-bottom: 18px;">
          <a-button class="editable-add-btn" :loading="startLoading" @click="reset">
            重置
          </a-button>
          <a-button type="primary" :loading="startLoading" @click="handleSubmit">
            查询
          </a-button>
        <a @click="toggleAdvanced" style="margin-left: 8px">
          {{advanced ? '收起' : '展开'}}
          <a-icon :type="advanced ? 'up' : 'down'" />
        </a>
      </span>
      </a-form-model>
    </div>
    <div class="operator">
      <a-button @click="onSetRowKeys" >
        批量停薪
      </a-button>
    </div>
    <a-table
      ref="tableInfo"
      :columns="columns"
      :loading="tableLoading"
      :row-key="record => record.employeeid"
      :data-source="dataSource"
      :pagination="pagination"
      :scroll="{ x: 1500, y: 500 }"
      :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
      @change="handleTableChange"
    >
    </a-table>
  </a-card>
</template>
<script>
import { mapActions, mapGetters } from 'vuex'
import moment from 'moment'
import Select from '../common/Select'

export default {
  name: 'addPay',
  components: { Select },
  data () {
    return {
      // 表单设置
      nextMonth: '',
      advanced: false,
      layout1: {
        labelCol: { span: 5 },
        wrapperCol: { span: 13 }
      },
      // 加载状态
      startLoading: false,
      tableLoading: false,
      // 查询条件
      formData: {
        employeeid: null,
        keyword: null,
        deptids: [],
        states: [],
        rsfws: [],
        rszfws: [],
        employeetypes: []
      },
      dataSource: [],
      selectedRowKeys: [],
      paginationInfo: null,
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      option: []
    }
  },
  created () {
    this.$store.dispatch('getPeriodInfo', {vm: this})
  },
  mounted () {
    this.fetch({...this.formData})
  },
  watch: {
    periodInfo: {
      handler: function (data) {
        if (data && data.id) {
          // this.formData.periodid = data.id
          this.formData.status = data.status
        }
      },
      immediate: true
    }
  },
  computed: {
    ...mapGetters(['zidianInfo', 'periodInfo']),
    columns () {
      let { dept, job, employeetype, rszfw, status } = this.zidianInfo
      return [
        {
          title: '人员编号',
          dataIndex: 'code',
          width: 100,
          scopedSlots: { customRender: 'code' }
        },
        {
          title: '姓名',
          dataIndex: 'employeename',
          width: 100,
          scopedSlots: { customRender: 'employeename' }
        },
        {
          title: '科室',
          dataIndex: 'dept',
          width: 100,
          customRender: (text, row, index) => {
            let option = dept.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: '岗位',
          dataIndex: 'job',
          width: 100,
          customRender: (text, row, index) => {
            let option = job.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: '人员类型',
          dataIndex: 'employeetype',
          width: 100,
          customRender: (text, row, index) => {
            let option = employeetype.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: '人事子范围',
          dataIndex: 'rszfw',
          width: 100,
          customRender: (text, row, index) => {
            let option = rszfw.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: '人员状态',
          dataIndex: 'status',
          width: 100,
          customRender: (text, row, index) => {
            let option = status.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {title: '操作',
          dataIndex: 'operation',
          width: 100,
          fixed: 'right',
          scopedSlots: {customRender: 'operation'}
        }
      ]
    }
  },
  methods: {
    ...mapActions(['getPeriodInfo']),
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    // 匹配模糊查询数据
    getSearchValue ({value}) {
      this.formData.employeeid = this.formData.keyword = value
    },
    // 查询
    handleSubmit () {
      this.startLoading = true
      this.fetch({
        ...this.formData
      })
      this.startLoading = false
    },
    // 多选
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    onSetRowKeys () {
      if (this.selectedRowKeys.length === 0) {
        this.$message.warning('请勾选您要停薪的记录！')
        return false
      }
      let that = this
      this.$confirm({
        title: '确认是否继续？',
        onOk () {
          that.$put1('system/emolument/cancelEmolumentList/' + that.selectedRowKeys.toString()).then(res => {
            if (res.data) {
              that.$message.success('设置成功！')
              that.fetch({...that.formData})
            }
          }).catch(err => {
            that.$message.warning(err)
          })
        },
        onCancel () {
        }
      })
    },
    handleTableChange (pagination, filters, sorter) {
      this.paginationInfo = pagination
      this.fetch({
        ...this.formData
      })
    },
    // 带条件的查询
    fetch (params = {}) {
      this.tableLoading = true
      if (this.paginationInfo) {
        // 如果分页信息不为空，则设置表格当前第几页，每页条数，并设置查询分页参数
        this.$refs.tableInfo.pagination.current = this.paginationInfo.current
        this.$refs.tableInfo.pagination.pageSize = this.paginationInfo.pageSize
        params.pageSize = this.paginationInfo.pageSize
        params.pageNum = this.paginationInfo.current
      } else {
        // 如果分页信息为空，则设置为默认值
        params.pageSize = this.pagination.defaultPageSize
        params.pageNum = this.pagination.defaultCurrent
      }
      // params.nowdate = moment(this.formData.nowdate).format('YYYY-MM')
      this.$get('system/emolument/getCancelEmolumentList', {...params}).then(res => {
        if (res.data) {
          let data = res.data
          const pagination = { ...this.pagination }
          pagination.total = data.total
          this.dataSource = data.rows
          this.pagination = pagination
          this.tableLoading = false
          this.dataSource.forEach(item => {
            item.editable = true
          })
        } else {
          this.tableLoading = false
        }
      }).catch(err => {
        this.$message.warning(err)
        this.tableLoading = false
      })
    },
    reset () {
      // 参数重置
      this.selectedRowKeys = []
      this.formData.employeeid = null
      this.formData.keyword = null
      this.formData.deptids = []
      this.formData.states = []
      this.formData.rsfws = []
      this.formData.rszfws = []
      this.formData.employeetypes = []
      // 重置分页
      this.$refs.tableInfo.pagination.current = this.pagination.defaultCurrent
      this.paginationInfo = null
      this.fetch({...this.formData})
    }
  }
}
</script>
<style scoped lang="less">
  .payMgt{
    width: 100%;
    .ruleForm{
      .ant-form-item{
        margin-bottom: 5px!important;
      }
      .ant-switch{
        width: 60px;
        height: 28px;
        line-height: 26px;
      }
      .ant-switch-loading-icon, .ant-switch::after{
        width: 24px;
        height: 24px;
      }
    }
    .editable-add-btn{
      margin-right: 10px;
    }
  }
</style>
<style lang="less">
  @import "../../../static/less/Common";
  .ant-form-item-span{
    color: rgba(0,0,0,0.85);
  }
</style>
