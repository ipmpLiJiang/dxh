<template>
  <a-card :bordered="false" class="card-area employeeMgt">
    <div :class="advanced ? 'search' : null">
      <a-form layout="horizontal">
        <div :class="advanced ? null: 'fold'">
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
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="日期"
                prop="inputdate"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-date-picker
                  v-model="queryParams.inputdate"
                  :format="dateFormat"
                  :valueFormat="dateFormat"
                  @change="inputdateChange"
                />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="科室"
                prop="deptids"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-select v-model="queryParams.deptids" mode="multiple" allowClear :filter-option="filterOption">
                  <a-select-option v-for="item in zidianInfo.dept" :value="item.keyy" :key="item.keyy">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="考核年度开始"
                prop="startdate"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-date-picker
                  mode="year"
                  format="YYYY"
                  valueFormat="YYYY"
                  style="width:45%"
                  :value="queryParams.startdate"
                  :open="yyyyStartOpen"
                  @panelChange="startPanelChange"
                  @openChange="startOpenChange"
                /> - 
                <a-date-picker
                  mode="year"
                  format="YYYY"
                  valueFormat="YYYY"
                  style="width:45%"
                  :value="queryParams.enddate"
                  :open="yyyyEndOpen"
                  @panelChange="endPanelChange"
                  @openChange="endOpenChange"
                />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="考核结果"
                prop="result"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-select v-model="queryParams.result" allowClear :filter-option="filterOption">
                  <a-select-option v-for="item in zidianInfo.checkresult" :value="item.keyy" :key="item.keyy">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
        </div>
        <span style="float: right; margin-top: 3px; ">
          <a-button type="primary" @click="search">查询</a-button>
          <a-button style="margin-left: 8px" @click="reset">重置</a-button>
          <a @click="toggleAdvanced" style="margin-left: 8px">
            {{advanced ? '收起' : '展开'}}
            <a-icon :type="advanced ? 'up' : 'down'" />
          </a>
        </span>
      </a-form>
    </div>
    <br>
    <a-row>
      <a-col class="operator" :span=4>
      <a-button @click="exportExcel">导出</a-button>
      </a-col>
      <a-col class="operator" :span=4>
        <a-upload
          accept=".xlsx"
          :fileList="fileList"
          :disabled="loadinginport"
          :beforeUpload="beforeUpload"
        >
          <a-button :loading="loadinginport">
            <a-icon type="upload"  /> 导入年度考核
          </a-button>
        </a-upload>
      </a-col>
    </a-row>
    <div>
      <a-table
        ref="TableInfo"
        :columns="columns"
        :rowKey="record => record.employeeid"
        :dataSource="dataSource"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
        :scroll="{ x: 900, y: 500 }">
      </a-table>
    </div>
  </a-card>
</template>
<script>
import Select from '../common/Select'
import { mapGetters, mapActions } from 'vuex'
import moment from 'moment'

