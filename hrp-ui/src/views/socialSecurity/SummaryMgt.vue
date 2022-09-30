<!--社保汇总报表-->
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
        <!--<a-button style="margin-left: 15px;" @click="exportFile"><a-icon type="download" /> 导出 </a-button>-->
      </a-form-model-item>
    </a-form-model>
    <a-table
      ref="tableInfo"
      :columns="columns"
      :loading="tableLoading"
      :row-key="record => record.cdeptnum"
      :data-source="dataSource"
      :pagination=false
      :scroll="{ x: 1500, y: 500 }"
    >
    </a-table>
  </a-card>
</template>
<script>
import moment from 'moment'
export default {
  name: 'SocialSecuritySummaryMgt',
  data () {
    return {
      // 表单设置
      dateFormat: 'YYYY/MM',
      // 加载状态
      tableLoading: false,
      // 查询条件
      formData: {
        period: moment(new Date().toLocaleDateString()).format('YYYY/MM')
      },
      dataSource: [],
    }
  },
  watch: {},
  computed: {
    columns () {
      return [
        {
          title: '部门',
          dataIndex: 'cdeptnum',
          width: 120,
          scopedSlots: { customRender: 'cdeptnum' }
        },
        {
          title: '大额医疗个人',
          dataIndex: 'deylgrjn',
          width: 150,
          scopedSlots: { customRender: 'deylgrjn' }
        },
        {
          title: '工伤单位',
          dataIndex: 'gsdwjn',
          width: 150,
          scopedSlots: { customRender: 'gsdwjn' }
        },
        {
          title: '公务员单位',
          dataIndex: 'bzyldw',
          width: 150,
          scopedSlots: { customRender: 'bzyldw' }
        },
        {
          title: '生育单位',
          dataIndex: 'shengyudw',
          width: 150,
          scopedSlots: { customRender: 'shengyudw' }
        },
        {
          title: '失业个人',
          dataIndex: 'shiygrbj',
          width: 150,
          scopedSlots: { customRender: 'shiygrbj' }
        },
        {
          title: '失业单位',
          dataIndex: 'shiydw',
          width: 150,
          scopedSlots: { customRender: 'shiydw' }
        },
        {
          title: '养老个人',
          dataIndex: 'yanglgr',
          width: 120,
          scopedSlots: { customRender: 'yanglgr' }
        },

        {
          title: '养老单位',
          dataIndex: 'yangldw',
          width: 150,
          scopedSlots: { customRender: 'yangldw' }
        },
        {
          title: '医疗个人',
          dataIndex: 'yilgrjn',
          width: 150,
          scopedSlots: { customRender: 'yilgrjn' }
        },
        {
          title: '医疗单位',
          dataIndex: 'yildw',
          width: 150,
          scopedSlots: { customRender: 'yildw' }
        },
      ]
    }
  },
  mounted () {
  },
  methods: {
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
      this.$export('system/sb/export', {
        ...this.formData
      })
    },
    // 带条件的查询
    fetch (params = {}) {
      this.tableLoading = true
      this.$get('system/cwgz/getSbSum', {...params}).then(res => {
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
