<template>
  <div class="employeeEvt">
    <a-form-model ref="searchForm" :form="searchForm" layout="inline" :model="searchForm" :rules="ruleSearchForm" style="margin: 20px 0" :label-col="labelCol" :wrapper-col="wrapperCol">
      <a-row>
        <a-col :span="6">
          <a-form-model-item label="事件类型" prop="eventtype">
            <a-select v-model="searchForm.eventtype" @change="selectVal(searchForm.eventtype, 'eventtype')">
              <a-select-option v-for="item in zidianInfo.eventtype" :value="item.keyy" :key="item.keyy">
                {{item.valuee}}
              </a-select-option>
            </a-select>
          </a-form-model-item>
        </a-col>
        <a-col :span="6">
          <a-form-model-item ref="keyword" label="员工姓名" prop="keyword">
            <Select :disabledName="disabledName" :keyword="searchForm.keyword" url="system/employee?search=" @getSearchValue="getSearchValue"></Select>
          </a-form-model-item>
        </a-col>
        <a-col :span="6">
          <a-form-model-item label="开始时间" prop="startdate" clear>
            <a-date-picker
              v-model="searchForm.startdate"
              :format="dateFormat"
              :valueFormat="dateFormat"
              @change="startdateChange"
              allowClear
            />
          </a-form-model-item>
        </a-col>
        <a-col :span="6">
          <a-form-model-item>
            <a-button type="primary" @click="handleSubmit" :loading="loading">
              执行
            </a-button>
            <a-button @click="reset" style="margin-left: 15px;" :loading="loading">
              重置
            </a-button>
          </a-form-model-item>
        </a-col>
      </a-row>
    </a-form-model>
    <div class="changeArea" v-if="show">
      <a-row style="margin-bottom: 20px">
        <a-col :span="3">
          <span class="title">姓名：</span>
          <span>{{coreCol.employeename}}</span>
        </a-col>
        <a-col :span="3">
          <span class="title">人员编号：</span>
          <span>{{coreCol.code}}</span>
        </a-col>
        <a-col :span="3">
          <span class="title">科室：</span>
          <span>{{coreCol.deptname}}</span>
        </a-col>
        <a-col :span="3">
          <span class="title">病区：</span>
          <span>{{coreCol.wardname}}</span>
        </a-col>
        <a-col :span="2">
          <span class="title">岗位：</span>
          <span>{{coreCol.jobname}}</span>
        </a-col>
        <a-col :span="2">
          <span class="title">人员类型：</span>
          <span>{{coreCol.employeetypename}}</span>
        </a-col>
        <a-col :span="3">
          <span class="title">人事范围：</span>
          <span>{{coreCol.rsfwname}}</span>
        </a-col>
          <a-col :span="2">
          <span class="title">人员状态：</span>
          <span>{{coreCol.statusname}}</span>
        </a-col>
      </a-row>
      <a-table
        :columns="columns"
        :loading="loading"
        :data-source="tableData"
        :keyRow="record => record.employeeid"
        :pagination="false"
        bordered
        :scroll="{ x: 1200 }"
      >
        <template slot="operation" slot-scope="text, record">
          <a-button class="editable-add-btn" @click="editRow(record)">
            编辑
          </a-button>
        </template>
      </a-table>
      <a-modal class="ant-modal-nofooter" v-model="coreVisible" title="修改" :width="650" @ok="handleOk">
        <a-form-model
          ref="formData"
          :model="formData"
          :rules="formDataRules"
          v-bind="layout"
        >
          <a-row>
            <a-col :span="12">
              <a-form-model-item label="修改人" prop="xtupdateby ">
                <a-input v-model="formData.xtupdateby" :disabled="true" allowClear></a-input>
              </a-form-model-item>
              <a-form-model-item label="开始时间" prop="startdate">
                <a-date-picker
                  v-model="formData.startdate"
                  :format="dateFormat"
                  :valueFormat="dateFormat"
                  @change="startdateChange"
                  :disabled="true"
                />
              </a-form-model-item>
              <a-form-model-item label="科室类型" prop="depttype">
                <a-select
                  v-model="formData.depttype"
                  show-search
                  placeholder="请输入关键字"
                  :filter-option="filterOption"
                  @change="getdepttype"
                  allowClear
                  :disabled="true"
                >
                  <a-select-option v-for="item in zidianInfo.depttype" :value="item.keyy" :key="item.keyy">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
              <a-form-model-item label="病区" prop="ward">
                <a-select
                  v-model="formData.ward"
                  show-search
                  placeholder="请输入关键字"
                  :filter-option="filterOption"
                  @change="getwardname(formData.ward)"
                  allowClear
                >
                  <a-select-option v-for="item in wardOptions" :value="item.keyy" :key="item.keyy" @click="selectWard(item)">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
              <a-form-model-item label="岗位" prop="job">
                <a-select
                  v-model="formData.job"
                  show-search
                  placeholder="请输入关键字"
                  :filter-option="filterOption"
                  @change="handleChange"
                  allowClear
                >
                  <a-select-option v-for="item in zidianInfo.job" :value="item.keyy" :key="item.keyy" @click="selectJob(item)">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
              <a-form-model-item label="人事范围" prop="rsfw">
                <a-select v-model="formData.rsfw" @change="getrsfwname" allowClear>
                  <a-select-option v-for="item in zidianInfo.rsfw" :value="item.keyy" :key="item.keyy" @click="selectRsfw(item)">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
              <a-form-model-item label="人事事件" prop="eventtype">
                <a-select v-model="formData.eventtype" allowClear>
                  <a-select-option v-for="item in zidianInfo.eventtype" :value="item.keyy" :key="item.keyy" @click="getname(item, 'eventtype')">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
              <a-form-model-item label="备注" prop="status">
                <a-textarea :rows="2" v-model="formData.memo" allowClear></a-textarea>
              </a-form-model-item>
            </a-col>
            <a-col :span="12">
              <a-form-model-item label="修改时间" prop="xtupdatedate">
                <a-date-picker
                  v-model="formData.xtupdatedate"
                  :format="dateFormat"
                  :valueFormat="dateFormat"
                  :disabled="disEdit"
                />
              </a-form-model-item>
              <a-form-model-item label="结束时间" prop="enddate">
                <a-date-picker
                  v-model="formData.enddate"
                  :format="dateFormat"
                  :valueFormat="dateFormat"
                  @change="enddateChange"
                  :disabled="true"
                />
              </a-form-model-item>
              <a-form-model-item label="科室" prop="dept">
                <a-select
                  v-model="formData.dept"
                  show-search
                  placeholder="请输入关键字"
                  :filter-option="filterOption"
                  @change="getdeptname"
                  allowClear
                >
                  <a-select-option v-for="item in zidianInfo.dept" :value="item.keyy" :key="item.keyy" @click="selectDept(item)">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
              <a-form-model-item label="职位" prop="position">
                <a-select
                  v-model="formData.position"
                  show-search
                  placeholder="请输入关键字"
                  :filter-option="filterOption"
                  @change="handleChange"
                  allowClear
                >
                  <a-select-option v-for="item in zidianInfo.position" :value="item.keyy" :key="item.keyy" @click="selectPosition(item)">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
              <a-form-model-item label="人员类型" prop="employeetype">
                <a-select v-model="formData.employeetype" allowClear>
                  <a-select-option v-for="item in zidianInfo.employeetype" :value="item.keyy" :key="item.keyy"  @click="selectEmployeetype(item)">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
              <a-form-model-item label="人事子范围" prop="rszfw">
                <a-select v-model="formData.rszfw" @change="getrszfwname" allowClear>
                  <a-select-option v-for="item in rszfwOptions" :value="item.keyy" :key="item.keyy" @click="selectRszfw(item)">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
              <a-form-model-item label="人员状态" prop="status">
                <a-select v-model="formData.status" allowClear>
                  <a-select-option v-for="item in zidianInfo.status" :value="item.keyy" :key="item.keyy" @click="selectStatus(item)">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
          </a-row>
          <a-form-model-item :wrapper-col="{ span: 14, offset: 4 }">
            <a-button class="editable-add-btn" @click="resetForm">
              重置
            </a-button>
            <a-button type="primary" style="margin-left: 20px;" @click="submit">
              确认
            </a-button>
          </a-form-model-item>
        </a-form-model>
      </a-modal>
    </div>
    <Table12 v-if="show && show1"></Table12>
  </div>
