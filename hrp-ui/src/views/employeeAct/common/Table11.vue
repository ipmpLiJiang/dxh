<!--职前教育及培训-->
<template>
  <div class="table11">
    <div class="actionBtn">
      <a-button v-if="formData.employeeid" type="primary" class="editable-add-btn editable-margin-right" @click="clickAdd('addBtn')">
        新增
      </a-button>
      <a-button v-if="formData.employeeid" class="editable-add-btn" @click="deleteRow">
        删除
      </a-button>
    </div>
    <a-table
      :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
      :columns="columns"
      :rowKey="record => record.id"
      :data-source="tableData"
      bordered
      :pagination="false"
    >
      <!--<template slot="operation" slot-scope="text, record">-->
        <!--<a-button class="editable-add-btn" @click="editRow(record, 'editBtn')">-->
          <!--编辑-->
        <!--</a-button>-->
      <!--</template>-->
      <a slot="operation" slot-scope="text, record" href="javascript:;" @click="editRow(record, 'editBtn')">编辑</a>
    </a-table>
    <a-modal class="ant-modal-nofooter" v-model="coreVisible" :title="btnType === 'addBtn' ? '新增' : '修改'" :width="650" @ok="handleOk">
      <a-form-model
        ref="formRules"
        :model="formData"
        :rules="formRules"
        :label-col="labelCol"
        :wrapper-col="wrapperCol"
      >
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="学位" prop="degrees">
              <a-select v-model="formData.degrees" @change="getname(formData.degrees, 'degrees')" allowClear>
                <a-select-option v-for="item in zidianInfo.degrees" :value="item.keyy" :key="item.keyy">
                  {{item.valuee}}
                </a-select-option>
              </a-select>
            </a-form-model-item>
            <a-form-model-item label="学历" prop="academic">
              <a-select v-model="formData.academic" @change="getname(formData.academic, 'academic')" allowClear>
                <a-select-option v-for="item in zidianInfo.academic" :value="item.keyy" :key="item.keyy">
                  {{item.valuee}}
                </a-select-option>
              </a-select>
            </a-form-model-item>
            <a-form-model-item label="学习形式" prop="studymethod">
              <a-select v-model="formData.studymethod" @change="getname(formData.studymethod, 'studymethod')" allowClear>
                <a-select-option v-for="item in zidianInfo.studymethod" :value="item.keyy" :key="item.keyy">
                  {{item.valuee}}
                </a-select-option>
              </a-select>
            </a-form-model-item>
            <a-form-model-item label="毕业学校" prop="school">
              <a-input
                v-model="formData.school"
                allowClear
              />
            </a-form-model-item>
            <a-form-model-item label="专业" prop="profession">
              <a-input
                v-model="formData.profession"
                allowClear
              />
            </a-form-model-item>
            <a-form-model-item label="结束时间" prop="enddate">
              <a-date-picker
                :value="getTime(formData.enddate)"
                :format="dateFormat"
                :valueFormat="dateFormat"
                @change="enddateChange"
              />
            </a-form-model-item>
          </a-col>
        </a-row>
        <a-form-model-item :wrapper-col="{ span: 14, offset: 4 }">
          <a-button @click="resetForm">
            重置
          </a-button>
          <a-button style="margin-left: 20px;" type="primary" @click="onSubmit">
            确认
          </a-button>
        </a-form-model-item>
      </a-form-model>
    </a-modal>
  </div>
</template>
<script>
import { mapGetters, mapActions } from 'vuex'
import { getTime } from '../../../utils/filter'

