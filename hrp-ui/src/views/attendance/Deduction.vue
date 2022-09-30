<template>
  <a-card :bordered="false" class="card-area deduction">
    <a-row style="margin-bottom: 10px;">
      <a-col :span="4">
        <span>当前时间:</span>
        <span class="ant-form-item-span">{{formData.kqdate}}</span>
      </a-col>
      <a-col :span="8">
        <span>当前状态:</span>
        <span class="ant-form-item-span">
          {{formData.status===1?'开启':'关闭'}}
        </span>
      </a-col>
      <!--<a-col :span="8" style="text-align: right">-->
      <!--<a-button type="primary" @click="finishBtn" :disabled="finishBtnDisabled">-->
      <!--确认-->
      <!--</a-button>-->
      <!--</a-col>-->
    </a-row>
    <a-form-model class="ruleForm" ref="formData" :model="formData" v-bind="layout1">
      <a-row >
        <a-col :md="5" :sm="24" >
          <a-form-model-item
            label="员工姓名"
            prop="employeename"
            :labelCol="{span: 7}"
            :wrapperCol="{span: 16, offset: 1}"
          >
            <Select :keyword="formData.employeename" url="system/employee?search=" @getSearchValue="getSearchValue"></Select>
          </a-form-model-item>
        </a-col>
        <a-col :md="5" :sm="24" >
          <a-form-model-item
            label="科室"
            prop="dept"
            :labelCol="{span: 7}"
            :wrapperCol="{span: 16, offset: 1}">
            <a-select v-model="formData.dept" allowClear>
              <a-select-option v-for="item in zidianInfo.kqdept" :value="item.valuee" :key="item.keyy">
                {{item.valuee}}
              </a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>
        <a-col :md="5" :sm="24" >
          <a-form-model-item
            label="提交状态"
            prop="state"
            :labelCol="{span: 7}"
            :wrapperCol="{span: 16, offset: 1}">
            <a-select
              v-model="formData.kqstatus"
              placeholder="请选择"
              allowClear
            >
              <a-select-option value="1">
                已提交
              </a-select-option>
              <a-select-option value="0">
                未提交
              </a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>
        <a-col :md="5" :sm="24" >
          <a-form-model-item
            label="扣款类型"
            prop="state"
            :labelCol="{span: 7}"
            :wrapperCol="{span: 16, offset: 1}">
            <a-select
              v-model="formData.kktype"
              placeholder="请选择"
            >
              <a-select-option value="1">
                考勤扣款
              </a-select-option>
              <a-select-option value="2">
                补扣
              </a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>
          <a-col :md="2" :sm="24" >
              <a-form-model-item
                  :labelCol="{span: 7}"
                  :wrapperCol="{span: 16, offset: 1}">
                  <a-button type="primary" :loading="startLoading" @click="handleSubmit" >
                      查询
                  </a-button>
              </a-form-model-item>
          </a-col>
      </a-row>
    </a-form-model>
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
      <template slot="operation" slot-scope="text, record">
        <a-button @click="editRow(record)" :disabled="finishBtnDisabled">
          编辑
        </a-button>
      </template>
    </a-table>
    <a-modal class="ant-modal-nofooter" v-model="editVisible" :title="'编辑'">
      <a-form-model
        ref="editForm"
        :model="editForm"
        :rules="editFormRules"
        v-bind="layout2"
      >
        <a-form-model-item label="扣款1" prop="kk1">
          <a-input
            v-model="editForm.kk1"
            allowClear
          />
        </a-form-model-item>
        <a-form-model-item label="扣款2" prop="kk2">
          <a-input
            v-model="editForm.kk2"
            allowClear
          />
        </a-form-model-item>
        <a-form-model-item label="备注" prop="memo">
          <a-input
            v-model="editForm.memo"
            allowClear
          />
        </a-form-model-item>
        <a-form-model-item :wrapper-col="{ span: 14, offset: 4 }">
          <a-button class="editable-add-btn" @click="resetForm">
            重置
          </a-button>
          <a-button type="primary" style="margin-left: 20px;"  @click="editSubmit">
            确认
          </a-button>
        </a-form-model-item>
      </a-form-model>
    </a-modal>
  </a-card>
</template>
<script>
import { mapActions, mapGetters } from 'vuex'
import Select from '../common/Select'

