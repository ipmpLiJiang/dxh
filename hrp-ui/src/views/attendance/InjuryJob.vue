<!--家庭信息-->
<template>
  <a-card :bordered="false" class="card-area injuryJob">
    <a-row class="injuryJob-top" type="flex" justify="space-between">
      <a-col :md="8" :sm="24" class="flex">
        <a-form-item
          label="员工姓名"
          prop="keyword"
          :labelCol="{ span: 4 }"
          :wrapperCol="{ span: 18, offset: 1 }"
        >
          <Select
            :keyword="queryParams.keyword"
            url="system/employee?search="
            @getSearchValue="getSearchValue"
          ></Select>
        </a-form-item>
        <a-form-item class="w-120">
          <a-button type="primary" @click="handleSubmit"> 查询 </a-button>
        </a-form-item>
      </a-col>
      <a-col :md="6" :sm="24">
        <a-form-item
          class="form-item"
          :labelCol="{ span: 7 }"
          :wrapperCol="{ span: 16, offset: 1 }"
        >
          <a-button
            type="primary"
            class="editable-add-btn"
            @click="clickAdd('addBtn')"
          >
            新增
          </a-button>
          <a-button @click="deleteRow"> 删除 </a-button>
        </a-form-item>
      </a-col>
    </a-row>
    <a-table
      :row-selection="{
        selectedRowKeys: selectedRowKeys,
        onChange: onSelectChange,
      }"
      ref="tableInfo"
      :columns="columns"
      :loading="loading"
      :row-key="(record) => record.id"
      :data-source="dataSource"
      :pagination="pagination"
      @change="handleTableChange"
      :scroll="{ x: 1200, y: 500 }"
    >
      <a
        slot="operation"
        slot-scope="text, record"
        href="javascript:;"
        @click="editRow(record, 'editBtn')"
        >编辑</a
      >
    </a-table>
    <a-modal
      class="ant-modal-nofooter"
      v-model="coreVisible"
      :title="btnType === 'addBtn' ? '新增' : '修改'"
      @ok="handleOk"
    >
      <a-form-model
        ref="coreRules"
        :model="formData"
        :rules="coreRules"
        :label-col="labelCol"
        :wrapper-col="wrapperCol"
      >
        <a-form-model-item label="员工姓名" prop="employeename">
          <Select
            :keyword="formData.employeename"
            :disabledName="disabledName"
            url="system/employee?search="
            @getSearchValue="getEditSearchValue"
          ></Select>
        </a-form-model-item>
        <a-form-model-item label="开始时间" prop="startdate">
          <a-date-picker
            v-model="formData.startdate"
            :format="dateFormat"
            :valueFormat="dateFormat"
          />
        </a-form-model-item>
        <a-form-model-item label="结束时间" prop="enddate">
          <a-date-picker
            v-model="formData.enddate"
            :format="dateFormat"
            :valueFormat="dateFormat"
          />
        </a-form-model-item>
        <a-form-model-item label="备注" prop="memo">
          <a-input v-model="formData.memo" allowClear />
        </a-form-model-item>
        <a-form-model-item class="injuryJob-footer">
          <a-button @click="resetForm"> 重置 </a-button>
          <a-button style="margin-left: 20px" type="primary" @click="onSubmit">
            确认
          </a-button>
        </a-form-model-item>
      </a-form-model>
    </a-modal>
  </a-card>
</template>
<script>
import { mapGetters } from "vuex";
import Select from "../common/Select";

