<template>
  <a-card :bordered="false" class="card-area payMgt">
    <div :class="advanced ? 'search' : null">
      <a-form-model
        class="ruleForm"
        ref="formData"
        :model="formData"
        v-bind="layout"
      >
        <div :class="advanced ? null : 'fold'">
          <a-row>
            <a-col :md="8" :sm="24">
              <a-form-item
                label="开始年月"
                prop="startdate"
                :labelCol="{ span: 7 }"
                :wrapperCol="{ span: 16, offset: 1 }"
              >
                <a-month-picker
                  v-model="formData.startdate"
                  :format="dateFormat"
                  :valueFormat="dateFormat"
                  @change="startdateChange"
                />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item
                label="结束年月"
                prop="enddate"
                :labelCol="{ span: 7 }"
                :wrapperCol="{ span: 16, offset: 1 }"
              >
                <a-month-picker
                  v-model="formData.enddate"
                  :format="dateFormat"
                  :valueFormat="dateFormat"
                  @change="enddateChange"
                />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-model-item
                label="员工姓名"
                prop="keyword"
                :labelCol="{ span: 7 }"
                :wrapperCol="{ span: 16, offset: 1 }"
              >
                <Select
                  :keyword="formData.keyword"
                  url="system/employee?search="
                  @getSearchValue="getSearchValue"
                ></Select>
              </a-form-model-item>
            </a-col>
          </a-row>
          <a-row v-if="advanced">
            <a-col :md="8" :sm="24">
              <a-form-model-item
                label="人员类型"
                prop="employeetype"
                :labelCol="{ span: 7 }"
                :wrapperCol="{ span: 16, offset: 1 }"
              >
                <a-select
                  v-model="formData.employeetype"
                  mode="multiple"
                  allowClear
                >
                  <a-select-option
                    v-for="item in zidianInfo.employeetype"
                    :value="item.keyy"
                    :key="item.keyy"
                  >
                    {{ item.valuee }}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-model-item
                label="科室"
                prop="dept"
                :labelCol="{ span: 7 }"
                :wrapperCol="{ span: 16, offset: 1 }"
              >
                <a-select v-model="formData.deptids" mode="multiple" allowClear>
                  <a-select-option
                    v-for="item in zidianInfo.dept"
                    :value="item.keyy"
                    :key="item.keyy"
                  >
                    {{ item.valuee }}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-model-item
                label="人员状态"
                prop="status"
                :labelCol="{ span: 7 }"
                :wrapperCol="{ span: 16, offset: 1 }"
              >
                <a-select v-model="formData.states" mode="multiple" allowClear>
                  <a-select-option
                    v-for="item in zidianInfo.status"
                    :value="item.keyy"
                    :key="item.keyy"
                  >
                    {{ item.valuee }}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-model-item
                label="人事子范围"
                prop="rszfwname"
                :labelCol="{ span: 7 }"
                :wrapperCol="{ span: 16, offset: 1 }"
              >
                <a-select v-model="formData.rszfws" mode="multiple" allowClear>
                  <a-select-option
                    v-for="item in zidianInfo.rszfw"
                    :value="item.keyy"
                    :key="item.keyy"
                  >
                    {{ item.valuee }}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
          </a-row>
        </div>
        <span style="float: right; margin-top: 3px; margin-bottom: 18px">
          <a-button
            class="editable-add-btn"
            :loading="startLoading"
            @click="reset"
          >
            重置
          </a-button>
          <a-button
            type="primary"
            :loading="startLoading"
            @click="handleSubmit"
          >
            查询
          </a-button>
          <a @click="toggleAdvanced" style="margin-left: 8px">
            {{ advanced ? "收起" : "展开" }}
            <a-icon :type="advanced ? 'up' : 'down'" />
          </a>
        </span>
      </a-form-model>
    </div>
    <div class="operator">
      <a-button @click="exportFile"><a-icon type="download" /> 导出 </a-button>
    </div>
    <a-row style="margin-bottom: 10px">
      <a-col :span="6">
        <span class="title">总人数：</span>
        <span>{{ coreCol.count }}</span>
      </a-col>
      <a-col :span="6">
        <span class="title">应发合计：</span>
        <span>{{ coreCol.issue }}</span>
      </a-col>
      <a-col :span="6">
        <span class="title">医疗补助合计：</span>
        <span>{{ coreCol.ylbz }}</span>
      </a-col>
      <a-col :span="6">
        <span class="title">劳模补助合计：</span>
        <span>{{ coreCol.lmbz }}</span>
      </a-col>
    </a-row>
    <a-table
      ref="tableInfo"
      :columns="columns"
      :loading="tableLoading"
      :row-key="(record) => record.employeeid"
      :data-source="dataSource"
      :pagination="pagination"
      @change="handleTableChange"
      :scroll="{ x: 1500, y: 500 }"
    >
      <template slot="operation" slot-scope="text, record">
        <a-icon
          type="setting"
          theme="twoTone"
          twoToneColor="#4a9ff5"
          @click="view(record)"
          title="上月差异"
        ></a-icon>
      </template>
    </a-table>
    <compare-pay @cancel="handleCancel" :modelVisible="modelVisible" :employeeid="employeeid" :startdate="startdate" :enddate="enddate">
    </compare-pay>
  </a-card>
