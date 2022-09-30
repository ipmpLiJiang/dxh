<template>
    <a-card :bordered="false" class="card-area rota">
        <a-form layout="horizontal" :rules="formRules">
            <a-row>
                <a-col :md="6" :sm="24">
                    <a-form-item
                        label="时间"
                        prop="startdate"
                        :labelCol="{span: 5}"
                        :wrapperCol="{span: 16, offset: 1}">
                        <a-week-picker
                            v-model="queryParams.startdate"
                            valueFormat="YYYY/MM/DD"
                            format="YYYY/MM/DD"
                            @change="dateChange"
                        />
                    </a-form-item>
                </a-col>
                <a-col :md="6" :sm="24" >
                    <a-form-item
                        label="人事子范围"
                        prop="rszfws"
                        :labelCol="{span: 7}"
                        :wrapperCol="{span: 16, offset: 1}"
                    >
                        <a-select
                            v-model="queryParams.rszfws"
                            mode="multiple"
                            allowClear
                            :filter-option="filterOption"
                        >
                            <a-select-option v-for="item in zidianInfo.rszfw" :value="item.keyy" :key="item.keyy">
                                {{item.valuee}}
                            </a-select-option>
                        </a-select>
                    </a-form-item>
                </a-col>
                <a-col :md="2" :sm="24" >
                    <a-form-item
                        :labelCol="{span: 7}"
                        :wrapperCol="{span: 16, offset: 1}">
                        <a-button type="primary" :loading="startLoading"  @click="search">开启</a-button>

                    </a-form-item>
                </a-col>
                <a-col :md="10" :sm="24" class="col-item">
                    <a-form-item
                        :labelCol="{span: 7}"
                        :wrapperCol="{span: 16, offset: 1}">

                        <a-button class="rota-top-btn" :loading="startLoading"  type="primary" icon="check-circle" @click="handleSave" :disabled="disSave">
                            保  存
                        </a-button>
                    </a-form-item>
                </a-col>
            </a-row>
        </a-form>
        <div class="operator">
            <a-upload
                :multiple="false"
                :file-list="fileList"
                :before-upload="beforeUpload"
                :customRequest="upLoad"
            >
                <a-button class="rota-top-btn" icon="upload"> 导入 </a-button>
            </a-upload>
            <a-button class="rota-top-btn" icon="download" @click="handleDownload">导出</a-button>
            <a-button class="rota-top-btn" icon="download" @click="handlePrint">打印</a-button>
        </div>
        <a-row style="margin-bottom: 10px">

            <a-col :span="6">
                备注：
                <a-tag color="red">1</a-tag> 表示非本科室排班
            </a-col>
            <a-col :span="6">
                <a-tag color="blue">2</a-tag> 表示已提交审核
            </a-col>
        </a-row>
        <table class="rota-table" id="week-rota-table" border="0" cellpadding="0" cellspacing="0">
            <thead>
            <tr>
                <th>工号</th>
                <th>姓名</th>
                <th>人事子范围</th>
                <th>
                    周一 {{weedDate[0]}}
                </th>
                <th>
                    周二 {{weedDate[1]}}
                </th>
                <th>
                    周三 {{weedDate[2]}}
                </th>
                <th>
                    周四 {{weedDate[3]}}
                </th>
                <th>
                    周五 {{weedDate[4]}}
                </th>
                <th>
                    周六 {{weedDate[5]}}
                </th>
                <th>
                    周天 {{weedDate[6]}}
                </th>
            </tr>
            </thead>
            <tbody>
            <tr v-if="weekOptions.employeeList&&weekOptions.employeeList.length>0" v-for="(item, index) in weekOptions.employeeList" :key="index">
                <td>{{item.code}}</td>
                <td>{{item.employeename}}</td>
                <td>{{filterRszfw(item.rszfw)}}</td>
                <td v-for="(temp, i) in item.rmxList" :key="i">
                    <a-select v-show="temp.editflag"
                              show-search
                              v-model="temp.scheduling"
                              :filter-option="filterOption"
                              option-filter-prop="children"
                              dropdownClassName="ant-select-dropdowns"
                              :showArrow="true"
                              :allowClear="temp.scheduling?true:false"
                    >
                        <a-icon v-show="temp.scheduling" slot="suffixIcon" type="copy" @click.stop="handleCopy(temp.scheduling, item.pbOptions)" />
                        <a-select-option
                            v-show="zidianInfo.deptpb&&zidianInfo.deptpb.length>0"
                            v-for="a in pbOptions"
                            :value="a.keyy"
                            :key="a.keyy"
                        >
                        {{a.valuee}}
                        </a-select-option>
                        <a-select-option
                            v-show="zidianInfo.deptpb.length==0"
                            v-for="a in item.pbOptions"
                            :value="a.keyy"
                            :key="a.keyy"
                        >
                            {{a.valuee}}
                        </a-select-option>
                    </a-select>
                    <div class="rota-table-text" v-show="!temp.editflag">
                        <a-tag v-if="temp.scheduling&&temp.updateby!=$store.state.account.user.username" color="red">
                            {{ filterType(temp.scheduling) }}
                        </a-tag>
                        <a-tag v-else-if="temp.scheduling&&temp.updateby==$store.state.account.user.username&&(temp.shstatus==1||temp.shstatus==2)" color="blue">
                            {{ filterType(temp.scheduling) }}
                        </a-tag>
                        <span v-else>{{ filterType(temp.scheduling) }}</span>
                    </div>
                </td>
            </tr>
            <tr v-else>
                <td colspan="7">暂无数据</td>
            </tr>
            </tbody>
        </table>
    </a-card>