export default {
  name: "InjuryJob",
  components: { Select },
  data() {
    return {
      dateFormat: "YYYY-MM-DD",
      coreVisible: false,
      disabledName: false,
      labelCol: { span: 6 },
      wrapperCol: { span: 14 },
      other: "",
      btnType: "addBtn",
      queryParams: {
        keyword: null,
        employeeid: null,
      },
      loading: false,
      selectedRowKeys: [],
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
      formData: {
        employeeid: null,
        employeename: null,
        enddate: null,
        startdate: null,
        memo: null,
      },
      coreRules: {
        employeename: [
          { required: true, message: "请输入姓名", trigger: "change" },
        ],
        startdate: [
          { required: true, message: "请输入开始时间", trigger: "change" },
        ],
        enddate: [
          { required: true, message: "请输入结束时间", trigger: "change" },
        ],
      },
    };
  },
  computed: {
    ...mapGetters(["zidianInfo"]),
    columns() {
      return [
        {
          title: "人员编号",
          dataIndex: "employeecode",
          width: 120,
          scopedSlots: { customRender: "employeecode" },
        },
        {
          title: "姓名",
          dataIndex: "employeename",
          width: 100,
          scopedSlots: { customRender: "employeename" },
        },
        {
          title: "科室",
          dataIndex: "deptname",
          width: 120,
          scopedSlots: { customRender: "deptname" },
          // customRender: (text, row, index) => {
          //     if (!text) return ''
          //     let option = dept.filter(item => item.keyy == text)[0]
          //     return option ? option.valuee : ''
          // },
        },
        {
          title: "人员类型",
          dataIndex: "employeetypename",
          width: 120,
          scopedSlots: { customRender: "employeetypename" },
        },
        {
          title: "人员状态",
          dataIndex: "statusname",
          width: 120,
          scopedSlots: { customRender: "statusname" },
        },
        {
          title: "人事子范围",
          dataIndex: "rszfwname",
          width: 120,
          scopedSlots: { customRender: "rszfwname" },
        },
        {
          title: "开始时间",
          dataIndex: "startdate",
          width: 150,
          scopedSlots: { customRender: "startdate" },
        },
        {
          title: "结束时间",
          dataIndex: "enddate",
          width: 150,
          scopedSlots: { customRender: "enddate" },
        },
        {
          title: "备注",
          dataIndex: "memo",
          width: 150,
          scopedSlots: { customRender: "memo" },
        },
        {
          title: "操作",
          dataIndex: "operation",
          scopedSlots: { customRender: "operation" },
          fixed: "right",
          width: 80,
        },
      ];
    },
  },

  mounted() {
    this.fetch({
      ...this.formData,
    });
  },
  methods: {
    birthdayChange(date) {
      this.formData.birth = date;
      console.log(date);
    },
    getSearchValue({ value }) {
      this.queryParams.employeeid = this.queryParams.keyword = value;
      this.fetch({
        ...this.queryParams,
      });
    },
    getEditSearchValue({ value }) {
      this.formData.employeename = this.formData.employeeid = value;
    },
    clickAdd(type) {
      this.btnType = type;
      this.coreVisible = true;
      this.disabledName = false;
      this.queryParams.keyword = null;
      this.resetForm();
    },
    editRow(record, type) {
      this.btnType = type;
      this.coreVisible = true;
      this.disabledName = true;
      this.formData = { ...this.formData, ...record };
    },
    onSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys;
    },
    deleteRow() {
      if (this.selectedRowKeys.length === 0) {
        this.$message.warning("请勾选您要删除的记录！");
        return false;
      } else {
        let that = this;
        this.$confirm({
          title: "确定删除选中记录吗?",
          content: "",
          onOk() {
            that
              .$delete("system/kq/gs/delete/" + that.selectedRowKeys.toString())
              .then((res) => {
                if (res.data.status === "OK") {
                  that.$message.success("删除成功！");
                  that.selectedRowKeys = [];
                  that.queryParams.keyword = null;
                  that.fetch({
                    ...that.queryParams,
                  });
                }
              })
              .catch((err) => {
                that.$message.warning(err);
              });
          },
          onCancel() {
            that.selectedRowKeys = [];
            that.$message.warning("取消删除！");
          },
        });
      }
    },
    handleOk() {
      this.coreVisible = false;
    },
    onSubmit() {
      this.$refs.coreRules.validate((valid) => {
        if (valid) {
          let params = { ...this.formData };
          params.id = this.btnType === "addBtn" ? null : this.formData.id;
          let http = this.btnType === "addBtn" ? this.$post : this.$put;
          http("/system/kq/gs", { ...params })
            .then((res) => {
              if (res.data.status === "OK") {
                this.fetch({
                  ...this.queryParams,
                });
                this.resetForm();
                this.coreVisible = false;
                this.$message.success(
                  this.btnType === "addBtn" ? "新增成功！" : "修改成功！"
                );
              }
            })
            .catch((err) => {
              this.$message.warning(err);
            });
        } else {
          return false;
        }
      });
    },
    handleTableChange(pagination, filters, sorter) {
      this.paginationInfo = pagination;
      this.fetch({
        ...this.queryParams,
      });
    },
    handleSubmit() {
      this.fetch({
        ...this.queryParams,
      });
    },
    // 列表查询
    fetch(params = {}) {
      this.loading = true;
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
      this.$get("system/kq/gs", { ...params })
        .then((res) => {
          if (res.data) {
            let data = res.data;
            const pagination = { ...this.pagination };
            pagination.total = data.total;
            this.dataSource = data.rows;
            this.pagination = pagination;
            this.loading = false;
          } else {
            this.loading = false;
          }
        })
        .catch((err) => {
          this.$message.warning(err);
          this.loading = false;
        });
    },
    resetForm() {
      this.formData.employeeid = null;
      this.formData.employeename = null;
      this.formData.startdate = null;
      this.formData.enddate = null;
      this.formData.memo = null;
    },
  },
};
</script>
<style lang="less" scoped>
.injuryJob {
  .injuryJob-top {
    margin-bottom: 5px;
    .ant-select {
      width: 100%;
    }
  }
  .editable-add-btn {
    margin-right: 10px;
  }
}
.flex {
  display: flex;
  align-items: center;
}
.w-120 {
  width: 120px !important;
}
</style>
<style lang="less">
@import "../../../static/less/Common";
.injuryJob-footer {
  text-align: right;
  margin-top: 20px;
}
.ant-modal-nofooter {
  .ant-modal-footer {
    display: none;
  }
  .ant-calendar-picker {
    width: 100%;
  }
}
.injuryJob .ant-form-item {
  width: 100%;
  margin-bottom: 0 !important;
}
.injuryJob .form-item .ant-col {
  width: 100%;
  text-align: right;
  margin-left: 0;
}
</style>