</template>
<script>
import Select from "../common/Select";
import { mapActions, mapGetters } from "vuex";
import moment from "moment";
import ComparePay from "./ComparePay.vue";
export default {
  name: "HistoryPay",
  components: {Select, ComparePay },
  data() {
    return {
      // 表单设置
      dateFormat: "YYYY-MM",
      advanced: false,
      layout: {
        labelCol: { span: 8 },
        wrapperCol: { span: 14 },
      },
      // 加载状态
      startLoading: false,
      tableLoading: false,
      // 查询条件
      formData: {
        employeeid: null,
        keyword: null,
        deptids: [],
        states: [],
        rsfws: [],
        rszfws: [],
        employeetypes: [],
        startdate: "",
        enddate: "",
      },
      coreCol: {},
      // 列表数据
      dataSource: [],
      paginationInfo: null,
      pagination: {
        pageSizeOptions: ["10", "20", "30", "40", "100"],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) =>
          `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`,
      },
      modelVisible: false,
      employeeid: "",
      startdate: "",
      enddate: "",
    };
  },
  watch: {},
  created() {},
  mounted() {
    // this.fetch({
    //   ...this.queryParams
    // })
  },
  computed: {
    ...mapGetters(["zidianInfo", "periodInfo"]),
    columns() {
      let { status, dept, job, employeetype, rszfw } = this.zidianInfo;
      return [
        {
          title: "人员编号",
          dataIndex: "code",
          width: 100,
          scopedSlots: { customRender: "code" },
        },
        {
          title: "姓名",
          dataIndex: "employeename",
          width: 100,
          scopedSlots: { customRender: "employeename" },
        },
        {
          title: "工资期间",
          dataIndex: "period",
          width: 100,
          scopedSlots: { customRender: "period" },
        },
        {
          title: "科室",
          dataIndex: "dept",
          width: 120,
          customRender: (text, row, index) => {
            let option = dept.filter((item) => item.keyy == text)[0];
            return option ? option.valuee : "";
          },
        },
        {
          title: "病区",
          dataIndex: "wardname",
          width: 120,
        },
        {
          title: "岗位",
          dataIndex: "job",
          width: 100,
          customRender: (text, row, index) => {
            let option = job.filter((item) => item.keyy == text)[0];
            return option ? option.valuee : "";
          },
        },
        {
          title: "人员类型",
          dataIndex: "employeetype",
          width: 100,
          customRender: (text, row, index) => {
            let option = employeetype.filter((item) => item.keyy == text)[0];
            return option ? option.valuee : "";
          },
        },
        {
          title: "人员状态",
          dataIndex: "employeestatu",
          width: 100,
          customRender: (text, row, index) => {
            if (!text) return ''
            let option = status && status.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          },
        },
        {
          title: "人事子范围",
          dataIndex: "rszfw",
          width: 120,
          customRender: (text, row, index) => {
            let option = rszfw.filter((item) => item.keyy == text)[0];
            return option ? option.valuee : "";
          },
        },
        {
          title: "其他",
          dataIndex: "qt",
          width: 100,
          scopedSlots: { customRender: "qt" },
        },
        {
          title: "岗位工资(元)",
          dataIndex: "gwgz",
          width: 120,
          scopedSlots: { customRender: "gwgz" },
        },
        {
          title: "冲销津贴(元)",
          dataIndex: "cxjt",
          width: 120,
          scopedSlots: { customRender: "cxjt" },
        },
        {
          title: "薪级工资(元)",
          dataIndex: "xjgz",
          width: 120,
          scopedSlots: { customRender: "xjgz" },
        },
        {
          title: "护龄津贴(元)",
          dataIndex: "hljt",
          width: 120,
          scopedSlots: { customRender: "hljt" },
        },
        {
          title: "护士10%(元)",
          dataIndex: "hs",
          width: 120,
          scopedSlots: { customRender: "hs" },
        },
        {
          title: "高出部(元)",
          dataIndex: "gcb",
          width: 120,
          scopedSlots: { customRender: "gcb" },
        },
        {
          title: "特岗津贴(元)",
          dataIndex: "tgjt",
          width: 120,
          scopedSlots: { customRender: "tgjt" },
        },
        {
          title: "房贴(元)",
          dataIndex: "ft",
          width: 100,
          scopedSlots: { customRender: "ft" },
        },
        {
          title: "卫生费(元)",
          dataIndex: "wsf",
          width: 100,
          scopedSlots: { customRender: "wsf" },
        },
        {
          title: "独子补贴(元)",
          dataIndex: "dzbt",
          width: 120,
          scopedSlots: { customRender: "dzbt" },
        },
        {
          title: "基础性绩效(元)",
          dataIndex: "jcxjx",
          width: 130,
          scopedSlots: { customRender: "jcxjx" },
        },
        {
          title: "高聘(元)",
          dataIndex: "gp",
          width: 100,
          scopedSlots: { customRender: "gp" },
        },
        {
          title: "交通补贴(元)",
          dataIndex: "jtbt",
          width: 120,
          scopedSlots: { customRender: "jtbt" },
        },
        {
          title: "物业补贴(元)",
          dataIndex: "wybt",
          width: 120,
          scopedSlots: { customRender: "wybt" },
        },
        {
          title: "差额补贴(元)",
          dataIndex: "cebt",
          width: 120,
          scopedSlots: { customRender: "cebt" },
        },
        {
          title: "工资预付(元)",
          dataIndex: "gzyf",
          width: 120,
          scopedSlots: { customRender: "gzyf" },
        },
        {
          title: "调代2016(元)",
          dataIndex: "td2016",
          width: 120,
          scopedSlots: { customRender: "td2016" },
        },
        {
          title: "调代2017(元)",
          dataIndex: "td2017",
          width: 120,
          scopedSlots: { customRender: "td2017" },
        },
        {
          title: "调代2018(元)",
          dataIndex: "td2018",
          width: 120,
          scopedSlots: { customRender: "td2018" },
        },
        {
          title: "调代2019(元)",
          dataIndex: "td2019",
          width: 120,
          scopedSlots: { customRender: "td2019" },
        },
        {
          title: "调代2020(元)",
          dataIndex: "td2020",
          width: 120,
          scopedSlots: { customRender: "td2020" },
        },
        {
          title: "差额(元)",
          dataIndex: "td2021",
          width: 120,
          scopedSlots: { customRender: "td2021" },
        },
        {
          title: "上存(元)",
          dataIndex: "sc",
          width: 100,
          scopedSlots: { customRender: "sc" },
        },
        {
          title: "应发(元)",
          dataIndex: "issue",
          width: 100,
          scopedSlots: { customRender: "issue" },
        },
        {
          title: "大额医疗(元)",
          dataIndex: "deylGr",
          width: 100,
        },
        {
          title: "失业(元)",
          dataIndex: "syGr",
          width: 100,
        },
        {
          title: "养老(元)",
          dataIndex: "yanglaoGr",
          width: 100,
          scopedSlots: { customRender: "issue" },
        },
        {
          title: "医疗(元)",
          dataIndex: "ylGr",
          width: 100,
          scopedSlots: { customRender: "issue" },
        },
        {
          title: "医疗补助(元)",
          dataIndex: "ylbz",
          width: 120,
          scopedSlots: { customRender: "ylbz" },
        },
        {
          title: "劳模补助(元)",
          dataIndex: "lmbz",
          width: 120,
          scopedSlots: { customRender: "lmbz" },
        },
        {
          title: "药费(元)",
          dataIndex: "drugcost",
          width: 100,
          scopedSlots: { customRender: "drugcost" },
        },
        {
          title: "水电(元)",
          dataIndex: "hydropowercost",
          width: 100,
          scopedSlots: { customRender: "hydropowercost" },
        },
        {
          title: "房费(元)",
          dataIndex: "roomcharge",
          width: 100,
          scopedSlots: { customRender: "roomcharge" },
        },
        {
          title: "会费(元)",
          dataIndex: "meetingcost",
          width: 100,
          scopedSlots: { customRender: "meetingcost" },
        },
        {
          title: "公积金(元)",
          dataIndex: "accumulationcost",
          width: 100,
          scopedSlots: { customRender: "accumulationcost" },
        },
        {
          title: "所得税(元)",
          dataIndex: "incometax",
          width: 100,
          scopedSlots: { customRender: "incometax" },
        },
        {
          title: "职工养老(元)",
          dataIndex: "zgongyl",
          width: 120,
          scopedSlots: { customRender: "zgongyl" },
        },
        {
          title: "职业年金(元)",
          dataIndex: "zynj",
          width: 120,
          scopedSlots: { customRender: "zynj" },
        },
        {
          title: "大病医(元)",
          dataIndex: "deylgrbj",
          width: 100,
          scopedSlots: { customRender: "deylgrbj" },
        },
        {
          title: "其它扣款项(元)",
          dataIndex: "hrotherreduce",
          width: 130,
          scopedSlots: { customRender: "hrotherreduce" },
        },
        {
          title: "下存(元)",
          dataIndex: "savedown",
          width: 100,
          scopedSlots: { customRender: "savedown" },
        },
        {
          title: "实发(元)",
          dataIndex: "actual",
          width: 100,
          scopedSlots: { customRender: "actual" },
        },
        {
          title: "代理养老(元)",
          dataIndex: "bzyl",
          width: 100,
          scopedSlots: { customRender: "bzyl" },
        },
        {
          title: "操作",
          dataIndex: "operation",
          scopedSlots: { customRender: "operation" },
          width: 100,
        },
      ];
    },
  },
  methods: {
    ...mapActions([""]),
    handleCancel(){
      this.modelVisible = false
    },
    // 当前时间
    startdateChange(date) {
      this.formData.startdate = date;
    },
    enddateChange(date) {
      this.formData.enddate = date;
    },
    toggleAdvanced() {
      this.advanced = !this.advanced;
    },
    getSearchValue({ value }) {
      this.formData.employeeid = this.formData.keyword = value;
    },
    // 查询
    handleSubmit() {
      this.startLoading = true;
      this.fetch({
        ...this.formData,
      });
      this.startLoading = false;
    },
    handleTableChange(pagination, filters, sorter) {
      this.paginationInfo = pagination;
      this.fetch({
        ...this.formData,
      });
    },
    view(record) {
      this.employeeid = record.employeeid;
      this.enddate = record.period;
      this.startdate = this.handleMonth(record.period);
      console.info(222)
      this.modelVisible = true;
    },
    handleMonth(period) {
      var year = period.split("-")[0];
      var month = period.split("-")[1];
      var month2 = parseInt(month) - 1;
      var year2 = year;
      if (month2 == 0) {
        year2 = parseInt(year2) - 1;
        month2 = 12;
      }
      if (month2 < 10) {
        month2 = "0" + month2;
      }
      return year2 + "-" + month2;
    },
    // 带条件的查询
    fetch(params = {}) {
      this.tableLoading = true;
      if (this.paginationInfo) {
        // 如果分页信息不为空，则设置表格当前第几页，每页条数，并设置查询分页参数
        this.$refs.tableInfo.pagination.current = this.paginationInfo.current;
        this.$refs.tableInfo.pagination.pageSize = this.paginationInfo.pageSize;
        params.pageSize = this.paginationInfo.pageSize;
        params.pageNum = this.paginationInfo.current;
      } else {
        // 如果分页信息为空，则设置为默认值
        params.pageSize = this.pagination.defaultPageSize;
        params.pageNum = this.pagination.defaultCurrent;
      }
      this.$get("system/emolument", { ...params })
        .then((res) => {
          if (res.data) {
            let data = res.data;
            const pagination = { ...this.pagination };
            pagination.total = data.total;
            this.dataSource = data.rows;
            this.coreCol = data.sum;
            this.pagination = pagination;
            this.tableLoading = false;
            this.dataSource.forEach((item) => {
              item.editable = true;
            });
          } else {
            this.tableLoading = false;
          }
        })
        .catch((err) => {
          this.$message.warning(err);
          this.tableLoading = false;
        });
    },
    // 导出
    exportFile() {
      this.$export("system/emolument/export", {
        ...this.formData,
      });
    },
    reset() {
      // 重置查询参数
      this.formData.startdate = "";
      this.formData.enddate = "";
      this.formData.employeeid = null;
      this.formData.keyword = null;
      this.formData.deptids = [];
      this.formData.states = [];
      this.formData.rsfws = [];
      this.formData.rsfws = [];
      this.formData.rszfws = [];
      this.formData.employeetypes = [];
      // 重置分页
      this.$refs.tableInfo.pagination.current = this.pagination.defaultCurrent;
      this.paginationInfo = null;
      this.fetch({ ...this.formData });
    },
  },
};
</script>
<style scoped lang="less">
.payMgt {
  width: 100%;
  .ruleForm {
    .ant-form-item {
      margin-bottom: 5px !important;
    }
    .ant-switch {
      width: 60px;
      height: 28px;
      line-height: 26px;
    }
    .ant-switch-loading-icon,
    .ant-switch::after {
      width: 24px;
      height: 24px;
    }
  }
  .editable-add-btn {
    margin-right: 10px;
  }
  .title {
    color: #999;
  }
}
</style>
<style lang="less">
@import "../../../static/less/Common";
.ant-form-item-span {
  color: rgba(0, 0, 0, 0.85);
}
</style>
