<!--核心信息-->
<template>
  <div>
    <div class="actionBtn">
      <a-button  v-if="formData.employeeid"  type="primary" class="editable-add-btn editable-margin-right" @click="clickAdd('addBtn')">
        新增
      </a-button>
    </div>
    <a-table
      :columns="columns"
      :loading="loading"
      :data-source="coreInfo"
      :keyRow="record => record.id"
      :pagination="false"
      bordered
      :scroll="{ x: 1200 }"
    >
      <!--<template slot="operation" slot-scope="text, record">-->
        <!--<a-button class="editable-add-btn" @click="editRow(record, 'editBtn')">-->
          <!--编辑-->
        <!--</a-button>-->
      <!--</template>-->
      <a slot="operation" slot-scope="text, record" href="javascript:;" @click="editRow(record, 'editBtn')">编辑</a>
    </a-table>
    <a-modal class="ant-modal-nofooter" v-model="coreVisible" :title="btnType === 'addBtn' ? '新增' : '修改'" :width="750" @ok="handleOk">
      <a-form-model
        ref="formData"
        :model="formData"
        :rules="formDataRules"
        v-bind="layout"
      >
        <a-row>
          <a-col :span="12">
            <a-form-model-item v-if="btnType === 'editBtn'" label="修改人" prop="xtupdateby">
              <a-input v-model="formData.xtupdateby" :disabled="disEdit" allowClear></a-input>
            </a-form-model-item>
            <a-form-model-item label="开始时间" prop="startdate">
              <a-date-picker
                v-model="formData.startdate"
                :format="dateFormat"
                :valueFormat="dateFormat"
                @change="startdateChange"
                :disabled="disEdit"
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
            <a-form-model-item v-if="btnType === 'editBtn'" label="备注" prop="memo">
              <a-textarea :rows="2" v-model="formData.memo" allowClear></a-textarea>
            </a-form-model-item>
          </a-col>
          <a-col :span="12">
            <a-form-model-item v-if="btnType === 'editBtn'" label="修改时间" prop="xtupdatedate">
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
                :disabled="disEdit"
              />
            </a-form-model-item>
            <a-form-model-item label="科室" prop="dept">
              <a-select
                v-model="formData.dept"
                show-search
                placeholder="请输入科室"
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
                <a-select-option v-for="item in zidianInfo.employeetype" :value="item.keyy" :key="item.keyy" @click="selectEmployeetype(item)">
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
          <a-button type="primary" style="margin-left: 20px;" @click="addSubmit">
            确认
          </a-button>
        </a-form-model-item>
      </a-form-model>
    </a-modal>
  </div>
