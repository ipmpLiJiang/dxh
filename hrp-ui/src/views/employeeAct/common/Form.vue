<!--职工基本信息-->
<template>
  <div class="employeeEvt-form" id="components-form-demo-advanced-search">
    <a-form-model class="baseInfoForm" ref="formData" :loading="loading" :model="formData" :rules="formRules"  :label-col="labelCol" :wrapper-col="wrapperCol">
      <div class="actionBtn">
        <a-button type="primary" class="formSave" :loading="saveLoading" @click="submit">
          保存
        </a-button>
      </div>
      <a-row :gutter="24">
        <a-col
          :span="20"
        >
          <a-row :gutter="24">
            <a-col :span="8">
              <a-form-model-item label="系统编号">
                <a-input
                  v-model="formData.employeeid"
                  :disabled="true"
                />
              </a-form-model-item>
            </a-col>
              <a-col :span="8">
                <a-form-model-item label="姓名" prop="employeename">
                  <a-input
                    v-model="formData.employeename"
                    allowClear
                  />
                </a-form-model-item>
              </a-col>
            <a-col :span="8">
              <a-form-model-item label="性别" prop="gender">
                <a-select
                  v-model="formData.gender"
                  placeholder="请选择"
                  allowClear
                >
                  <a-select-option value="男">
                    男
                  </a-select-option>
                  <a-select-option value="女">
                    女
                  </a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="身份证号" prop="idnumber">
                <a-tooltip :trigger="['focus']" placement="topLeft" overlay-class-name="numeric-input">
                  <span v-if="formData.idnumber" slot="title" class="numeric-input-title">
                    {{ formData.idnumber !== '-' ? formatNumber(formData.idnumber) : '-' }}
                  </span>
                  <template v-else slot="title">
                    请填写身份证号
                  </template>
                  <a-input
                    v-model="formData.idnumber"
                    :max-length="18"
                    @change="onChange"
                  />
                </a-tooltip>
                <upload-file
                name="filesfzh"
                :baseId="formData.employeeid"
                refTab="employee_sfzh"
                btnTitle="上传身份证号附件"
                ></upload-file>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="联系方式" prop="phonenumber">
                <a-input
                  v-model="formData.phonenumber"
                  :max-length="11"
                  allowClear
                />
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="籍贯" prop="homeaddress">
                <a-input
                  v-model="formData.homeaddress"
                  allowClear
                />
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="出生日期" prop="birth">
                <a-date-picker
                  v-model="formData.birth"
                  :format="dateFormat"
                  :valueFormat="dateFormat"
                  @change="birthChange"
                />
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="参加工作时间" prop="workdate" allowClear>
                <a-date-picker
                  v-model="formData.workdate"
                  :format="dateFormat"
                  :valueFormat="dateFormat"
                  @change="workdateChange"
                />
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="来院时间" prop="comedate">
                <a-date-picker
                  v-model="formData.comedate"
                  :format="dateFormat"
                  :valueFormat="dateFormat"
                  @change="comedateChange"
                />
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="民族" prop="nation">
                <a-input
                  v-model="formData.nation"
                  allowClear
                />
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="入党时间" prop="rddate" allowClear>
                <a-date-picker
                  v-model="formData.rddate"
                  :format="dateFormat"
                  :valueFormat="dateFormat"
                  @change="rddateChange"
                />
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="入团时间" prop="rtdate" allowClear>
                <a-date-picker
                  v-model="formData.rtdate"
                  :format="dateFormat"
                  :valueFormat="dateFormat"
                  @change="rtdateChange"
                />
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="政治面貌" prop="politicaloutlook">
                <a-select
                  v-model="formData.politicaloutlook"
                  :dropdownMatchSelectWidth="false"
                  allowClear
                >
                  <a-select-option v-for="item in politicaloutlookOptions" :key="item.keyy" :value="item.keyy">
                    {{item.valuee}}
                  </a-select-option>
                </a-select>
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="离职时间" prop="lzdate" allowClear>
                <a-date-picker
                  v-model="formData.lzdate"
                  :format="dateFormat"
                  :valueFormat="dateFormat"
                  @change="lzdateChange"
                />
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="在职档案号" prop="workfilenum">
                <a-input
                  v-model="formData.workfilenum"
                  allowClear
                />
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="返聘时间" prop="fpdate" allowClear>
                <a-date-picker
                  v-model="formData.fpdate"
                  :format="dateFormat"
                  :valueFormat="dateFormat"
                  @change="fpdateChange"
                />
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="退休档案号" prop="retirefilenum">
                <a-input
                  v-model="formData.retirefilenum"
                  allowClear
                />
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="退休时间" prop="txdate" allowClear>
                <a-date-picker
                  v-model="formData.txdate"
                  :format="dateFormat"
                  :valueFormat="dateFormat"
                  @change="txdateChange"
                />
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="天堂档案号" prop="deathfilenum">
                <a-input
                  v-model="formData.deathfilenum"
                  allowClear
                />
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="死亡时间" prop="swdate" allowClear>
                <a-date-picker
                  v-model="formData.swdate"
                  :format="dateFormat"
                  :valueFormat="dateFormat"
                  @change="swdateChange"
                />
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="备注" prop="memo">
                <a-input
                  type="texteare"
                  v-model="formData.memo"
                  allowClear
                />
              </a-form-model-item>
            </a-col>
              <a-col :span="8">
                  <a-form-model-item label="是否通过规培" prop="gpflag">
                      <a-select
                          v-model="formData.gpflag"
                          allowClear
                      >
                          <a-select-option value='1'>
                              通过
                          </a-select-option>
                          <a-select-option value='2'>
                              未通过
                          </a-select-option>
                      </a-select>
                  </a-form-model-item>
              </a-col>
              <a-col :span="8">
              <a-form-model-item label="执业资格证编号" prop="zyzgzbh">
                <a-input
                  v-model="formData.zyzgzbh"
                  allowClear
                />
              </a-form-model-item>
            </a-col>
            <a-col :span="8">
              <a-form-model-item label="执业范围" prop="zyfw">
                <a-input
                  v-model="formData.zyfw"
                  allowClear
                />
                <upload-file
                name="filezyzs"
                :baseId="formData.employeeid"
                refTab="employee_zyzs"
                btnTitle="上传执业证书附件"
                ></upload-file>
              </a-form-model-item>
            </a-col>
              <a-col :span="8" v-if="formData.gpflag==1">
                  <a-form-model-item label="规培通过时间" prop="gpdate" allowClear>
                      <a-date-picker
                          v-model="formData.gpdate"
                          :format="dateFormat"
                          :valueFormat="dateFormat"
                      />
                  </a-form-model-item>
              </a-col>
          </a-row>
        </a-col>
        <a-col :span="4" class="upLoadCol">
          <a-form-model-item prop="picturepath">
            <a-upload
              name="avatar"
              list-type="picture-card"
              class="avatar-uploader"
              :show-upload-list="false"
              :before-upload="beforeUpload"
              accept="image/jpeg,image/jpg,image/png"
              :customRequest="upLoad"
              @change="handleChange"
            >
              <img v-if="formData.picturepath" :src="formData.picturepath" alt="avatar" />
              <div v-else>
                <a-icon :type="loading ? 'loading' : 'plus'" />
              </div>
              <div v-if="!formData.picturepath" class="ant-upload-text">
                请上传照片
              </div>
            </a-upload>
          </a-form-model-item>
        </a-col>
      </a-row>
    </a-form-model>
  </div>
