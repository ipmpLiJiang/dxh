<template>
  <div class="personalData">
    <a-form-model ref="searchForm" layout="inline" :model="searchForm" :rules="ruleSearchForm" style="margin: 30px 0">
      <a-form-model-item ref="keyword" label="员工姓名" prop="keyword">
        <Select url="system/employee?search=" width="200" @getSearchValue="getSearchValue"></Select>
      </a-form-model-item>
      <a-form-model-item>
        <a-button type="primary" @click="handleSubmit">
          查询
        </a-button>
      </a-form-model-item>
    </a-form-model>
  </div>
</template>
<script>
import Select from '../common/Select'
import { mapActions, mapGetters } from 'vuex'

export default {
  name: 'EmployeeSearch',
  components: { Select },
  data () {
    return {
      pageStatus: '',
      searchForm: {
        keyword: null
      },
      ruleSearchForm: {
        keyword: [
          { required: true, message: '请输入员工姓名', trigger: 'change' }
        ]
      }
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
    // 匹配模糊查询数据
    getSearchValue ({value}) {
      this.searchForm.keyword = value
    },
    // 提交搜索关键字
    handleSubmit () {
      if (!this.searchForm.keyword) {
        this.$message.warning('没有查询到该员工，请核实信息后再查询~')
        return
      }
      this.$store.dispatch('getSearch', {vm: this, id: this.searchForm.keyword})
      this.$store.dispatch('getQuery', {employeeid: this.searchForm.keyword, pageStatus: 'edit'})
      this.$store.dispatch('getzidian', {vm: this})
      this.$router.push({path: '/employee',query: {id: this.searchForm.keyword}})
      // this.searchForm.keyword = null
    }
  },
  destroy () {}
}
</script>
<style scoped lang="less">
#demo {
  padding: 30px 0;
}
.personalData{
  width: 100%;
  .h3{
    text-align: center;
    font-size: 24px;
    color: #000;
  }
  .action-btn{
    float: right;
  }
  .personaljl{
    position: relative;
  }
}

</style>
<style lang="less">
  @import "../../../static/less/Common";
  .actionBtn{
    text-align: right;
    margin-bottom: 15px;
    .editable-margin-right{
      /*margin-right: 15px;*/
    }
  }
  .ant-card-head{
    font-size: 18px!important;
    color: #000;
  }
  .ant-calendar-picker {
    width: 100%;
  }
</style>
