<template>
  <a-card :bordered="false" class="card-area payMgt">
    <a-form-model class="ruleForm" ref="statusForm" :model="statusForm" v-bind="layout">
      <a-row>
        <a-col :span="8">
          <a-form-model-item label="当前时间" prop="period">
            <span class="ant-form-item-span">{{statusForm.period}}</span>
          </a-form-model-item>
        </a-col>
        <a-col :span="8">
          <a-form-model-item label="是否开启" prop="checked">
            <a-switch checked-children="开" un-checked-children="关" :loading="startLoading" v-model="statusForm.checked" @change="switchChange" :disabled="switchDis" />
          </a-form-model-item>
        </a-col>
        <!--<a-col :span="8" style="text-align: right">-->
          <!--<a-button type="primary" :loading="finishLoading" :disabled="finishBtnDisabled" @click="finishSubmit">-->
            <!--完成-->
          <!--</a-button>-->
        <!--</a-col>-->
      </a-row>
      <a-row>
        <a-col :span="8">
          <a-form-model-item label="当前状态">
            <span class="ant-form-item-span">
              {{statusForm.status===1?'开启':'关闭'}}
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
                prop="keyword"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}"
              >
                <Select :keyword="formData.keyword" url="system/employee?search=" @getSearchValue="getSearchValue"></Select>
              </a-form-model-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-model-item
                label="人员类型"
                prop="employeetypes"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-select v-model="formData.employeetypes" mode="multiple" allowClear>
                  <a-select-option v-for="item in zidianInfo.employeetype" :value="item.keyy" :key="item.keyy">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
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
            <a-col :md="8" :sm="24">
              <a-form-item
                label="来院时间"
                :labelCol="{span: 5}"
                :wrapperCol="{span: 18, offset: 1}">
                <range-date @change="handleDateChange" ref="createTime"></range-date>
              </a-form-item>
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
        <a @click="toggleAdvanced" style="margin-left: 8px">
          {{advanced ? '收起' : '展开'}}
          <a-icon :type="advanced ? 'up' : 'down'" />
        </a>
      </span>
      </a-form-model>
    </div>
    <div class="operator">
      <a-button  @click="onSetRowKeys('pledit')">批量设置 </a-button>
      <a-upload
        :multiple="false"
        :file-list="formData.fileList"
        :before-upload="beforeUpload"
        :customRequest="upLoad"
        @change="handleChange"
      >
        <a-button> <a-icon type="upload" /> 导入 </a-button>
      </a-upload>
      <a-button @click="exportFile"><a-icon type="download" /> 导出 </a-button>
    </div>
    <a-row style="margin-bottom: 10px">
      <a-col :span="6">
        <span class="title">总人数：</span>
        <span>{{coreCol.count}}</span>
      </a-col>
      <a-col :span="6">
        <span class="title">应发合计：</span>
        <span>{{coreCol.issue}}</span>
      </a-col>
      <a-col :span="6">
        <span class="title">医疗补助合计：</span>
        <span>{{coreCol.ylbz}}</span>
      </a-col>
      <a-col :span="6">
        <span class="title">劳模补助合计：</span>
        <span>{{coreCol.lmbz}}</span>
      </a-col>
    </a-row>
    <a-table
      ref="tableInfo"
      :columns="columns"
      :loading="tableLoading"
      :row-key="record => record.id"
      :data-source="dataSource"
      :pagination="pagination"
      :scroll="{ x: 1500, y: 500 }"
      :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
      @change="handleTableChange"
    >
      <template slot="operation" slot-scope="text, record">
        <a-icon type="setting" theme="twoTone" twoToneColor="#4a9ff5" @click="editRow(record, 'edit')" title="设置"></a-icon>
      </template>
    </a-table>
    <a-modal class="ant-modal-nofooter" v-model="editVisible" :title="this.actionType==='edit'? '设置' : '批量设置'" :width="800">
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
            </a-form-model-item>  -->
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
import RangeDate from '@/components/datetime/RangeDate'
import Select from '../common/Select'
import { mapActions, mapGetters } from 'vuex'
import moment from 'moment'
export default {
  name: 'PayMgt',
  components: { Select, RangeDate },
  data () {
    return {
      // 表单设置
      switchDis: false,
      advanced: false,
      dateFormat: 'YYYY-MM-DD',
      layout: {
        labelCol: { span: 5 },
        wrapperCol: { span: 13 }
      },
      layout1: {
        labelCol: { span: 8 },
        wrapperCol: { span: 14 }
      },
      layout2: {
        labelCol: { span: 8 },
        wrapperCol: { span: 14 }
      },
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
        status: '',
        id: ''
      },
      fileList: [],
      // 查询条件
      formData: {
        employeeid: null,
        keyword: null,
        deptids: [],
        states: [],
        rsfws: [],
        rszfws: [],
        comedate: null,
        employeetypes: []
      },
      fileInfo: {},
      coreCol: {},
      // 列表数据
      columnType: '',
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
      editVisible: false,
      actionType: 'pledit',
      periodid: null,
      editFormid: null,
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
       // bzyl: 0,
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
        qt: 0,
      },
      editFormRules: {
        // code: [
        //   { required: true, message: '请输入人员编号', trigger: 'change' }
        // ],
        // employeename: [
        //   { required: true, message: '请输入员工姓名', trigger: 'change' }
        // ]
        // employeetype: [
        //   { required: true, message: '请输入人员类型', trigger: 'change' }
        // ],
        // dept: [
        //   { required: true, message: '请输入员工科室', trigger: 'change' }
        // ],
        // job: [
        //   { required: true, message: '请输入员工岗位', trigger: 'change' }
        // ]
      }
    }
  },
  watch: {
    periodInfo: {
      handler: function (data) {
        if (data && data.id) {
          this.periodid = data.id
          this.formData.periodid = data.id
          this.formData.period = data.period
          this.formData.emolumentflag = true
          this.statusForm.period = data.period
          this.statusForm.status = data.status
          this.statusForm.id = data.id
          if (data.status === 1) {
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
    this.$store.dispatch('getPeriodInfo', {vm: this})
  },
  mounted () {},
  computed: {
    ...mapGetters(['zidianInfo', 'periodInfo']),
    columns () {
      let { dept, job, employeetype, status, rszfw } = this.zidianInfo
      return [
        {
          title: '人员编号',
          dataIndex: 'code',
          width: 150,
          fixed: 'left',
          scopedSlots: { customRender: 'code' }
        },
        {
          title: '姓名',
          dataIndex: 'employeename',
          width: 150,
          fixed: 'left',
          scopedSlots: { customRender: 'employeename' }
        },
        {
          title: '科室',
          dataIndex: 'dept',
          width: 150,
          customRender: (text, row, index) => {
            let option = dept.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: '岗位',
          dataIndex: 'job',
          width: 150,
          customRender: (text, row, index) => {
            let option = job.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: '人员类型',
          dataIndex: 'employeetype',
          width: 120,
          customRender: (text, row, index) => {
            let option = employeetype.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: '人员状态',
          dataIndex: 'employeestatu',
          width: 120,
          customRender: (text, row, index) => {
            let option = status.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: '人事子范围',
          dataIndex: 'rszfw',
          width: 120,
          customRender: (text, row, index) => {
            let option = rszfw.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: '其他',
          dataIndex: 'qt',
          width: 150,
          scopedSlots: { customRender: 'qt' }
        },
        {
          title: '岗位工资(元)',
          dataIndex: 'gwgz',
          width: 150,
          scopedSlots: { customRender: 'gwgz' }
        },
        {
          title: '冲销津贴(元)',
          dataIndex: 'cxjt',
          width: 150,
          scopedSlots: { customRender: 'cxjt' }
        },
        {
          title: '薪级工资(元)',
          dataIndex: 'xjgz',
          width: 150,
          scopedSlots: { customRender: 'xjgz' }
        },
        {
          title: '护龄津贴(元)',
          dataIndex: 'hljt',
          width: 150,
          scopedSlots: { customRender: 'hljt' }
        },
        {
          title: '护士10%(元)',
          dataIndex: 'hs',
          width: 150,
          scopedSlots: { customRender: 'hs' }
        },
        {
          title: '高出部(元)',
          dataIndex: 'gcb',
          width: 150,
          scopedSlots: { customRender: 'gcb' }
        },
        {
          title: '特岗津贴(元)',
          dataIndex: 'tgjt',
          width: 150,
          scopedSlots: { customRender: 'tgjt' }
        },
        {
          title: '房贴(元)',
          dataIndex: 'ft',
          width: 150,
          scopedSlots: { customRender: 'ft' }
        },
        {
          title: '卫生费(元)',
          dataIndex: 'wsf',
          width: 150,
          scopedSlots: { customRender: 'wsf' }
        },
        {
          title: '独子补贴(元)',
          dataIndex: 'dzbt',
          width: 150,
          scopedSlots: { customRender: 'dzbt' }
        },
        {
          title: '基础性绩效(元)',
          dataIndex: 'jcxjx',
          width: 150,
          scopedSlots: { customRender: 'jcxjx' }
        },
        {
          title: '高聘(元)',
          dataIndex: 'gp',
          width: 150,
          scopedSlots: { customRender: 'gp' }
        },
        {
          title: '交通补贴(元)',
          dataIndex: 'jtbt',
          width: 150,
          scopedSlots: { customRender: 'jtbt' }
        },
        {
          title: '物业补贴(元)',
          dataIndex: 'wybt',
          width: 150,
          scopedSlots: { customRender: 'wybt' }
        },
        {
          title: '差额补贴(元)',
          dataIndex: 'cebt',
          width: 150,
          scopedSlots: { customRender: 'cebt' }
        },
        {
          title: '工资预付(元)',
          dataIndex: 'gzyf',
          width: 150,
          scopedSlots: { customRender: 'gzyf' }
        },
        {
          title: '调代2016(元)',
          dataIndex: 'td2016',
          width: 150,
          scopedSlots: { customRender: 'td2016' }
        },
        {
          title: '调代2017(元)',
          dataIndex: 'td2017',
          width: 150,
          scopedSlots: { customRender: 'td2017' }
        },
        {
          title: '调代2018(元)',
          dataIndex: 'td2018',
          width: 150,
          scopedSlots: { customRender: 'td2018' }
        },
        {
          title: '调代2019(元)',
          dataIndex: 'td2019',
          width: 150,
          scopedSlots: { customRender: 'td2019' }
        },
        {
          title: '调代2020(元)',
          dataIndex: 'td2020',
          width: 150,
          scopedSlots: { customRender: 'td2020' }
        },
        {
          title: '差额(元)',
          dataIndex: 'td2021',
          width: 150,
          scopedSlots: { customRender: 'td2021' }
        },
        {
          title: '上存(元)',
          dataIndex: 'sc',
          width: 150,
          scopedSlots: { customRender: 'sc' }
        },
        {
          title: '应发(元)',
          dataIndex: 'issue',
          width: 120,
          scopedSlots: { customRender: 'issue' }
        },
        {
          title: '医疗补助(元)',
          dataIndex: 'ylbz',
          width: 150,
          scopedSlots: { customRender: 'ylbz' }
        },
        {
          title: '劳模补助(元)',
          dataIndex: 'lmbz',
          width: 150,
          scopedSlots: { customRender: 'lmbz' }
        },
        // {
        //   title: '药费(元)',
        //   dataIndex: 'drugcost',
        //   width: 100,
        //   scopedSlots: { customRender: 'drugcost' }
        // },
        // {
        //   title: '水电(元)',
        //   dataIndex: 'hydropowercost',
        //   width: 100,
        //   scopedSlots: { customRender: 'hydropowercost' }
        // },{
        //   title: '房费(元)',
        //   dataIndex: 'roomcharge',
        //   width: 100,
        //   scopedSlots: { customRender: 'roomcharge' }
        // },{
        //   title: '会费(元)',
        //   dataIndex: 'meetingcost',
        //   width: 100,
        //   scopedSlots: { customRender: 'meetingcost' }
        // },{
        //   title: '公积金(元)',
        //   dataIndex: 'accumulationcost',
        //   width: 100,
        //   scopedSlots: { customRender: 'accumulationcost' }
        // },{
        //   title: '所得税(元)',
        //   dataIndex: 'incometax',
        //   width: 100,
        //   scopedSlots: { customRender: 'incometax' }
        // },{
        //   title: '职工养老(元)',
        //   dataIndex: 'zgongyl',
        //   width: 120,
        //   scopedSlots: { customRender: 'zgongyl' }
        // },{
        //   title: '职业年金(元)',
        //   dataIndex: 'zynj',
        //   width: 120,
        //   scopedSlots: { customRender: 'zynj' }
        // },{
        //   title: '大病医(元)',
        //   dataIndex: 'deylgrbj',
        //   width: 100,
        //   scopedSlots: { customRender: 'deylgrbj' }
        // },
        {
           title: '代理养老(元)',
           dataIndex: 'bzyl',
           width: 100,
           scopedSlots: { customRender: 'bzyl' }
         },
        // {
        //   title: '其它扣款项(元)',
        //   dataIndex: 'hrotherreduce',
        //   width: 130,
        //   scopedSlots: { customRender: 'hrotherreduce' }
        // },
        // {
        //   title: '下存(元)',
        //   dataIndex: 'savedown',
        //   width: 100,
        //   scopedSlots: { customRender: 'savedown' }
        // },{
        //   title: '实发(元)',
        //   dataIndex: 'actual',
        //   width: 100,
        //   scopedSlots: { customRender: 'actual' }
        // },
      
        {title: '操作',
          dataIndex: 'operation',
          width: 100,
          fixed: 'right',
          scopedSlots: {customRender: 'operation'}
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
    // 当前时间
    nowdateChange (date) {
      this.formData.period = date
    },
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    // 开启&关闭状态触发 用periodid 查询初始列表数据
    switchChange (checked) {
      let that = this
      this.startLoading = true
      this.$confirm({
        title: '本月薪酬一经确认开启后，将进入维护且无法操作关闭！是否继续？',
        onOk () {
          let params = JSON.parse(JSON.stringify({...that.statusForm}))
          delete params.checked
          that.$post('system/period', {...params}).then(res => {
            that.startLoading = false
            that.tableLoading = false
            let data = res.data
            if (data) {
              if (data.status === 1) {
                that.seachBtndis = false
                that.switchDis = true
                that.finishBtnDisabled = false
                that.fetch({
                  ...that.formData
                })
              }
            }
          }).catch(err => {
            that.startLoading = false
            that.$message.warning(err)
            that.tableLoading = false
            that.switchDis = false
            that.statusForm.checked = false
          })
        },
        onCancel () {
          that.startLoading = false
          that.switchDis = false
          that.statusForm.checked = false
        }
      })
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
    // 多选
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    // 批量设置
    onSetRowKeys (type) {
      this.resetForm()
      this.actionType = type
      if (this.selectedRowKeys.length === 0) {
        this.$message.warning('请勾选您要批量操作的记录！')
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
    handleDateChange (value) {
      if (value) {
        this.formData.comestartdate = value[0]
        this.formData.comeenddate = value[1]
      }
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
      this.$get('system/emolument/list', {...params}).then(res => {
        if (res.data) {
          this.tableLoading = false
          let data = res.data
          const pagination = { ...this.pagination }
          pagination.total = data.total
          this.dataSource = data.rows
          this.pagination = pagination
          this.coreCol = data.sum
        } else {
          this.tableLoading = false
        }
      }).catch(err => {
        this.$message.warning(err)
        this.tableLoading = false
      })
    },

    editRow (record, type) {
      this.actionType = type
      this.editVisible = true
      for (let key in this.editForm) {
        this.editForm[key] = record[key]
      }
      this.editFormid = record.id
    },
    // 保存
    editSubmit () {
      this.$refs.editForm.validate(valid => {
        if (valid) {
          let params = {...this.editForm}
          let url = this.actionType === 'pledit' ? 'system/emolument/updateEmolumentList' : 'system/emolument'
          if (this.actionType === 'pledit') {
            params.ids = this.selectedRowKeys.toString()
          } else {
            params.id = this.editFormid
          }
          params.issue = this.totalMoney
          params.periodid = this.statusForm.id
          this.$put(url, {...params}).then(res => {
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
    // 完成
    finishSubmit () {
      let that = this
      this.$confirm({
        title: '确认是否继续？点击确认按钮后，将关闭该月数据系统！',
        onOk () {
          that.finishLoading = true
          let params = {...that.statusForm}
          delete params.checked
          that.$get('system/emolument/passEmolument', {...params}).then(res => {
            if (res.data) {
              that.finishLoading = false
              that.finishBtnDisabled = true
              that.$message.success('审核通过!')
              that.$store.dispatch('getPeriodInfo', {vm: that})
              that.dataSource = []
              that.paginationInfo = null
              that.pagination.total = 0
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
    // 导出
    exportFile () {
      this.$export('system/emolument/export', {
        ...this.formData
      })
    },
    // 导入之前
    beforeUpload  (file) {
      console.log(file)
      let fileName = file.name.split('.')
      if (fileName[fileName.length - 1] === 'xlsx') {
        this.fileInfo = file
        return true
      } else {
        this.$message.warning('请上传正确格式的表格')
        return false
      }
    },
    // 自定义上传
    upLoad () {
      const formImgData = new FormData()
      formImgData.append('file', this.fileInfo)
      this.$upload('system/emolument/import', formImgData).then(res => {
        if (res.data) {
          this.dataSource = res.data.data.data
          this.formData.fileList = []
          this.$message.success('上传成功！')
          this.fetch({...this.formData})
        }
      }).catch(err => {
        this.$message.warning(err)
      })
    },
    handleChange (info) {
      let fileList = [...info.fileList]
      fileList = fileList.slice(-2)

      fileList = fileList.map(file => {
        if (file.response) {
          // Component will show file.url as link
          file.url = file.response.url
        }
        return file
      })

      this.fileList = fileList
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
        gwgz: 0, cxjt: 0, xjgz: 0, hljt: 0, hs: 0, gcb: 0, tgjt: 0, qt: 0, ft: 0, wsf: 0, ylbz: 0, dzbt: 0, jcxjx: 0, gp: 0, jtbt: 0, wybt: 0, cebt: 0, gzyf: 0, td2016: 0, td2017: 0, td2018: 0, td2019: 0, td2020: 0, td2021: 0, sc: 0, issue: 0, lmbz: 0
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
    .title{
      color: #999;
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