</template>
<script>
import { mapGetters, mapActions } from 'vuex'
import { getTime, formatNumber, politicaloutlookOptions } from '../../../utils/filter'
import UploadFile from '../../common/UploadFile'
function getBase64 (img, callback) {
  const reader = new FileReader()
  reader.addEventListener('load', () => callback(reader.result))
  reader.readAsDataURL(img)
}
export default {
  components: { UploadFile },
  data () {
    // 验证身份号
    let idnumberReg = /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/
    let validateIdnumber = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请填写身份证号'))
      } else if (!idnumberReg.test(value)) {
        callback(new Error('输入身份证号不合法'))
      } else {
        if (value !== '' && idnumberReg.test(value)) {
          callback()
        }
      }
    }
    return {
      saveLoading: false,
      pageStatus: '',
      labelCol: { span: 9 },
      wrapperCol: { span: 15 },
      dateFormat: 'YYYY-MM-DD',
      politicaloutlookOptions,
      query: {},
      formData: {
        employeename: '',
        employeeid: null,
        gender: '',
        idnumber: '',
        phonenumber: '',
        workdate: '',
        comedate: '',
        homeaddress: '',
        nation: '',
        birth: '',
        picturepath: '',
        rddate: '',
        rtdate: '',
        politicaloutlook: '',
        workfilenum: '',
        retirefilenum: '',
        deathfilenum: '',
        memo: '',
        txdate: '',
        fpdate: '',
        lzdate: '',
        swdate: '',
        gpflag: null,
        gpdate: null,
        zyzgzbh: '',
        zyfw: '',
      },
      loading: false,
      picFile: {},
      formRules: {
        gender: [
          { required: true, message: '请输入员工性别', trigger: 'change' }
        ],
        idnumber: [
          { required: true, validate: validateIdnumber, trigger: 'change' }
        ],
        employeename: [
          { required: true, message: '请填写员工姓名', trigger: 'change' }
        ],
        phonenumber: [
          { required: true, message: '请填写员工联系方式', trigger: 'change' }
        ],
        homeaddress: [
          { required: true, message: '请填写员工籍贯', trigger: 'change' }
        ],
        comedate: [
          { required: true, message: '请填写员工来院时间', trigger: 'change' }
        ],
        workdate: [
          { required: true, message: '请填写员工参加工作时间', trigger: 'change' }
        ],
        nation: [
          { required: true, message: '请填写员工所属民族', trigger: 'change' }
        ],
        birth: [
          { required: true, message: '请填写员工出生日期', trigger: 'change' }
        ]
        // picturepath: [
        //   { required: true, message: '请上传照片', trigger: 'change' }
        // ]
      }
    }
  },
  computed: {
    ...mapGetters(
      ['baseInfo', 'queryData']
    )
  },
  mounted () {
    this.formData = {...this.formData, ...this.baseInfo}
    this.formData.gpflag = this.formData.gpflag?this.formData.gpflag.toString():""
  },
  watch: {
    queryData: {
      handler: function (data) {
        this.query.startdate = data.startdate
        this.pageStatus = data.pageStatus
        this.formData.employeeid = data.employeeid
      },
      immediate: true
    },
    baseInfo: function (data) {
      if (data) {
        this.formData = {...this.formData, ...data}
      } else {
        this.formData = {}
      }
    }
  },
  methods: {
    ...mapActions(
      ['getSearch']
    ),
    // 初始化时间
    getTime,
    formatNumber,
    workdateChange (date) {
      this.formData.workdate = date
    },
    comedateChange (date) {
      this.formData.comedate = date
    },
    // 入党时间
    rddateChange (date) {
      this.formData.rddate = date
    },
    // 生日
    birthChange (date) {
      this.formData.birth = date
    },
    // 入团时间
    rtdateChange (date) {
      this.formData.rtdate = date
    },
    txdateChange (date) {
      this.formData.txdate = date
    },
    lzdateChange (date) {
      this.formData.lzdate = date
    },
    fpdateChange (date) {
      this.formData.fpdate = date
    },
    swdateChange (date) {
      this.formData.swdate = date
    },
    handleChange (info) {
      if (info.file.status === 'uploading') {
        this.loading = true
        return
      }
      if (info.file.status === 'done') {
        getBase64(info.file.originFileObj, imageUrl => {
          this.formData.picturepath = imageUrl
          this.loading = false
        })
      }
    },
    // 图片上传之前的校验
    beforeUpload (file) {
      const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png'
      if (!isJpgOrPng) {
        this.$message.error('请上传正确的照片格式！')
      }
      const isLt2M = file.size / 1024 / 1024 < 10
      if (!isLt2M) {
        this.$message.error('上传的照片不能超过10MB!')
      }
      this.picFile = file
      return isJpgOrPng && isLt2M
    },
    // 自定义上传
    upLoad () {
      const formImgData = new FormData()
      formImgData.append('file', this.picFile)
      this.$upload('system/employee/picture/upload/baseInfo', formImgData).then(res => {
        if (res.data) {
          this.formData.picturepath = res.data.path
          this.$refs['formData'].clearValidate('picturepath')
          this.loading = false
        }
      })
    },
    // 提交基本信息
    submit () {
      this.$refs.formData.validate(valid => {
        if (valid) {
          this.saveLoading = true
          delete this.formData.xtUpdatedate
          if (!this.formData.employeeid) {
            this.$post('system/employee', this.formData).then(res => {
              let result = res.data
              if (result.status === 'OK') {
                this.saveLoading = false
                this.formData.employeeid = result.data.employeeid
                this.$message.success('保存成功!')
                this.$store.dispatch('getSearch', {vm: this, id: this.formData.employeeid})
                // 新增时没有id, 根据是否有id控制新增删除按钮
                this.$store.dispatch('getQuery', {employeeid: this.formData.employeeid, pageStatus: this.pageStatus, startdate: this.query.startdate})
              } else {
                this.$message.warning('保存失败!')
                this.saveLoading = false
              }
            }).catch(() => {
              this.$message.warning('请勿重复提交！')
              this.saveLoading = false
            })
          } else {
            // 修改
            this.$put('system/employee', this.formData).then(res => {
              if (res.data.status === 'OK') {
                this.saveLoading = false
                this.$message.success('保存成功!')
                this.$store.dispatch('getSearch', {vm: this, id: this.formData.employeeid, pageStatus: this.pageStatus, startdate: this.query.startdate})
              } else {
                this.$message.warning('保存失败!')
                this.saveLoading = false
              }
            }).catch(err => {
              this.$message.warning(err)
              this.saveLoading = false
            })
          }
        } else {
          return false
        }
      })
    },
    onChange (e) {
      const { value } = e.target
      const reg = /^-?[0-9]*(\.[0-9]*)?$/
      if ((!isNaN(value) && reg.test(value)) || value === '' || value === '-') {
        this.formData.idnumber = value
      }
    }
  }
}
</script>
<style lang="less" scoped>
  .ant-advanced-search-form {
    padding: 24px;
    background: #fbfbfb;
    border: 1px solid #d9d9d9;
    border-radius: 6px;
  }

  .ant-advanced-search-form .ant-form-item {
    display: flex;
  }

  .ant-advanced-search-form .ant-form-item-control-wrapper {
    flex: 1;
  }

  #components-form-demo-advanced-search .ant-form {
    max-width: none;
  }
  #components-form-demo-advanced-search .search-result-list {
    margin-top: 16px;
    border: 1px dashed #e9e9e9;
    border-radius: 6px;
    background-color: #fafafa;
    min-height: 200px;
    text-align: center;
    padding-top: 80px;
  }

  .ant-upload-select-picture-card i{
    font-size: 32px;
    color: #999;
  }
  .avatar-uploader > .ant-upload{
    img {
      width: 100%;
      height: auto;
    }
  }
  .ant-upload-select-picture-card{
    margin: 0!important;
    height: 150px!important;
  }
  .ant-upload-select-picture-card .ant-upload-text{
    margin-top: 8px;
    color: #666;
  }
  .baseInfoForm{
    position: relative;
  }
  .formSave{
    position: absolute;
    top: -80px;
    right: 0;
  }
  .upLoadCol{
    height: 180px;
    /*margin: 0!important;*/
    .ant-form-item{
      height: 180px;
    }
    .ant-form-item-control-wrapper{
      width: 100%!important;
      height: 100%!important;
    }
  }
</style>
<style>
  .employeeEvt-form .upLoadCol .ant-upload-select-picture-card{
      margin: 0!important;
      width: 120px!important;
      height: 180px!important;
    }
  .employeeEvt-form .upLoadCol .ant-form-item-control-wrapper{
      width: 100%!important;
      height: 100%!important;
      float: right;
    }
  .employeeEvt-form .upLoadCol .ant-upload.ant-upload-select-picture-card > .ant-upload{
      padding: 0!important;
      width: 180px!important;
    }
</style>
