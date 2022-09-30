<!--员工基本情况表-->
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
      :row-key="record => record.id"
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
  name: 'EmployeeBirthEducationReport',
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
      pagination: {
        defaultPageSize: 50
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
              title: '人员类别',
              dataIndex: 'rylb',
              customRender: (text, record, index) => {
                const obj = {
                  children: text !== null ? text : "",
                  attrs: {},
                }
                obj.attrs.rowSpan = this.mergeCells(
                  text,
                  this.dataSource,
                  "rylb",
                  index
                )
                return obj
              },
              width: 150
            },
            {
              title: '聘任岗位',
              dataIndex: 'joblevelname',
              width: 120
            }
          ]
        },
        {
          title: '学历',
          children: [
            {
              title: '小计',
              dataIndex: 'xlxj',
              width: 100
            },
            {
              title: '大专及以下',
              dataIndex: 'xldzyx',
              width: 120
            },
            {
              title: '本科',
              dataIndex: 'xlbk',
              width: 100
            },
            {
              title: '硕士',
              dataIndex: 'xlss',
              width: 100
            },
            {
              title: '博士',
              dataIndex: 'xlbs',
              width: 120
            }
          ]
        },
        {
          title: '年龄',
          children: [
            {
              title: '35岁及以下',
              dataIndex: 'nl35x',
              width: 120
            },
            {
              title: '36-40岁',
              dataIndex: 'nl40',
              width: 100
            },
            {
              title: '41-45岁',
              dataIndex: 'nl45',
              width: 100
            },
            {
              title: '46-50岁',
              dataIndex: 'nl50',
              width: 120
            },
            {
              title: '51-54岁',
              dataIndex: 'nl54',
              width: 120
            },
            {
              title: '55-59岁',
              dataIndex: 'nl59',
              width: 120
            },
            {
              title: '60岁及以上',
              dataIndex: 'nl60s',
              width: 120
            }
          ]
        }
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
    mergeCells(text, data, key, index) {
      // 上一行该列数据是否一样
      if (index !== 0 && text === data[index - 1][key]) {
        return 0
      }
      let rowSpan = 1
      // 判断下一行是否相等
      for (let i = index + 1; i < data.length; i++) {
        if (text !== data[i][key]) {
          break
        }
        rowSpan++
      }
      return rowSpan
    },
    // 导出
    exportFile () {
      let startdate = this.formData.startdate
      this.formData.startdate = startdate + '-01'
      this.$export('system/employee/exportEmployeeBirthEducation', {
        ...this.formData
      })
      this.formData.startdate = startdate
    },
    // 带条件的查询
    fetch (params = {}) {
      this.tableLoading = true
      params.startdate = params.startdate + '-01'
      this.$get('system/employee/employeeBirthEducationReportList', {...params}).then(res => {
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
