<template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <a-form layout="horizontal">
        <div :class="advanced ? null: 'fold'">
          <a-row >
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="事件类型"
                prop="states"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-select v-model="queryParams.eventtypes" mode="multiple">
                  <a-select-option v-for="item in zidianInfo.eventtype" :value="item.keyy" :key="item.keyy">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="事件开始时间"
                prop="eventstartdate"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-date-picker
                  v-model="queryParams.eventstartdate"
                  :format="dateFormat"
                  :valueFormat="dateFormat"
                  @change="eventstartdateChange"
                />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="事件结束时间"
                prop="eventenddate"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-date-picker
                  v-model="queryParams.eventenddate"
                  :format="dateFormat"
                  :valueFormat="dateFormat"
                  @change="eventenddateChange"
                />
              </a-form-item>
            </a-col>
          </a-row>
        </div>
        <span style="float: right; margin-top: 3px; margin-bottom: 18px;">
          <a-button type="primary" @click="search">查询</a-button>
          <a-button style="margin-left: 8px" @click="reset">重置</a-button>
          <a @click="toggleAdvanced" style="margin-left: 8px">
            {{advanced ? '收起' : '展开'}}
            <a-icon :type="advanced ? 'up' : 'down'" />
          </a>
        </span>
      </a-form>
    </div>
    <div>
      <div class="operator">
        <a-button @click="exportExcel">导出</a-button>
      </div>
      <a-table ref="TableInfo"
               :columns="columns"
               :rowKey="record => record.id"
               :dataSource="dataSource"
               :pagination="pagination"
               :loading="loading"
               @change="handleTableChange"
               :scroll="{ x: 2500, y: 500 }">
        <!--<template slot="operation" slot-scope="text, record">-->
          <!--<a-icon type="setting" @click="edit(record)" title="编辑"></a-icon>-->
        <!--</template>-->
      </a-table>
    </div>
  </a-card>
</template>
<script>
import { mapGetters, mapActions } from 'vuex'
import { getTime } from '../../utils/filter'
import moment from 'moment'