</template>
<script>
import Select from '../../common/Select'
import { mapGetters, mapActions } from 'vuex'
import moment from 'moment'
export default {
  components: { Select },
  data () {
    return {
      dateFormat: 'YYYY-MM-DD',
      loading: false,
      coreVisible: false,
      btnType: 'addBtn',
      tableData: [],
      pageStatus: '',
      query: {},
      layout: {
        labelCol: { span: 6 },
        wrapperCol: { span: 17 }
      },
      startdateValue: 0,
      enddateValue: 0,
      lastdateValue: 0,
      lastdate: '',
      disEdit: false,
      formData: {
        employeeid: null,
        startdate: '',
        enddate: '9999-12-31',
        depttype: '',
        dept: null,
        ward: null,
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
      title: '',
      formDataRules: {
        startdate: [
          { required: true, message: '请输入开始时间', trigger: 'change' }
        ],
        enddate: [
          { required: true, message: '请输入结束时间', trigger: 'change' }
        ],
        // depttype: [
        //   { required: true, message: '请输入科室', trigger: 'change' }
        // ],
        dept: [
          { required: true, message: '请输入科室', trigger: 'change' }
        ],
        rsfw: [
          { required: true, message: '请输入人事范围', trigger: 'change' }
        ],
        rszfw: [
          { required: true, message: '请输入人事子范围', trigger: 'change' }
        ],
        ward: [
          { required: true, message: '请输入病区', trigger: 'change' }
        ],
        // job: [
        //   { required: true, message: '请输入岗位', trigger: 'change' }
        // ],
        // employeetype: [
        //   { required: true, message: '请输入人员类型', trigger: 'change' }
        // ],

        eventtype: [
          { required: true, message: '请输入事件类型', trigger: 'change' }
        ],
        status: [
          { required: true, message: '请输入人员状态', trigger: 'change' }
        ]
      }
    }
  },
  watch: {
    queryData: {
      handler: function (data) {
        if (data) {
          this.query = data
          this.formData.employeeid = data.employeeid
          this.pageStatus = data.pageStatus
          this.formData.eventtype = data.eventtype
          this.formData.startdate = data.startdate
          this.formData.status = data.status
          this.formData.statusname = data.statusname
        }
      },
      immediate: true
    },
    coreInfo: {
      handler: function (data) {
        if (data && data.length > 0) {
          this.tableData = data
          this.formData.eventtype = data[0].eventtype&&data[0].eventtype.toString()
          this.formData.eventtypename = data[0].eventtypename&&data[0].eventtypename.toString()
          if (this.btnType === 'addBtn') {
            this.lastdateValue = moment(data[0].startdate).valueOf()
            this.lastdate = data[0].startdate
            this.formData.startdate = moment(this.lastdate, 'YYYY-MM-DD').add(1, 'days')
          }
        } else {
          this.tableData = []
          this.formData.eventtype = this.query.eventtype || '1'
          this.formData.eventtypename = this.query.eventtypename || '入职'
          this.formData.startdate = this.query.startdate
          this.lastdate = ''
          this.lastdateValue = 0
        }
      },
      immediate: true
    },
    'formData.startdate': {
      handler: function (data) {
        if (data) {
          this.startdateValue = moment(data).valueOf()
          if (this.btnType === 'addBtn' && this.lastdate && this.startdateValue <= this.lastdateValue) {
            this.$message.warning('开始时间不能小于或等于上一次开始时间！')
            this.formData.startdate = moment(this.lastdate, 'YYYY-MM-DD').add(1, 'days')
          }
          if (this.btnType === 'addBtn' && this.lastdate && this.startdateValue && this.enddateValue && this.startdateValue >= this.enddateValue) {
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
          if (this.btnType === 'addBtn' && this.startdateValue && this.enddateValue && this.startdateValue >= this.enddateValue) {
            this.$message.warning('结束时间不能小于或等于开始时间')
            this.formData.enddate = '9999-12-31'
          }
        }
      },
      immediate: true
    }
  },
  computed: {
    ...mapGetters(
      ['coreInfo', 'queryData', 'zidianInfo', 'deptOptions', 'wardOptions', 'rszfwOptions']
    ),
    columns () {
      let { depttype, dept, ward, job, position, employeetype, rsfw, rszfw, eventtype, status } = this.zidianInfo
      return [
        { title: '开始时间', dataIndex: 'startdate', width: 130, scopedSlots: { customRender: 'startdate' } },
        { title: '结束时间', dataIndex: 'enddate', width: 130, scopedSlots: { customRender: 'enddate' } },
        { title: '科室类型',
          dataIndex: 'depttype',
          width: 100,
          customRender: (text, row, index) => {
            let option = depttype.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '科室',
          dataIndex: 'dept',
          width: 100,
          customRender: (text, row, index) => {
            let option = dept.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '病区',
          dataIndex: 'ward',
          width: 100,
          customRender: (text, row, index) => {
            let option = ward.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '岗位',
          dataIndex: 'job',
          width: 100,
          customRender: (text, row, index) => {
            let option = job.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '职位',
          dataIndex: 'position',
          width: 100,
          customRender: (text, row, index) => {
            let option = position.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '人员类型',
          dataIndex: 'employeetype',
          width: 100,
          customRender: (text, row, index) => {
            let option = employeetype.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '人事范围',
          dataIndex: 'rsfw',
          width: 100,
          customRender: (text, row, index) => {
            let option = rsfw.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '人事子范围',
          dataIndex: 'rszfw',
          width: 120,
          customRender: (text, row, index) => {
            let option = rszfw.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '人事事件',
          dataIndex: 'eventtype',
          width: 100,
          customRender: (text, row, index) => {
            let option = eventtype.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '人员状态',
          dataIndex: 'status',
          width: 100,
          customRender: (text, row, index) => {
            let option = status.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {title: '操作',
          dataIndex: 'operation',
          width: 90,
          fixed: 'right',
          scopedSlots: {customRender: 'operation'}
        }
      ]
    }
  },
  created () {},
  mounted () {
    this.formData.startdate = this.query.startdate
  },
  methods: {
    ...mapActions(['getdept', 'getward', 'getrszfw']),
    startdateChange (date) {
      this.formData.startdate = date
    },
    enddateChange (date) {
      this.formData.enddate = date
    },
    editRow (record, type) {
      this.btnType = type
      this.coreVisible = true
      this.disEdit = true
      this.formData = {...this.formData, ...record}
      this.formData.ward = this.formData.ward ? this.formData.ward.toString() : ''
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
    // 新增
    clickAdd (type) {
      this.btnType = type
      this.coreVisible = true
      this.disEdit = false
      this.formData.id = null
      this.resetForm()
      if (this.lastdate && this.startdateValue <= this.lastdateValue) {
        this.$message.warning('开始时间不能小于或等于上一次开始时间！')
        this.formData.startdate = moment(this.lastdate, 'YYYY-MM-DD').add(1, 'days')
      }
      this.formData.enddate = '9999-12-31'
      this.getname({keyy: this.formData.eventtype}, 'eventtype')
    },
    handleOk () {
      this.coreVisible = false
    },
    getname (item, name) {
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
    getdeptname (value) {
      if (!value) {
        this.formData.ward = null
        this.formData.wardname = null
        this.$store.dispatch('getward', {vm: this, id: null})
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
        this.$store.dispatch('setrszfw', {vm: this})
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
    handleChange (value) {
      // this.formData.position = value
      console.log(`selected ${value}`)
    },
    filterOption (input, option) {
      return (
        option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      )
    },
    addSubmit () {
      this.$refs.formData.validate(valid => {
        if (valid) {
          let params = {}
          params.id = this.btnType === 'addBtn' ? null : this.formData.id
          params.employeeid = this.formData.employeeid
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
          let http = this.btnType === 'addBtn' ? this.$post : this.$put
          http('system/employee/core', {...params}).then(res => {
            if (res.data.status === 'OK') {
              this.$store.dispatch('getSearch', {vm: this, id: this.formData.employeeid})
              this.resetForm()
              this.coreVisible = false
              this.$message.success(this.btnType === 'addBtn' ? '新增成功！' : '修改成功！')
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
      this.formData.depttype = null
      this.formData.dept = null
      this.formData.ward = null
      this.formData.job = null
      this.formData.position = null
      this.formData.employeetype = null
      this.formData.rsfw = null
      this.formData.rszfw = null
    }
  }
}
</script>
<style lang="less" scoped>
  .actionBtn {
    margin-bottom: 10px;
    text-align: right;
  }
</style>
