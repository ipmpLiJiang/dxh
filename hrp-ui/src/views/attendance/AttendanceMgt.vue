
<template>
  <a-card :bordered="false" class="card-area attendanceMgt">
    <div :class="advanced ? 'search' : null">
      <a-form-model class="ruleForm" ref="formData" :model="formData" v-bind="layout1">
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
                <a-select v-model="formData.dept" allowClear>
                  <a-select-option v-for="item in zidianInfo.kqdept" :value="item.keyy" :key="item.keyy">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-model-item
                label="开始时间"
                prop="startdate"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-month-picker
                  v-model="formData.startdate"
                  :format="dateFormat"
                  :valueFormat="dateFormat"
                />
              </a-form-model-item>
            </a-col>
          </a-row>
          <a-row v-if="advanced">
            <a-col :md="8" :sm="24" >
              <a-form-model-item
                label="结束时间"
                prop="enddate"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-month-picker
                  v-model="formData.enddate"
                  :format="dateFormat"
                  :valueFormat="dateFormat"
                />
              </a-form-model-item>
            </a-col>
          </a-row>
        </div>
        <span style="float: right; margin-top: 3px; margin-bottom: 18px;">
          <a-button class="editable-add-btn" :loading="startLoading" @click="reset">
            重置
          </a-button>
          <a-button type="primary" :loading="startLoading" @click="handleSubmit" :disabled="disSeach">
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
      <a-button style="margin-left: 15px;" @click="exportFile"><a-icon type="download" /> 导出 </a-button>
    </div>
    <a-row>
      <a-col :span="6">
          <span class="title">扣款1合计：</span>
          <span>{{coreCol.kk1||0}}</span>
      </a-col>
      <a-col :span="6">
          <span class="title">扣款2合计：</span>
          <span>{{coreCol.kk2||0}}</span>
      </a-col>
      <a-col :span="6">
          <span class="title">总计：</span>
          <span>{{coreCol.sum||0}}</span>
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
    </a-table>
  </a-card>
