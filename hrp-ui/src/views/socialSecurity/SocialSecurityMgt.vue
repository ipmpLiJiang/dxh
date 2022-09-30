<!--社保报表-->
<template>
  <a-card :bordered="false" class="card-area socialSecurity">
    <div :class="advanced ? 'search' : null">
      <a-form layout="horizontal">
        <div :class="advanced ? null: 'fold'">
          <a-row >
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="应缴类型"
                prop="jftype"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-select v-model="queryParams.jftype">
                  <a-select-option v-for="item in zidianInfo.jftype" :value="item.keyy" :key="item.keyy">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="员工姓名"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <a-input v-model="queryParams.employeename"/>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="开始时间"
                prop="startdate"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-month-picker
                  v-model="queryParams.startdate"
                  :format="dateFormat"
                  :valueFormat="dateFormat"
                  @change="startdateChange"
                />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row >
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="结束时间"
                prop="enddate"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-month-picker
                  v-model="queryParams.enddate"
                  :format="dateFormat"
                  :valueFormat="dateFormat"
                  @change="enddateChange"
                />
              </a-form-item>
            </a-col>
          </a-row>
        </div>
        <span style="float: right; margin-top: 3px; margin-bottom: 18px;">
          <a-button type="primary" @click="handleSubmit">查询</a-button>
          <a-button style="margin-left: 8px" @click="reset">重置</a-button>
        </span>
      </a-form>
    </div>
    <div class="operator">
      <a-button style="margin-left: 15px;" @click="exportFile"><a-icon type="download" /> 导出 </a-button>
    </div>
    <a-tabs type="card" v-model="tabKey" @change="callback">
      <a-tab-pane v-for="item in bxtypeOptions" :key="item.keyy" :tab="item.valuee">
      </a-tab-pane>
    </a-tabs>
      <a-table
          ref="tableInfo"
          :columns="columns"
          :loading="tableLoading"
          :row-key="record => record.id"
          :data-source="dataSource"
          :pagination="pagination"
          @change="handleTableChange"
          :scroll="{ x: 1500, y: 500 }"
      >
      </a-table>
  </a-card>
