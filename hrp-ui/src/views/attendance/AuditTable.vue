<template>
  <a-card :bordered="false" class="card-area auditTable">
    <div :class="advanced ? 'search' : null">
      <a-form-model class="ruleForm" ref="formData" :model="formData" v-bind="layout">
        <div :class="advanced ? null: 'fold'">
          <a-row >
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="日期"
                prop="pbdate"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-month-picker
                  v-model="formData.pbdate"
                  :format="dateFormat"
                  :valueFormat="dateFormat"
                  @change="pbdateChange"
                />
              </a-form-item>
            </a-col>
              <a-col :md="8" :sm="24" >
                  <a-form-model-item
                      label="科室"
                      prop="pbdept"
                      :labelCol="{span: 7}"
                      :wrapperCol="{span: 16, offset: 1}">
                      <a-select
                          show-search
                          v-model="formData.pbdept"
                          :filter-option="filterOption"
                          option-filter-prop="children"
                          @change="searchPbdept"
                          allowClear
                      >
                          <a-select-option v-for="item in zidianInfo.kqdept" :value="item.keyy" :key="item.keyy">
                              {{item.valuee}}
                          </a-select-option>
                      </a-select>
                  </a-form-model-item>
              </a-col>
              <a-col :md="8" :sm="24" >
                  <a-form-model-item
                      label="审核状态"
                      prop="shstatus"
                      :labelCol="{span: 7}"
                      :wrapperCol="{span: 16, offset: 1}">
                      <a-select v-model="formData.shstatus" allowClear @change="searchShstatus">
                          <a-select-option v-for="item in zidianInfo.shstatus" :value="item.keyy" :key="item.keyy">
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
          <a-button @click="exportFile"><a-icon type="download" /> 导出 </a-button>
      </div>
    <a-table
      ref="tableInfo"
      :columns="columns"
      :loading="tableLoading"
      :row-key="record => record.id"
      :data-source="dataSource"
      :pagination="pagination"
      @change="handleTableChange"
      :scroll="{ x: 1200, y: 500 }"
    >
        <a v-if="record.shstatus==1" slot="operation" slot-scope="text, record" href="javascript:;" @click="audit(record)">审 核</a>
        <a v-else slot="operation" slot-scope="text, record" href="javascript:;" @click="audit(record)">查 看</a>
    </a-table>
      <a-modal v-model="visible" class="ant-modal-nofooter" width="80%" title="审核" @ok="handleOk">
          <a-table
              :columns="auditColumns"
              :loading="loading"
              :row-key="record => record.id"
              :data-source="auditDataSource"
              :scroll="{ x: 1000, y: 500 }"
              :pagination="false"
          >
          </a-table>
          <a-form-model
              v-if="auditDetail.shstatus==1"
              ref="auditRuleForm"
              :model="auditRuleForm"
              :rules="coreRules"
              layout="inline"
              class="auditTable-footer"
          >
              <a-form-model-item label="审核" prop="shstatus">
                  <a-select class="shstatus" v-model="auditRuleForm.shstatus" style="width: 120px" @change="changeShstatus" allowClear>
                      <a-select-option v-for="item in auditOptions" :value="item.keyy" :key="item.keyy">
                          {{item.valuee}}
                      </a-select-option>
                  </a-select>
              </a-form-model-item>
              <a-form-model-item label="驳回理由" prop="memo" v-if="auditRuleForm.shstatus==3">
                  <a-input
                      type="textarea"
                      v-model="auditRuleForm.memo"
                      allowClear
                  />
              </a-form-model-item>
              <a-form-model-item>
                  <a-button @click="resetForm">
                      取 消
                  </a-button>
                  <a-button style="margin-left: 20px;" type="primary" @click="saveSubmit">
                      确 认
                  </a-button>
              </a-form-model-item>
          </a-form-model>
      </a-modal>
  </a-card>
