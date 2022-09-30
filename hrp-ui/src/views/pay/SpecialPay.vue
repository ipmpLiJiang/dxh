<!--特殊工资项目-->
<template>
    <a-card :bordered="false" class="card-area injuryJob">
        <a-row class="injuryJob-top" type="flex" justify="space-between">
            <a-col :md="6" :sm="24" >
                <a-form-item
                    label="员工姓名"
                    prop="keyword"
                    :labelCol="{span: 7}"
                    :wrapperCol="{span: 16, offset: 1}"
                >
                    <Select :keyword="queryParams.keyword" url="system/employee?search=" @getSearchValue="getSearchValue"></Select>
                </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24" >
                <a-form-item
                    label="工资项目"
                    prop="keyword"
                    :labelCol="{span: 7}"
                    :wrapperCol="{span: 16, offset: 1}"
                >
                    <a-select
                        v-model="queryParams.gzxm"
                        allowClear
                    >
                        <a-select-option v-for="item in zidianInfo.gzxm" :value="item.keyy" :key="item.keyy">
                            {{item.valuee}}
                        </a-select-option>
                    </a-select>
                </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
                <a-form-item
                    :labelCol="{span: 7}"
                    :wrapperCol="{span: 16, offset: 1}"
                >
                    <a-button type="primary" @click="handleSubmit">
                        查询
                    </a-button>
                </a-form-item>
            </a-col>
            <a-col :md="6" :sm="24">
                <a-form-item
                    :labelCol="{span: 7}"
                    :wrapperCol="{span: 16, offset: 1}"
                >

                </a-form-item>
            </a-col>
        </a-row>
        <div class="operator">
            <a-button type="primary" class="editable-add-btn" @click="clickAdd('addBtn')">
                新增
            </a-button>
            <a-button class="editable-add-btn"  @click="deleteRow">
                删除
            </a-button>
            <a-upload
                :multiple="false"
                :file-list="fileList"
                :before-upload="beforeUpload"
                :customRequest="upLoad"
            >
                <a-button class="rota-top-btn" icon="upload"> 导入 </a-button>
            </a-upload>
        </div>
        <a-row style="margin-bottom: 10px">
            <a-col :span="3">
                <span class="title">总金额：</span>
                <span>{{coreCol.sum||0}} 元</span>
            </a-col>
            <a-col :span="3">
                <span class="title">总人数：</span>
                <span>{{coreCol.total||0}} 人</span>
            </a-col>
        </a-row>
        <a-table
            :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
            ref="tableInfo"
            :columns="columns"
            :loading="loading"
            :row-key="record => record.id"
            :data-source="dataSource"
            :pagination="pagination"
            @change="handleTableChange"
            :scroll="{ x: 900, y: 500 }"
        >
            <a slot="operation" slot-scope="text, record" href="javascript:;" @click="editRow(record, 'editBtn')">编辑</a>
        </a-table>
        <a-modal class="ant-modal-nofooter" v-model="coreVisible" :title="btnType === 'addBtn' ? '新增' : '修改'" @ok="handleOk">
            <a-form-model
                ref="coreRules"
                :model="formData"
                :rules="coreRules"
                :label-col="labelCol"
                :wrapper-col="wrapperCol"
            >
                <a-form-model-item label="员工姓名" prop="employeename">
                    <Select :keyword="formData.employeename" :disabledName="btnType=='editBtn'" url="system/employee?search=" @getSearchValue="getEditSearchValue"></Select>
                </a-form-model-item>
                <a-form-model-item label="时间" prop="period">
                    <a-month-picker
                        v-model="formData.period"
                        :format="dateFormat"
                        :valueFormat="dateFormat"
                        :disabled="btnType=='editBtn'"
                    />
                </a-form-model-item>
                <a-form-model-item label="工资项目" prop="gzxm">
                    <a-select
                        v-model="formData.gzxm"
                        allowClear
                    >
                        <a-select-option v-for="item in zidianInfo.gzxm" :value="item.keyy" :key="item.keyy">
                            {{item.valuee}}
                        </a-select-option>
                    </a-select>
                </a-form-model-item>
                <a-form-model-item label="金额" prop="je">
                    <a-input
                        v-model="formData.je"
                        allowClear
                    />
                </a-form-model-item>
                <a-form-model-item label="备注" prop="memo">
                    <a-input
                        v-model="formData.memo"
                        allowClear
                    />
                </a-form-model-item>
                <a-form-model-item class="injuryJob-footer">
                    <a-button @click="resetForm">
                        重置
                    </a-button>
                    <a-button style="margin-left: 20px;" type="primary" @click="onSubmit">
                        确认
                    </a-button>
                </a-form-model-item>
            </a-form-model>
        </a-modal>
    </a-card>
