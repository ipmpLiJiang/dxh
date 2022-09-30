<template>
  <div class="card-area employeeFiles">
    <div class="employeeFiles__header">
      <div class="employeeFiles__header-return actionBtn">
        <a-button type="primary" @click="goBabck">返回</a-button>
      </div>
      <div class="employeeFiles__header-save actionBtn">
        <a-button @click="exportFile">打印</a-button>
        <a-button type="primary" @click="onSave" :loading="loading">保存</a-button>
      </div>
    </div>
    <div id="employeeFile" class="employeeFiles__connent">
      <h2>干 部 档 案 目 录</h2>
      <table class="table" border="1" cellpadding="0" cellspacing="0">
        <thead>
          <tr class="table-header">
            <th rowspan="2">类<br>号</th>
            <th rowspan="2" class="w-260"><span class="f-c">材料名称</span></th>
            <th colspan="3"><span>材料制成时间</span></th>
            <th rowspan="2">份<br>数</th>
            <th rowspan="2">页<br>数</th>
            <th rowspan="2" class="w-200 f-b">备注</th>
            <th rowspan="2" class="table_td">附件</th>
            <th rowspan="2" class="table_td"></th>
          </tr>
          <tr>
            <th>年</th>
            <th>月</th>
            <th>日</th>
          </tr>
        </thead>
        <tbody v-for="(item, index) in archivesOptions" :key="index">
          <tr>
            <th class="w-100">{{item.firstno}}</th>
            <th class="text-left w-260">{{item.textname}}</th>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td class="table_td"></td>
            <td class="table_td"></td>
          </tr>
          <tr v-for="(temp, i) in item.children" :key="i">
            <td>
              <div class="table_input" v-show="item.isEdit">
                <a-input v-model="temp.sortno" placeholder="请输入内容"></a-input>
              </div>
              <div class="table_text" v-show="!item.isEdit">{{temp.sortno}}</div>
            </td>
            <td class="text-left w-260">
              <div class="table_input" v-show="item.isEdit">
                <a-input v-model="temp.textname" :max-length="25" placeholder="请输入内容"></a-input>
              </div>
              <div class="table_text" v-show="!item.isEdit">{{temp.textname}}</div>
            </td>
            <td class="w-100">
              <div class="table_input" v-show="item.isEdit">
                <a-input v-model="temp.year" :max-length="4" placeholder="请输入年份"></a-input>
              </div>
              <div class="table_text" v-show="!item.isEdit">{{temp.year}}</div>
            </td>
            <td class="w-100">
              <div class="table_input" v-show="item.isEdit">
                <a-input-number v-model="temp.month" :min="1" :max="12" placeholder="请输入日"></a-input-number>
                <!--<a-select-->
                  <!--v-model="temp.month"-->
                <!--&gt;-->
                  <!--<a-select-option v-for="item in monthOptions" :value="item" :key="item">-->
                    <!--{{item}}-->
                  <!--</a-select-option>-->
                <!--</a-select>-->
              </div>
              <div class="table_text" v-show="!item.isEdit">{{temp.month}}</div>
            </td>
            <td class="w-100">
              <div class="table_input" v-show="item.isEdit">
                <a-input-number v-model="temp.date" :min="1" :max="dayCounts" placeholder="请输入日"></a-input-number>
                <!--<a-select-->
                  <!--v-model="temp.date"-->
                <!--&gt;-->
                  <!--<a-select-option v-for="item in dateOptions" :value="item" :key="item">-->
                    <!--{{item}}-->
                  <!--</a-select-option>-->
                <!--</a-select>-->
              </div>
              <div class="table_text" v-show="!item.isEdit">{{temp.date}}</div>
            </td>
            <td>
              <div class="table_input" v-show="item.isEdit">
                <a-input-number v-model="temp.count" :min="1" placeholder="请输入份数"></a-input-number>
              </div>
              <div class="table_text" v-show="!item.isEdit">{{temp.count}}</div>
            </td>
            <td>
              <div class="table_input" v-show="item.isEdit">
                <a-input-number v-model="temp.page" :min="1" placeholder="请输入页数"></a-input-number>
              </div>
              <div class="table_text" v-show="!item.isEdit">{{temp.page}}</div>
            </td>
            <td class="w-200">
              <div class="table_input" v-show="item.isEdit">
                <a-input v-model="temp.memo" :max-length="25" placeholder="请输入备注"></a-input>
              </div>
              <div class="table_text" v-show="!item.isEdit">{{temp.memo}}</div>
            </td>
            <td class="table_td">
              <div v-if="!item.isEdit">
                <a
                  :href="baseurl+temp.sername"
                  v-if="temp.sername!=null && temp.sername !=''"
                  target="_blank"
                >{{temp.filename}}</a>
              </div>
              <div v-else>
                <a-button
                  type="dashed"
                  block
                  @click="openFile(temp)"
                >
                  {{temp.sername!=null &&temp.sername !=''?'已上传':'上传' }}
                </a-button>
              </div>
            </td>
            <td class="table_td table_delete">
              <a-button class="actionBtn" plain @click="onDelete(item, temp)">删除</a-button>
            </td>
          </tr>
          <tr class="table_tr">
            <td colspan="9" class="table_add">
              <a-button class="actionBtn mr-10" plain @click="onAdd(item)">新增</a-button>
              <a-button v-if="item.children&&item.children.length>0" class="actionBtn" plain @click="onEdit(item)">修改</a-button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <catalog-upload-file
      ref="upFile"
      :fileVisiable="fileVisiable"
      @setFileId="setFileId"
    >
    </catalog-upload-file>
  </div>
