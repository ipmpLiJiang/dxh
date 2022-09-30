<!--家庭信息-->
<template>
  <div>
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
      :data-source="tableData"
      :rowKey="record => record.id"
      bordered
      :pagination="false"
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
        <a-form-model-item label="称谓" prop="cw">
          <!--<a-input-->
            <!--v-model="formData.cw"-->
            <!--allowClear-->
          <!--/>-->
            <a-select v-model="formData.cw" allowClear>
                <a-select-option v-for="item in zidianInfo.cw" :value="item.keyy" :key="item.keyy">
                    {{item.valuee}}
                </a-select-option>
            </a-select>
        </a-form-model-item>
        <a-form-model-item label="姓名" prop="name">
          <a-input
            v-model="formData.name"
            allowClear
          />
        </a-form-model-item>
        <a-form-model-item label="性别" prop="gender" allowClear>
          <a-select v-model="formData.gender">
            <a-select-option value="男">
              男
            </a-select-option>
            <a-select-option value="女">
              女
            </a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item label="出生年月" prop="birth">
          <a-date-picker
            v-model="formData.birth"
            :format="dateFormat"
            :valueFormat="dateFormat"
            @change="birthdayChange"
          />
        </a-form-model-item>
        <a-form-model-item label="工作岗位及职务" prop="job">
          <a-input
            v-model="formData.job"
            allowClear
          />
        </a-form-model-item>
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

export default {
  data () {
    return {
      dateFormat: 'YYYY-MM-DD',
      coreVisible: false,
      tableData: [],
      selectedRowKeys: [],
      labelCol: { span: 6 },
      wrapperCol: { span: 14 },
      other: '',
      btnType: 'addBtn',
      formData: {
        cw: '',
        name: '',
        gender: '',
        birth: '',
        job: '',
        employeeid: null
      },
      coreRules: {
        cw: [
          { required: true, message: '请输入称谓', trigger: 'change' }
        ],
        name: [
          { required: true, message: '请输入姓名', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    ...mapGetters(['family', 'queryData', 'baseInfo', 'zidianInfo']),
      columns(){
        let { cw } = this.zidianInfo
        return [
            { title: '称谓', dataIndex: 'cw',
                customRender: (text, row, index) => {
                    let option = cw.filter(item => item.keyy == text)[0]
                    return option ? option.valuee : ''
                }
            },
            { title: '姓名', dataIndex: 'name', },
            { title: '性别', dataIndex: 'gender', },
            { title: '出生年月', dataIndex: 'birth', },
            { title: '工作单位及职务', dataIndex: 'job', },
            { title: '联系电话', dataIndex: 'tel', },
            {
                title: '操作',
                key: 'operation',
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
          this.formData.employeeid = data.employeeid
        }
      },
      immediate: true
    },
    family: {
      handler: function (data) {
        if (data && data.length > 0) {
          this.tableData.forEach(item => {
            item.birth = item.birth ? item.birth.split(' ')[0] : ''
          })
          this.tableData = data
        } else {
          this.tableData = []
        }
      },
      immediate: true
    }
  },
  mounted () {},
  methods: {
      ...mapActions(['getSearch']),

    // 入党时间
    birthdayChange (date) {
      this.formData.birth = date
      console.log(date)
    },
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
    },
    onSelectChange (selectedRowKeys) {
      this.selectedRowKeys = selectedRowKeys
    },
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
            that.$delete('system/family/delete/' + that.selectedRowKeys.toString()).then(res => {
              if (res.data.status === 'OK') {
                that.$message.success('删除成功！')
                that.selectedRowKeys = []
                that.$store.dispatch('getSearch', {vm: that, id: that.formData.employeeid})
                // 需要在回调查询解救
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
      }
    },
    handleOk (e) {
      this.coreVisible = false
    },
    onSubmit () {
      this.$refs.coreRules.validate(valid => {
        if (valid) {
          let params = {}
          params.id = this.btnType === 'addBtn' ? null : this.formData.id
          params.employeeid = this.formData.employeeid
          params.cw = this.formData.cw
          params.name = this.formData.name
          params.gender = this.formData.gender
          params.birth = this.formData.birth
          params.job = this.formData.job
          let http = this.btnType === 'addBtn' ? this.$post : this.$put
          http('/system/family', {...params}).then(res => {
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
    resetForm () {
      this.formData.cw = null
      this.formData.name = null
      this.formData.gender = null
      this.formData.birth = null
      this.formData.job = null
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
