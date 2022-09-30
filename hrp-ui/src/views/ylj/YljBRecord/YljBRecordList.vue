<template>
  <a-card :bordered="false" class="card-area">
    <div :class="advanced ? 'search' : null">
      <a-form layout="horizontal">
        <a-row>
          <div :class="advanced ? null : 'fold'">
            <a-col :md="8" :sm="24">
              <a-form-item label="姓名" v-bind="formItemLayout">
                <a-input v-model="queryParams.userName" />
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="开始年月"
                prop="startdate"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-month-picker
                  v-model="queryParams.startdate"
                  :format="dateFormat"
                  :valueFormat="dateFormat"
                  @change="startdateChange"
                />
              </a-form-item>
            </a-col>
         
            <a-col :md="8" :sm="24" >
              <a-form-item
                label="结束年月"
                prop="enddate"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-month-picker
                  v-model="queryParams.enddate"
                  :format="dateFormat"
                  :valueFormat="dateFormat"
                  @change="enddateChange"
                />
              </a-form-item>
            </a-col>
         
          </div>
          <span style="float: right; margin-top: 3px">
            <a-button type="primary" @click="search">查询</a-button>
            <a-button style="margin-left: 8px" @click="reset">重置</a-button>
            <a @click="toggleAdvanced" style="margin-left: 8px">
              {{ advanced ? "收起" : "展开" }}
              <a-icon :type="advanced ? 'up' : 'down'" />
            </a>
          </span>
        </a-row>
      </a-form>
    </div>
    <div>
      <div class="operator">
         <a-upload
                accept=".xls,.xlsx"
                :fileList="fileList"
                :beforeUpload="beforeUpload"
                @change="handleChangeFile"
              >
                <a-button>
                  <a-icon type="upload" /> 上传养老金数据
                </a-button>
              </a-upload>
               <a-button
            @click="exportExcel"
          >
            导出
          </a-button>
      </div>
      <!-- 表格区域 -->
      <a-table
        ref="TableInfo"
        :columns="columns"
        :rowKey="(record) => record.id"
        :dataSource="dataSource"
        :pagination="pagination"
        :loading="loading"
        :rowSelection="{
          selectedRowKeys: selectedRowKeys,
          onChange: onSelectChange,
        }"
        @change="handleTableChange"
        :bordered="bordered"
        :scroll="{ x: 2000 }"
      >
        <template slot="remark" slot-scope="text, record">
          <a-popover placement="topLeft">
            <template slot="content">
              <div style="max-width: 200px">{{ text }}</div>
            </template>
            <p style="width: 200px; margin-bottom: 0">{{ text }}</p>
          </a-popover>
        </template>
      </a-table>
    </div>
  
  </a-card>
</template>

<script>

