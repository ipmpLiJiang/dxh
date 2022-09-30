<template>
    <div class="financialPush">
        <a-row class="injuryJob-top" type="flex" justify="space-between">
            <a-col :md="8" :sm="24">
                <a-form-item
                    :labelCol="{span: 7}"
                    :wrapperCol="{span: 16, offset: 1}"
                    label="缴纳年月"
                >
                    <a-month-picker
                        v-model="formData.period"
                        :format="dateFormat"
                        :valueFormat="dateFormat"
                        @change="onSeach"
                    />
                </a-form-item>
            </a-col>
        </a-row>
      <div>
        <div class="operator">
          <a-button @click="exportExcel">导出</a-button>
        </div>
        <a-table
          ref="tableInfo"
          :columns="columns"
          :loading="loading"
          :row-key="record => record.cPsnNum"
          :data-source="dataSource"
          :pagination="false"
          :scroll="{ x: 1200, y: 500 }"
        >
        </a-table>
      </div>
    </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  import moment from 'moment'
  export default {
    name: "SbForward",
    data() {
      return {
        dateFormat: "YYYY/MM",
        formData: {
          period: moment(new Date()).format('YYYY/MM'),
        },
        dataSource: [],
      }
    },
    computed: {
      ...mapGetters(['zidianInfo']),
      columns () {
        let { employeetype, dept } = this.zidianInfo
        return [
          {
            title: '人员编号',
            dataIndex: 'cpsnnum',
            width: 120,
            scopedSlots: { customRender: 'cpsnnum' }
          },
          {
            title: '姓名',
            dataIndex: 'cpsnname',
            width: 100,
            scopedSlots: { customRender: 'cpsnname' }
          },
          {
            title: '科室',
            dataIndex: 'cdeptnum',
            width: 120,
            customRender: (text, row, index) => {
              if (text==null) return ''
              let option = dept.filter(item => item.keyy == text)[0]
              return option ? option.valuee : ''
            },
          },
          {
            title: '人员类型',
            dataIndex: 'rpersontype',
            width: 120,
            customRender: (text, row, index) => {
              if (text==null) return ''
              let option = employeetype.filter(item => item.keyy == text)[0]
              return option ? option.valuee : ''
            },
          },
          {
            title: '社保个人编号',
            dataIndex: 'socialnum',
            width: 120,
            scopedSlots: { customRender: 'socialnum' }
          },
          {
            title: '身份证号',
            dataIndex: 'idnum',
            width: 120,
            scopedSlots: { customRender: 'idnum' }
          },
          {
            title: '大额医疗',
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
            width: 120,
            scopedSlots: { customRender: 'shengyudw' }
          },
          {
            title: '失业个人',
            dataIndex: 'Shiygrbj',
            width: 150,
            scopedSlots: { customRender: 'Shiygrbj' }
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
            width: 150,
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
          {
            title: '操作',
            dataIndex: 'operation',
            scopedSlots: {customRender: 'operation'},
            fixed: 'right',
            width: 80
          }
        ]

      }
    },
    mounted() {
      this.fetch()
    },
    methods: {
      onSeach() {
        this.fetch()
      },
      // 列表查询
      fetch () {
        this.loading = true
        this.$get('system/cwgz/getSb', {...this.formData}).then(res => {
          if (res.data) {
            this.dataSource = res.data
            this.loading = false
          } else {
            this.loading = false
          }
        }).catch(err => {
          this.$message.warning(err)
          this.loading = false
        })
      },
      exportExcel () {
          this.$export('/system/cwgz/export', {
              ...this.formData
          })
      },
    },
  }
</script>

<style lang="less" scoped>
  .financialPush {
    width: 100%;
    padding: 20px 0 0 20px;
    .operator {
      margin-bottom: 20px;
      }
    }
</style>