<template>
  <a-select
    show-search
    :value="value"
    placeholder="请输入关键字"
    :style="{width: width + 'px'}"
    :default-active-first-option="false"
    :dropdownMatchSelectWidth="false"
    :show-arrow="false"
    :filter-option="false"
    :not-found-content="null"
    @search="handleSearch"
    @change="handleChange"
    :disabled="disabledName"
    allowClear
  >
    <a-select-option v-for="d in data" :key="d.value">
      {{d.text + ' ' + d.code + ' ' + d.gender + ' ' + d.birth}}
    </a-select-option>
  </a-select>
</template>
<script>
let timeout
let currentValue
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
    keyword: {
      type: Number
    }
  },
  data () {
    return {
      data: [],
      value: ''
    }
  },
  watch: {
    keyword: {
      handler: function (data) {
        this.value = data
      },
      immediate: true
    }
  },
  mounted () {

  },
  methods: {
    fetch (value, callback) {
      if (timeout) {
        clearTimeout(timeout)
        timeout = null
      }
      currentValue = value
      let that = this
      function fake () {
        that.$get(that.url + value).then(d => {
          if (currentValue === value) {
            const result = d.data
            const data = []
            result.forEach(r => {
              data.push({
                value: r.employeeid,
                text: r.employeename,
                code: r.code,
                gender: r.gender,
                birth: r.birth
              })
            })
            callback(data)
          }
        })
      }
      timeout = setTimeout(fake, 500)
    },
    handleSearch (value) {
      this.fetch(value, data => (this.data = data))
    },
    handleChange (value) {
      this.value = value
      this.fetch(value, data => (this.data = data))
      let data = {list: this.data, value: value}
      this.$emit('getSearchValue', {...data})
    }
  }
}
</script>