</template>
<script>
import { mapActions, mapGetters } from 'vuex'
import moment from 'moment'
export default {
  name: 'AuditTable',
  components: {},
  data () {
    return {
        visible: false,
      // 表单设置
      dateFormat: 'YYYY-MM',
      advanced: false,
      layout: {
        labelCol: { span: 8 },
        wrapperCol: { span: 14 }
      },
      // 加载状态
      startLoading: false,
      tableLoading: false,
      // 查询条件
      formData: {
          pbdept: null,
          shstatus: null,
          pbdate: null,
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
      },

        loading: false,
        auditDataSource: [],
        daysList: [],
        auditColumns: [],
        auditOptions: [
            {keyy: 2, valuee: "通过"},
            {keyy: 3, valuee: "拒绝"},
        ],
        auditDetail: {},
        auditRuleForm: {
          shstatus: null,
          memo: ""
        },
      coreRules: {
        shstatus: [
          { required: true, message: ' ', trigger: 'change' }
        ],
        memo: [
          { required: true, message: ' ', trigger: 'change' }
        ],
      }
    }
  },
  watch: {
      "$route.path": {
          handler(to) {
              if (to === "/attendance/auditTable") {
                  this.fetch({
                    ...this.formData
                  })
              }
          },
          deep: true
      }
  },
    computed: {
        ...mapGetters(['zidianInfo']),
        columns () {
            let { shstatus, kqdept } = this.zidianInfo
            return [
                {
                    title: '排班科室',
                    dataIndex: 'pbdept',
                    width: 100,
                    customRender: (text, row, index) => {
                        let option = kqdept.filter(item => item.keyy == text)[0]
                        return option ? option.valuee : ''
                    }
                },
                {
                    title: '排班日期',
                    dataIndex: 'pbdate',
                    width: 100,
                    scopedSlots: { customRender: 'pbdate' }
                },
                {
                    title: '提交日期',
                    dataIndex: 'tjdate',
                    width: 100,
                    scopedSlots: { customRender: 'tjdate' }
                },
                {
                    title: '科室人数',
                    dataIndex: 'kscount',
                    width: 100,
                    scopedSlots: { customRender: 'kscount' }
                },
                {
                  title: '审核状态',
                  dataIndex: 'shstatus',
                  width: 100,
                  customRender: (text, row, index) => {
                    let option = shstatus.filter(item => item.keyy == text)[0]
                    return option ? option.valuee : ''
                  }
                },
                {
                    title: '备注',
                    dataIndex: 'memo',
                    width: 100,
                    scopedSlots: { customRender: 'memo' }
                },
                {
                    title: '操作',
                    dataIndex: 'operation',
                    scopedSlots: {customRender: 'operation'},
                    fixed: 'right',
                    width: 80
                }
            ]
        },
      defColumns() {
        let { rszfw } = this.zidianInfo
        return [
          {
            title: '工号',
            dataIndex: 'employeecode',
            width: 100,
            scopedSlots: {customRender: 'employeecode'}
          },
          {
            title: '姓名',
            dataIndex: 'employeename',
            width: 100,
            scopedSlots: {customRender: 'employeename'}
          },
          {
            title: '人事子范围',
            dataIndex: 'rszfw',
            width: 120,
            customRender: (text, row, index) => {
              if(!text) return ""
              let option = rszfw.filter(item => item.keyy == text)
              let result = option[0]?option[0].valuee: ""
              return result
            }
          },
        ]

      }
    },
  created () {
  },
  mounted () {
      this.fetch({
        ...this.formData
      })
  },

  methods: {
    ...mapActions(['']),
      filterOption (input, option) {
          return (
              option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
          )
      },
    // 时间
    pbdateChange (date) {
      this.formData.pbdate = date
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
      searchShstatus(value) {
          if(!value) {
              this.formData.shstatus = ""
              return
          }
          this.formData.shstatus = value
      },
      searchPbdept(value) {
        if(!value) {
            this.formData.pbdept = ""
            return
        }
          this.formData.pbdept = value
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
    // 列表查询
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
      this.$get('system/kq/sqb', {...params}).then(res => {
        if (res.data) {
          let data = res.data
          const pagination = { ...this.pagination }
          pagination.total = data.total
          this.dataSource = data.rows
          this.coreCol = data.sum
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
      // 导出
      exportFile () {
          this.$export('system/kq/sqb/export', {
              ...this.formData
          })
      },
    reset () {
      // 重置查询参数
      this.formData.shstatus = null
      this.formData.pbdept = null
      this.formData.pbdate = null
      // 重置分页
      this.$refs.tableInfo.pagination.current = this.pagination.defaultCurrent
      this.paginationInfo = null
      this.fetch({...this.formData})
    },
    handleOk(e) {
      this.visible = false;
    },
    // 审核按钮
    audit(row) {
      this.visible = true
      this.auditDetail = row
      this.auditRuleForm.memo = null
      this.auditRuleForm.shstatus = null
      this.getDateList(row.pbdate)
    },
    changeShstatus(value) {
      if (value==3) {
        this.coreRules.memo[0].required = true
      } else {
        this.coreRules.memo[0].required = false
      }
    },
      // 表头数据
    getDateList (pbdate) {
      var days = moment(pbdate, "YYYY-MM").daysInMonth()
      let columns = []
      for(let i = 1;i <= days;i ++) {
        if (i < 10) {
          i= "0" + i
        }
        let label = moment(pbdate).format('MM') + "/" + i
        columns.push(this.getColumns(label, "a" + i, 100, i))
      }
      this.auditColumns = this.defColumns.concat(columns)
      this.auditFetch()
    },
      //处理表头
      getColumns(title, dataIndex, width, customRender) {
        let { kq } = this.zidianInfo
        return {
          title: title,
          dataIndex: dataIndex,
          width: width,
          // scopedSlots: { customRender:  customRender},
          customRender: (text, row, index) => {
            let option = kq.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        }
      },
    resetForm() {
      this.auditRuleForm.memo = null
      this.auditRuleForm.shstatus = null
      this.visible = false
    },
    // 提交审核结果
      saveSubmit() {
        this.$refs.auditRuleForm.validate(valid => {
          if (valid) {
            let params = {}
            debugger
            params.shstatus = this.auditRuleForm.shstatus
            params.id = this.auditDetail.id
            params.memo = params.shstatus==2?"":this.auditRuleForm.memo //审核为通过 清空备注
            this.$put1('system/kq/sqb/shSqb', params).then(res => {
              this.$message.success("提交成功！")
              this.visible = false
              this.fetch({...this.formData})
            }).catch(err => {
              this.$message.warning(err)
            })
          } else {
            console.log('error submit!!');
            return false;
          }
        });

      },
      // 查询考勤明细
      auditFetch () {
        this.loading = true
        this.$get('system/kq/sqb/getymxById?id=' + this.auditDetail.id).then(res => {
          if (res.data) {
            this.auditDataSource = res.data
            this.loading = false
          } else {
            this.loading = false
          }
        }).catch(err => {
          this.$message.warning(err)
          this.loading = false
        })
      },
  }
}
</script>
<style scoped lang="less">
  .auditTable{
    width: 100%;
  }
</style>
<style lang="less">
  @import "../../../static/less/Common";
  .auditTable-footer {
      text-align: right;
      margin-top: 20px;
      .shstatus {
          margin-right: 20px;
      }
  }
  .ant-modal-nofooter {
      .ant-modal-footer {
          display: none;
      }
  }
</style>