</template>
<script>
import Select from '../common/Select'
import Table12 from './common/Table12'
import { mapActions, mapGetters } from 'vuex'
import moment from 'moment'

export default {
  name: 'EmployeeEvt',
  components: { Table12, Select },
  data () {
    return {
      value: '',
      loading: false,
      show: false,
      show1: false,
      disabledName: false,
      disEdit: true,
      labelCol: { span: 7 },
      wrapperCol: { span: 14 },
      dateFormat: 'YYYY-MM-DD',
      searchForm: {
        keyword: null,
        // startdate: new Date().toLocaleDateString(),
        startdate: null,
        employeeid: null,
        eventtype: '',
        eventtypename: ''
      },
      tableLoading: false,
      tableData: [
        {startdate: null, enddate: null}
      ],
      coreVisible: false,
      layout: {
        labelCol: { span: 6 },
        wrapperCol: { span: 12 }
      },
      selectData: {},
      coreCol: {
        code: '',
        employeename: '',
        deptname: '',
        wardname: '',
        jobname: '',
        employeetypename: '',
        rszfwname: ''
      },
      startdateValue: 0,
      enddateValue: 0,
      lastdateValue: 0,
      lastdate: '',
      formData: {
        employeeid: null,
        startdate: null,
        enddate: '9999-12-31',
        depttype: '',
        dept: '',
        ward: '',
        job: '',
        position: '',
        employeetype: '',
        rsfw: '',
        rszfw: '',
        eventtype: '',
        status: '',
        memo: '',
        deptname: '',
        wardname: '',
        jobname: '',
        employeetypename: '',
        rsfwname: '',
        rszfwname: '',
        positionname: '',
        xtupdateby: '',
        xtupdatedate: moment(new Date()).format('YYYY-MM-DD')
      },
      ruleSearchForm: {
        keyword: [
          { required: true, message: '请输入员工姓名', trigger: 'change' }
        ],
        startdate: [
          { required: true, message: '请输入开始时间', trigger: 'change' }
        ],
        eventtype: [
          { required: true, message: '请输入事件类型', trigger: 'change' }
        ]
      },
      formDataRules: {
        startdate: [
          { required: true, message: '请输入开始时间', trigger: 'change' }
        ],
        enddate: [
          { required: true, message: '请输入结束时间', trigger: 'change' }
        ]
        // dept: [
        //   { required: true, message: '请输入科室', trigger: 'change' }
        // ]
        // ward: [
        //   { required: true, message: '请输入病区', trigger: 'change' }
        // ],
        // job: [
        //   { required: true, message: '请输入岗位', trigger: 'change' }
        // ],
        // employeetype: [
        //   { required: true, message: '请输入人员类型', trigger: 'change' }
        // ],
        // rsfwn: [
        //   { required: true, message: '请输入人事范围', trigger: 'change' }
        // ],
        // rszfw: [
        //   { required: true, message: '请输入人事子范围', trigger: 'change' }
        // ],
        // eventtype: [
        //   { required: true, message: '请输入事件类型', trigger: 'change' }
        // ],
        // status: [
        //   { required: true, message: '请输入人员状态', trigger: 'change' }
        // ]
      }
    }
  },
  watch: {
    'searchForm.startdate': {
      handler: function (data) {
        if (data) {
          this.startdateValue = moment(data).valueOf()
          // 当开始时间小于或等于上一次的开始时间 就会在上一次的开始时间上往后推一天
          if (this.searchForm.eventtype!==1&&this.startdateValue <= this.lastdateValue) {
            this.$message.warning('开始时间不能小于或等于上一次开始时间，请重新设置开始时间！')
            this.searchForm.startdate = moment(this.lastdate, 'YYYY-MM-DD').add(1, 'days')
            this.show = false
          }
          if (this.startdateValue && this.enddateValue && this.startdateValue >= this.enddateValue) {
            this.$message.warning('开始时间不能大于或等于结束时间')
          }
        }
      },
      immediate: true
    },
    'formData.enddate': {
      handler: function (data) {
        if (data) {
          this.enddateValue = moment(data).valueOf()
          if (this.startdateValue && this.enddateValue && this.startdateValue >= this.enddateValue) {
            this.$message.warning('结束时间不能小于或等于开始时间')
            this.formData.enddate = '9999-12-31'
          }
        }
      },
      immediate: true
    }
  },
  created () {
  },
  mounted () {
    this.tableData = []
    this.$store.dispatch('getzidian', {vm: this})
    this.getStartdate()
  },
  computed: {
    ...mapGetters(
      ['queryData', 'zidianInfo', 'deptOptions', 'wardOptions', 'rszfwOptions']
    ),
    columns () {
      let { depttype, dept, ward, job, position, employeetype, rsfw, rszfw, eventtype, status } = this.zidianInfo
      return [
        { title: '开始时间', dataIndex: 'startdate', width: 150, scopedSlots: { customRender: 'startdate' } },
        { title: '结束时间', dataIndex: 'enddate', width: 150, scopedSlots: { customRender: 'enddate' } },
        { title: '科室类型',
          dataIndex: 'depttype',
          width: 150,
          customRender: (text, row, index) => {
            let option = depttype.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '科室',
          dataIndex: 'dept',
          width: 150,
          customRender: (text, row, index) => {
            let option = dept.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '病区',
          dataIndex: 'ward',
          width: 150,
          customRender: (text, row, index) => {
            let option = ward.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '岗位',
          dataIndex: 'job',
          width: 150,
          customRender: (text, row, index) => {
            let option = job.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '职位',
          dataIndex: 'position',
          width: 150,
          customRender: (text, row, index) => {
            let option = position.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '人员类型',
          dataIndex: 'employeetype',
          width: 150,
          customRender: (text, row, index) => {
            let option = employeetype.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '人事范围',
          dataIndex: 'rsfw',
          width: 150,
          customRender: (text, row, index) => {
            let option = rsfw.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '人事子范围',
          dataIndex: 'rszfw',
          width: 150,
          customRender: (text, row, index) => {
            let option = rszfw.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '人事事件',
          dataIndex: 'eventtype',
          width: 150,
          customRender: (text, row, index) => {
            let option = eventtype.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '人员状态',
          dataIndex: 'status',
          width: 150,
          customRender: (text, row, index) => {
            let option = status.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {title: '操作',
          dataIndex: 'operation',
          width: 100,
          fixed: 'right',
          scopedSlots: {customRender: 'operation'}
        }
      ]
    }
  },
  methods: {
    ...mapActions(['getSearch', 'getQuery', 'getzidian', 'getward', 'getrszfw']),
    startdateChange (date) {
      this.formData.startdate = this.searchForm.startdate = date
      this.getStartdate()
    },
    enddateChange (date) {
      this.formData.enddate = date
    },
    // 匹配模糊查询数据
    getSearchValue ({list, value}) {
      this.selectData = list && list.length > 0 ? list[0] : {}
      this.searchForm.keyword = this.searchForm.employeeid = value
    },
    // 选择事件状态
    selectVal (value, name) {
      this.searchForm.eventtype = value
      this.formData.eventtype = value
      // this.getname({keyy:value}, name)
      if (value === '1') {
        this.show = false
        this.disabledName = true
        this.searchForm.keyword = null
        this.searchForm.employeeid = null
        this.ruleSearchForm.keyword[0].required = false
      } else {
        this.disabledName = false
        this.ruleSearchForm.keyword[0].required = true
      }
    },
    // 提交关键词之前查询上一条核心数据的时间 用来跟最新的时间做对比
    getStartdate () {
      if (!this.searchForm.eventtype || !this.searchForm.employeeid) return
      this.$get('system/employee/core/getLatestCore/' + this.searchForm.employeeid).then(res => {
        let newData = res.data
        if (newData) {
          this.lastdate = newData.startdate
          this.lastdateValue = moment(this.lastdate).valueOf()
          // 当开始时间小于或等于上一次的开始时间 就会在上一次的开始时间上加1天
          if (this.startdateValue <= this.lastdateValue) {
            this.$message.warning('开始时间不能小于或等于上一次开始时间，请重新设置开始时间！')
            this.searchForm.startdate = moment(this.lastdate, 'YYYY-MM-DD').add(1, 'days')
            this.show = false
          }
        }
      })
    },
    // 提交搜索关键字
    handleSubmit () {
      this.loading = true
      this.$refs.searchForm.validate(valid => {
        if (valid) {
          // 人员状态为'入职'状态 就新增 新入职没有id
          if (this.searchForm.eventtype === '1') {
            this.loading = false
            this.show = false
            this.$store.dispatch('getQuery', {employeeid: null, pageStatus: 'add', startdate: this.searchForm.startdate, eventtype: this.searchForm.eventtype, status: 1, statusname: '在职'})
            this.$store.dispatch('getSearch', {vm: this, id: 0})
            this.$router.push({path: '/employee'})
            this.reset()
          } else {
            this.show = true
            this.loading = false
            this.getTable()
            if (this.searchForm.eventtype === '9' || this.searchForm.eventtype === '10') {
              this.show1 = true
              this.$store.dispatch('getSearch', {vm: this, id: this.searchForm.employeeid})
            } else {
              this.show1 = false
            }
          }
        } else {
          this.loading = false
        }
      })
    },
    // 获取table 数据
    getTable () {
      let that = this
      this.tableLoading = true
      that.$get('system/employee/core/getLatestCore/' + that.searchForm.employeeid).then(res => {
        that.tableLoading = false
        let newData = res.data
        if (newData) {
          newData.eventtype = that.formData.eventtype
          newData.status = that.formData.status
          newData.startdate = moment(that.searchForm.startdate).format('YYYY-MM-DD')
          // 当人员状态为 '离职'，'退休'，'死亡' 清空 科室 病区 岗位
          let { dept, ward, job, employeetype, rsfw, eventtype, status } = that.zidianInfo
          that.coreCol = {...that.coreCol, ...newData}
          that.coreCol.deptname = dept.filter(item => item.keyy == that.coreCol.dept)[0] ? dept.filter(item => item.keyy == that.coreCol.dept)[0].valuee : ''
          that.coreCol.wardname = ward.filter(item => item.keyy == that.coreCol.ward)[0] ? ward.filter(item => item.keyy == that.coreCol.ward)[0].valuee : ''
          that.coreCol.jobname = job.filter(item => item.keyy == that.coreCol.job)[0] ? job.filter(item => item.keyy == that.coreCol.job)[0].valuee : ''
          that.coreCol.employeetypename = employeetype.filter(item => item.keyy == that.coreCol.employeetype)[0] ? employeetype.filter(item => item.keyy == that.coreCol.employeetype)[0].valuee : ''
          that.coreCol.statusname = status.filter(item => item.keyy == that.coreCol.status)[0] ? status.filter(item => item.keyy == that.coreCol.status)[0].valuee : ''
          that.coreCol.rsfwname = rsfw.filter(item => item.keyy == that.coreCol.rsfw)[0] ? rsfw.filter(item => item.keyy == that.coreCol.rsfw)[0].valuee : ''
          that.coreCol.eventtype = eventtype.filter(item => item.keyy == that.coreCol.eventtype)[0] ? eventtype.filter(item => item.keyy == that.coreCol.eventtype)[0].valuee : ''
          that.coreCol.employeename = that.selectData.employeename ? that.selectData.employeename : ''
          that.coreCol.code = that.selectData.code ? that.selectData.code : ''
          if (that.formData.eventtype === '4') { // 退休
            newData.ward = null
            newData.dept = null
            newData.depttype = null
            newData.job = null
            newData.position = null
          } else if (that.formData.eventtype === '6' || that.formData.eventtype === '7' || that.formData.eventtype === '8') { // 离职外调死亡
            newData.job = null
            newData.position = null
          }
          that.tableData = [{...newData}]
        }
      }).catch(err => {
        that.$message.warning(err)
        that.tableLoading = false
      })
    },
    editRow (record) {
      this.coreVisible = true
      this.formData = {...this.formData, ...record}
      console.log(this.formData, 99999999)
      this.formData.xtupdateby = this.$store.state.account.user.username
      this.formData.startdate = this.searchForm.startdate
      this.formData.enddate = record.enddate ? record.enddate : '9999-12-31'
      this.formData.employeetype = this.formData.employeetype ? this.formData.employeetype.toString() : ''
      this.formData.eventtype = this.formData.eventtype ? this.formData.eventtype.toString() : ''
      this.formData.status = this.formData.status ? this.formData.status.toString() : ''
      if (this.formData.depttype) {
        this.$store.dispatch('getdept', {vm: this, id: this.formData.depttype})
      }
      if (this.formData.dept) {
        this.$store.dispatch('getward', {vm: this, id: this.formData.dept})
      }
      if (this.formData.rsfw) {
        this.$store.dispatch('getrszfw', {vm: this, id: this.formData.rsfw})
      }
      this.getname({keyy:this.formData.eventtype}, "eventtype")
    },
    handleOk () {
      this.coreVisible = false
    },
    getname (item, name) {
      console.log(item, 77777777)
      this.formData.eventtypename = item.keyy
      if (item && name === 'eventtype') {
        if (item.keyy === '1' || item.keyy === '2' || item.keyy === '3' || item.keyy === '9' || item.keyy === '10' || item.keyy === '14') {
          this.formData.status = '1'
          this.formData.statusname = '在职'
          this.formData.eventtypename = '入职'
        } else if (item.keyy === '4') {
          this.formData.status = '2'
          this.formData.statusname = '退休'
        } else if (item.keyy === '5') {
          this.formData.status = '3'
          this.formData.statusname = '返聘'
        } else if (item.keyy === '6') {
          this.formData.status = '4'
          this.formData.statusname = '外调'
        } else if (item.keyy === '7') {
          this.formData.status = '5'
          this.formData.statusname = '离职'
        } else if (item.keyy === '8') {
          this.formData.status = '6'
          this.formData.statusname = '死亡'
        }
      }
    },
    getdepttype (value) {
    //   if (!value) {
    //     this.formData.dept = null
    //     this.formData.ward = null
    //     this.$message.warning('请选择科室类型！')
    //     return
    //   }
    //   this.$store.dispatch('getdept', {vm: this, id: value})
    //   this.formData.dept = null
    //   this.formData.ward = null
    },
    getdeptname (value) {
      if (!value) {
        this.formData.ward = null
        this.$message.warning('请选择科室！')
        return
      }
      // if (!this.formData.depttype) {
      //   this.formData.dept = null
      //   this.formData.ward = null
      //   this.$message.warning('请选择科室类型！')
      //   return
      // }
      this.$store.dispatch('getward', {vm: this, id: value})
      this.formData.ward = null
    },
    getwardname (value) {
      if (!value) {
        this.formData.ward = null
        this.$message.warning('请选择病区！')
        return
      }
      if (!this.formData.dept) {
        this.formData.dept = null
        this.formData.ward = null
        this.$message.warning('请选择科室！')
      }
    },
    getrsfwname (value) {
      if (!value) {
        this.formData.rszfw = null
        this.$message.warning('请选择人事范围！')
        return
      }
      this.$store.dispatch('getrszfw', {vm: this, id: value})
      this.formData.rszfw = null
    },
    getrszfwname (value) {
      if (!value) {
        this.$message.warning('请选择人事子范围！')
      }
      if (!this.formData.rsfw) {
        this.formData.rszfw = null
        this.$message.warning('请选择人事范围！')
      }
    },
    selectDept (item) {
      this.formData.deptname = item.valuee
      this.formData.depttype = item.parentid
    },
    selectWard (item) {
      this.formData.wardname = item.valuee
    },
    selectJob (item) {
      this.formData.jobname = item.valuee
    },
    selectRsfw (item) {
      this.formData.rsfwname = item.valuee
    },
    selectRszfw (item) {
      this.formData.rszfwname = item.valuee
    },
    selectStatus (item) {
      this.formData.statusname = item.valuee
    },
    selectPosition (item) {
      this.formData.positionname = item.valuee
    },
    selectEmployeetype (item) {
      this.formData.employeetypename = item.valuee
    },
    handleChange (value) {
      console.log(`selected ${value}`)
    },
    filterOption (input, option) {
      return (
        option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      )
    },
    // 保存
    submit () {
      this.$refs.formData.validate(valid => {
        if (valid) {
          let params = {}
          params.employeeid = this.searchForm.employeeid
          params.startdate = moment(this.formData.startdate).format('YYYY-MM-DD')
          params.enddate = this.formData.enddate
          params.depttype = this.formData.depttype
          params.dept = this.formData.dept
          params.ward = this.formData.ward
          params.position = this.formData.position
          params.job = this.formData.job
          params.employeetype = this.formData.employeetype
          params.rsfw = this.formData.rsfw
          params.rszfw = this.formData.rszfw
          params.eventtype = this.formData.eventtype
          params.status = this.formData.status
          params.memo = this.formData.memo
          params.deptname = this.formData.deptname
          params.wardname = this.formData.wardname
          params.rsfwname = this.formData.rsfwname
          params.rszfwname = this.formData.rszfwname
          params.employeetypename = this.formData.employeetypename
          params.positionname = this.formData.positionname
          params.statusname = this.formData.statusname
          params.jobname = this.formData.jobname
          params.eventtypename = this.formData.eventtypename
          params.xtupdateby = this.formData.xtupdateby
          params.xtupdatedate = this.formData.xtupdatedate
          this.$post('system/employee/core', {...params}).then(res => {
            if (res.data.status === 'OK') {
              this.getTable()
              this.resetForm()
              this.reset()
              this.coreVisible = false
              this.$message.success('修改成功！')
            }
          }).catch(err => {
            this.$message.warning(err)
          })
        } else {
          return false
        }
      })
    },
    resetForm () {
      this.formData.wardname = null
      this.formData.depttype = null
      this.formData.dept = null
      this.formData.ward = null
      this.formData.job = null
      this.formData.employeetype = null
      this.formData.position = null
      this.formData.rsfw = null
      this.formData.rszfw = null
      this.formData.memo = null
      this.formData.xtupdateby = null
    },
    reset () {
      // this.searchForm.startdate = this.formData.startdate = new Date().toLocaleDateString()
      this.searchForm.startdate = null
      this.lastdateValue = 0
      this.startdateValue = 0
      this.enddateValue = 0
      this.searchForm.employeeid = null
      this.searchForm.keyword = null
      this.searchForm.eventtype = null
      this.show = false
      this.disabledName = false
    }
  }
}
</script>
<style scoped lang="less">
  .employeeEvt{
    width: 100%;
    .ant-form-item{
      width: 100%;
    }
    .changeArea{
      .title{
        color: #000;
      }
    }
  }
  .editable-row-operations a {
    margin-right: 8px;
  }

</style>
<style lang="less">
  .ant-modal-nofooter{
    .ant-modal-footer{
      display: none;
    }
  }
  .table12 {
    .actionBtn{
      text-align: right;
      margin-bottom: 15px;
    }
  }
</style>
