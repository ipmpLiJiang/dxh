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
      :row-key="record => record.bxtype"
      :data-source="dataSource"
      :pagination=false
      :scroll="{ x: 1500, y: 650 }"
    >
    <template slot="xzcount" slot-scope="text, record, index">
      <a v-if="text !== 0" @click="getXz (record)">{{ text }}</a>
      <p v-else>{{ text }}</p>
    </template>
    <template slot="jscount" slot-scope="text, record, index">
      <a v-if="text !== 0" @click="getJs (record)">{{ text }}</a>
      <p v-else>{{ text }}</p>
    </template>
    <template slot="bjcount" slot-scope="text, record, index">
      <a v-if="text !== 0" @click="getBj (record)">{{ text }}</a>
      <p v-else>{{ text }}</p>
    </template>
    </a-table>
    <a-modal v-model="showVisible" :footer="null" :title="showTitle" @ok="hideShowModal">
      <a-row>
        <a-col :span="3">合计：</a-col>
        <a-col :span="10">养老单位缴纳：{{shwrScore(showDw)}}</a-col>
        <a-col :span="10">养老个人缴纳：{{shwrScore(showGr)}}</a-col>
      </a-row>
      <br>
      <a-table
        ref="tableInfo1"
        :row-key="record => record.id"
        size="small"
        :columns="columns1"
        :data-source="dataRySource"
      >
      </a-table>
    </a-modal>
  </a-card>