</template>

<script>
  import { downloadPDF, previewImg, compare } from '../../utils/filter'
  import iPrint from "@/utils/print";
  import CatalogUploadFile from '../common/CatalogUploadFile'

  export default {
    name: "EmployeeFiles",
    components: {CatalogUploadFile},
    data() {
      return {
        loading: false,
        input: null,
        archivesOptions: [],
        monthOptions: [],
        dateOptions: [],
        dayCounts: 0,
        editRecord: {},
        fileVisiable:false,
        gparName:'catalog',
        baseurl: this.$baseURL
        // code: null,
      }
    },
    watch: {
      "$route.query.id": {
        handler(v) {
          console.log(v, 777)
          // this.code = this.$route.query.code
          v && this.onSeach()
          this.getDate()
        },
        immediate: true,
      },
    },
    mounted() {
    },
    methods: {
      goBabck() {
        this.$router.go(-1);
      },
      openFile (record) {
        this.editRecord = record
        this.fileVisiable = true
        this.$refs.upFile.fetch(
          this.$route.query.id,
          record.filename,
          record.sername,
          this.gparName
        )
      },
      setFileId (filename,sername,fileUrl,type) {
        if(type !== 2){
        this.fileVisiable = false
        }
        if(type==1 || type==2){
          this.editRecord["filename"] = filename
          this.editRecord["sername"] = sername
        }
      },
      onSave() {
        this.loading = true
        let params = this.archivesOptions
        params.forEach(item => {
          item.employeeid = this.$route.query.id
          item.children.forEach(temp => {
            temp.employeeid = this.$route.query.id
          })
        })
        this.$put1('system/catalog/' + this.$route.query.id, params).then(res => {
          if (res.data.status === 'OK') {
            this.loading = false
            this.$message.success('保存成功！')
            this.onSeach()
          }
        }).catch(err => {
          this.$message.warning(err)
          this.loading = false
        })
      },
      onAdd(item) {
        let form = {};
        form.textname = null;
        form.year = null;
        form.month = null;
        form.date = null;
        form.count = null;
        form.page = null;
        form.memo = null;
        form.sortno = null;
        form.parentid = item.id
        this.archivesOptions.forEach(row => {
          row.isEdit = false;
          if (item.id === row.id) {
            row.isEdit = true;
            row.children.push(form)
            form.sortNum = row.children.length
          }
        })
      },
      onEdit(item) {
        let form = {}
        this.archivesOptions.push(form)
        this.archivesOptions.forEach(row => {
          row.isEdit = false;
          if (item.id === row.id) {
            row.isEdit = true;
            row.sortNum = row.children.length
          }
        })
        this.archivesOptions.pop()
      },
      onDelete(item, temp) {
        console.log(item, temp)
        let _this = this
        this.$confirm({
          title: '确定删除选中记录吗?',
          content: '',
          onOk () {
            _this.archivesOptions.forEach(row => {
              row.children = row.children.filter(v => v.id !== temp.id||v.sortNum!==temp.sortNum)
              row.children.forEach((v, i) => {
                v.sortNum = i + 1
              })
            })
            _this.onSave()
            _this.$message.success('删除成功！')

          },
          onCancel () {
            _this.$message.warning('取消删除！')
          }
        })
      },
      // 导出
      exportFile () {
        let flag = this.archivesOptions.some(item =>item.isEdit);
        if (flag) {
          this.$message.warning('您还未保存，请先进行保存！')
          return false;
        }
        // setTimeout(downloadPDF(document.querySelector('#employeeFile'), '档案资料'), 30000);
        let css = `
        h2 {
          text-align: center;
        }
        table {
          border-collapse: collapse;
          margin: 0 20px;
          text-align: center;
          border: none;
        }
        table td ,
        table th{
          border: 1px solid #000000;
          color: #000000;
          height: 36px;
          padding: 2px 10px;
          font-size: 14px;
          width: 100px;
        }
        .table-header th {
          width: 100px;
          background: #eef5f9;
          font-size: 16px;
          font-weight: 600;
        }
        .actionBtn {
          display: none;
        }
        .table_tr {
          display: none;
        }
        .table_td {
          display: none;
        }
        .w-260 {
          width: 260px;
        }
        .text-left {
          text-align: left;
        }
          `;
        let config = {
          headerAndFooter: false,
          size: "A4",
        };
        iPrint(`#employeeFile`, config, css);
      },
      getDate() {
        var date = new Date()
        var year = date.getFullYear()
        var month = date.getMonth() + 1
        var d = new Date(year, month, 0)
        this.dayCounts = d.getDate()

        this.dateOptions = []
        this.monthOptions = []
        for (var i = 0; i <= this.dayCounts - 1; i++) {
          this.dateOptions.push(String(i + 1))
        }
        for (var i = 0; i <= 12; i++) {
          this.monthOptions.push(String(i + 1))
        }
      },
      onSeach() {
        this.$get('system/catalog?employeeid=' + this.$route.query.id).then(res => {
          if (res.data && res.data.length > 0) {
            this.archivesOptions = res.data;
            this.archivesOptions.forEach(item => {
              item.isEdit = false;
              // item && item.children.length > 0 && item.children.sort(compare('sortNum', true));
            })
          }
        })
      },
    }
  }
</script>

<style scoped lang="less">
  .employeeFiles {
    padding-bottom: 20px;
    &__header {
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
    &__connent {
      text-align: center;
      margin-bottom: 20px;
      h2 {
        letter-spacing: 8px;
        margin: 20px auto;
      }
      table {
        /*border: 1px solid #e8e8e8;*/
        border-collapse: collapse;
        width: 60%;
        margin: 0 auto;
        th, td {
          padding: 10px;
          height: 50px;
          min-height: 50px;
          width: 80px;
          min-width: 80px;
        }
        .text-left {
          text-align: left;
          min-width: 200px;
        }
        .table_add {
          text-align: left;
        }
        .w-60 {
          min-width: 60px;
        }
        .w-200 {
          min-width: 200px;
        }
        .w-260 {
          width: 260px;
        }
        .w-100 {
          min-width: 100px;
        }
        .f-c {
          letter-spacing: 8px;
        }
        .f-b {
          letter-spacing: 50px;
        }
      }
    }
  }

</style>
<style lang="less">
  .employeeFiles {
    table {
      border-collapse:collapse;
    }
    .ant-select {
      width: 100%!important;
    }
    .mr-10 {
      margin-right: 10px;
    }
  }
</style>