export default {
  name: 'EmployeeMgt',
  components: { Select },
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
      queryParams: {
        inputdate: moment(new Date()).format('YYYY-MM-DD'),
        startdate: moment(new Date()).format('YYYY-MM-DD'),
        enddate: moment(new Date()).format('YYYY-MM-DD'),
        keyword: null,
        employeeid: null,
        deptids: [],
        result: null
      },
      gparName:'checkinfo',
      fileList: [],
      yyyyStartOpen: false,
      yyyyEndOpen: false,
      loading: false,
      loadinginport: false,
      archivesOptions: [],
      rowRecord: {}
    }
  },
  watch: {},
  computed: {
    ...mapGetters(['zidianInfo']),
    columns () {
      let { employeetype, checkresult, dept, rszfw } = this.zidianInfo
      return [
        {
          title: '人员编号',
          dataIndex: 'code',
          width: 100
        },
        {
          title: '姓名',
          dataIndex: 'employeename',
          width: 100
        },
        {
          title: '性别',
          dataIndex: 'gender',
          width: 80
        },
        { title: '科室',
          dataIndex: 'deptname',
          width: 150,
          customRender: (text, row, index) => {
            let option = dept.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: '人员类型',
          dataIndex: 'employeetype',
          customRender: (text, row, index) => {
          if (!text) return ''
          let option = employeetype.filter(item => item.keyy == text)[0]
          return option ? option.valuee : ''
          },
          width: 100
        },
        {
        title: '人事子范围',
        dataIndex: 'rszfwname',
        customRender: (text, row, index) => {
          if (!text) return ''
          let option = rszfw.filter(item => item.keyy == text)[0]
          return option ? option.valuee : ''
        },
        width: 120
        },
        {
          title: '考核结果',
          dataIndex: 'result',
          width: 140,
          customRender: (text, row, index) => {
            let option = checkresult.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: '考核年度',
          dataIndex: 'year',
          width: 100
        },
        {
          title: '备注',
          dataIndex: 'memo',
          ellipsis: true,
          width: 200
        }
      ]
    }
  },
  created () {
    this.$store.dispatch('getzidian', {vm: this})
  },
  mounted () {
    this.queryParams.inputdate = moment(this.queryParams.inputdate).format('YYYY-MM-DD')
    this.queryParams.startdate = moment(this.queryParams.startdate).format('YYYY-MM-DD')
    this.queryParams.enddate = moment(this.queryParams.enddate).format('YYYY-MM-DD')
    this.yyyyStartOpen = false
    this.yyyyEndOpen = false
    this.fetch({...this.queryParams})
  },
  methods: {
    ...mapActions('getQuery'),
    // 查询时间
    inputdateChange (date) {
      this.queryParams.inputdate = date
    },
    startPanelChange (date) {
      if(date){
        this.queryParams.startdate = moment(date).format('YYYY-MM-DD')
      } else {
        this.queryParams.startdate = null
      }
      this.yyyyStartOpen = false
    },
    startOpenChange (status) {
      if(status){
        this.yyyyStartOpen = true
      } else {
        this.yyyyStartOpen = false
      }
    },
    endPanelChange (date) {
      if(date){
        this.queryParams.enddate = moment(date).format('YYYY-MM-DD')
      } else {
        this.queryParams.enddate = null
      }
      this.yyyyEndOpen = false
    },
    endOpenChange (status) {
      if(status){
        this.yyyyEndOpen = true
      } else {
        this.yyyyEndOpen = false
      }
    },
    filterOption (input, option) {
      return (
        option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      )
    },
    getSearchValue ({value}) {
      this.queryParams.employeeid = this.queryParams.keyword = value
    },
    // 选择事件状态
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    // 多选
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    exportExcel () {
      this.$export('system/check/export', {
        ...this.queryParams
      })
    },
    beforeUpload (file) {
      let isType = false
      let fileName = file.name.split('.')
      if (fileName[fileName.length - 1] === 'xlsx') {
        isType = true
      } else {
        this.$error({
          title: '只能上传.xlsx 格式~'
        })
        return
      }
      const isLt3M = file.size / 1024 < 3001
      if (!isLt3M) {
        this.$error({
          title: '文件超3MB限制，不允许上传~'
        })
        return
      }
      return isType && isLt3M && this.customRequest(file)
    },
    customRequest (file) {
      this.loadinginport = true
      const formData = new FormData()
      formData.append('file', file)
      formData.append('gparname', this.gparName)
      this.$upload('system/check/import', formData).then(res => {
        let data = res.data.data.data
        let error = res.data.data.error
        if (data && data.length > 0) {
          this.$message.success('导入成功！')
        }
        if (error && error.length > 0) {
          this.$message.warning('导入文件内容有误！')
        }
        this.loadinginport = false
      }).catch(err => {
        this.$message.warning(err)
        this.loadinginport = false
      })
    },
    search () {
      if (!this.queryParams.startdate) {
        this.queryParams.startdate = moment(new Date()).format('YYYY-MM-DD')
        this.queryParams.enddate = moment(new Date()).format('YYYY-MM-DD')
        this.$message.warn('考核年度开始必填！')
        return
      }
      if (!this.queryParams.enddate) {
        this.queryParams.startdate = moment(new Date()).format('YYYY-MM-DD')
        this.queryParams.enddate = moment(new Date()).format('YYYY-MM-DD')
        this.$message.warn('考核年度结束必填！')
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
        this.pagination = {
          pageSizeOptions: ['10', '20', '30', '40', '100'],
          defaultCurrent: 1,
          defaultPageSize: 10,
          showQuickJumper: true,
          showSizeChanger: true,
          showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
        }
        // 如果分页信息为空，则设置为默认值
        params.pageSize = this.pagination.defaultPageSize
        params.pageNum = this.pagination.defaultCurrent
      }
      if(params.deptids == undefined || params.deptids == '') {
        params.deptids = null
      }
      if(params.result == undefined || params.result == '') {
        params.result = null
      }
      this.$get('system/check/getCheckInfoReport', {...params}).then((r) => {
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
      // 取消选中
      this.selectedRowKeys = []
      // 重置查询参数
      this.queryParams.inputdate = moment(this.queryParams.inputdate).format('YYYY-MM-DD')
      this.queryParams.startdate = moment(new Date()).format('YYYY-MM-DD')
      this.queryParams.enddate = moment(new Date()).format('YYYY-MM-DD')

      this.queryParams.result = null
      this.queryParams.keyword = null
      this.queryParams.employeeid = null
      this.queryParams.deptids = []
      this.yyyyStartOpen = false
      this.yyyyEndOpen = false
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