export default {
  data () {
    return {
      dateFormat: 'YYYY-MM-DD',
      pageStatus: '',
      coreVisible: false,
      tableData: [],
      selectedRowKeys: [],
      labelCol: { span: 6 },
      wrapperCol: { span: 12 },
      other: '',
      fileInfo: {},
      btnType: 'addBtn',
      startdateValue: 0,
      enddateValue: 0,
      lastdateValue: 0,
      lastdate: '',
      disEdit: false,
      formData: {
        employeeid: null,
        enddate: '',
        academic: '',
        school: '',
        degrees: '',
        studymethod: '',
        profession: ''
      },
      formRules: {
        // degrees: [
        //   { required: true, message: '请选择学历', trigger: 'change' }
        // ],
        // academic: [
        //   { required: true, message: '请选择学位', trigger: 'change' }
        // ],
        // enddate: [
        //   { required: true, message: '请输入结束时间', trigger: 'change' }
        // ],
        // school: [
        //   { required: true, message: '请输入毕业学校', trigger: 'change' }
        // ]
      }
    }
  },
  computed: {
    ...mapGetters(['educationBeforeWork', 'queryData', 'baseInfo', 'zidianInfo']),
    columns () {
      let { academic, degrees, studymethod } = this.zidianInfo
      return [
        { title: '学历',
          dataIndex: 'academic',
          key: '1',
          customRender: (text, row, index) => {
            let option = academic.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '学位',
          dataIndex: 'degrees',
          key: '2',
          customRender: (text, row, index) => {
            let option = degrees.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '学习形式',
          dataIndex: 'studymethod',
          key: '3',
          customRender: (text, row, index) => {
            let option = studymethod.filter(item => item.keyy == text)[0]
            return option ? option.valuee : ''
          }
        },
        { title: '毕业学校', dataIndex: 'school', key: '4' },
        { title: '专业', dataIndex: 'profession', key: '5' },
        { title: '结束时间', dataIndex: 'enddate', key: '6' },
        {
          title: '操作',
          key: 'operation',
          fixed: 'right',
          width: 100,
          scopedSlots: { customRender: 'operation' }
        }
      ]
    }
  },
  watch: {
    queryData: {
      handler: function (data) {
        if (data) {
          this.pageStatus = data.pageStatus
          this.formData.employeeid = data.employeeid
        }
      },
      immediate: true
    },
    educationBeforeWork: function (data) {
      if (data && data.length > 0) {
        data.forEach(item => {
          item.enddate = item.enddate ? item.enddate.split(' ')[0] : ''
        })
        this.tableData = data
      } else {
        this.tableData = []
      }
    }
  },
  mounted () {},
  methods: {
    ...mapActions(['getSearch']),
    // 初始化时间
    getTime,
    enddateChange (date) {
      this.formData.enddate = date
    },
    // 新增
    clickAdd (type) {
      this.btnType = type
      this.coreVisible = true
      this.formData.id = null
      this.resetForm()
    },
    editRow (record, type) {
      this.btnType = type
      this.coreVisible = true
      this.formData = {...this.formData, ...record}
      this.formData.academic = this.formData.academic ? this.formData.academic.toString() : ''
      this.formData.degrees = this.formData.degrees ? this.formData.degrees.toString() : ''
      this.formData.studymethod = this.formData.studymethod ? this.formData.studymethod.toString() : ''
    },
    getname (value, name) {
    },
    // 选中
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
    // 删除
    deleteRow () {
      if (this.selectedRowKeys.length === 0) {
        this.$message.warning('请勾选您要删除的记录！')
        return false
      } else {
        let that = this
        this.$confirm({
          title: '确定删除选中记录吗?',
          content: '',
          onOk () {
            that.$delete('system/education/before/work/delete/' + that.selectedRowKeys.toString()).then(res => {
              if (res.data.status === 'OK') {
                that.$message.success('删除成功！')
                that.$store.dispatch('getSearch', {vm: that, id: that.formData.employeeid})
                // 需要在回调查询解救
              }
            }).catch(err => {
              that.$message.warning(err)
            })
          },
          onCancel () {
            that.$message.warning('取消删除！')
          }
        })
      }
    },
    // 新增确认弹框
    handleOk (e) {
      this.coreVisible = false
    },
    // 新增保存
    onSubmit () {
      this.$refs.formRules.validate(valid => {
        if (valid) {
          let params = {}
          params.id = this.btnType === 'addBtn' ? null : this.formData.id
          params.employeeid = this.formData.employeeid
          params.enddate = this.formData.enddate
          params.academic = this.formData.academic
          params.degrees = this.formData.degrees
          params.school = this.formData.school
          params.profession = this.formData.profession
          params.studymethod = this.formData.studymethod
          let http = this.btnType === 'addBtn' ? this.$post : this.$put
          http('system/education/before/work', {...params}).then(res => {
            if (res.data.status === 'OK') {
              this.$store.dispatch('getSearch', {vm: this, id: this.formData.employeeid})
              this.resetForm()
              this.coreVisible = false
              this.$message.success(this.btnType === 'addBtn' ? '新增成功！' : '修改成功！')
            }
          }).catch(err => {
            this.$message.warning(err)
          })
        } else {
          return false
        }
      })
    },
    // 重置
    resetForm () {
      this.formData.enddate = null
      this.formData.degrees = null
      this.formData.academic = null
      this.formData.school = null
      this.formData.studymethod = null
      this.formData.profession = null
    }
  }
}
</script>
<style lang="less" scoped>
  .actionBtn {
    margin-bottom: 10px;
    text-align: right;
  }
</style>
