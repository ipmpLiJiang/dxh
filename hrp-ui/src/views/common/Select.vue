
<template>
  <a-select
    v-model="selectedVal"
    :loading="dataLoading"
    placeholder="输入搜索内容"
    :style="{width: width + 'px'}"
    show-search
    option-filter-prop="children"
    @search="handleSearch"
    @popupScroll="handlePopupScroll"
    @change="handleSelect"
    dropdownClassName="ant-select-drop"
    :disabled="disabledName"
    allowClear
  >
    <a-select-option v-for="d in renderedOptions" :value="d.employeeid" :key="d.employeeid">
      {{d.employeename + ' ' + d.code + ' ' + d.gender + ' ' + d.birth}}
    </a-select-option>
  </a-select>
</template>
<script>
import debounce from 'lodash/debounce'
const LOAD_NUM = 20
export default {
  props: {
    url: {
      type: String,
      default: 'system/employee?search=' + ''
    },
    disabledName: {
      type: Boolean,
      default: false
    },
    width: {
      // type: Number
    },
    keyword: {}
  },
  data () {
    return {
      selectedVal: null, // Select框选中的值
      oriDataList: [], // 原数据列表 -- 从接口获取
      dataLoading: false, // 原数据列表的加载状态 -- 接口的响应状态
      searchVal: '', // 搜索的内容
      filterDataList: [], // 过滤的数据列表 -- 从dataList中过滤出含搜索内容的数据
      renderedOptions: [] // 已渲染的下拉列表
    }
  },
  watch: {
    keyword: {
      handler (data) {
        this.selectedVal = data
        this.getDataList()
      },
      immediate: true
    }
  },
  mounted () {
    this.getDataList()
  },
  methods: {
    getDataList () {
      this.dataLoading = true
      this.$get(this.url + this.selectedVal).then(res => {
        this.dataLoading = false
        this.oriDataList = res.data // 该接口返回的数据存放在res.result（根据实际自行修改）
        this.renderedOptions = this.oriDataList.slice(0, LOAD_NUM)
      }).catch(err => {
        this.dataLoading = false
      })
    },
    // 文本框值变化时触发 -- 从数据源中过滤出含搜索内容的数据，并取过滤结果的前n条作为下拉列表的可选项
    handleSearch (val) {
      this.selectedVal = this.searchVal = val
      let filterList = []
      if (val) {
        filterList = (this.oriDataList).filter(item => item.employeename.indexOf(val) > -1)
        if(filterList.length == 0) {
          filterList = (this.oriDataList).filter(item => item.code.indexOf(val) > -1)
        }
      } else {
        filterList = this.oriDataList
      }
      this.filterDataList = filterList
      this.renderedOptions = filterList.length < LOAD_NUM ? filterList : filterList.slice(0, LOAD_NUM)
    },
    // 滚动时触发（防止抖动）
    handlePopupScroll: debounce(function () {
      if (this.searchVal === '') {
        this.loadMoreData(this.oriDataList)
      } else {
        this.loadMoreData(this.filterDataList)
      }
    }, 400),
    // 加载更多数据到select框
    loadMoreData (dataList) {
      const renderedLen = this.renderedOptions.length // 已渲染的下拉列表长度
      const totalLen = dataList.length // 当前数据源的长度
      let addList = []
      if (renderedLen < totalLen) {
        if (renderedLen + LOAD_NUM <= totalLen) {
          addList = dataList.slice(renderedLen, renderedLen + LOAD_NUM)
        } else {
          addList = dataList.slice(renderedLen, renderedLen + (totalLen % LOAD_NUM))
        }
        this.renderedOptions = (this.renderedOptions).concat(addList)
      }
    },
    // 被选中时调用，参数为选中项的 value (或 key) 值
    handleSelect (val) {
        if (val == undefined) {
            val = null
        }
      this.searchVal = val
      let selectedArr = []
      if (this.searchVal) {
        selectedArr = (this.oriDataList).filter(item => item.employeeid === val) // 从数据源中过滤出下拉框选中的值，并返回一个数组
        const restList = (this.oriDataList).filter(item => item.employeeid !== val) // 从数据源中过滤出其他的值，返回一个数组
        const newList = selectedArr.concat(restList).slice(0, LOAD_NUM) // 将选中的元素放到下拉列表的第一位
        this.renderedOptions = newList // 更新已渲染的下拉列表
        this.oriDataList = selectedArr.concat(restList) // 更新数据源
        // this.searchVal = '' // 因为触发handleSelect函数时，会自动清空用户输入的内容。因此，searchVal需要重置。
      } else {
        this.searchVal = null
      }
      let data = {list: selectedArr, value: val}
      this.$emit('getSearchValue', {...data})
    }
  }
}
</script>
<style lang="less">
    .ant-select-drop {
        width: 250px !important;
    }
</style>
