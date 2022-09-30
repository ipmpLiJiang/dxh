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
          </a-row>
          <a-row v-if="advanced">
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="人员状态"
                prop="states"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-select v-model="queryParams.states" mode="multiple" allowClear :filter-option="filterOption">
                  <a-select-option v-for="item in zidianInfo.status" :value="item.keyy" :key="item.keyy">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="人事范围"
                prop="rsfws"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-select v-model="queryParams.rsfws" mode="multiple" allowClear :filter-option="filterOption">
                  <a-select-option v-for="item in zidianInfo.rsfw" :value="item.keyy" :key="item.keyy">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="人事子范围"
                prop="rszfws"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-select v-model="queryParams.rszfws" mode="multiple" allowClear :filter-option="filterOption">
                  <a-select-option v-for="item in zidianInfo.rszfw" :value="item.keyy" :key="item.keyy">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row v-if="advanced">
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="人员类型"
                prop="employeetypes"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-select v-model="queryParams.employeetypes" mode="multiple" allowClear :filter-option="filterOption">
                  <a-select-option v-for="item in zidianInfo.employeetype" :value="item.keyy" :key="item.keyy">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="聘用职称"
                prop="title"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-select v-model="queryParams.title" allowClear :filter-option="filterOption">
                  <a-select-option v-for="item in zidianInfo.title" :value="item.keyy" :key="item.keyy">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="聘用等级"
                prop="joblevel"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-select v-model="queryParams.joblevel" allowClear :filter-option="filterOption">
                  <a-select-option v-for="item in zidianInfo.joblevel" :value="item.keyy" :key="item.keyy">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row v-if="advanced">
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="取得职称"
                prop="titleget"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-select v-model="queryParams.titleget" allowClear :filter-option="filterOption">
                  <a-select-option v-for="item in zidianInfo.title" :value="item.keyy" :key="item.keyy">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="学历"
                prop="academic"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-select v-model="queryParams.academic" allowClear :filter-option="filterOption">
                  <a-select-option v-for="item in zidianInfo.academic" :value="item.keyy" :key="item.keyy">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
             <a-col :md="8" :sm="24" >
              <a-form-item
                label="学位"
                prop="degree"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-select v-model="queryParams.degree" allowClear :filter-option="filterOption">
                  <a-select-option v-for="item in zidianInfo.degrees" :value="item.keyy" :key="item.keyy">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>
          <a-row v-if="advanced">
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="职前学历"
                prop="zqacademic"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-select v-model="queryParams.zqacademic" allowClear :filter-option="filterOption">
                  <a-select-option v-for="item in zidianInfo.academic" :value="item.keyy" :key="item.keyy">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="职前学位"
                prop="zqdegree"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-select v-model="queryParams.zqdegree" allowClear :filter-option="filterOption">
                  <a-select-option v-for="item in zidianInfo.degrees" :value="item.keyy" :key="item.keyy">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="出生开始日期"
                prop="birthstartdate"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                 <a-month-picker
                  v-model="queryParams.birthstartdate"
                  :format="monthFormat"
                  :valueFormat="monthFormat"
                />
              </a-form-item>
            </a-col>
          </a-row>
          <a-row v-if="advanced">
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="出生结束日期"
                prop="birthenddate"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-month-picker
                  v-model="queryParams.birthenddate"
                  :format="monthFormat"
                  :valueFormat="monthFormat"
                />
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
            <a-icon type="upload"  /> 导入员工档案
          </a-button>
        </a-upload>
      </a-col>
      <a-col class="operator" :span=4>
        <a-upload
          accept=".xlsx"
          :fileList="fileList"
          :disabled="loadinginport"
          :beforeUpload="beforeUploadEce"
        >
          <a-button :loading="loadinginport">
            <a-icon type="upload"  /> 导入员工信息
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
        :scroll="{ x: 2500, y: 400 }">
        <template slot="operation" slot-scope="text, record">
          <a-dropdown>
            <a class="ant-dropdown-link">
              <a-icon type="down-circle" style="font-size: 1.1rem"/>
            </a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a href="javascript:void(0)" @click="editRow(record)">员工履历编辑</a>
              </a-menu-item>
              <a-menu-item>
                <a href="javascript:void(0)" v-hasPermission="['employeeMgt:checkArchives']" @click="checkArchives(record)">员工档案</a>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </template>
      </a-table>
    </div>
  </a-card>
</template>
<script>
import Select from '../common/Select'
import { mapGetters, mapActions } from 'vuex'
import { politicaloutlookOptions } from '../../utils/filter'
import moment from 'moment'

