<template>
  <div class="personalData">
    <div class="personalData__header">
      <div class="personalData__header-return">
        <a-button type="primary" @click="returnBtn">
          返回
        </a-button>
      </div>
      <div class="personalData__header-save">
        <a-button @click="exportFile">
          导出
        </a-button>
      </div>
    </div>
    <div id="personaljl" >
      <h3 class="h3">东西湖区人民医院员工简历</h3>
      <a-card title="职工基本信息" :bordered="false">
        <Form></Form>
      </a-card>
      <a-card title="人员编号信息" :bordered="false">
        <Table0></Table0>
      </a-card>
      <a-card title="职工核心信息" :bordered="false">
        <Table1></Table1>
      </a-card>
      <a-card title="教育及培训" :bordered="false">
        <Table2></Table2>
      </a-card>
      <!--<a-card title="职前教育信息" :bordered="false">-->
        <!--<Table11></Table11>-->
      <!--</a-card>-->
      <a-card title="家庭成员" :bordered="false">
        <Table3></Table3>
      </a-card>
      <!--<a-card title="电子档案信息" :bordered="false">-->
        <!--<Table4></Table4>-->
      <!--</a-card>-->
      <a-card title="职称岗位等级信息" :bordered="false">
        <Table5></Table5>
        <Table6></Table6>
      </a-card>
      <a-card title="工勤等级" :bordered="false">
        <Table15></Table15>
      </a-card>
      <a-card title="进修信息" :bordered="false">
        <Table12></Table12>
      </a-card>
      <a-card title="取得职称信息" :bordered="false">
        <Table9></Table9>
      </a-card>
      <a-card title="行政级别及职务" :bordered="false">
        <Table7></Table7>
        <Table8></Table8>
      </a-card>
      <a-card title="合同管理" :bordered="false">
        <Table10></Table10>
      </a-card>
      <a-card title="荣誉情况" :bordered="false">
        <Table13></Table13>
      </a-card>
      <a-card title="年度考核" :bordered="false">
        <Table14></Table14>
      </a-card>
      <a-card title="工作学习经历" :bordered="false">
        <Form1></Form1>
      </a-card>
    </div>
  </div>
</template>
<script>
import Table0 from './common/Table0'
import Table1 from './common/Table1'
import Table2 from './common/Table2'
import Table3 from './common/Table3'
import Table4 from './common/Table4'
import Table5 from './common/Table5'
import Table6 from './common/Table6'
import Table7 from './common/Table7'
import Table8 from './common/Table8'
import Table9 from './common/Table9'
import Table10 from './common/Table10'
import Table11 from './common/Table11'
import Table12 from './common/Table12'
import Table13 from './common/Table13'
import Table14 from './common/Table14'
import Table15 from './common/Table15'
import Form from './common/Form'
import Form1 from './common/Form1'
import Select from '../common/Select'
import { mapActions, mapGetters } from 'vuex'
import { downloadPDF } from '../../utils/filter'

export default {
  name: 'Employee',
  components: { Select, Form, Form1, Table0, Table1, Table2, Table3, Table4, Table5, Table6, Table7, Table8, Table9, Table10, Table11, Table12, Table13, Table14, Table15 },
  data () {
    return {
      pageStatus: '',
    }
  },
  computed: {
    ...mapGetters(['baseInfo', 'queryData'])
  },
  // 检测路由变化显示搜索弹框
  watch: {
    queryData: {
      handler: function (data) {
        this.pageStatus = data.pageStatus
      },
      immediate: true
    }
  },
  create () {},
  mounted () {},
  methods: {
    ...mapActions(['getSearch', 'getQuery']),
    // 导出
    exportFile () {
      if (!this.queryData.employeeid) {
        this.$message.warning('数据查询后，稍后再试');
        return;
      }
      let ele = document.querySelectorAll('.actionBtn');
      ele.forEach(item => {
        item.style.display = 'none'
      })
      setTimeout(downloadPDF(document.querySelector('#personaljl'), this.baseInfo.employeename + '的简历'), 5000)
      ele.forEach(item => {
        item.style.display = 'inline-block';
        item.style.textAlign = 'right';
      });
    },
    returnBtn () {
      this.$router.go(-1)
    },
  },
  destroy () {}
}
</script>
<style scoped lang="less">
.personalData{
  width: 100%;
  .personalData__header{
    position: sticky;
    top: 0;
    z-index: 11;
    height: 50px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    box-shadow: 0 2px 4px 0 #eee;
    background: #fff;
    padding: 0 20px;
  }
  .personalData__header-save {
    margin-bottom: 0!important;
  }
  .h3{
    text-align: center;
    font-size: 24px;
    color: #000;
    margin: 20px 0;
  }
}
.ant-upload-list-item-card-actions{
  display: none;
}

</style>
<style lang="less">
  @import "../../../static/less/Common";
  .ant-calendar-picker {
    width: 100%;
  }
  .actionBtn, .personalData__header-save {
    text-align: right;
    width: 100%;
  }

</style>