</template>
<script>
import { mapActions, mapGetters } from 'vuex'
import moment from 'moment'
export default {
  name: 'SocialSecurityAnsMgt',
  data () {
    return {
      showDw: 0,
      showGr: 0,
      showVisible: false,
      showTitle: '列表',
      // 表单设置
      dateFormat: 'YYYY-MM',
      // 加载状态
      tableLoading: false,
      // 查询条件
      formData: {
        period: moment(new Date().toLocaleDateString()).format('YYYY-MM')
      },
      dataSource: [],
      dataRySource: [],
      bxtypeOptions: [
        {keyy: 1, valuee: '养老'},
        {keyy: 2, valuee: '医疗'},
        {keyy: 3, valuee: '失业'},
        {keyy: 4, valuee: '工伤'},
        {keyy: 5, valuee: '生育'},
        {keyy: 6, valuee: '大额医疗'},
        {keyy: 7, valuee: '补助医疗'},
        {keyy: 8, valuee: '在编养老'},
        {keyy: 9, valuee: '在编年金'}
      ]
    }
  },
  watch: {},
  computed: {
    ...mapGetters([]),
    columns () {
      return [
        {
          title: '保险类型',
          dataIndex: 'bxtype',
          width: 120,
          customRender: (text, row, index) => {
            let option = this.bxtypeOptions.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        {
          title: '缴纳年月',
          dataIndex: 'shdate',
          width: 150,
          scopedSlots: { customRender: 'shdate' }
        },
        {
          title: '本月应缴人数',
          dataIndex: 'byyjcount',
          width: 150,
          scopedSlots: { customRender: 'byyjcount' }
        },
        {
          title: '上月应缴纳人数',
          dataIndex: 'syyjcount',
          width: 150,
          scopedSlots: { customRender: 'syyjcount' }
        },
        {
          title: '本月新增人数',
          dataIndex: 'xzcount',
          width: 150,
          scopedSlots: { customRender: 'xzcount' }
        },
        {
          title: '本月减少人数',
          dataIndex: 'jscount',
          width: 150,
          scopedSlots: { customRender: 'jscount' }
        },
        {
          title: '个人缴纳金额',
          dataIndex: 'gryjsum',
          width: 150,
          scopedSlots: { customRender: 'grsum' }
        },
        {
          title: '单位缴纳金额',
          dataIndex: 'dwyjsum',
          width: 120,
          scopedSlots: { customRender: 'dwsum' }
        },

        {
          title: '本月应缴总金额',
          dataIndex: 'yjsum',
          width: 150,
          scopedSlots: { customRender: 'yjsum' }
        },
        {
          title: '补缴总人数',
          dataIndex: 'bjcount',
          width: 150,
          scopedSlots: { customRender: 'bjcount' }
        },
        {
          title: '个人补缴金额',
          dataIndex: 'grbjsum',
          width: 150,
          scopedSlots: { customRender: 'grbjsum' }
        },
        {
          title: '单位补缴金额',
          dataIndex: 'dwbjsum',
          width: 150,
          scopedSlots: { customRender: 'dwbjsum' }
        },
        {
          title: '本月补缴总额',
          dataIndex: 'bjsum',
          width: 150,
          scopedSlots: { customRender: 'bjsum' }
        },
        {
          title: '本月缴纳总额',
          dataIndex: 'jnsum',
          width: 150,
          scopedSlots: { customRender: 'jnsum' }
        }
      ]
    },
    columns1 () {
      return [
        {
          title: '人事编码',
          dataIndex: 'employeecode',
          width: 150,
        },
        {
          title: '姓名',
          dataIndex: 'employeename'
        },
        {
          title: '养老单位缴纳',
          dataIndex: 'dw'
        },
        {
          title: '养老个人缴纳',
          dataIndex: 'gr'
        }
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
    shwrScore(score) {
      if(score!=null && score!=undefined && score!=0) {
        var result = parseFloat(score);
        result = Math.round(score * 100) / 100;
        score = result
        // score = score.toFixed(2)
      }
      return score
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
    hideShowModal () {
      this.showVisible = false
    },
    getXz (record) {
      if (!this.formData.period) {
        this.$message.warning('日期必填！')
        return
      }
  
      let url = null
      this.showTitle = '新增列表'
      let option = this.bxtypeOptions.filter(item => item.keyy == record.bxtype)[0]
      this.showTitle = option.valuee + this.showTitle
      if (record.bxtype === 1) {
        url = 'system/sb/yanglao/getXz'
      } else if (record.bxtype === 2) {
        url = 'system/sb-yl/getXz'
      } else if (record.bxtype === 3) {
        url = 'system/sb-sy/getXz'
      } else if (record.bxtype === 4) {
        url = 'system/sb-gs/getXz'
      } else if (record.bxtype === 5) {
        url = 'system/sb-shengyu/getXz'
      } else if (record.bxtype === 6) {
        url = 'system/sb-deyl/getXz'
      } else if (record.bxtype === 7) {
        url = 'system/sb/bzyl/getXz'
      } else if (record.bxtype === 8) {
        url = 'yljBRecord/getXz'
      } else if (record.bxtype === 9) {
        url = 'yljBRecord/getXz'
      }
      if (!url) {
        this.$message.warning('类型有误！')
        return
      }
      this.showVisible = true
      this.dataRySource = []
      this.showDw = 0
      this.showGr = 0
      this.$get(url + '?dtr=' + this.formData.period).then((r) => {
        if (r.data.data.success === 1) {
          this.dataRySource = r.data.data.data
          let dw = 0
          let gr = 0
          console.log(this.dataRySource)
          this.dataRySource.forEach((item,index,arr)=> {
            dw = dw + item.dw
            gr = gr + item.gr
          })
          this.showDw = dw
          this.showGr = gr
        } else {
          this.$message.warning('获取数据失败')
        }
      }).catch((e) => {
        console.error(e)
        this.$message.error('获取数据失败')
      })
    },
    getJs (record) {
      if (!this.formData.period) {
        this.$message.warning('日期必填！')
        return
      }
  
      let url = null
      this.showTitle = '减少列表'
      let option = this.bxtypeOptions.filter(item => item.keyy == record.bxtype)[0]
      this.showTitle = option.valuee + this.showTitle
      if (record.bxtype === 1) {
        url = 'system/sb/yanglao/getJs'
      } else if (record.bxtype === 2) {
        url = 'system/sb-yl/getJs'
      } else if (record.bxtype === 3) {
        url = 'system/sb-sy/getJs'
      } else if (record.bxtype === 4) {
        url = 'system/sb-gs/getJs'
      } else if (record.bxtype === 5) {
        url = 'system/sb-shengyu/getJs'
      } else if (record.bxtype === 6) {
        url = 'system/sb-deyl/getJs'
      } else if (record.bxtype === 7) {
        url = 'system/sb/bzyl/getJs'
      } else if (record.bxtype === 8) {
        url = 'yljBRecord/getJs'
      } else if (record.bxtype === 9) {
        url = 'yljBRecord/getJs'
      }
      if (!url) {
        this.$message.warning('类型有误！')
        return
      }
      this.showVisible = true
      this.dataRySource = []
      this.showDw = 0
      this.showGr = 0
      this.$get(url + '?dtr=' + this.formData.period).then((r) => {
        if (r.data.data.success === 1) {
          this.dataRySource = r.data.data.data
          let dw = 0
          let gr = 0
          console.log(this.dataRySource)
          this.dataRySource.forEach((item,index,arr)=> {
            dw = dw + item.dw
            gr = gr + item.gr
          })
          this.showDw = dw
          this.showGr = gr
        } else {
          this.$message.warning('获取数据失败')
        }
      }).catch((e) => {
        console.error(e)
        this.$message.error('获取数据失败')
      })
    },
    getBj (record) {
      if (!this.formData.period) {
        this.$message.warning('日期必填！')
        return
      }
  
      let url = null
      this.showTitle = '补缴列表'
      let option = this.bxtypeOptions.filter(item => item.keyy == record.bxtype)[0]
      this.showTitle = option.valuee + this.showTitle
      if (record.bxtype === 1) {
        url = 'system/sb/yanglao/getBj'
      } else if (record.bxtype === 2) {
        url = 'system/sb-yl/getBj'
      } else if (record.bxtype === 3) {
        url = 'system/sb-sy/getBj'
      } else if (record.bxtype === 4) {
        url = 'system/sb-gs/getBj'
      } else if (record.bxtype === 5) {
        url = 'system/sb-shengyu/getBj'
      } else if (record.bxtype === 6) {
        url = 'system/sb-deyl/getBj'
      } else if (record.bxtype === 7) {
        url = 'system/sb/bzyl/getBj'
      } else if (record.bxtype === 8) {
        url = 'yljBRecord/getBj'
      } else if (record.bxtype === 9) {
        url = 'yljBRecord/getBj'
      }
      if (!url) {
        this.$message.warning('类型有误！')
        return
      }
      this.showVisible = true
      this.dataRySource = []
      this.showDw = 0
      this.showGr = 0
      this.$get(url + '?dtr=' + this.formData.period).then((r) => {
        if (r.data.data.success === 1) {
          this.dataRySource = r.data.data.data
          let dw = 0
          let gr = 0
          console.log(this.dataRySource)
          this.dataRySource.forEach((item,index,arr)=> {
            dw = dw + item.dw
            gr = gr + item.gr
          })
          this.showDw = dw
          this.showGr = gr
        } else {
          this.$message.warning('获取数据失败')
        }
      }).catch((e) => {
        console.error(e)
        this.$message.error('获取数据失败')
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
      this.$get('system/sb', {...params}).then(res => {
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
