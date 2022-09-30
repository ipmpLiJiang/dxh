<template>
  <a-card :bordered="false" class="card-area payMgt">
    <div :class="advanced ? 'search' : null">
      <a-form-model class="ruleForm" ref="formDataaddPay" :model="formData" v-bind="layout1">
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
                <a-select v-model="formData.deptids" mode="multiple" allowClear>
                  <a-select-option v-for="item in zidianInfo.dept" :value="item.keyy" :key="item.keyy">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-model-item
                label="人员类型"
                prop="employeetype"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-select v-model="formData.employeetype" mode="multiple" allowClear>
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
                label="人员状态"
                prop="status"
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
      <a-button @click="onSetRowKeys">
        批量启薪
      </a-button>
      <a-upload
        :multiple="false"
        :file-list="fileList"
        :before-upload="beforeUpload"
        :customRequest="handleUpload"
        :remove="handleRemove"
      >
        <a-button > <a-icon type="upload" /> 导入</a-button>
      </a-upload>
      <a-button @click="exportFile"><a-icon type="download" /> 导出 </a-button>
    </div>
    <a-table
      ref="tableInfo"
      :columns="columns"
      :loading="tableLoading"
      :row-key="record => record.employeeid"
      :data-source="dataSource"
      :pagination="pagination"
      :scroll="{ x: 1500, y: 500 }"
      :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
      @change="handleTableChange"
    >
    </a-table>
    <a-modal class="ant-modal-nofooter" v-model="editVisible" :title="'批量修改'" :width="700">
      <a-form-model
        ref="editForm"
        :model="editForm"
        :rules="editFormRules"
        v-bind="layout2"
      >
        <a-row>
          <a-col :span="8">
            <a-form-model-item label="其他" prop="qt">
              <a-input
                v-model="editForm.qt"
                allowClear
              />
            </a-form-model-item>

            <a-form-model-item label="薪级工资" prop="xjgz">
              <a-input
                v-model="editForm.xjgz"
                allowClear
              />
            </a-form-model-item>
            <a-form-model-item label="高出部" prop="gcb">
              <a-input
                v-model="editForm.gcb"
                allowClear
              />
            </a-form-model-item>
            <a-form-model-item label="卫生费" prop="wsf">
              <a-input
                v-model="editForm.wsf"
                allowClear
              />
            </a-form-model-item>
            <a-form-model-item label="高聘" prop="gp">
              <a-input
                v-model="editForm.gp"
                allowClear
              />
            </a-form-model-item>
            <a-form-model-item label="差额补贴" prop="cebt">
              <a-input
                v-model="editForm.cebt"
                allowClear
              />
            </a-form-model-item>
            <a-form-model-item label="调代2017" prop="td2017">
              <a-input
                v-model="editForm.td2017"
                allowClear
              />
            </a-form-model-item>
            <a-form-model-item label="调代2020" prop="td2020">
              <a-input
                v-model="editForm.td2020"
                allowClear
              />
            </a-form-model-item>

            <a-form-model-item label="医疗补助" prop="ylbz">
              <a-input
                v-model="editForm.ylbz"
                allowClear
              />
            </a-form-model-item>
          </a-col>
          <a-col :span="8">
            <a-form-model-item label="岗位工资" prop="gwgz">
              <a-input
                v-model="editForm.gwgz"
                allowClear
              />
            </a-form-model-item>
            <a-form-model-item label="护龄津贴" prop="hljt">
              <a-input
                v-model="editForm.hljt"
                allowClear
              />
            </a-form-model-item>
            <a-form-model-item label="特岗津贴" prop="tgjt">
              <a-input
                v-model="editForm.tgjt"
                allowClear
              />
            </a-form-model-item>
            <a-form-model-item label="独子补贴" prop="dzbt">
              <a-input
                v-model="editForm.dzbt"
                allowClear
              />
            </a-form-model-item>
            <a-form-model-item label="交通补贴" prop="jtbt">
              <a-input
                v-model="editForm.jtbt"
                allowClear
              />
            </a-form-model-item>
            <a-form-model-item label="工资预付" prop="gzyf">
              <a-input
                v-model="editForm.gzyf"
                allowClear
              />
            </a-form-model-item>
            <a-form-model-item label="调代2018" prop="td2018">
              <a-input
                v-model="editForm.td2018"
                allowClear
              />
            </a-form-model-item>
            <a-form-model-item label="差额" prop="td2021">
              <a-input
                v-model="editForm.td2021"
                allowClear
              />
            </a-form-model-item>
            <a-form-model-item label="劳模补助" prop="lmbz">
              <a-input
                v-model="editForm.lmbz"
                allowClear
              />
            </a-form-model-item>

          </a-col>
          <a-col :span="8">
            <a-form-model-item label="冲销津贴" prop="cxjt">
              <a-input
                v-model="editForm.cxjt"
                allowClear
              />
            </a-form-model-item>
            <a-form-model-item label="护士10%" prop="hs">
              <a-input
                v-model="editForm.hs"
                allowClear
              />
            </a-form-model-item>
            <a-form-model-item label="房贴" prop="ft">
              <a-input
                v-model="editForm.ft"
                allowClear
              />
            </a-form-model-item>
            <a-form-model-item label="基础性绩效" prop="jcxjx">
              <a-input
                v-model="editForm.jcxjx"
                allowClear
              />
            </a-form-model-item>
            <a-form-model-item label="物业补贴" prop="wybt">
              <a-input
                v-model="editForm.wybt"
                allowClear
              />
            </a-form-model-item>
            <a-form-model-item label="调代2016" prop="td2016">
              <a-input
                v-model="editForm.td2016"
                allowClear
              />
            </a-form-model-item>
            <a-form-model-item label="调代2019" prop="td2019">
              <a-input
                v-model="editForm.td2019"
                allowClear
              />
            </a-form-model-item>
            <a-form-model-item label="上存" prop="sc">
              <a-input
                v-model="editForm.sc"
                allowClear
              />
            </a-form-model-item>
            <a-form-model-item label="应发" prop="issue">
              <a-input
                v-model="totalMoney"
                allowClear
                :disabled="true"
              />
            </a-form-model-item>
             <!-- <a-form-model-item label="代理养老" prop="bzyl">
              <a-input
                v-model="editForm.bzyl"
                allowClear
              />
            </a-form-model-item> -->
          </a-col>
        </a-row>
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
  name: 'StartupPay',
  components: { Select },
  data () {
    return {
      fileList: [],
      // 表单设置
      advanced: false,
      layout1: {
        labelCol: { span: 5 },
        wrapperCol: { span: 13 }
      },
      layout2: {
        labelCol: { span: 8 },
        wrapperCol: { span: 14 }
      },
      // 加载状态
      startLoading: false,
      tableLoading: false,
      // 查询条件
      formData: {
        periodid: null,
        employeeid: null,
        keyword: null,
        deptids: [],
        states: [],
        rsfws: [],
        rszfws: [],
        employeetypes: [],
        id: null
      },
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
      option: [],
      // 新增
      editVisible: false,
      editForm: {
        gwgz: 0,
        cxjt: 0,
        xjgz: 0,
        hljt: 0,
        hs: 0,
        gcb: 0,
        tgjt: 0,
        ft: 0,
        wsf: 0,
        //bzyl: 0,
        ylbz: 0,
        dzbt: 0,
        jcxjx: 0,
        gp: 0,
        jtbt: 0,
        wybt: 0,
        cebt: 0,
        gzyf: 0,
        td2016: 0,
        td2017: 0,
        td2018: 0,
        td2019: 0,
        td2020: 0,
        td2021: 0,
        sc: 0,
        issue: 0,
        qt: 0
      },
      editFormRules: {}
    }
  },
  watch: {
    periodInfo: {
      handler: function (data) {
        if (data && data.id) {
          this.formData.periodid = data.id
          this.formData.status = data.status
          this.formData.period = data.period
        }
      },
      immediate: true
    }
  },
  created () {
    this.$store.dispatch('getPeriodInfo', {vm: this})
  },
  mounted () {
    this.fetch({...this.formData})
  },
  computed: {
    ...mapGetters(['zidianInfo', 'periodInfo']),
    columns () {
      let { dept, job, employeetype, rszfw } = this.zidianInfo
      return [
        {
          title: '人员编号',
          dataIndex: 'code',
          scopedSlots: { customRender: 'code' }
        },
        {
          title: '姓名',
          dataIndex: 'employeename',
          scopedSlots: { customRender: 'employeename' }
        },
        {
          title: '科室',
          dataIndex: 'deptname',
          scopedSlots: { customRender: 'deptname' }
        },
        {
          title: '岗位',
          dataIndex: 'job',
          customRender: (text, row, index) => {
            let option = job.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: '人员类型',
          dataIndex: 'employeetype',
          customRender: (text, row, index) => {
            let option = employeetype.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: '人事子范围',
          dataIndex: 'rszfwname',
          scopedSlots: { customRender: 'rszfwname' }
        }
      ]
    },
    totalMoney () {
      let that = this
      let total = 0
      let data = JSON.parse(JSON.stringify(that.editForm))
      delete data.ylbz
      delete data.lmbz
      delete data.issue
      for (let key in data) {
        total += Number(data[key])
      }
      that.editForm.issue = total.toFixed(2)
      return that.editForm.issue
    }
  },
  methods: {
    ...mapActions(['getPeriodInfo']),
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    // 匹配模糊查询数据
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
    // 多选
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    onSetRowKeys () {
      this.resetForm()
      if (this.selectedRowKeys.length === 0) {
        this.$message.warning('请勾选您要启薪的记录！')
        return false
      }
      this.editVisible = true
    },
    handleTableChange (pagination, filters, sorter) {
      this.paginationInfo = pagination
      this.fetch({
        ...this.formData
      })
    },
    // 导出
    exportFile () {
      this.$export('system/emolument/export/qx', {
        ...this.formData
      })
    },
    handleRemove (file) {
      const index = this.fileList.indexOf(file)
      const newFileList = this.fileList.slice()
      newFileList.splice(index, 1)
      this.fileList = newFileList
    },
    // 导入之前
    beforeUpload (file) {
      let fileName = file.name.split('.')
      if (fileName[fileName.length - 1] === 'xlsx') {
        this.fileInfo = file
        return true
      } else {
        // this.fileList = []
        // this.fileInfo = []
        this.$message.warning('请上传正确格式的表格!')
        return false
      }
    },
    // 自定义上传
    handleUpload () {
      const formImgData = new FormData()
      formImgData.append('file', this.fileInfo)
      this.$upload('/system/emolument/import/qx', formImgData).then(res => {
        let data = res.data.data.data
        let error = res.data.data.error
        if (data && data.length > 0) {
          this.$message.success('导入成功！')
          this.fileList = [...this.fileList, this.fileInfo]
        }
        if (error && error.length > 0) {
          this.$message.warning('导入文件内容有误！')
        }
      }).catch(err => {
        this.$message.warning(err)
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
      this.$get('system/emolument/getUnEmolumentList', {...params}).then(res => {
        if (res.data) {
          let data = res.data
          const pagination = { ...this.pagination }
          pagination.total = data.total
          this.dataSource = data.rows
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
    // 保存
    editSubmit () {
      this.$refs.editForm.validate(valid => {
        if (valid) {
          let params = {...this.editForm}
          params.issue = this.totalMoney
          params.employeeids = this.selectedRowKeys
          params.periodid = this.formData.periodid
          params.period = this.formData.period
          this.$post('system/emolument/batchAddEmolument', {...params}).then(res => {
            if (res.data) {
              this.$message.success('修改成功！')
              this.editVisible = false
              this.fetch({...this.formData})
              this.selectedRowKeys = []
            }
          }).catch(err => {
            this.$message.warning(err)
          })
        } else {
          return false
        }
      })
    },
    reset () {
      // 参数重置
      this.selectedRowKeys = []
      this.formData.employeeid = null
      this.formData.keyword = null
      this.formData.deptids = []
      this.formData.states = []
      this.formData.rsfws = []
      this.formData.rszfws = []
      this.formData.employeetypes = []
      // 重置分页
      this.$refs.tableInfo.pagination.current = this.pagination.defaultCurrent
      this.paginationInfo = null
      this.fetch({...this.formData})
    },
    resetForm () {
      this.editForm = {
        gwgz: 0, cxjt: 0, xjgz: 0, hljt: 0, hs: 0, gcb: 0, tgjt: 0, ft: 0, qt: 0,  wsf: 0, ylbz: 0, dzbt: 0, jcxjx: 0, gp: 0, jtbt: 0, wybt: 0, cebt: 0, gzyf: 0, td2016: 0, td2017: 0, td2018: 0, td2019: 0, td2020: 0, td2021: 0, sc: 0, issue: 0, lmbz: 0
      }
    }
  }
}
</script>
<style scoped lang="less">
  .payMgt{
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
