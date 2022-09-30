<!--年龄职称分布报表-->
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
      :row-key="record => record.rszfwname"
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
  name: 'EmployeeAgeTitleReport',
  data () {
    return {
      // 表单设置
      dateFormat: 'YYYY-MM',
      // 加载状态
      tableLoading: false,
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
          title: '项目',
          children: [
            {
              title: '人事子范围',
              dataIndex: 'rszfwname',
              width: 120
            }
          ]
        },
        {
          title: '员级',
          children: [
            {
              title: '25岁以下',
              dataIndex: 'yj25x',
              width: 100
            },
            {
              title: '26-30岁',
              dataIndex: 'yj30',
              width: 100
            },
            {
              title: '31-35岁',
              dataIndex: 'yj35',
              width: 100
            },
            {
              title: '36-40岁',
              dataIndex: 'yj40',
              width: 100
            },
            {
              title: '41岁及以上',
              dataIndex: 'yj41s',
              width: 120
            }
          ]
        },
        {
          title: '初级',
          children: [
            {
              title: '25岁以下',
              dataIndex: 'cj25x',
              width: 100
            },
            {
              title: '26-30岁',
              dataIndex: 'cj30',
              width: 100
            },
            {
              title: '31-35岁',
              dataIndex: 'cj35',
              width: 100
            },
            {
              title: '36-40岁',
              dataIndex: 'cj40',
              width: 100
            },
            {
              title: '41岁及以上',
              dataIndex: 'cj41s',
              width: 120
            }
          ]
        },
        {
          title: '中级',
          children: [
            {
              title: '25岁以下',
              dataIndex: 'zj25x',
              width: 100
            },
            {
              title: '26-30岁',
              dataIndex: 'zj30',
              width: 100
            },
            {
              title: '31-35岁',
              dataIndex: 'zj35',
              width: 100
            },
            {
              title: '36-40岁',
              dataIndex: 'zj40',
              width: 100
            },
            {
              title: '41岁及以上',
              dataIndex: 'zj41s',
              width: 120
            }
          ]
        },
        {
          title: '副高',
          children: [
            {
              title: '25岁以下',
              dataIndex: 'fg25x',
              width: 100
            },
            {
              title: '26-30岁',
              dataIndex: 'fg30',
              width: 100
            },
            {
              title: '31-35岁',
              dataIndex: 'fg35',
              width: 100
            },
            {
              title: '36-40岁',
              dataIndex: 'fg40',
              width: 100
            },
            {
              title: '41岁及以上',
              dataIndex: 'fg41s',
              width: 120
            }
          ]
        },
        {
          title: '正高',
          children: [
            {
              title: '25岁以下',
              dataIndex: 'zg25x',
              width: 100
            },
            {
              title: '26-30岁',
              dataIndex: 'zg30',
              width: 100
            },
            {
              title: '31-35岁',
              dataIndex: 'zg35',
              width: 100
            },
            {
              title: '36-40岁',
              dataIndex: 'zg40',
              width: 100
            },
            {
              title: '41岁及以上',
              dataIndex: 'zg41s',
              width: 120
            }
          ]
        },
        {
          title: '未定',
          children: [
            {
              title: '25岁以下',
              dataIndex: 'wd25x',
              width: 100
            },
            {
              title: '26-30岁',
              dataIndex: 'wd30',
              width: 100
            },
            {
              title: '31-35岁',
              dataIndex: 'wd35',
              width: 100
            },
            {
              title: '36-40岁',
              dataIndex: 'wd40',
              width: 100
            },
            {
              title: '41岁及以上',
              dataIndex: 'wd41s',
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
      this.$export('system/employee/exportEmployeeAgeTitle', {
        ...this.formData
      })
      this.formData.startdate = startdate
    },
    // 带条件的查询
    fetch (params = {}) {
      this.tableLoading = true
      params.startdate = params.startdate + '-01'
      this.$get('system/employee/employeeAgeTitleReportList', {...params}).then(res => {
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