export default {
  name: 'Deduction',
  components: { Select },
  data () {
    return {
      // 表单设置
      layout: {
        labelCol: { span: 5 },
        wrapperCol: { span: 13 }
      },
      layout1: {
        labelCol: { span: 5 },
        wrapperCol: { span: 13 }
      },
      layout2: {
        labelCol: { span: 5 },
        wrapperCol: { span: 13 }
      },
        statusOption: [
            {keyy: 0, valuee: "未提交"},
            {keyy: 1, valuee: "已提交"},
        ],
      finishLoading: false,
      // 禁止状态
      finishBtnDisabled: true,
      // 加载状态
      startLoading: false,
      tableLoading: false,
      // 禁止状态
      disSeach: true,
      // 查询条件
      formData: {
        employeeid: null,
        employeename: null,
        dept: null,
        kqstatus: null,
        kqdate: null,
        kktype: "1",
      },
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
      // 新增
      editVisible: false,
      editForm: {
        kk1: 0,
        kk2: 0,
        id: null,
        memo: "",
      },
      editFormRules: {},
      coreCol: {},
    }
  },
  watch: {
    kqperiodInfo: {
      handler: function (data) {
        if (data && data.period) {
          this.formData.kqdate = data.period
          this.formData.id = data.id
          this.formData.status = data.state
          if (data.state === 1) {
            this.finishBtnDisabled = false
            this.fetch({...this.formData})
          } else {
            this.finishBtnDisabled = true
          }
        }
      },
      immediate: true
    }
  },
  created () {},
  mounted () {
    this.$store.dispatch('getkqPeriodInfo', {vm: this})
    // if (this.formData.status === 1) {
    //   this.$message.warning('本月考勤系统已开启状态！')
    // }
  },
  computed: {
    ...mapGetters(['zidianInfo', 'kqperiodInfo']),
    columns () {
      let { employeetype, kqdept } = this.zidianInfo
      let statusOption = this.statusOption
      return [
        {
          title: '人员编号',
          dataIndex: 'employeecode',
            width: 100,
            scopedSlots: { customRender: 'employeecode' },
        },
        {
          title: '姓名',
          dataIndex: 'employeename',
            width: 100,
            scopedSlots: { customRender: 'employeename' }
        },
        {
          title: '考勤单元',
          dataIndex: 'dept',
            width: 100,
            customRender: (text, row, index) => {
                if(text == null) return ""
                let option = kqdept.filter(item => item.keyy == text)[0]
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
              title: '提交状态',
              dataIndex: 'kqstatus',
              width: 100,
              customRender: (text, row, index) => {
                  let option = statusOption.filter(item => item.keyy == text)[0]
                  return option ? option.valuee : ''
              }
          },
        {
          title: '考勤年月',
          dataIndex: 'kqdate',
            width: 120,
            scopedSlots: { customRender: 'kqdate' }
        },
        {
          title: '病假',
          dataIndex: 'bing',
            width: 100,
            scopedSlots: { customRender: 'bing' }
        },
        {
          title: '事假',
          dataIndex: 'shi',
            width: 100,
          scopedSlots: { customRender: 'shi' }
        },
        {
          title: '空',
          dataIndex: 'kong',
          width: 100,
          scopedSlots: { customRender: 'kong' }
        },
        {
          title: '岗位工资',
          dataIndex: 'gwgz',
            width: 100,
          scopedSlots: { customRender: 'gwgz' }
        },
        {
          title: '薪级工资',
          dataIndex: 'xjgz',
            width: 100,
          scopedSlots: { customRender: 'xjgz' }
        },
        {
          title: '卫生费',
          dataIndex: 'wsf',
            width: 100,
            scopedSlots: { customRender: 'wsf' }
        },
        {
          title: '基础性绩效',
          dataIndex: 'jcxjx',
            width: 120,
          scopedSlots: { customRender: 'jcxjx' }
        },
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
          title: '备注',
          dataIndex: 'memo',
          width: 120,
          scopedSlots: { customRender: 'memo' }
        },
        {
          title: '操作',
          dataIndex: 'operation',
          width: 100,
          fixed: 'right',
          scopedSlots: {customRender: 'operation'}
        }
      ]
    }
  },
  methods: {
    ...mapActions(['getkqPeriodInfo']),
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
    // 导出
    exportFile () {
      this.$export('system/kq/hz/export/kk', {
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
        params.kqstatus = params.kqstatus===undefined?null:params.kqstatus
        this.$get('system/kq/hz', {...params}).then(res => {
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
    editRow (row) {
      this.editVisible = true
      this.editForm = {...this.editForm, ...row}
    },
    // 保存
    editSubmit () {
      this.$refs.editForm.validate(valid => {
        if (valid) {
          let params = {}
          params.id = this.editForm.id
          params.kk1 = this.editForm.kk1
          params.kk2 = this.editForm.kk2
          params.memo = this.editForm.memo
          this.$put('system/kq/hz', {...params}).then(res => {
            if (res.data) {
              this.$message.success('修改成功！')
              this.editVisible = false
              this.fetch({...this.formData})
            }
          }).catch(err => {
            this.$message.warning(err)
          })
        } else {
          return false
        }
      })
    },

    finishBtn () {
      let that = this
      this.$confirm({
        title: '确认是否继续？点击确认按钮后，将本月数据推送给财务 且关闭当前考勤期间！',
        onOk () {
          let params = {
            id: that.formData.id,
            period: that.formData.kqdate
          }
          that.finishLoading = true
          that.$get('system/kq/hz/passKqHz', {...params}).then(res => {
            if (res.data) {
              that.$message.success('提交成功!')
              that.finishLoading = false
              that.finishBtnDisabled = true
            }
          }).catch(err => {
            that.$message.warning(err)
            that.finishLoading = false
            that.finishBtnDisabled = false
          })
        },
        onCancel () {
        }
      })
    },

    resetForm () {
      this.editForm.kk1 = 0
      this.editForm.kk2 = 0
      this.editForm.memo = ""
    }
  }
}
</script>
<style scoped lang="less">
  .deduction{
    width: 100%;
    .ruleForm{
      .ant-form-item{
        margin-bottom: 5px!important;
      }
    }
    .actionBtn{
      height: 45px;
      position: relative;
      .button{
        position: absolute;
        right: 0;
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
