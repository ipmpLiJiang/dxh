<!--社保分析报表-->
<template>
  <a-card :bordered="false" class="card-area socialSecurityMgt">
    <a-form-model ref="formData" layout="inline" :model="formData">
      <a-form-model-item
        label="日期"
        :labelCol="{span: 7}"
        :wrapperCol="{span: 16, offset: 1}">
        <a-month-picker
          v-model="formData.period"
          :format="dateFormat"
          :valueFormat="dateFormat"
          @change="periodChange"
        />
      </a-form-model-item>
      <a-form-model-item>
        <a-button type="primary" @click="handleSubmit">
          查询
        </a-button>
        <a-button style="margin-left: 15px;" @click="exportFile"><a-icon type="download" /> 导出 </a-button>
      </a-form-model-item>
    </a-form-model>
    <a-table
      ref="tableInfo"
      :columns="columns"
      :loading="tableLoading"
      :row-key="record => record.idnumber"
      :pagination="pagination"
      :data-source="dataSource"
      :scroll="{ x: 1500, y: 650 }"
    >
    </a-table>
  </a-card>
</template>
<script>
import { mapActions, mapGetters } from 'vuex'
import moment from 'moment'
export default {
  name: 'SocialSecurityWts',
  data () {
    return {
      // 表单设置
      dateFormat: 'YYYY-MM',
      // 加载状态
      tableLoading: false,
      // 查询条件
      formData: {
        period: moment(new Date().toLocaleDateString()).format('YYYY-MM')
      },
      pagination: {
        pageSizeOptions: ['10', '20', '30', '40', '100'],
        showQuickJumper: true,
        showSizeChanger: true,
        showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
      },
      dataSource: []
    }
  },
  watch: {},
  computed: {
    ...mapGetters(['zidianInfo']),
    columns () {
      let { employeetype } = this.zidianInfo
      return [{
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
          title: '身份证号码',
          dataIndex: 'idnumber',
          width: 180
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
        { 
          title: '科室',
          dataIndex: 'deptname',
          // customRender: (text, row, index) => {
          //   let option = dept.filter(item => item.keyy == text)[0]
          //   return option ? option.valuee : ''
          // }
          width: 150,
        },
        {
          title: '人事子范围',
          dataIndex: 'rszfwName',
          // customRender: (text, row, index) => {
          //   if (!text) return ''
          //   let option = rszfw.filter(item => item.keyy == text)[0]
          //   return option ? option.valuee : ''
          // },
          width: 120
        },
        {
          title: '养老个人缴纳合计',
          dataIndex: 'yanglaoTotal',
          width: 120
        },
        {
          title: '医疗个人缴纳合计',
          dataIndex: 'ylTotal',
          width: 120
        },
        {
          title: '失业个人缴纳合计',
          dataIndex: 'syTotal',
          width: 120
        },
        {
          title: '大额医疗个人缴纳合计',
          dataIndex: 'deylTotal',
          width: 120
        },
        {
          title: '在编养老个人缴纳合计',
          dataIndex: 'zxylTotal',
          width: 120
        },
        {
          title: '在编年金缴纳合计',
          dataIndex: 'zxnjTotal',
          width: 120
        },
      ]
    }
  },
  mounted () {
  },
  methods: {
    ...mapActions(),
    periodChange (date) {
      this.formData.period = date
    },
    // 查询
    handleSubmit () {
      if (!this.formData.period) {
        this.$message.warning('日期必填！')
        return
      }
      this.fetch({
        ...this.formData
      })
    },
    // 导出
    exportFile () {
      this.$export('system/sbwts/export', {
        ...this.formData
      })
    },
    // 带条件的查询
    fetch (params = {}) {
      this.tableLoading = true
      this.$get('system/sbwts/getList', {...params}).then(res => {
        if (res.data.data) {
          this.dataSource = res.data.data
          this.tableLoading = false
        } else {
          this.tableLoading = false
        }
      }).catch(err => {
        this.$message.warning(err)
        this.tableLoading = false
      })
    }
  }
}
</script>
<style scoped lang="less">
  .socialSecurityMgt{
    width: 100%;
  }
</style>
