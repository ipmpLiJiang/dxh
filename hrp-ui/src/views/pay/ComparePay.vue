<template>
  <a-modal
    title="最近两个月数据对比"
    @cancel="handleCancel"
    width="80%"
    :footer="null"
    v-model="show"
  >
    <a-card :bordered="false" class="card-area payMgt">
      <a-table
        ref="tableInfo"
        :columns="columns"
        :loading="tableLoading"
        :row-key="(record) => record.period"
        :data-source="dataSource"
        :pagination="false"
        :scroll="{ x: 1500, y: 500 }"
      >
      </a-table>
    </a-card>
  </a-modal>
</template>
<script>
import moment from "moment";
import { mapActions, mapGetters } from "vuex";
export default {
  name: "HistoryPay",
  components: {},
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
      arrFilds: [
        "qt",
        "gwgz",
        "cxjt",
        "xjgz",
        "hljt",
        "hs",
        "gcb",
        "tgjt",
        "ft",
        "wsf",
        "dzbt",
        "jcxjx",
        "gp",
        "jtbt",
        "wybt",
        "cebt",
        "gzyf",
        "td2016",
        "td2017",
        "td2018",
        "td2019",
        "td2020",
        "td2021",
        "sc",
        "issue",
        "deylGr",
        "syGr",
        "yanglaoGr",
        "ylGr",
        "ylbz",
        "lmbz",
        "drugcost",
        "hydropowercost",
        "roomcharge",
        "meetingcost",
        "accumulationcost",
        "incometax",
        "zgongyl",
        "zynj",
        "deylgrbj",
        "hrotherreduce",
        "savedown",
        "actual",
        "bzyl",
      ],
    };
  },
  watch: {
    modelVisible() {
      if (this.modelVisible) {
        this.fetch({
          employeeid: this.employeeid,
          startdate: this.startdate,
          enddate: this.enddate,
        });
      }
    },
  },
  created() {},
  props: {
    modelVisible: {
      default: false,
    },
    employeeid: {
      default: "",
    },
    startdate: {
      default: "",
    },
    enddate: {
      default: "",
    },
  },
  computed: {
    ...mapGetters(["zidianInfo", "periodInfo"]),
    show: {
      get: function () {
        return this.modelVisible;
      },
      set: function () {},
    },
    columns() {
      let { dept, job, employeetype, rszfw } = this.zidianInfo;
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
      ];
    },
  },
  methods: {
    ...mapActions([""]),
    handleCancel() {
      this.$emit("cancel");
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
            this.dataSource = data.rows;
            var rows = data.rows;
            var addRow = {};

            if (rows.length > 1) {
              this.arrFilds.forEach((key) => {
                addRow[key] =
                  parseFloat(this.handleNumer(rows[0][key])) -
                  parseFloat(this.handleNumer(rows[1][key]));
              });
              addRow["period"] = rows[0].period.substr(0,4);
              addRow['employeename'] = '数据差异:'
              rows.push(addRow);
            }
            this.dataSource = rows;
            this.tableLoading = false;
          } else {
            this.tableLoading = false;
          }
        })
        .catch((err) => {
          this.$message.warning(err);
          this.tableLoading = false;
        });
    },
    handleNumer(value) {
        console.info(value)
      if (value == "") return 0;
       if (value == null) return 0;
      return value;
    },
    // 导出
    exportFile() {
      this.$export("system/emolument/export", {
        ...this.formData,
      });
    },
    reset() {},
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