export default {
  name: 'EmployeeMgt',
  components: { Select },
  data () {
    return {
      dateFormat: 'YYYY-MM-DD',
      monthFormat: 'YYYY-MM',
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
        startdate: moment(new Date().toLocaleDateString()).format('YYYY-MM-DD'),
        keyword: null,
        employeeid: null,
        deptids: [],
        states: [],
        rsfws: [],
        rszfws: [],
        employeetypes: [],
        title: '',
        joblevel: '',
        titleget: '',
        academic: '',
        degree: '',
        zqacademic: '',
        zqdegree: '',
        birthstartdate: '',
        birthenddate: ''
      },
      gparName:'catalog',
      fileList: [],
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
      let { status, employeetype, dept, ward, rsfw, rszfw, title, duty, joblevel, studymethod, academic, degrees } = this.zidianInfo
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
        {
          title: '出生年月',
          dataIndex: 'birth',
          width: 120
        },
        {
          title: '身份证号',
          dataIndex: 'idnumber',
          width: 120
        },
        {
          title: '籍贯',
          dataIndex: 'homeaddress',
          width: 120
        },
         {
          title: '民族',
          dataIndex: 'nation',
          width: 120
        },
        {
          title: '状态',
          dataIndex: 'statusname',
          customRender: (text, row, index) => {
            if (!text) return ''
            let option = status && status.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          },
        width: 100
        },
        {
          title: '人员类型',
          dataIndex: 'employeetype',
          customRender: (text, row, index) => {
          if (!text) return ''
          let option = employeetype.filter(item => item.keyy == text)[0]
          return option ? option.valuee : ''
          },
          width: 120
        },
        { title: '科室',
          dataIndex: 'deptname',
          width: 150,
          customRender: (text, row, index) => {
            let option = dept.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '病区',
          dataIndex: 'wardname',
          width: 150,
          customRender: (text, row, index) => {
            let option = ward.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
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
          title: '政治面貌',
          dataIndex: 'politicaloutlookname',
          customRender: (text, row, index) => {
            if (!text) return ''
            let option = politicaloutlookOptions.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          },
          width: 100
        },
        {
          title: '最高学历',
          dataIndex: 'academicname',
          customRender: (text, row, index) => {
            if (!text) return ''
            let option = academic.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          },
          width: 100
        },
        {
          title: '学校',
          dataIndex: 'academicschool',
          width: 100
        },
        {
          title: '专业',
          dataIndex: 'academicprofession',
          width: 100
        },
        {
          title: '学习形式',
          dataIndex: 'academicstudymethod',
          width: 120,
          customRender: (text, row, index) => {
            if (!text) return ''
            let option = studymethod && studymethod.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          },
        },
        {
          title: '毕业时间',
          dataIndex: 'academicenddate',
          width: 120
        },
        {
          title: '最高学位',
          dataIndex: 'degreesname',
          customRender: (text, row, index) => {
            if (!text) return ''
            let option = degrees.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          },
          width: 100
        },
        {
          title: '学校',
          dataIndex: 'degreesschool',
          width: 100
        },
        {
          title: '专业',
          dataIndex: 'degreesprofession',
          width: 100
        },
        {
          title: '学习形式',
          dataIndex: 'degreesstudymethod',
          width: 120,
          customRender: (text, row, index) => {
            if (!text) return ''
            let option = studymethod && studymethod.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          },
        },
        {
          title: '毕业时间',
          dataIndex: 'degreesenddate',
          width: 130
        },
        {
          title: '最高学历(职前)',
          dataIndex: 'zqacademicname',
          customRender: (text, row, index) => {
            if (!text) return ''
            let option = academic.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          },
          width: 140
          },
        {
          title: '学校(职前)',
          dataIndex: 'zqacademicschool',
          width: 100
        },
        {
          title: '专业(职前)',
          dataIndex: 'zqacademicprofession',
          width: 100
        },
        {
          title: '学习形式(职前)',
          dataIndex: 'zqacademicstudymethod',
          width: 140,
          customRender: (text, row, index) => {
            if (!text) return ''
            let option = studymethod && studymethod.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          },
        },
        {
          title: '毕业时间(职前)',
          dataIndex: 'zqacademicenddate',
          width: 140
        },
        {
          title: '最高学位(职前)',
          dataIndex: 'zqdegreesname',
          customRender: (text, row, index) => {
              if (!text) return ''
              let option = degrees.filter(item => item.keyy == text)[0]
              return option ? option.valuee : ''
          },
          width: 140
        },
        {
          title: '学校(职前)',
          dataIndex: 'zqdegreesschool',
          width: 100
        },
        {
          title: '专业(职前)',
          dataIndex: 'zqdegreesprofession',
          width: 100
        },
        {
          title: '学习形式(职前)',
          dataIndex: 'zqdegreesstudymethod',
          width: 140,
          customRender: (text, row, index) => {
            if (!text) return ''
            let option = studymethod && studymethod.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          },
        },
        {
          title: '毕业时间(职前)',
          dataIndex: 'zqdegreesenddate',
          width: 140
        },
        {
          title: '来院时间',
          dataIndex: 'comedate',
          width: 130
        },
        {
          title: '参加工作时间',
          dataIndex: 'workdate',
          width: 140
        },
        {
          title: '首次签约时间',
          dataIndex: 'contractstartdate',
          width: 130
        },
        {
          title: '合同到期时间',
          dataIndex: 'contractenddate',
          width: 130
        },
        {
          title: '聘用职称',
          dataIndex: 'titlename',
          customRender: (text, row, index) => {
            if (!text) return ''
            let option = title.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          },
          width: 120
        },
        {
          title: '聘用专业技术职务',
          dataIndex: 'dutyname',
          customRender: (text, row, index) => {
            if (!text) return ''
            let option = duty.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          },
          width: 150
        },
        {
          title: '聘用专业岗位等级',
          dataIndex: 'technicallevelname',
          customRender: (text, row, index) => {
            if (!text) return ''
            let option = joblevel.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          },
          width: 150
        },
        {
          title: '聘用管理岗位等级',
          dataIndex: 'mangelevelname',
          customRender: (text, row, index) => {
            if (!text) return ''
            let option = joblevel.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          },
          width: 150
        },
        {
          title: '聘用工勤岗位等级',
          dataIndex: 'workerlevelname',
          customRender: (text, row, index) => {
            if (!text) return ''
            let option = joblevel.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          },
          width: 150
        },
        {
          title: '取得职称',
          dataIndex: 'qdtitlename',
          customRender: (text, row, index) => {
              if (!text) return ''
              let option = title.filter(item => item.keyy == text)[0]
              return option ? option.valuee : ''
          },
          width: 130
        },
        {
          title: '取得专业技术职务',
          dataIndex: 'qddutyname',
          customRender: (text, row, index) => {
              if (!text) return ''
              let option = duty.filter(item => item.keyy == text)[0]
              return option ? option.valuee : ''
          },
          width: 150
        },
        {
          title: '取得时间',
          dataIndex: 'qddate',
          width: 130
        },
        {
          title: '操作',
          dataIndex: 'operation',
          scopedSlots: {customRender: 'operation'},
          fixed: 'right',
          width: 80
        }
      ]
    }
  },
  created () {
    this.$store.dispatch('getzidian', {vm: this})
  },
  mounted () {
    this.queryParams.startdate = moment(this.queryParams.startdate).format('YYYY-MM-DD')
    this.fetch({...this.queryParams})
  },
  methods: {
    ...mapActions('getQuery'),
    // 查询时间
    startdateChange (date) {
      this.queryParams.startdate = date
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
    editRow (record) {
      this.$store.dispatch('getQuery', { employeeid: record.employeeid, pageStatus: 'edit' })
      this.$store.dispatch('getSearch', { vm: this, id: record.employeeid })
      this.$router.push({path: '/employee', query: {id: record.employeeid}})
    },
    checkArchives (record) {
      console.log(record, 99922222)
      this.rowRecord = record
      this.$router.push({path: "/employeeFiles", query: {id: record.employeeid}, params: {name: record.employeename}})
    },

    exportExcel () {
      this.$export('system/employee/export', {
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
      this.$upload('system/catalog/import', formData).then(res => {
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
    beforeUploadEce (file) {
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
      return isType && isLt3M && this.customRequestEce(file)
    },
    customRequestEce (file) {
      this.loadinginport = true
      const formData = new FormData()
      formData.append('file', file)
      this.$upload('system/employee/importEmployeeCoreEducation', formData).then(res => {
        let data = res.data.data.success
        let error = res.data.data.message
        if (data === 1) {
          this.$message.success('导入成功！')
        } else if (error && error.length > 0) {
          this.$message.warning(error)
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
      if (params.joblevel == undefined) {
        params.joblevel = null
      }
      if (params.title == undefined) {
        params.title = null
      }
      if (params.titleget == undefined) {
        params.titleget = null
      }
      if (params.academic == undefined) {
        params.academic = null
      }
      if (params.degree == undefined) {
        params.degree = null
      }
      if (params.zqacademic == undefined) {
        params.zqacademic = null
      }
      if (params.zqdegree == undefined) {
        params.zqdegree = null
      }
      
      this.$get('system/employee/getEmployeeReport', {...params}).then((r) => {
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
      this.queryParams.startdate = moment(new Date()).format('YYYY-MM-DD')
      this.queryParams.keyword = null
      this.queryParams.employeeid = null
      this.queryParams.deptids = []
      this.queryParams.states = []
      this.queryParams.rsfws = []
      this.queryParams.rszfws = []
      this.queryParams.employeetypes = []
      this.queryParams.joblevel = ''
      this.queryParams.titleget = ''
      this.queryParams.academic = ''
      this.queryParams.degree = ''
      this.queryParams.zqacademic = ''
      this.queryParams.zqdegree = ''
      this.queryParams.birthstartdate = ''
      this.queryParams.birthenddate = ''
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