</template>
<script>
import { mapActions, mapGetters } from 'vuex'
import Select from '../common/Select'
export default {
  name: 'AttendanceMgt',
  components: { Select },
  data () {
    return {
      // 表单设置
      dateFormat: 'YYYY-MM',
      advanced: false,
      layout1: {
        labelCol: { span: 5 },
        wrapperCol: { span: 13 }
      },
      // 加载状态
      startLoading: false,
      tableLoading: false,
      // 禁止状态
      disSeach: false,
      // 查询条件
      formData: {
        employeeid: null,
        keyword: null,
        dept: [],
        startdate: "",
        enddate: ""
      },
      coreCol: {},
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
      }
    }
  },
  watch: {
    // kqperiodInfo: {
    //   handler: function (data) {
    //     if (data && data.id) {
    //       if (data.state === 1) {
    //         this.disSeach = true
    //         this.fetch({...this.formData})
    //       } else {
    //         this.disSeach = false
    //       }
    //     }
    //   },
    //   immediate: true
    // }
  },
  created () {},
  mounted () {
    this.$store.dispatch('getkqPeriodInfo', {vm: this})
  },
  computed: {
    ...mapGetters(['zidianInfo', 'kqperiodInfo']),
    columns () {
      let {employeetype, kqdept} = this.zidianInfo
      return [
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
          title: '考勤科室',
          dataIndex: 'dept',
          width: 150,
          customRender: (text, row, index) => {
            if (!text) return ''
            let option = kqdept.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          },
        },
        {
          title: '人员类型',
          dataIndex: 'employeetype',
          width: 150,
          customRender: (text, row, index) => {
            let option = employeetype.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: '考勤年月',
          dataIndex: 'kqdate',
          width: 150,
          scopedSlots: { customRender: 'kqdate' }
        },
        // {
        //   title: 'PH',
        //   dataIndex: 'ph',
        //   width: 150,
        //   scopedSlots: { customRender: 'ph' }
        // },
        // {
        //   title: '0000',
        //   dataIndex: 'zero',
        //   width: 150,
        //   scopedSlots: { customRender: 'zero' }
        // },
        {
          title: '病假',
          dataIndex: 'bing',
          width: 150,
          scopedSlots: { customRender: 'bing' }
        },
        {
          title: '事假',
          dataIndex: 'shi',
          width: 150,
          scopedSlots: { customRender: 'shi' }
        },
        {
          title: '产假',
          dataIndex: 'chan',
          width: 150,
          scopedSlots: { customRender: 'chan' }
        },
        {
          title: '备注',
          dataIndex: 'memo',
          width: 150,
          scopedSlots: { customRender: 'memo' }
        },
        // {
        //   title: '待',
        //   dataIndex: 'dai',
        //   width: 150,
        //   scopedSlots: { customRender: 'dai' }
        // },
        // {
        //   title: '对口支援',
        //   dataIndex: 'dkzy',
        //   width: 150,
        //   scopedSlots: { customRender: 'dkzy' }
        // },
        // {
        //   title: '工',
        //   dataIndex: 'gong',
        //   width: 150,
        //   scopedSlots: { customRender: 'gong' }
        // },
        // {
        //   title: '规',
        //   dataIndex: 'gui',
        //   width: 150,
        //   scopedSlots: { customRender: 'gui' }
        // },
        // {
        //   title: '婚',
        //   dataIndex: 'hun',
        //   width: 150,
        //   scopedSlots: { customRender: 'hun' }
        // },
        // {
        //   title: '计',
        //   dataIndex: 'ji',
        //   width: 150,
        //   scopedSlots: { customRender: 'ji' }
        // },
        // {
        //   title: '进',
        //   dataIndex: 'jin',
        //   width: 150,
        //   scopedSlots: { customRender: 'jin' }
        // },
        // {
        //   title: '旷',
        //   dataIndex: 'kuang',
        //   width: 150,
        //   scopedSlots: { customRender: 'kuang' }
        // },
        // {
        //   title: '年',
        //   dataIndex: 'nian',
        //   width: 150,
        //   scopedSlots: { customRender: 'nian' }
        // },
        // {
        //   title: '丧',
        //   dataIndex: 'sang',
        //   width: 150,
        //   scopedSlots: { customRender: 'sang' }
        // },
        // {
        //   title: '探',
        //   dataIndex: 'tan',
        //   width: 150,
        //   scopedSlots: { customRender: 'tan' }
        // },
        // {
        //   title: '下基层',
        //   dataIndex: 'xjc',
        //   width: 150,
        //   scopedSlots: { customRender: 'xjc' }
        // },
        // {
        //   title: '援藏',
        //   dataIndex: 'yz',
        //   width: 150,
        //   scopedSlots: { customRender: 'yz' }
        // },
        // {
        //   title: '援疆',
        //   dataIndex: 'yj',
        //   width: 150,
        //   scopedSlots: { customRender: 'yj' }
        // },
        // {
        //   title: '援外',
        //   dataIndex: 'yw',
        //   width: 150,
        //   scopedSlots: { customRender: 'yw' }
        // },
        // {
        //   title: '无',
        //   dataIndex: 'wu',
        //   width: 150,
        //   scopedSlots: { customRender: 'wu' }
        // },
        // {
        //   title: '放射',
        //   dataIndex: 'fs',
        //   width: 150,
        //   scopedSlots: { customRender: 'fs' }
        // },
        // {
        //   title: '异动',
        //   dataIndex: 'yd',
        //   width: 150,
        //   scopedSlots: { customRender: 'yd' }
        // },
        // {
        //   title: '隔离',
        //   dataIndex: 'gl',
        //   width: 150,
        //   scopedSlots: { customRender: 'gl' }
        // },
        // {
        //   title: '补休',
        //   dataIndex: 'bx',
        //   width: 150,
        //   scopedSlots: { customRender: 'bx' }
        // },
        // {
        //   title: '休息',
        //   dataIndex: 'xx',
        //   width: 150,
        //   scopedSlots: { customRender: 'xx' }
        // },
        // {
        //   title: '补年休',
        //   dataIndex: 'bnx',
        //   width: 150,
        //   scopedSlots: { customRender: 'bnx' }
        // },
        {
          title: '扣款1',
          dataIndex: 'kk1',
          width: 100,
          scopedSlots: { customRender: 'kk1' }
        },
        {
            title: '扣款2',
            dataIndex: 'kk2',
            width: 100,
            scopedSlots: { customRender: 'kk2' }
        },
        {
          title: '合计',
          dataIndex: 'sum',
          width: 150,
          scopedSlots: { customRender: 'sum' }
        },
      ]
    },
  },
  methods: {
    ...mapActions(['getkqPeriodInfo']),
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
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
      this.$get('system/kq/hz/getHzReport', {...params}).then(res => {
        if (res.data) {
          let data = res.data
          const pagination = { ...this.pagination }
          pagination.total = data.total
          this.dataSource = data.rows
          this.coreCol = {...data.sum}
          this.pagination = pagination
          this.tableLoading = false
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
      this.$export('system/kq/hz/export', {
        ...this.formData
      })
    },
    reset () {
      // 重置查询参数
      this.formData.employeeid = null
      this.formData.keyword = null
      this.formData.dept = null
      this.formData.startdate = null
      this.formData.enddate = null
      // 重置分页
      this.$refs.tableInfo.pagination.current = this.pagination.defaultCurrent
      this.paginationInfo = null
      this.fetch({...this.formData})
    }
  }
}
</script>
<style scoped lang="less">
  .attendanceMgt{
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
  .ant-calendar-picker{
    width: 100%;
  }
</style>
