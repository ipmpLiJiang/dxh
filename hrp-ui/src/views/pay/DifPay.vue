<template>
  <a-card :bordered="false" class="card-area difPay">
    <div :class="advanced ? 'search' : null">
      <a-form-model class="ruleForm" ref="formData" :model="formData" v-bind="layout1" :rules="formRules">
        <div :class="advanced ? null: 'fold'">
          <a-row >
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="开始时间"
                prop="startdate"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-month-picker
                  v-model="formData.startdate"
                  :format="dateFormat"
                  :valueFormat="dateFormat"
                  @change="startdateChange"
                />
              </a-form-item>
            </a-col>
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
                label="人员类型"
                prop="employeetypes"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-select v-model="formData.employeetypes" mode="multiple" allowClear>
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
                label="人员状态"
                prop="states"
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
          <a-button class="editable-add-btn" @click="reset">
            重置
          </a-button>
          <a-button type="primary" @click="handleSubmit">
            查询
          </a-button>
        <a @click="toggleAdvanced" style="margin-left: 8px">
          {{advanced ? '收起' : '展开'}}
          <a-icon :type="advanced ? 'up' : 'down'" />
        </a>
      </span>
      <div class="operator">
        <a-button @click="exportFile"><a-icon type="download" /> 导出 </a-button>
      </div>
      </a-form-model>
    </div>
    <div class="operator">
    </div>
    <a-row style="margin-bottom: 10px">
      <a-col :span="6">
        <span class="title">上月应发合计：</span>
        <span>{{coreCol.syyf}}</span>
      </a-col>
      <a-col :span="6">
        <span class="title">本月应发合计：</span>
        <span>{{coreCol.byyf}}</span>
      </a-col>
      <a-col :span="6">
        <span class="title">差异金额合计：</span>
        <span>{{coreCol.ce}}</span>
      </a-col>
    </a-row>
    <a-table
      ref="tableInfo"
      :columns="columns"
      :loading="tableLoading"
      :row-key="record => record.employeeid"
      :data-source="dataSource"
      :pagination="pagination"
      @change="handleTableChange"
      :scroll="{ x: 1500, y: 500 }"
    >
    <template slot="cyje" slot-scope="text, record, index">
      <a v-if="text !== 0" @click="getDetail (record)">{{ text }}</a>
      <p v-else>{{ text }}</p>
    </template>
    </a-table>
    <a-modal v-model="showVisible" :footer="null" title="差异明细" @ok="hideShowModal">
      <a-row>
        <a-col :span=12>工号：{{showCode}}</a-col>
        <a-col :span=12>姓名：{{showName}}</a-col>
      </a-row>
      <br>
      <a-table
        ref="tableInfo1"
        :row-key="record => record.project"
        size="small"
        :columns="columns1"
        :data-source="dataDetailSource"
      >
      </a-table>
    </a-modal>
  </a-card>