</template>

<script>
    import { mapGetters } from 'vuex'
    import moment from 'moment'
    import { downloadPDF, previewImg } from '../../utils/filter'

    export default {
        name: "Rota",
        data() {
            return {
                disSave: true,
                queryParams: {
                    startdate: "",
                    pbdept: this.$store.state.account.user.deptId,
                    username: this.$store.state.account.user.username,
                    rszfws: [],
                },
                fileList: [],
                fileInfo: {},
                startLoading: false,
                weekOptions: [],
                weedDate: [],
                employeeList: [],
                pbOptions: [],
                formRules: {
                    startdate: [
                        { required: true, message: '请输入时间', trigger: 'change' }
                    ],
                }
            }
        },
        watch: {
            employeeList: {
                handler(data) {
                    data.forEach(item => {
                        item.rmxList.map(tepm => {
                            if (tepm.scheduling==undefined) {
                                tepm.scheduling = null
                            }else if (tepm.scheduling||tepm.scheduling==="0") {
                                tepm.scheduling = Number(tepm.scheduling)
                            }
                        })
                    })
                    console.log(data, 111000000)
                },
                deep: true,
            }
        },
        computed: {
            ...mapGetters(['zidianInfo']),
        },
        mounted() {
            this.getRota()
        },
        methods: {
            filterOption(input, option) {
                return (
                    option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
                )
            },
            filterType(data) {
                if (data==null||data==undefined) return ""
                let option = this.zidianInfo.kq.filter(item => item.keyy==data)
                let result = option[0]?option[0].valuee: ""
                return result
            },
            filterRszfw(data) {
                if (!data) return
                let option = this.zidianInfo.rszfw.filter(item => item.keyy == data)
                let result = option[0]?option[0].valuee: ""
                return result
            },
            dateChange(date) {
                if(!date) {
                    return
                }
                this.queryParams.startdate = date
            },
            handleCopy(value, pbOptions) {
                let options = this.zidianInfo.deptpb.length>0?this.pbOptions:pbOptions
                const target = options.filter(item => value === item.keyy)[0];
                let data = target.valuee
                this.copy(data)
            },
            copy(data){
                let oInput = document.createElement('input');
                oInput.value = data;
                document.body.appendChild(oInput);
                oInput.select(); // 选择对象;
                console.log(oInput.value)
                document.execCommand("Copy"); // 执行浏览器复制命令
                this.$message.success("复制成功！")
                oInput.remove()
            },
            handleSave() {
                this.startLoading = true;
                let params = this.weekOptions.employeeList
                this.$put1('system/kq/rmxb/' + this.queryParams.pbdept + '/' + this.queryParams.username, params).then(() => {
                    this.$message.success("保存成功！")
                    let that = this
                    this.$nextTick(() => {
                        this.disSave = true
                        that.weekOptions.employeeList.forEach(item => {
                            item.rmxList.forEach(temp => {
                                temp.editflag = false
                            })
                        })
                    })
                    this.startLoading = false;
                }).catch(err => {
                    this.startLoading = false;
                    this.disSave = false
                    this.$message.warning(err)
                })
            },
            handleDownload() {
                if (!this.queryParams.startdate) {
                    this.$message.warning("请填写时间！")
                    return
                }
                this.$export('system/kq/rmxb/export/week', {
                    ...this.queryParams
                })
            },
            // 打印
            handlePrint() {
                setTimeout(downloadPDF(document.querySelector('#week-rota-table'),  '周排班表'), 5000)
            },
            // 文件上传之前的校验
            beforeUpload (file) {
                const isXlsx = file.type.indexOf(".sheet") < 0? false: true;
                if (!isXlsx) {
                  this.$message.error('请上传正确的文件格式！')
                }
                this.fileInfo = file
                return isXlsx
            },
            // 自定义上传
            upLoad () {
                const formData = new FormData()
                formData.append('file', this.fileInfo)
                this.$upload(`system/kq/rmxb/import/${this.queryParams.pbdept}`, formData).then(res => {
                    if (res.data) {
                        this.$message.success('上传成功！')
                        this.search()
                    }
                }).catch(err => {
                    this.$message.success(err)
                })
            },
            search () {
                this.queryData({
                    ...this.queryParams
                })
            },
            queryData(params = {}) {
                this.startLoading = true
                if (!params.startdate) {
                    this.$message.warning("请填写时间！")
                    return
                }
                params.username = this.queryParams.username
                params.rszfws = this.queryParams.rszfws.toString()
                this.$get('system/kq/rmxb/getPbByWeek', params).then(res => {
                    if (res.data) {
                        this.disSave = false
                        this.weekOptions = res.data
                        this.weedDate = []
                        res.data.date.forEach(item => {
                            this.weedDate.push(moment(item).format('MM/DD'))
                        })
                        this.employeeList = res.data.employeeList
                        this.employeeList.forEach(item => {
                            item.pbOptions = this.getRota(item)
                        })
                        this.weekOptions = Object.assign({}, this.weekOptions, { employeeList: this.employeeList})
                        this.startLoading = false
                    }
                }).catch(err => {
                    this.startLoading = false
                    this.disSave = true
                    this.$message.warning(err)
                })
            },
            getRota(item) {
                this.pbOptions = []
                let deptpb = this.zidianInfo.deptpb
                let typb = this.zidianInfo.typb
                let yjpb = this.zidianInfo.yjpb
                let hlpb = this.zidianInfo.hlpb
                if (deptpb&&deptpb.length>0) { // 特殊科室
                    this.pbOptions = deptpb.concat(typb)
                }else { // 非特殊科室
                    if (item&&(item.rszfw==2001||item.rszfw==2002||item.rszfw==2003)) {
                        return yjpb.concat(typb)
                    } else if (item&&item.rszfw=="2004") {
                        return hlpb.concat(typb)
                    } else if(item) {
                        return typb
                    }
                }
            }
        }
    }