const formItemLayout = {
  labelCol: { span: 8 },
  wrapperCol: { span: 15, offset: 1 },
};
export default {
  name: "YljBRecord",
  components: {  },
  data() {
    return {
      advanced: false,
      dataSource: [],
      selectedRowKeys: [],
      sortedInfo: null,
      paginationInfo: null,
      formItemLayout,
      pagination: {
        pageSizeOptions: ["10", "20", "30", "40", "100"],
        defaultCurrent: 1,
        defaultPageSize: 10,
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) =>
          `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`,
      },
      queryParams: {},
      addVisiable: false,
      editVisiable: false,
      // 表单设置
      dateFormat: 'YYYY-MM',
      loading: false,
      bordered: true,
      fileList: []
    };
  },
  computed: {
    columns() {
      let { sortedInfo } = this;
      sortedInfo = sortedInfo || {};
      return [
        {
          title: "个人编号",
          dataIndex: "grbh",
          width: 100,
        },
        {
          title: "姓名",
          dataIndex: "userName",
          width: 100,
        },
        {
          title: "公民身份证号码",
          dataIndex: "idCard",
          width: 100,
        },
        {
          title: "费款所属期",
          dataIndex: "fkssq",
          width: 100,
        },
        {
          title: "开始年月",
          dataIndex: "startDate",
          width: 100,
        },
        {
          title: "终止年月",
          dataIndex: "endDate",
          width: 100,
        },
        {
          title: "缴费类型",
          dataIndex: "jflx",
          width: 100,
        },
        {
          title: "缴费基数合计",
          dataIndex: "jfjshj",
          width: 100,
        },
        {
          title: "养老单位缴费",
          dataIndex: "yldwjf",
          width: 100,
        },
        {
          title: "养老个人缴费",
          dataIndex: "ylgrjf",
          width: 100,
        },
        {
          title: "养老单位利息",
          dataIndex: "yldwlx",
          width: 100,
        },
        {
          title: "养老个人利息",
          dataIndex: "ylgrlx",
          width: 100,
        },
        {
          title: "养老小计",
          dataIndex: "ylxj",
          width: 100,
        },
        {
          title: "年金单位缴费虚账",
          dataIndex: "njdwjfxz",
          width: 100,
        },
        {
          title: "年金单位缴费实账",
          dataIndex: "njdwjfsz",
          width: 100,
        },
        {
          title: "年金个人缴费",
          dataIndex: "njgrjf",
          width: 100,
        },
        {
          title: "单位虚账利息",
          dataIndex: "dwxzxl",
          width: 100,
        },
        {
          title: "单位实账利息",
          dataIndex: "dwszlx",
          width: 100,
        },
        {
          title: "个人利息",
          dataIndex: "grlx",
          width: 100,
        },
        {
          title: "做实金额",
          dataIndex: "zsje",
          width: 100,
        },
        {
          title: "年金小计",
          dataIndex: "njxj",
          width: 100,
        },
        {
          title: "单位应缴合计",
          dataIndex: "dwyjhj",
          width: 100,
        },
        {
          title: "个人应缴合计",
          dataIndex: "gryjhj",
          width: 100,
        },
        {
          title: "总计",
          dataIndex: "zj",
          width: 100,
        }
      ];
    },
  },
  mounted() {
    this.fetch();
  },
  methods: {
    startdateChange (date) {
      this.queryParams.startDate = date
    },
    enddateChange (date) {
      this.queryParams.endDate = date
    },
    onSelectChange(selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys;
    },
    toggleAdvanced() {
      this.advanced = !this.advanced;
      if (!this.advanced) {
        this.queryParams.comments = "";
      }
    },
    handleChangeFile (info) {
      if (info.file.status === 'uploading') {
        this.handleUpload()
      }
    },
    handleRemove (file) {
      this.fileList = []
    },
    beforeUpload (file) {
      console.info(file.type)
      const isJPG = (file.type==='application/vnd.ms-excel' ||file.type === 'application/x-excel' || file.type ==='application/vnd.openxmlformats-officedocument.spreadsheetml.sheet')
      //console.info(file.type)
      if (!isJPG) {
        this.$message.error('请只上传excel文件!')
      }
      const isLt2M = file.size / 1024 / 1024 < 4
      if (!isLt2M) {
        this.$message.error('附件必须小于 4MB!')
      }
      if (isJPG && isLt2M) {
        this.fileList = [...this.fileList, file]
      }
      return isJPG && isLt2M
    },
    handleUpload () {
      const { fileList } = this
      const formData = new FormData()
      formData.append('file', fileList[0])
      // You can use any AJAX library you like
      this.$upload('yljBRecord/importYlj', formData).then((r) => {
        this.$message.success('上传成功.')
      }).catch(() => {
        this.$message.error('上传失败.')
      })
      this.fileList =[]
    },
    batchDelete() {
      if (!this.selectedRowKeys.length) {
        this.$message.warning("请选择需要删除的记录");
        return;
      }
      let that = this;
      this.$confirm({
        title: "确定删除所选中的记录?",
        content: "当您点击确定按钮后，这些记录将会被彻底删除",
        centered: true,
        onOk() {
          let yljBRecordIds = that.selectedRowKeys.join(",");
          that.$delete("yljBRecord/" + yljBRecordIds).then(() => {
            that.$message.success("删除成功");
            that.selectedRowKeys = [];
            that.search();
          });
        },
        onCancel() {
          that.selectedRowKeys = [];
        },
      });
    },
    exportExcel() {
      let { sortedInfo } = this;
      let sortField, sortOrder;
      // 获取当前列的排序和列的过滤规则
      if (sortedInfo) {
        sortField = sortedInfo.field;
        sortOrder = sortedInfo.order;
      }
      this.$export("yljBRecord/excel", {
        pageSize: 10000,
        page: 1,
        sortField: sortField,
        sortOrder: sortOrder,
        ...this.queryParams,
      });
    },
    search() {
      let { sortedInfo } = this;
      let sortField, sortOrder;
      // 获取当前列的排序和列的过滤规则
      if (sortedInfo) {
        sortField = sortedInfo.field;
        sortOrder = sortedInfo.order;
      }
      this.fetch({
        sortField: sortField,
        sortOrder: sortOrder,
        ...this.queryParams,
      });
    },
    reset() {
      // 取消选中
      this.selectedRowKeys = [];
      // 重置分页
      this.$refs.TableInfo.pagination.current = this.pagination.defaultCurrent;
      if (this.paginationInfo) {
        this.paginationInfo.current = this.pagination.defaultCurrent;
        this.paginationInfo.pageSize = this.pagination.defaultPageSize;
      }
      // 重置列排序规则
      this.sortedInfo = null;
      this.paginationInfo = null;
      // 重置查询参数
      this.queryParams = {};
      this.fetch();
    },
    handleTableChange(pagination, filters, sorter) {
      this.sortedInfo = sorter;
      this.paginationInfo = pagination;
      this.fetch({
        sortField: sorter.field,
        sortOrder: sorter.order,
        ...this.queryParams,
      });
    },
    fetch(params = {}) {
      this.loading = true;
      if (this.paginationInfo) {
        // 如果分页信息不为空，则设置表格当前第几页，每页条数，并设置查询分页参数
        this.$refs.TableInfo.pagination.current = this.paginationInfo.current;
        this.$refs.TableInfo.pagination.pageSize = this.paginationInfo.pageSize;
        params.pageSize = this.paginationInfo.pageSize;
        params.pageNum = this.paginationInfo.current;
      } else {
        // 如果分页信息为空，则设置为默认值
        params.pageSize = this.pagination.defaultPageSize;
        params.pageNum = this.pagination.defaultCurrent;
      }
      this.$get("yljBRecord", {
        ...params,
      }).then((r) => {
        let data = r.data;
        const pagination = { ...this.pagination };
        pagination.total = data.total;
        this.loading = false;
        this.dataSource = data.rows;
        this.pagination = pagination;
      });
    },
  },
};
</script>

<style lang="less" scoped>
@import "../../../../static/less/Common";
</style>