</template>
<script>
import Select from '../common/Select'
import { mapActions, mapGetters } from 'vuex'
import moment from 'moment'
export default {
  name: 'DifPay',
  components: { Select },
  data () {
    return {
      // 表单设置
      dateFormat: 'YYYY-MM',
      switchDis: false,
      advanced: false,
      layout: {
        labelCol: { span: 5 },
        wrapperCol: { span: 13 }
      },
      layout1: {
        labelCol: { span: 8 },
        wrapperCol: { span: 14 }
      },
      layout2: {
        labelCol: { span: 8 },
        wrapperCol: { span: 14 }
      },
      // 加载状态
      tableLoading: false,
      // 查询条件
      formData: {
        employeeid: null,
        keyword: null,
        deptids: [],
        states: [],
        rsfws: [],
        rszfws: [],
        employeetypes: [],
        startdate: moment(new Date()).format('YYYY-MM')
      },
      coreCol: {
      },
      showVisible: false,
      dataDetailSource: [],
      showCode: '',
      showName: '',
      // 列表数据
      dataSource: [],
      paginationInfo: null,
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      formRules: {
        startdate: [
          { required: true, message: '请输入查询时间', trigger: 'change' }
        ]
      }
    }
  },
  watch: {
  },
  created () {
  },
  mounted () {
    this.fetch({
      ...this.formData
    })
  },
  computed: {
    ...mapGetters(['zidianInfo']),
    columns () {
      let { status, dept, employeetype } = this.zidianInfo
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
          dataIndex: 'deptname',
          width: 120,
          scopedSlots: { customRender: 'deptname' }
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
          title: "人员状态",
          dataIndex: "employeestatu",
          width: 100,
          customRender: (text, row, index) => {
            if (!text) return ''
            let option = status && status.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          },
        },
        {
          title: '上月应发(元)',
          dataIndex: 'syyf',
          width: 100,
          scopedSlots: { customRender: 'syyf' }
        },
        {
          title: '本月应发(元)',
          dataIndex: 'byyf',
          width: 100,
          scopedSlots: { customRender: 'syyf' }
        },
        {
          title: '差异金额(元)',
          dataIndex: 'ce',
          width: 100,
          scopedSlots: { customRender: 'cyje' }
        }
      ]
    },
    columns1 () {
      return [{
          title: '项目',
          dataIndex: 'project',
          width: 100
        },
        {
          title: '上月',
          dataIndex: 'lastPay',
          width: 100
        },
        {
          title: '本月',
          dataIndex: 'currentPay',
          width: 100
        },
        {
          title: '差异',
          dataIndex: 'differentPayStr',
          width: 100
        }
      ]
    }
  },
  methods: {
    ...mapActions(['']),
    // 当前时间
    startdateChange (date) {
      this.formData.startdate = date
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    getSearchValue ({value}) {
      this.formData.employeeid = this.formData.keyword = value
    },
    hideShowModal () {
      this.showVisible = false
    },
    // 导出
    exportFile() {
      this.$export("system/emolument/cyExport", {
        ...this.formData,
      });
    },
    getDetail (record) {
      let paramsReport = {
        startdate: record.period,
        employeeid: record.employeeid
      }
      this.showCode = record.code
      this.showName = record.employeename
      this.showVisible = true
      this.dataDetailSource = []
      this.$get('system/emolument/getDifferentReportDetail', paramsReport).then((r) => {
        if (r.data.data.success === 1) {
          this.dataDetailSource = r.data.data.data
        } else {
          this.$message.warning('获取数据失败')
        }
      }).catch((e) => {
        console.error(e)
        this.$message.error('获取数据失败')
      })
    },
    // 查询
    handleSubmit () {
      if (!this.formData.startdate) {
        this.$message.warning('请输入查询时间！')
        return
      }
      this.fetch({
        ...this.formData
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
      this.$get('system/emolument/getDifferentReport', {...params}).then(res => {
        this.tableLoading = false
        if (res.data) {
          let data = res.data
          const pagination = { ...this.pagination }
          pagination.total = data.total
          this.dataSource = data.rows
          this.pagination = pagination
          this.coreCol = data.sum
        }
      }).catch(err => {
        this.$message.warning(err)
        this.tableLoading = false
      })
    },
    reset () {

      // 重置查询参数
      this.formData.employeeid = null
      this.formData.keyword = null
      this.formData.deptids = []
      this.formData.states = []
      this.formData.rsfws = []
      this.formData.rszfws = []
      this.formData.employeetypes = []
      this.formData.startdate = moment(new Date()).format('YYYY-MM')
        // 重置分页
      this.$refs.tableInfo.pagination.current = this.pagination.defaultCurrent
      this.paginationInfo = null
      this.fetch({...this.formData})
    }
  }
}
</script>
<style scoped lang="less">
  .difPay{
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
    .title{
      color: #999;
    }
  }
</style>
<style lang="less">
  @import "../../../static/less/Common";
  .ant-form-item-span{
    color: rgba(0,0,0,0.85);
  }
</style>
