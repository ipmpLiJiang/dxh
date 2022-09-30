<template>
  <a-card :bordered="false" class="card-area employeeMgt">
    <div :class="advanced ? 'search' : null">
      <a-form layout="horizontal">
        <div :class="advanced ? null: 'fold'">
          <a-row>
            <a-col :md="6" :sm="24" >
              <a-form-item
                label="员工姓名"
                prop="keyword"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}"
              >
                <Select :keyword="queryParams.keyword" url="system/employee?search=" @getSearchValue="getSearchValue"></Select>
              </a-form-item>
            </a-col>
              <a-col :md="6" :sm="24" >
                  <a-form-item
                      label="开始时间"
                      prop="startdate"
                      :labelCol="{span: 7}"
                      :wrapperCol="{span: 16, offset: 1}">
                      <a-date-picker
                          v-model="queryParams.startdate"
                          :format="dateFormat"
                          :valueFormat="dateFormat"
                          @change="startdateChange"
                      />
                  </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24" >
                  <a-form-item
                      label="结束时间"
                      prop="enddate"
                      :labelCol="{span: 7}"
                      :wrapperCol="{span: 16, offset: 1}">
                      <a-date-picker
                          v-model="queryParams.enddate"
                          :format="dateFormat"
                          :valueFormat="dateFormat"
                          @change="enddateChange"
                      />
                  </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24" >
                  <a-form-item
                      label="科室"
                      prop="pbdept"
                      :labelCol="{span: 7}"
                      :wrapperCol="{span: 16, offset: 1}">
                      <a-select
                          show-search
                          v-model="queryParams.pbdept"
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
          </a-row>
          <a-row v-if="advanced">
              <a-col :md="6" :sm="24" >
                  <a-form-item
                      label="人事子范围"
                      prop="rszfw"
                      :labelCol="{span: 7}"
                      :wrapperCol="{span: 16, offset: 1}">
                      <a-select
                          show-search
                          v-model="queryParams.rszfw"
                          :filter-option="filterOption"
                          option-filter-prop="children"
                          @change="searchRszfw"
                          allowClear
                      >
                          <a-select-option v-for="item in zidianInfo.rszfw" :value="item.keyy" :key="item.keyy">
                              {{item.valuee}}
                          </a-select-option>
                      </a-select>
                  </a-form-item>
              </a-col>
            <a-col :md="6" :sm="24">
              <a-form-item
                label="人员类型"
                prop="employeetypes"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-select
                    show-search
                    v-model="queryParams.employeetype"
                    :filter-option="filterOption"
                    option-filter-prop="children"
                    @change="searchEmployeetype"
                    allowClear
                >
                  <a-select-option v-for="item in zidianInfo.employeetype" :value="item.keyy" :key="item.keyy">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
              <a-col :md="6" :sm="24" >
                  <a-form-item
                      label="班种"
                      prop="scheduling"
                      :labelCol="{span: 7}"
                      :wrapperCol="{span: 16, offset: 1}">
                      <a-select
                          show-search
                          v-model="queryParams.scheduling"
                          :filter-option="filterOption"
                          option-filter-prop="children"
                          @change="searchScheduling"
                          allowClear
                      >
                          <a-select-option v-for="item in zidianInfo.kq" :value="item.keyy" :key="item.keyy">
                              {{item.valuee}}
                          </a-select-option>
                      </a-select>
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
        <a-button @click="exportExcel" :disabled="true">导出</a-button>
      </div>
      <a-table
        ref="TableInfo"
        :columns="columns"
        :rowKey="record => record.id"
        :dataSource="dataSource"
        :pagination="pagination"
        :loading="loading"
        @change="handleTableChange"
        :scroll="{ x: 1000, y: 500 }">
          <template slot="operation" slot-scope="text, record">
              <a-button type="text" @click="editRow(record)" :disabled="!record.flag">
                  修改
              </a-button>
          </template>
      </a-table>
        <a-modal class="ant-modal-nofooter" v-model="editVisible" :title="'修改'">
            <a-form-model
                ref="editForm"
                :model="editForm"
                :rules="editFormRules"
                :label-col="labelCol"
                :wrapper-col="wrapperCol"
            >
                <a-form-model-item label="班种" prop="scheduling">
                    <a-select show-search
                              v-model="editForm.scheduling"
                              :filter-option="filterOption"
                              option-filter-prop="children"
                              @change="handleChange"
                              allowClear
                    >
                        <a-select-option
                            v-for="(a, index) in zidianInfo.kq"
                            :value="a.keyy"
                            :key="index"
                        >
                            {{a.valuee}}
                        </a-select-option>
                    </a-select>
                </a-form-model-item>
                <a-form-model-item :wrapper-col="{ span: 14, offset: 4 }">
                    <a-button class="editable-add-btn" @click="resetForm">
                        取消
                    </a-button>
                    <a-button type="primary" style="margin-left: 20px;"  @click="editSubmit">
                        确认
                    </a-button>
                </a-form-model-item>
            </a-form-model>
        </a-modal>
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
      editVisible: false,
      dateFormat: 'YYYY-MM-DD',
      advanced: false,
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
        pbdept: null,
        rszfw: null,
        employeetype: null,
        startdate: null,
        enddate: null,
        scheduling: null,
      },
      loading: false,
      labelCol: { span: 6 },
      wrapperCol: { span: 14 },
      rowRecord: {},
      editFormRules: {},
      editForm: {
         scheduling: ""
      }
    }
  },
  watch: {},
  computed: {
    ...mapGetters(['zidianInfo']),
    columns () {
      let { shstatus, employeetype, kqdept, rszfw, kq } = this.zidianInfo
      return [
        {
          title: '人员编号',
          dataIndex: 'employeecode',
          width: 100
        }, {
          title: '姓名',
          dataIndex: 'employeename',
          width: 100
        }, {
              title: '人员类型',
              dataIndex: 'employeetype',
              customRender: (text, row, index) => {
                  if (text==null) return ''
                  let option = employeetype.filter(item => item.keyy == text)[0]
                  return option ? option.valuee : ''
              },
              width: 100
          }, {
              title: '人事子范围',
              dataIndex: 'rszfw',
              customRender: (text, row, index) => {
                  if (text==null) return ''
                  let option = rszfw.filter(item => item.keyy == text)[0]
                  return option ? option.valuee : ''
              },
              width: 100
          }, {
              title: '时间',
              dataIndex: 'pbdate',
              width: 120
          }, {
          title: '班种',
          dataIndex: 'scheduling',
          customRender: (text, row, index) => {
            if (text==null) return ''
            let option = kq && kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          },
          width: 100
        }, {
          title: '排班科室',
          dataIndex: 'pbdept',
          customRender: (text, row, index) => {
            if (text==null) return ''
            let option = kqdept.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          },
          width: 100
        }, {
              title: '审核状态',
              dataIndex: 'shstatus',
              customRender: (text, row, index) => {
                  if (text==null) return ''
                  let option = shstatus.filter(item => item.keyy == text)[0]
                  return option ? option.valuee : ''
              },
              width: 100
          }
        ,{
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
    // this.fetch({...this.queryParams})
  },
  methods: {
    ...mapActions('getQuery'),

    filterOption (input, option) {
      return (
        option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      )
    },
    startdateChange (date) {
       this.queryParams.startdate = date
    },
    enddateChange (date) {
      this.queryParams.enddate = date
    },
    getSearchValue ({value}) {
      this.queryParams.employeeid = this.queryParams.keyword = value
    },
      searchPbdept(value) {
        if (!value) {
            this.queryParams.pbdept = null
            return
        }
        this.queryParams.pbdept = value
      },
      searchRszfw(value) {
          if (!value) {
              this.queryParams.rszfw = null
              return
          }
          this.queryParams.rszfw = value
      },
      searchEmployeetype(value) {
          if (!value) {
              this.queryParams.employeetype = null
              return
          }
          this.queryParams.employeetype = value
      },
    searchScheduling(value) {
      if (!value) {
        this.queryParams.scheduling = null
        return
      }
      this.queryParams.scheduling = value
    },
    // 选择事件状态
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    editRow (record) {
        this.editVisible = true
        this.editForm= {...record}
        this.editForm.scheduling = Number(record.scheduling)
    },
      handleChange() {

      },
      editSubmit() {
          let params = {...this.editForm}
          this.$put('system/kq/rmxb/updateRmx', {...params}).then(res => {
              if (res.data) {
                  this.editVisible = false
                  this.$message.success('修改成功!')
                  this.fetch({
                      ...this.queryParams
                  })
              }
          }).catch(err => {
              this.$message.warning(err)
          })
      },
      resetForm() {
          this.editVisible = false
          this.editForm.scheduling = null
      },

    exportExcel () {
      this.$export('system/kq/rmxb/export/month', {
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
      this.$get('system/kq/rmxb/getRmxList', {...params}).then((r) => {
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
      this.queryParams.pbdept = null
      this.queryParams.rszfw = null
      this.queryParams.employeetype = null
      this.queryParams.startdate = null
      this.queryParams.enddate = null
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
