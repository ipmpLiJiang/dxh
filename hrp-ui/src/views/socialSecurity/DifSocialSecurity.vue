<!--差异明细报表-->
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
        <a-button style="margin-left: 15px;" @click="exportFile" ><a-icon type="download" /> 导出 </a-button>
      </a-form-model-item>
    </a-form-model>
    <a-table
      ref="tableInfo"
      :columns="columns"
      :loading="tableLoading"
      :row-key="record => record.id"
      :data-source="dataSource"
      :pagination=false
      :scroll="{ x: 1500, y: 500 }"
    >
    </a-table>
  </a-card>
</template>
<script>
import { mapActions, mapGetters } from 'vuex'
import moment from 'moment'
export default {
  name: 'DifSocialSecurity',
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
      dataSource: [],
      bxtypeOptions: [
        {keyy: 1, valuee: '养老'},
        {keyy: 2, valuee: '医疗'},
        {keyy: 3, valuee: '失业'},
        {keyy: 4, valuee: '工伤'},
        {keyy: 5, valuee: '生育'},
        {keyy: 6, valuee: '大额医疗'},
        {keyy: 7, valuee: '补助医疗'}
      ],
      type: [
        {keyy: 1, valuee: '新增'},
        {keyy: 2, valuee: '减少'},
        {keyy: 3, valuee: '补缴'},
      ]
    }
  },
  watch: {},
  computed: {
    ...mapGetters([]),
    columns () {
      return [
        {
          title: '险种',
          dataIndex: 'bxtype',
          width: 80,
          customRender: (text, row, index) => {
            let option = this.bxtypeOptions.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: '类型',
          dataIndex: 'type',
          width: 80,
          scopedSlots: { customRender: 'type' }
        },
        {
          title: '姓名',
          dataIndex: 'employeename',
          width: 100,
          scopedSlots: { customRender: 'employeename' }
        },
        {
          title: '社保个人编号',
          dataIndex: 'socialnum',
          width: 100,
          scopedSlots: { customRender: 'socialnum' }
        },
        {
          title: '审核日期',
          dataIndex: 'shdate',
          width: 120,
          scopedSlots: { customRender: 'shdate' }
        },
        {
          title: '缴纳日期',
          dataIndex: 'paydate',
          width: 120,
          scopedSlots: { customRender: 'paydate' }
        }
      ]
    }
  },
  mounted () {
  },
  methods: {
    ...mapActions([]),
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
      this.$export('system/sb/export/DifferentReport', {
        ...this.formData
      })
    },
    // 带条件的查询
    fetch (params = {}) {
      this.tableLoading = true
      this.$get('system/sb/getDifferentReport', {...params}).then(res => {
        if (res.data) {
          this.dataSource = res.data
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
