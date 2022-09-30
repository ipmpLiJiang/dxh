<!--取得职称与聘任职称分布报表-->
<template>
  <a-card :bordered="false" class="card-area socialSecurityMgt">
    <a-form-model ref="formData" layout="inline" :model="formData">
      <a-form-model-item label="科室">
        <a-select v-model="formData.deptids" mode="multiple" style="width:200px" allowClear :filter-option="filterOption">
          <a-select-option v-for="item in zidianInfo.dept" :value="item.keyy" :key="item.keyy">
            {{item.valuee}}
          </a-select-option>
        </a-select>
      </a-form-model-item>
      <a-form-model-item
        label="日期">
        <a-month-picker
          v-model="formData.startdate"
          :format="dateFormat"
          style="width:100px"
          :valueFormat="dateFormat"
          @change="startChange"
        />
      </a-form-model-item>
      <a-form-model-item label="人员类型">
        <a-select v-model="formData.employeetypes" mode="multiple" style="width:120px" allowClear :filter-option="filterOption">
          <a-select-option v-for="item in zidianInfo.employeetype" :value="item.keyy" :key="item.keyy">
            {{item.valuee}}
          </a-select-option>
        </a-select>
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
      bordered
      :columns="columns"
      :loading="tableLoading"
      :row-key="record => record.joblevelname"
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
  name: 'EmployeeJobLevelTitleGetReport',
  data () {
    return {
      // 表单设置
      dateFormat: 'YYYY-MM',
      // 加载状态
      tableLoading: false,
      pagination: {
        defaultPageSize: 20
      },
      // 查询条件
      formData: {
        startdate: moment(new Date().toLocaleDateString()).format('YYYY-MM'),
        employeetype: '',
        dept: '',
      },
      dataSource: []
    }
  },
  watch: {},
  computed: {
    ...mapGetters(['zidianInfo']),
    columns () {
      let { dept, employeetype } = this.zidianInfo
      return [{
          title: '聘任岗级|取得职称',
          children: [
            {
              title: '类别',
              dataIndex: 'joblevelname',
              width: 120
            }
          ]
        },
        {
          title: '小计',
          dataIndex: 'xj',
          width: 120
        },
        {
          title: '员级',
          children: [
            {
              title: '医师',
              dataIndex: 'yjys',
              width: 100
            },
            {
              title: '护理',
              dataIndex: 'yjhl',
              width: 100
            },
            {
              title: '医技',
              dataIndex: 'yjyj',
              width: 100
            },
            {
              title: '其他专技人员',
              dataIndex: 'yjqt',
              width: 120
            }
          ]
        },
        {
          title: '初级',
          children: [
            {
              title: '医师',
              dataIndex: 'cjys',
              width: 100
            },
            {
              title: '护理',
              dataIndex: 'cjhl',
              width: 100
            },
            {
              title: '医技',
              dataIndex: 'cjyj',
              width: 100
            },
            {
              title: '其他专技人员',
              dataIndex: 'cjqt',
              width: 120
            }
          ]
        },
        {
          title: '中级',
          children: [
            {
              title: '医师',
              dataIndex: 'zjys',
              width: 100
            },
            {
              title: '护理',
              dataIndex: 'zjhl',
              width: 100
            },
            {
              title: '医技',
              dataIndex: 'zjyj',
              width: 100
            },
            {
              title: '其他专技人员',
              dataIndex: 'zjqt',
              width: 120
            }
          ]
        },
        {
          title: '副高',
          children: [
            {
              title: '医师',
              dataIndex: 'fgys',
              width: 100
            },
            {
              title: '护理',
              dataIndex: 'fghl',
              width: 100
            },
            {
              title: '医技',
              dataIndex: 'fgyj',
              width: 100
            },
            {
              title: '其他专技人员',
              dataIndex: 'fgqt',
              width: 120
            }
          ]
        },
        {
          title: '正高',
          children: [
            {
              title: '医师',
              dataIndex: 'zgys',
              width: 100
            },
            {
              title: '护理',
              dataIndex: 'zghl',
              width: 100
            },
            {
              title: '医技',
              dataIndex: 'zgyj',
              width: 100
            },
            {
              title: '其他专技人员',
              dataIndex: 'zgqt',
              width: 120
            }
          ]
        },
        {
          title: '未定',
          children: [
            {
              title: '医师',
              dataIndex: 'wdys',
              width: 100
            },
            {
              title: '护理',
              dataIndex: 'wdhl',
              width: 100
            },
            {
              title: '医技',
              dataIndex: 'wdyj',
              width: 100
            },
            {
              title: '其他专技人员',
              dataIndex: 'wdqt',
              width: 120
            }
          ]
        },
      ]
    }
  },
  mounted () {
  },
  methods: {
    ...mapActions(),
    startChange (date) {
      this.formData.startdate = date
    },
    filterOption (input, option) {
      return (
        option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      )
    },
    // 查询
    handleSubmit () {
      if (!this.formData.startdate) {
        this.$message.warning('日期必填！')
        return
      }
      this.fetch({
        ...this.formData
      })
    },
    // 导出
    exportFile () {
      let startdate = this.formData.startdate
      this.formData.startdate = startdate + '-01'
      this.$export('system/employee/exportEmployeeJobLevelTitleGet', {
        ...this.formData
      })
      this.formData.startdate = startdate
    },
    // 带条件的查询
    fetch (params = {}) {
      this.tableLoading = true
      params.startdate = params.startdate + '-01'
      this.$get('system/employee/employeeJobLevelTitleGetReportList', {...params}).then(res => {
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