</template>
<script>
import Select from '../common/Select'
import { mapActions, mapGetters } from 'vuex'
export default {
  name: 'SocialSecurityMgt',
  components: { Select },
  data () {
    return {
      fileInfo: {},
      tabKey: 1,
      advanced: false,
      // 表单设置
      dateFormat: 'YYYY-MM',
      // 加载状态
      tableLoading: false,
      // 查询条件
      queryParams: {
        startdate: '',
        enddate: '',
        employeeid: null,
        employeename: null,
        jftype: '1',
        bxtype: 1
      },
      paginationInfo: null,
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      dataSource: [],
      bxtypeOptions: [
        {keyy: 1, valuee: '养老'},
        {keyy: 2, valuee: '医疗'},
        {keyy: 3, valuee: '失业'},
        {keyy: 4, valuee: '工伤'},
        {keyy: 5, valuee: '生育'},
        {keyy: 6, valuee: '大额医疗'},
        {keyy: 7, valuee: '补助医疗'}
      ]
    }
  },
  watch: {
  },
  created () {
    this.$store.dispatch('getzidian', {vm: this})
  },
  mounted () {
    this.callback(this.tabKey)
  },
  computed: {
    ...mapGetters(['zidianInfo']),
    columns () {
      let { employeetype, jftype } = this.zidianInfo
      return [
        {
          title: '审核年月',
          dataIndex: 'shdate',
          width: 150,
          scopedSlots: { customRender: 'shdate' }
        },
        {
          title: '缴费年月',
          dataIndex: 'paydate',
          width: 150,
          scopedSlots: { customRender: 'paydate' }
        },
        {
          title: '社保个人编号',
          dataIndex: 'socialnum',
          width: 150,
          scopedSlots: { customRender: 'socialnum' }
        },
        {
          title: '人员编号',
          dataIndex: 'employeecode',
          width: 150,
          scopedSlots: { customRender: 'employeecode' }
        },
        {
          title: '姓名',
          dataIndex: 'employeename',
          width: 150,
          scopedSlots: { customRender: 'employeename' }
        },
        {
          title: '公民身份证号码',
          dataIndex: 'idnumber',
          width: 150,
          scopedSlots: { customRender: 'idnumber' }
        },
        {
          title: '人员类型',
          dataIndex: 'employeetype',
          width: 120,
          customRender: (text, row, index) => {
            let option = employeetype.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: '应缴类型',
          dataIndex: 'jftype',
          width: 150,
          customRender: (text, row, index) => {
            let option = jftype.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: '缴费基数',
          dataIndex: 'js',
          width: 150,
          scopedSlots: { customRender: 'js' }
        },
        {
          title: '应缴金额',
          dataIndex: 'yj',
          width: 150,
          scopedSlots: { customRender: 'yj' }
        },
        {
          title: '单位应缴金额',
          dataIndex: 'dw',
          width: 150,
          scopedSlots: { customRender: 'dw' }
        },
        {
          title: '个人应缴金额',
          dataIndex: 'gr',
          width: 120,
          scopedSlots: { customRender: 'gr' }
        },

        {
          title: '补缴利息',
          dataIndex: 'lx',
          width: 150,
          scopedSlots: { customRender: 'lx' }
        },
        {
          title: '滞纳金/积累额',
          dataIndex: 'zn',
          width: 150,
          scopedSlots: { customRender: 'zn' }
        }
      ]
    }
  },
  methods: {
    ...mapActions(['getzidian']),
    startdateChange (date) {
      this.queryParams.startdate = date
    },
    enddateChange (date) {
      this.queryParams.enddate = date
    },
    // 选择事件状态
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    // 切换tab
    callback (key) {
      this.queryParams.bxtype = this.tabKey = key
      this.paginationInfo = null
      this.pagination.current = 1
      this.pagination.pageSize = 10
      this.fetch({
        ...this.queryParams
      })
    },
    // 查询
    handleSubmit () {
      this.fetch({
        ...this.queryParams
      })
    },
    handleTableChange (pagination, filters, sorter) {
      this.pagination.current = pagination.current
      this.pagination.pageSize = pagination.pageSize
      this.paginationInfo = pagination
      this.fetch({
        ...this.queryParams
      })
    },
    // 带条件的查询
    fetch (params = {}) {
      this.tableLoading = true
      if (this.paginationInfo) {
        params.pageSize = this.paginationInfo.pageSize
        params.pageNum = this.paginationInfo.current
      } else {
        // 如果分页信息为空，则设置为默认值
        params.pageSize = this.pagination.defaultPageSize
        params.pageNum = this.pagination.defaultCurrent
      }
      this.$get('system/sb/getHistoryList', {...params}).then(res => {
        if (res.data) {
          this.tableLoading = false
          let data = res.data
          const pagination = { ...this.pagination }
          pagination.total = data.total
          this.dataSource = data.rows
          this.pagination = pagination
        } else {
          this.tableLoading = false
        }
      }).catch(err => {
        this.$message.warning(err)
        this.tableLoading = false
      })
    },
    // 导出
    exportFile () {
      this.$export('system/sb/export/HistoryList', {
        ...this.queryParams
      })
    },
    reset () {
      // 重置查询参数
      this.queryParams.startdate = null
      this.queryParams.enddate = null
      this.queryParams.employeename = null
      this.queryParams.employeeid = null
      this.queryParams.jftype = '1'
      this.queryParams.bxtype = 1
      this.tabKey = 1
      // 重置分页
      this.callback(this.tabKey)
      this.pagination.current = 1
      this.pagination.pageSize = 10
      this.paginationInfo = null
      this.fetch({
        ...this.queryParams
      })
    }
  }
}
</script>
<style scoped lang="less">
  .socialSecurity{
    width: 100%;
    .ant-upload-list-item-card-actions{
      right: -30px;
    }
  }
</style>
<style lang="less">
  @import "../../../static/less/Common";
</style>
