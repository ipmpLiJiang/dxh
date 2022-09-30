<template>
  <a-card :bordered="false" class="card-area employeeMgt">

      <a-form layout="horizontal">
          <a-row>
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="员工姓名"
                prop="keyword"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}"
              >
                <Select :keyword="queryParams.keyword" url="system/employee?search=" @getSearchValue="getSearchValue"></Select>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item
                label="科室"
                prop="kqdept"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-select
                    show-search
                    v-model="queryParams.kqdept"
                    :filter-option="filterOption"
                    option-filter-prop="children"
                    @change="searchPbdept"
                    allowClear
                >
                  <a-select-option v-for="item in zidianInfo.kqdept" :value="item.keyy" :key="item.keyy">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
              <a-col :md="8" :sm="24" >
                  <a-form-item
                      :labelCol="{span: 7}"
                      :wrapperCol="{span: 16, offset: 1}">
                      <a-button type="primary" @click="search">查询</a-button>
                      <a-button style="margin-left: 8px" @click="reset">重置</a-button>
                  </a-form-item>
              </a-col>
          </a-row>
      </a-form>
    <div>
      <div class="operator">
        <a-button @click="exportExcel" :disabled="true">导出</a-button>
      </div>
      <a-table
        ref="TableInfo"
        :columns="columns"
        :rowKey="record => record.employeeid"
        :dataSource="dataSource"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
        :scroll="{ y: 500 }">
          <a slot="operation" slot-scope="text, record" @click="editRow(record)">修改科室</a>
      </a-table>
    </div>
  </a-card>
</template>
<script>
import Select from '../common/Select'
import { mapGetters, mapActions } from 'vuex'
import { politicaloutlookOptions, compare } from '../../utils/filter'
import moment from 'moment'

export default {
  name: 'EmployeeMgt',
  components: { Select },
  data () {
    return {
      dateFormat: 'YYYY-MM-DD',
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
      queryParams: {
        keyword: null,
        employeeid: null,
        kqdept: null,
      },
      loading: false,
      rowRecord: {}
    }
  },
  watch: {},
  computed: {
    ...mapGetters(['zidianInfo']),
    columns () {
      let { status, kqdept, rszfw } = this.zidianInfo
      return [
        {
          title: '人员编号',
          dataIndex: 'code',
          width: 100
        }, {
          title: '姓名',
          dataIndex: 'employeename',
          width: 100
        }, {
          title: '考勤单元',
          dataIndex: 'kqdept',
          customRender: (text, row, index) => {
            if (!text) return ''
            let option = kqdept.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          },
          width: 120
        }, {
          title: '状态',
          dataIndex: 'statusname',
          customRender: (text, row, index) => {
            if (!text) return ''
            let option = status.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          },
          width: 100
        }, {
          title: '人事子范围',
          dataIndex: 'rszfw',
          customRender: (text, row, index) => {
            if (!text) return ''
            let option = rszfw.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          },
          width: 100
        },{
          title: '操作',
          dataIndex: 'operation',
          scopedSlots: {customRender: 'operation'},
          fixed: 'right',
          width: 100
        }
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

    filterOption (input, option) {
      return (
        option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      )
    },
    getSearchValue ({value}) {
      this.queryParams.employeeid = this.queryParams.keyword = value
    },
      searchPbdept(value) {
          if (!value) {
              this.queryParams.kqdept = null
              return
          }
          this.queryParams.kqdept = value
      },
    editRow (record) {
      console.log(this.$store.state.account.user.deptId)
      let that = this
      this.$confirm({
        title: '确认是否修改？',
        onOk () {
          let params = {...record}
          params.kqdept = that.$store.state.account.user.deptId
          that.$put('system/employee/updateKqDept', {...params}).then(res => {
            if (res.data) {
              that.fetch({
                ...that.queryParams
              })
              that.$message.success('修改成功!')
            }
          }).catch(err => {
            that.$message.warning(err)
            that.finishLoading = false
          })
        },
        onCancel () {
        }
      })
    },

    exportExcel () {
      this.$export('system/employee/export', {
        ...this.queryParams
      })
    },
    search () {
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
        this.$refs.TableInfo.pagination.current = this.paginationInfo.current
        this.$refs.TableInfo.pagination.pageSize = this.paginationInfo.pageSize
        params.pageSize = this.paginationInfo.pageSize
        params.pageNum = this.paginationInfo.current
      } else {
        params.pageSize = this.pagination.defaultPageSize
        params.pageNum = this.pagination.defaultCurrent
      }
      this.$get('system/employee/deptFindEmployeeList', {...params}).then((r) => {
        this.loading = false
        let data = r.data
        const pagination = { ...this.pagination }
        pagination.total = data.total
        this.dataSource = data.rows
        this.pagination = pagination
      }).catch(err => {
        this.$message.warning(err)
        this.loading = false
      })
    },
    reset () {
      // 重置查询参数
      this.queryParams.keyword = null
      this.queryParams.employeeid = null
      this.queryParams.kqdept = ""

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
  .employeeMgt {
    .ant-calendar-picker{
      width: 100%;
    }
  }

</style>
<style lang="less">
  .employeeArchives {
    .ant-form-item-label label {
      font-weight: 400!important;
      font-size: 16px!important;
    }
    .ant-tag{
      display: block;
      height: 36px;
      line-height: 34px;
      margin-bottom: 10px;
      margin-right: 0;
      font-size: 14px;
      /*.anticon-close{*/
        /*line-height: 34px;*/
        /*float: right;*/
        /*display: none;*/
      /*}*/
      .anticon-plus{
        height: 36px;
        display: flex;
        justify-content: center;
        align-items: center;
      }
    }
    .ant-input-sm {
      width: 100% !important;
      height: 34px;
      line-height: 34px
    }
    .ant-tag:last-child{
      margin-bottom: 0px;
    }
  }
  .ant-modal-nofooter{
    .ant-modal-footer{
      display: none;
    }
  }
</style>
