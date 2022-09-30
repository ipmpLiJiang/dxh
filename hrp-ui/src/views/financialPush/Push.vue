<template>
    <div class="financialPush">
        <!--<a-row class="injuryJob-top" type="flex" justify="space-between">-->
            <!--<a-col :md="8" :sm="24">-->
                <!--<a-form-item-->
                    <!--:labelCol="{span: 7}"-->
                    <!--:wrapperCol="{span: 16, offset: 1}"-->
                <!--&gt;-->
                    <!--<a-month-picker-->
                        <!--v-model="formData.period"-->
                        <!--:format="dateFormat"-->
                        <!--:valueFormat="dateFormat"-->
                        <!--@change="onSeach"-->
                        <!--disabled-->
                    <!--/>-->
                <!--</a-form-item>-->
            <!--</a-col>-->
        <!--</a-row>-->
        <a-row>
            <a-col :span="6">
                <span class="title">考勤扣款：</span>
                <span>{{coreCol.kk}}</span>
                <span>({{coreCol.kkdate}})</span>
            </a-col>
            <a-col :span="6">
                <span class="title">工资：</span>
                <span>{{coreCol.gz}}</span>
                <span>({{coreCol.gzdate}})</span>
            </a-col>
            <a-col :span="6">
                <span class="title">社保：</span>
                <span>{{coreCol.sb}}</span>
                <span>({{coreCol.gzdate}})</span>
            </a-col>
             <a-col :span="6">
                <span class="title">在编养老：</span>
                <span>{{coreCol.yl}}</span>
                <span>({{coreCol.gzdate}})</span>
            </a-col>
            <a-col :span="6">
                <a-button type="primary" :loading="iconLoading" :disabled="disSubmit" @click="handleSubmit">
                    提交
                </a-button>
            </a-col>
        </a-row>
    </div>
</template>

<script>
  var timer;
  import moment from 'moment'
  export default {
    name: "Push",
    data() {
      return {
        iconLoading: false,
        disSubmit: false,
        dateFormat: "YYYY/MM",
        coreCol: {},
        formData: {
          period: moment(new Date()).format('YYYY/MM'),
        }
      }
    },
    mounted() {
      this.onSeach()
    },
    methods: {
      onSeach() {
        this.$get("system/cwgz/list").then(res => {
          if (res.status===200) {
            this.coreCol = res.data
            this.coreCol.kkdate = moment(this.coreCol.kkdate).format('YYYY-MM')
            this.coreCol.gzdate = moment(this.coreCol.gzdate).format('YYYY-MM')
            if (this.coreCol&&this.coreCol.status==1&&this.coreCol.kk>0&&this.coreCol.gz>0&&this.coreCol.sb>0&&this.coreCol.yl>0) {
             this.disSubmit = false
            }
          }
        })
      },
      handleSubmit() {
        this.iconLoading = true
        timer = setTimeout(()=> {
          this.$get("system/cwgz").then(res => {
            if (res.status===200) {
              this.iconLoading = false
              this.disSubmit = true
              this.$message.success("推送成功！")
              this.onSeach()
            }
          }).catch(() => {
            this.iconLoading = false
            this.iconLoading = false
            this.$message.warning("推送失败！")
          })
        }, 3000)
      }
    },
    beforeDestroy() {
      clearInterval(timer)
      timer = null
    }

  }
</script>

<style lang="less">
  .financialPush {
    width: 100%;
    padding: 20px 0 0 20px;
    .ant-col {
      margin-left: 0!important;
      }
    }
</style>