</template>
<script>
    import { mapGetters, mapActions } from 'vuex'
    import Select from '../common/Select'

    export default {
        name: "InjuryJob",
        components: {Select},
        data () {
            return {
                dateFormat: 'YYYY-MM',
                coreVisible: false,
                labelCol: { span: 6 },
                wrapperCol: { span: 14 },
                btnType: 'addBtn',
                queryParams: {
                    keyword: null,
                    employeeid: null
                },
                loading: false,
                selectedRowKeys: [],
                dataSource: [],
                paginationInfo: null,
                pagination: {
                    pageSizeOptions: ['10', '20', '30', '40', '100'],
                    defaultCurrent: 1,
                    defaultPageSize: 10,
                    showQuickJumper: true,
                    showSizeChanger: true,
                    showTotal: (total, range) => `显示 ${range[0]} ~ ${range[1]} 条记录，共 ${total} 条记录`
                },
                coreCol: {},
                formData: {
                    employeeid: null,
                    employeename: null,
                    period: null,
                    je: null,
                    gzxm: null,
                    memo: null,
                },
                coreRules: {
                    employeename: [
                        { required: true, message: '请输入姓名', trigger: 'change' }
                    ],
                    period: [
                        { required: true, message: '请输入时间', trigger: 'change' }
                    ],
                    gzxm: [
                        { required: true, message: '请输入工资项目', trigger: 'blur' }
                    ],
                    je: [
                        { required: true, message: '请输入金额', trigger: 'blur' }
                    ],
                }
            }
        },
        computed: {
            ...mapGetters(['zidianInfo']),
            columns () {
                let { gzxm } = this.zidianInfo
                return [
                    {
                        title: '姓名',
                        dataIndex: 'employeename',
                        width: 100,
                        scopedSlots: { customRender: 'employeename' }
                    },
                    {
                        title: '期间',
                        dataIndex: 'period',
                        width: 120,
                        scopedSlots: { customRender: 'period' }
                    },
                    {
                        title: '工资项目',
                        dataIndex: 'gzxm',
                        width: 120,
                        customRender: (text, row, index) => {
                            if(text == null) return ""
                            let option = gzxm.filter(item => item.keyy == text)[0]
                            return option ? option.valuee : ''
                        }
                    },
                    {
                        title: '金额',
                        dataIndex: 'je',
                        width: 120,
                        scopedSlots: { customRender: 'je' }
                    },
                    {
                        title: '备注',
                        dataIndex: 'memo',
                        width: 120,
                        scopedSlots: { customRender: 'memo' }
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

        mounted () {
            this.$store.dispatch('getzidian', {vm: this})
            this.fetch({
                ...this.queryParams
            })
        },
        methods: {
            ...mapActions(['getzidian']),

            getSearchValue ({value}) {
                this.queryParams.employeeid = this.queryParams.keyword = value
                this.fetch({
                    ...this.queryParams
                })
            },
            getEditSearchValue({value}) {
                this.formData.employeename = this.formData.employeeid = value

            },
            clickAdd (type) {
                this.btnType = type
                this.coreVisible = true
                this.resetForm()
                this.formData.employeename = null
                this.formData.employeeid = null
            },
            editRow (record, type) {
                this.btnType = type
                this.coreVisible = true
                this.formData = {...this.formData, ...record}
            },
            onSelectChange (selectedRowKeys) {
                this.selectedRowKeys = selectedRowKeys
            },
            deleteRow () {
                if (this.selectedRowKeys.length === 0) {
                    this.$message.warning('请勾选您要删除的记录！')
                    return
                }
                let that = this
                this.$confirm({
                    title: '确定删除选中记录吗?',
                    content: '',
                    onOk () {
                        that.$delete('system/tsxmymx/' + that.selectedRowKeys.toString()).then(res => {
                            if (res.data.status === 'OK') {
                                that.$message.success('删除成功！')
                                that.selectedRowKeys = []
                                that.queryParams.keyword = null
                                that.fetch({
                                    ...that.queryParams
                                })
                            }
                        }).catch(err => {
                            that.$message.warning(err)
                        })
                    },
                    onCancel () {
                        that.selectedRowKeys = []
                        that.$message.warning('取消删除！')
                    }
                })
            },
            handleOk () {
                this.coreVisible = false
            },
            onSubmit () {
                this.$refs.coreRules.validate(valid => {
                    if (valid) {
                        let params = {...this.formData}
                        params.id = this.btnType === 'addBtn' ? null : this.formData.id
                        let http = this.btnType === 'addBtn' ? this.$post : this.$put
                        http('system/tsxmymx', {...params}).then(res => {
                            if (res.data.status === 'OK') {
                                this.coreVisible = false
                                this.$message.success('保存成功！')
                                this.fetch({
                                    ...this.queryParams
                                })
                            }
                        }).catch(err => {
                            this.$message.warning(err)
                        })
                    } else {
                        return false
                    }
                })
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
                this.$upload(`system/tsxmymx/import`, formData).then(res => {
                    if (res.data) {
                        this.$message.success('上传成功！')
                        this.search()
                    }
                }).catch(err => {
                    this.$message.success(err)
                })
            },
            handleTableChange (pagination, filters, sorter) {
                this.paginationInfo = pagination
                this.fetch({
                    ...this.queryParams
                })
            },
            handleSubmit() {
                this.fetch({
                    ...this.queryParams
                })
            },
            // 列表查询
            fetch (params = {}) {
                this.loading = true
                if (this.paginationInfo) {
                    // 如果分页信息不为空，则设置表格当前第几页，每页条数，并设置查询分页参数
                    this.$refs.tableInfo.pagination.current = this.paginationInfo.current
                    this.$refs.tableInfo.pagination.pageSize = this.paginationInfo.pageSize
                    params.pageSize = this.paginationInfo.pageSize
                    params.pageNum = this.paginationInfo.current
                } else {
                    // 如果分页信息为空，则设置为默认值
                    params.pageSize = this.pagination.defaultPageSize
                    params.pageNum = this.pagination.defaultCurrent
                }
                this.$get('system/tsxmymx', {...params}).then(res => {
                    if (res.data) {
                        let data = res.data
                        const pagination = { ...this.pagination }
                        pagination.total = data.total
                        this.dataSource = data.rows
                        this.coreCol.sum = data.sum
                        this.coreCol.total = data.total
                        this.pagination = pagination
                        this.loading = false
                    } else {
                        this.loading = false
                    }
                }).catch(err => {
                    this.$message.warning(err)
                    this.loading = false
                })
            },
            resetForm () {
                this.formData.period = null
                this.formData.gzxm = null
                this.formData.je = null
                this.formData.memo = null
            }
        }
    }
</script>
<style lang="less" scoped>
    .injuryJob {
        .injuryJob-top {
            .ant-select {
                width: 100%;
            }
        }
        .editable-add-btn{
            margin-right: 10px;
        }

    }
</style>
<style lang="less">
    @import "../../../static/less/Common";
    .injuryJob-footer {
        text-align: right;
        margin-top: 20px;
    }
    .ant-modal-nofooter {
        .ant-modal-footer {
            display: none;
        }
        .ant-calendar-picker {
            width: 100%;
        }
    }
</style>
