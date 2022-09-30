<template>
  <a-card :bordered="false" class="card-area startAttendance">
    <a-form-model class="ruleForm" ref="statusForm" :model="statusForm" v-bind="layout">
      <a-row>
        <a-col :span="8">
          <a-form-model-item label="当前时间" prop="period">
            <span class="ant-form-item-span">{{statusForm.period}}</span>
          </a-form-model-item>
        </a-col>
        <a-col :span="8">
          <a-form-model-item label="是否开启" prop="checked">
            <a-switch checked-children="开" un-checked-children="关" v-model="statusForm.checked" @change="switchChange" :disabled="switchDis" />
          </a-form-model-item>
        </a-col>
        <a-col :span="8" style="text-align: right">
          <a-button type="primary" :loading="finishLoading" :disabled="finishBtnDisabled" @click="finishSubmit">
            确认
          </a-button>
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="8">
          <a-form-model-item label="当前状态">
              <span class="ant-form-item-span">
                {{statusForm.state===1?'开启':'关闭'}}
              </span>
          </a-form-model-item>
        </a-col>
      </a-row>
    </a-form-model>
    <div :class="advanced ? 'search' : null">
      <a-form-model class="ruleForm" ref="formData" :model="formData" v-bind="layout1">
        <div :class="advanced ? null: 'fold'">
          <a-row >
            <a-col :md="8" :sm="24" >
              <a-form-model-item
                label="员工姓名"
                prop="employeename"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}"
              >
                <Select :keyword="formData.employeename" url="system/employee?search=" @getSearchValue="getSearchValue"></Select>
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
                label="提交状态"
                prop="state"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-select v-model="formData.state">
                  <a-select-option v-for="item in stateOption" :value="item.keyy" :key="item.keyy">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
          </a-row>
        </div>
        <span style="float: right; margin-top: 3px; margin-bottom: 18px;">
          <a-button class="editable-add-btn" :loading="startLoading" @click="reset" :disabled="seachBtndis">
            重置
          </a-button>
          <a-button type="primary" :loading="startLoading" @click="handleSubmit" :disabled="seachBtndis">
            查询
          </a-button>
      </span>
      </a-form-model>
    </div>
    <div class="operator">
      <a-button @click="exportFile" ><a-icon type="download" /> 导出 </a-button>
    </div>
    <a-table
      ref="tableInfo"
      :columns="columns"
      :loading="tableLoading"
      :row-key="record => record.employeecode"
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
import moment from 'moment'
export default {
  name: 'startAttendance',
  components: { Select },
  data () {
    return {
      // 表单设置
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
      stateOption: [
        {keyy: 0, valuee: '未提交'},
        {keyy: 1, valuee: '已提交'}
      ],
      // 加载状态
      startLoading: false,
      finishLoading: false,
      tableLoading: false,
      // 禁止状态
      finishBtnDisabled: true,
      seachBtndis: true,
      disInput: true,
      statusForm: {
        period: moment(new Date()).format('YYYY-MM'),
        checked: false,
        state: '',
        id: null
      },
      fileList: [],
      // 查询条件
      formData: {
        employeeid: null,
        employeename: null,
        dept: null,
        state: null,
        startdate: null
      },
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
      }
    }
  },
  watch: {
    kqperiodInfo: {
      handler: function (data) {
        if (data && data.id) {
          this.statusForm.period = data.period
          this.statusForm.state = data.state
          this.statusForm.id = data.id
          this.formData.startdate = data.period
          if (data.state === 1) {
            this.statusForm.checked = true
            this.finishBtnDisabled = false
            this.switchDis = true
            this.seachBtndis = false
            this.fetch({
              ...this.formData
            })
          } else {
            this.statusForm.checked = false
            this.switchDis = false
            this.seachBtndis = true
            this.finishBtnDisabled = true
          }
        }
      },
      immediate: true
    }
  },
  created () {
      this.$store.dispatch('getzidian', {vm: this})
      this.$store.dispatch('getkqPeriodInfo', {vm: this})
  },
  mounted () {},
  computed: {
    ...mapGetters(['zidianInfo', 'kqperiodInfo']),
    columns () {
      let { employeetype, kq, kqdept } = this.zidianInfo
      let stateOption = this.stateOption
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
          title: '考勤单元',
          dataIndex: 'kqdept',
          width: 150,
            customRender: (text, row, index) => {
                if(text == null) return ""
                let option = kqdept.filter(item => item.keyy == text)[0]
                return option ? option.valuee : ''
            }
        },
          {
              title: '提交单元',
              dataIndex: 'tjdept',
              width: 150,
              customRender: (text, row, index) => {
                  if(text == null) return ""
                  let option = kqdept.filter(item => item.keyy == text)[0]
                  return option ? option.valuee : ''
              }
          },
        {
          title: '人员类型',
          dataIndex: 'employeetype',
          width: 150,
          customRender: (text, row, index) => {
              if(text == null) return ""
            let option = employeetype.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: '考勤年月',
          dataIndex: 'startdate',
          width: 120
        },
        {
          title: '提交状态',
          dataIndex: 'state',
          width: 120,
          customRender: (text, row, index) => {
              if(text==null) return
            let option = stateOption.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a01',
          dataIndex: 'a01',
          width: 150,
          customRender: (text, row, index) => {
              let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a02',
          dataIndex: 'a02',
          width: 150,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a03',
          dataIndex: 'a03',
          width: 150,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a04',
          dataIndex: 'a04',
          width: 150,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a05',
          dataIndex: 'a05',
          width: 150,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a06',
          dataIndex: 'a06',
          width: 150,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a07',
          dataIndex: 'a07',
          width: 150,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a08',
          dataIndex: 'a08',
          width: 150,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a09',
          dataIndex: 'a09',
          width: 150,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a10',
          dataIndex: 'a10',
          width: 150,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a11',
          dataIndex: 'a11',
          width: 150,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a12',
          dataIndex: 'a12',
          width: 150,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a13',
          dataIndex: 'a13',
          width: 150,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a14',
          dataIndex: 'a14',
          width: 150,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a15',
          dataIndex: 'a15',
          width: 150,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a16',
          dataIndex: 'a16',
          width: 150,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a17',
          dataIndex: 'a17',
          width: 150,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a18',
          dataIndex: 'a18',
          width: 150,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a19',
          dataIndex: 'a19',
          width: 150,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a20',
          dataIndex: 'a20',
          width: 150,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a21',
          dataIndex: 'a21',
          width: 150,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a22',
          dataIndex: 'a22',
          width: 150,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a23',
          dataIndex: 'a23',
          width: 150,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a24',
          dataIndex: 'a24',
          width: 120,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a25',
          dataIndex: 'a25',
          width: 120,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a26',
          dataIndex: 'a26',
          width: 120,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a27',
          dataIndex: 'a27',
          width: 120,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a28',
          dataIndex: 'a28',
          width: 120,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a29',
          dataIndex: 'a29',
          width: 120,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a30',
          dataIndex: 'a30',
          width: 120,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: 'a31',
          dataIndex: 'a31',
          width: 120,
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        }
      ]
    }
  },
  methods: {
    ...mapActions(['getkqPeriodInfo']),
    // 初始化开始时间
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    // 开启&关闭状态触发 查询初始列表数据
    switchChange (checked) {
      let that = this
      this.$confirm({
        title: '本月考勤一经确认开启后，将进入维护且无法操作关闭！是否继续？',
        onOk () {
          let params = JSON.parse(JSON.stringify({...that.statusForm}))
          if (that.paginationInfo) {
            // 如果分页信息不为空，则设置表格当前第几页，每页条数，并设置查询分页参数
            that.$refs.tableInfo.pagination.current = that.paginationInfo.current
            that.$refs.tableInfo.pagination.pageSize = that.paginationInfo.pageSize
            params.pageSize = that.paginationInfo.pageSize
            params.pageNum = that.paginationInfo.current
          } else {
            // 如果分页信息为空，则设置为默认值
            params.pageSize = that.pagination.defaultPageSize
            params.pageNum = that.pagination.defaultCurrent
          }
          params.dept = params.dept===undefined?null:params.dept
          params.state = params.state===undefined?null:params.state
          delete params.checked
          that.$post('system/kq/period', {...params}).then(res => {
            that.tableLoading = false
            let data = res.data
            if (data) {
              if (data.state === 1) {
                that.statusForm.checked = true
                that.finishBtnDisabled = false
                that.switchDis = true
                that.seachBtndis = false
                that.fetch({
                  ...that.formData
                })
              }
            }
          }).catch(err => {
            that.$message.warning(err)
            that.tableLoading = false
            that.switchDis = false
            that.statusForm.checked = false
          })
        },
        onCancel () {
          that.switchDis = false
          that.statusForm.checked = false
        }
      })
    },
    getSearchValue ({value}) {
      this.formData.employeeid = this.formData.employeename = value
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
        params.dept = params.dept===undefined?null:params.dept
        params.state = params.state===undefined?null:params.state
      this.$get('system/kq/jk', {...params}).then(res => {
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
    // 完成
    finishSubmit () {
      let that = this
        var flag = that.dataSource.find(item => item.state == 0)
        if (flag) {
            that.$confirm({
                title: '当前有未提交考勤的人员，是否确认生成汇总？',
                onOk () {
                    that.pass()
                }
            })
        } else {
            that.pass()
        }
    },
    pass () {
      let that = this
      that.finishLoading = true
      let params = JSON.parse(JSON.stringify({...that.statusForm}))
      delete params.checked
      that.$post('system/kq/jk', {...params}).then(res => {
        if (res.data) {
          that.finishLoading = false
          that.finishBtnDisabled = true
          that.$message.success('审核通过!')
          that.reset()
          that.$store.dispatch('getkqPeriodInfo', {vm: that})
        }
      }).catch(err => {
        that.$message.warning(err)
        that.finishLoading = false
        that.finishBtnDisabled = false
      })
    },
    // 导出
    exportFile () {
      this.$export('system/kq/jk/export', {
        ...this.formData
      })
    },
    reset () {
      this.formData.employeeid = null
      this.formData.employeename = null
      this.formData.dept = null
      this.formData.state = null
      // 重置分页
      this.$refs.tableInfo.pagination.current = this.pagination.defaultCurrent
      this.paginationInfo = null
      this.fetch({...this.formData})
    }
  }
}
</script>
<style scoped lang="less">
  .startAttendance{
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