</script>

<style scoped lang="less">
    .rota {
        width: 100%;
        .rota-top {
            display: flex;
            justify-content: space-between;
            margin-bottom: 10px;
        }
        .rota-top-btn {
            margin-left: 10px;
        }
        .rota-top-right {
            display: flex;
        }
        .rota-table{
            width: 100%;
            tr {
                th {
                    background: #fafafa;
                    color: rgba(0, 0, 0, 0.85);
                    border-bottom: 1px solid #e8e8e8;
                    padding: 16px;
                }
                td {
                    position: relative;
                    border-bottom: 1px solid #e8e8e8;
                    padding: 16px 8px;
                    overflow-wrap: break-word;
                }

            }
            /deep/.ant-select {
                width: 95%;
                .ant-select-selection__rendered {
                    margin-left: 6px;
                    margin-right: 8px;
                }
                .ant-select-arrow {
                    right: 15px;
                    top: 55%;
                    .ant-select-arrow-icon {
                        font-size: 12px!important;
                    }
                }
                .ant-select-selection__clear {
                    right: 2px;
                    top: 8px;
                    .ant-select-clear-icon {
                        font-size: 10px!important;
                    }
                }
                .ant-select-selection-selected-value {
                    .anticon-copy {
                        display: none;
                    }
                }
            }
            .rota-table-text {
                padding: 10px;
            }
        }
        .operator {
            margin-bottom: 10px;
            display: flex;
        }
        .hover-div {
            display: none;
            padding: 10px;
            border: 1px solid #eee;
            border-radius: 6px;
            width: 300px;
            position: absolute;
            top: 10%;
            left: 10%;
            z-index: 10;
            .ant-btn-primary {
                display: inline-block;
                margin: 5px;
            }
        }
        .rota-table {
            .rota-input:hover+ .hover-div{
                display: inline-block;
            }
        }
        .col-item {
            text-align: right;
            /deep/.ant-col {
                width: 100%;
                margin-left: 0!important;
            }
        }
    }
</style>
<style lang="less">
    .ant-select-dropdowns {
        width: 120px!important;
        -moz-user-select: text!important;
        -ms-user-select: text!important;
        -webkit-user-select: text!important;
        user-select: text!important;
        .anticon-copy {
            font-size: 12px;
            color: rgba(0, 0, 0, 0.25);
            margin-left: 20px;
        }
    }
</style>
