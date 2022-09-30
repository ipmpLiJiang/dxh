<template>
    <a-card :bordered="false" class="card-area rotaTable">
        <a-form-model ref="formData" layout="inline" :model="formData">
            <a-form-model-item
                label="日期"
                :labelCol="{span: 7}"
                :wrapperCol="{span: 16, offset: 1}">
                <a-month-picker
                    v-model="formData.startdate"
                    :format="dateFormat"
                    :valueFormat="dateFormat"
                    @change="dateChange"
                />
            </a-form-model-item>
            <a-form-model-item>
                <a-button type="primary" @click="handleSubmit">查询</a-button>
                <a-button style="margin-left: 10px;" @click="exportFile"><a-icon type="download" /> 导 出 </a-button>
            </a-form-model-item>
            <a-form-model-item style="float: right; margin-right: 0">
                <a-button type="primary" @click="saveSubmit" :loading="startLoading"  :disabled="disabled"> 提 交 </a-button>
            </a-form-model-item>
        </a-form-model>
        <div class="rotaTable-status">状态：
            <span v-if="dataSource.sqb==null||dataSource.sqb==0">未提交</span>
            <span v-else>{{filterShstatus(dataSource.sqb.shstatus)}}</span>
            <span v-if="dataSource.sqb&&dataSource.sqb.shstatus==3">驳回理由: {{dataSource.sqb.memo}}</span>
        </div>
        <a-table
            ref="tableInfo"
            :columns="columns"
            :loading="loading"
            :row-key="record => record.employeecode"
            :data-source="dataSource.pblist"
            :scroll="{ x: 1200, y: 500 }"
            :pagination="false"
        >
        </a-table>
    </a-card>
</template>
<script>
    import { mapGetters } from 'vuex'
    import moment from 'moment'
    export default {
        name: 'RotaTable',
        components: {},
        data () {
            return {
                disabled: false,
                dateFormat: 'YYYY-MM',
                loading: false,
                formData: {
                    startdate: moment(new Date()).format('YYYY-MM'),
                    pbdept: this.$store.state.account.user.deptId,
                },
                dataSource: [],
                daysList: [],
                startLoading: false,
                columns: []
            }
        },
        computed: {
            ...mapGetters(['zidianInfo']),
            defColumns() {
              let { rszfw } = this.zidianInfo
                return [
                  {
                    title: '工号',
                    dataIndex: 'employeecode',
                    width: 100,
                    scopedSlots: {customRender: 'employeecode'}
                  },
                    {
                        title: '姓名',
                        dataIndex: 'employeename',
                        width: 100,
                        scopedSlots: {customRender: 'employeename'}
                    },
                    {
                        title: '人事子范围',
                        dataIndex: 'rszfw',
                        width: 120,
                        customRender: (text, row, index) => {
                            if(text==null) return ""
                            let option = rszfw.filter(item => item.keyy == text)
                            let result = option[0]?option[0].valuee: ""
                            return result
                        }
                    },
                ]

            }
        },
        watch: {
            "$route.path": {
                handler(to) {
                    if (to === "/attendance/rotaTable") {
                    this.dateChange(this.formData.startdate)
                }
            },
            deep: true
            }
        },
        mounted () {
            this.dateChange(this.formData.startdate)
        },

        methods: {
            filterShstatus(data) {
                if (!data) return
                let option = this.zidianInfo.shstatus.filter(item => item.keyy==data)
                let result = option[0]?option[0].valuee: ""
                return result
            },
            // 表头数据
            getDateList () {
                var days = moment(this.formData.startdate, "YYYY-MM").daysInMonth()
                let columns = []
                for(let i = 1;i <= days;i ++) {
                    if (i < 10) {
                        i= "0" + i
                    }
                    let label = moment(this.formData.startdate).format('MM') + "/" + i
                    columns.push(this.getColumns(label, "a" + i, 100, i))
                }
                this.columns = this.defColumns.concat(columns)
                this.fetch()
            },
            //处理表头
            getColumns(title, dataIndex, width, customRender) {
                let { kq } = this.zidianInfo
                return {
                    title: title,
                    dataIndex: dataIndex,
                    width: width,
                    customRender: (text, row, index) => {
                        let option = kq.filter(item => item.keyy == text)[0]
                        return option ? option.valuee : ''
                    }
                }
            },
            // 当前时间
            dateChange (date) {
                this.formData.startdate = date
                this.getDateList()
            },
            // 查询
            handleSubmit () {
                this.dateChange(this.formData.startdate)
            },
            saveSubmit() {
                this.startLoading = true
                let params = {}
                params.startdate = moment(this.formData.startdate).format('YYYY-MM') + "-01"
                params.pbdept = this.formData.pbdept
                this.$post('system/kq/rmxb', params).then(res => {
                    this.$message.success("提交成功！")
                    // this.disabled = true
                    this.dateChange(this.formData.startdate)
                    this.startLoading = false
                }).catch(err => {
                    this.$message.warning(err)
                    this.startLoading = false
                })
            },
            // 带条件的查询
            fetch () {
                if(!this.formData.startdate) {
                    this.$message.warning("请填写时间！")
                    return
                }
                this.loading = true
                let params = {}
                params.startdate = moment(this.formData.startdate).format('YYYY-MM') + "-01"
                params.pbdept = this.formData.pbdept
                this.$get('system/kq/rmxb/getPbByMonth', params).then(res => {
                    if (res.data) {
                        this.dataSource = res.data
                        if (!res.data.sqb||res.data.sqb.shstatus==0||res.data.sqb.shstatus==3) {
                            this.disabled = false
                        } else {
                            this.disabled = true
                        }
                        this.loading = false
                    } else {
                        this.loading = false
                    }
                }).catch(err => {
                    this.$message.warning(err)
                    this.loading = false
                })
            },
            // 导出
            exportFile () {
                let params = {...this.formData}
                params.startdate = moment(this.formData.startdate).format('YYYY-MM') + "-01"
                this.$export('system/kq/rmxb/export/month', {
                    ...params
                })
            },
        }
    }
</script>
<style scoped lang="less">
    .rotaTable{
        width: 100%;
        .formData{
            .ant-form-item{
                margin-bottom: 5px!important;
            }
            .ant-switch{
                width: 60px;
                height: 28px;
                line-height: 26px;
            }
            .ant-switch-loading-icon, .ant-switch::after{
                width: 24px;
                height: 24px;
            }
        }
        .editable-add-btn{
            margin-right: 10px;
        }
        .title{
            color: #999;
        }
        .rotaTable-status{
            margin: 10px 0;
        }
    }
</style>
<style lang="less">
    @import "../../../static/less/Common";
    .ant-form-item-span{
        color: rgba(0,0,0,0.85);
    }
</style>