export default {
  name: 'EmployeeMgt',
  components: {},
  data () {
    return {
      dateFormat: 'YYYY-MM-DD',
      advanced: false,
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
      genderOptions: [
        {keyy: '男', valuee: '男'},
        {keyy: '女', valuee: '女'}
      ],
      option: [],
      queryParams: {
        eventstartdate: moment(new Date()).format('YYYY-MM-DD'),
        eventenddate: '',
        eventtypes: []
      },
      loading: false
    }
  },
  computed: {
    ...mapGetters(['zidianInfo']),
    columns () {
      let { status, dept, ward, rsfw, rszfw, eventtype, employeetype } = this.zidianInfo
      let genderOptions = this.genderOptions
      return [{
        title: '人员编号',
        dataIndex: 'code',
        width: 100
      }, {
        title: '姓名',
        dataIndex: 'employeename',
        width: 100
      }, {
        title: '事件类型',
        dataIndex: 'eventtype',
        customRender: (text, row, index) => {
          let option = eventtype.filter(item => item.keyy == text)[0]
          return option ? option.valuee : ''
        },
        width: 100
      }, {
        title: '事件时间',
        dataIndex: 'eventdate',
        width: 130
      }, {
        title: '性别',
        dataIndex: 'gender',
        customRender: (text, row, index) => {
          let option = genderOptions.filter(item => item.keyy == text)[0]
          return option ? option.valuee : ''
        },
        width: 100
      }, {
        title: '人员状态',
        dataIndex: 'statusname',
        customRender: (text, row, index) => {
          let option = status.filter(item => item.keyy == text)[0]
          return option ? option.valuee : ''
        },
        width: 100
      }, {
        title: '科室',
        dataIndex: 'deptname',
        customRender: (text, row, index) => {
          let option = dept.filter(item => item.keyy == text)[0]
          return option ? option.valuee : ''
        },
        width: 120
      }, {
        title: '病区',
        dataIndex: 'wardname',
        customRender: (text, row, index) => {
          let option = ward.filter(item => item.keyy == text)[0]
          return option ? option.valuee : ''
        },
        width: 120
      }, {
        title: '人员类型',
        dataIndex: 'employeetype',
        customRender: (text, row, index) => {
          let option = employeetype.filter(item => item.keyy == text)[0]
          return option ? option.valuee : ''
        },
        width: 120
      }, {
        title: '人事范围',
        dataIndex: 'rsfwname',
        customRender: (text, row, index) => {
          let option = rsfw.filter(item => item.keyy == text)[0]
          return option ? option.valuee : ''
        },
        width: 120
      }, {
        title: '人事子范围',
        dataIndex: 'rszfwname',
        customRender: (text, row, index) => {
          let option = rszfw.filter(item => item.keyy == text)[0]
          return option ? option.valuee : ''
        },
        width: 120
      }, {
        title: '身份证号',
        dataIndex: 'idnumber',
        width: 180
      }, {
        title: '出生日期',
        dataIndex: 'birth',
        width: 130
      }, {
        title: '参加工作时间',
        dataIndex: 'workdate',
        width: 130
      }, {
        title: '来院时间',
        dataIndex: 'comedate',
        width: 130
      }, {
        title: '联系方式',
        dataIndex: 'phonenumber',
        width: 120
      }, {
        title: '民族',
        dataIndex: 'nation',
        width: 100
      }, {
        title: '籍贯',
        dataIndex: 'homeaddress',
        width: 120
      },{
        title: '修改人',
        dataIndex: 'xtupdateby',
        width: 120
      }, {
        title: '修改时间',
        dataIndex: 'xtupdatedate',
        width: 100
      }, {
        title: '备注',
        dataIndex: 'memo',
        width: 120
      },
      //   {
      //   title: '操作',
      //   dataIndex: 'operation',
      //   scopedSlots: {customRender: 'operation'},
      //   fixed: 'right',
      //   width: 80
      // }
      ]
    }
  },
  created () {
    this.$store.dispatch('getzidian', {vm: this})
  },
  mounted () {
    this.fetch({...this.queryParams})
  },
  methods: {
    ...mapActions('getQuery'),
    getTime,
    eventstartdateChange (date) {
      this.queryParams.eventstartdate = date
    },
    eventenddateChange (date) {
      this.queryParams.eventenddate = date
    },
    // 选择事件状态
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    // edit (record) {
    //   console.log(record)
    //   this.$store.dispatch('getQuery', {employeeid: record.employeeid, pageStatus: 'edit'})
    //   this.$store.dispatch('getSearch', {vm: this, id: record.employeeid})
    //   this.$router.push({path: '/employee'})
    // },
    exportExcel () {
      this.$export('system/event/export', {
        ...this.queryParams
      })
    },
    search () {
      if (!this.queryParams.eventstartdate) {
        this.queryParams.eventstartdate = moment(new Date()).format('YYYY-MM-DD')
        this.$message.warn('开始时间必填！')
        return
      }
      this.fetch({
        ...this.queryParams
      })
    },
    handleTableChange (pagination, filters, sorter) {
      this.paginationInfo = pagination
      this.fetch({
        ...this.queryParams
      })
    },
    fetch (params = {}) {
      this.loading = true
      if (this.paginationInfo) {
        // 如果分页信息不为空，则设置表格当前第几页，每页条数，并设置查询分页参数
        this.$refs.TableInfo.pagination.current = this.paginationInfo.current
        this.$refs.TableInfo.pagination.pageSize = this.paginationInfo.pageSize
        params.pageSize = this.paginationInfo.pageSize
        params.pageNum = this.paginationInfo.current
      } else {
        // 如果分页信息为空，则设置为默认值
        params.pageSize = this.pagination.defaultPageSize
        params.pageNum = this.pagination.defaultCurrent
      }
      this.$get('system/event/getEventReport', {...params}).then((r) => {
        let data = r.data
        const pagination = { ...this.pagination }
        pagination.total = data.total
        this.loading = false
        this.dataSource = data.rows
        this.pagination = pagination
      }).catch(err => {
        this.$message.warning(err)
        this.loading = false
      })
    },
    reset () {

      // 重置查询参数
      this.queryParams.eventstartdate = moment(new Date().toLocaleDateString()).format('YYYY-MM-DD')
      this.queryParams.eventenddate = null
      this.queryParams.keyword = null
      this.queryParams.employeeid = null
      this.queryParams.eventtypes = []
      // 重置分页
      this.$refs.TableInfo.pagination.current = this.pagination.defaultCurrent
      this.paginationInfo = null
      this.fetch({...this.queryParams})
    }
  }
}
</script>

<style lang="less" scoped>
  @import "../../../static/less/Common";
  .ant-calendar-picker{
    width: 100%;
  }
</style>
