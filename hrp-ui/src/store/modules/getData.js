// import db from 'utils/localstorage'

export default {
  state: {
    resumeInfo: {}, // 员工所有信息对象
    baseInfo: {}, // 基础信息
    codeInfo: '',
    coreInfo: [], // 核心信息
    family: [], // 家庭信息
    educationInfo: [], // 教育信息
    electronicRecordsInfo: [], // 电子档案
    titleInfo: [], // 职称
    jobTypeInfo: [], // 岗位
    getTitleInfo: [], // 职称
    getGqtitleInfo: [], // 工勤等级
    administrativeLevelInfo: [], // 行政级别
    administrativePostInfo: [], // 行政职务
    contractInfo: [], // 合同管理
    educationBeforeWork: [],
    continueEducation: [],
    honorInfo: [],
    checkInfo: [],
    workExperiences: [],
    queryData: { // 页面信息参数
      employeeid: null,
      employeename: '',
      pageStatus: '',
      startdate: ''
    },
    zidianInfo: {},
    deptOptions: [],
    wardOptions: [],
    rszfwOptions: [],
    joblevelOptions: [],
    dutyOptions: [],
    periodInfo: {},
    kqperiodInfo: {}
  },

  getters: {
    resumeInfo: state => state.resumeInfo,
    queryData: state => state.queryData,
    baseInfo: state => state.resumeInfo.baseInfo,
    codeInfo: state => state.resumeInfo.codeInfo,
    coreInfo: state => state.resumeInfo.coreInfo,
    family: state => state.resumeInfo.family,
    educationInfo: state => state.resumeInfo.educationInfo,
    electronicRecordsInfo: state => state.resumeInfo.electronicRecordsInfo,
    titleInfo: state => state.resumeInfo.titleInfo,
    jobTypeInfo: state => state.resumeInfo.jobTypeInfo,
    getTitleInfo: state => state.resumeInfo.getTitle,
    getGqtitleInfo: state => state.resumeInfo.gqtitle,
    administrativeLevelInfo: state => state.resumeInfo.administrativeLevelInfo,
    administrativePostInfo: state => state.resumeInfo.administrativePostInfo,
    contractInfo: state => state.resumeInfo.contract,
    educationBeforeWork: state => state.resumeInfo.educationBeforeWork,
    continueEducation: state => state.resumeInfo.continueEducation,
    honorInfo: state => state.resumeInfo.honorInfo,
    checkInfo: state => state.resumeInfo.checkInfo,
    workExperiences: state => state.resumeInfo.workExperiences,
    zidianInfo: state => state.zidianInfo,
    deptOptions: state => state.deptOptions,
    wardOptions: state => state.wardOptions,
    rszfwOptions: state => state.rszfwOptions,
    joblevelOptions: state => state.joblevelOptions,
    dutyOptions: state => state.dutyOptions,
    periodInfo: state => state.periodInfo,
    kqperiodInfo: state => state.kqperiodInfo
  },

  mutations: {
    getzidian (state, data) {
      state.zidianInfo = data
    },
    getdept (state, data) {
      state.deptOptions = data
    },
    getward (state, data) {
      state.wardOptions = data
    },
    getrszfw (state, data) {
      state.rszfwOptions = data
    },
    getjoblevel (state, data) {
      state.joblevelOptions = data
    },
    getduty (state, data) {
      state.dutyOptions = data
    },
    // 查询简历
    getSearch (state, data) {
      state.resumeInfo = data
    },
    getQuery (state, data) {
      state.queryData = data
    },
    getPeriodInfo (state, data) {
      state.periodInfo = data
    },
    getkqPeriodInfo (state, data) {
      state.kqperiodInfo = data
    },
    setRszfw (state, data) {
      state.rszfwOptions = data
    }
  },

  // 现在，你可以通过 store.state 来获取状态对象，以及通过 store.commit 方法触发状态变更：
  actions: {
    getSearch ({commit}, option = {}) {
      let that = option.vm
      that.$get('system/employee/getEmployeeInfo?employeeid=' + option.id).then(res => {
        if (res.data) {
          commit('getSearch', res.data)
        }
      }).catch(() => {
        that.$store.state.baseInfo = {}
        that.$store.state.codeInfo = []
        that.$store.state.family = []
        that.$store.state.educationInfo = []
        that.$store.state.electronicRecordsInfo = []
        that.$store.state.titleInfo = []
        that.$store.state.jobTypeInfo = []
        that.$store.state.getTitleInfo = []
        that.$store.state.administrativeLevelInfo = []
        that.$store.state.administrativePostInfo = []
        that.$store.state.contractInfo = []
        that.$store.state.educationBeforeWork = []
        that.$store.state.continueEducation = []
        that.$store.state.honorInfo = []
        that.$store.state.checkInfo = []
        that.$store.state.workExperiences = []
      })
    },
    getQuery ({commit}, option = {}) {
      commit('getQuery', option)
    },
    getzidian ({commit}, option = {}) {
      let that = option.vm
      that.$get('dict/getDictMap/' + that.$store.state.account.user.deptId).then(res => {
        if (res.data) {
          commit('getzidian', res.data)
        }
      })
    },
    getdept ({commit}, option = {}) {
      let that = option.vm
      that.$get('system/department?parentDeptId=' + option.id).then(res => {
        if (res.data) {
          commit('getdept', res.data)
        }
      })
    },
    getward ({commit}, option = {}) {
      let that = option.vm
      that.$get('system/department?parentDeptId=' + option.id).then(res => {
        if (res.data) {
          commit('getward', res.data)
        }
      })
    },
    getrszfw ({commit}, option = {}) {
      let that = option.vm
      that.$get('system/rszfw/detail?id=' + option.id).then(res => {
        if (res.data) {
          commit('getrszfw', res.data)
        }
      })
    },
    getjoblevel ({commit}, option = {}) {
      let that = option.vm
      that.$get('system/job/level/detail?id=' + option.id).then(res => {
        if (res.data) {
          commit('getjoblevel', res.data)
        }
      })
    },
    getduty ({commit}, option = {}) {
      let that = option.vm
      that.$get('system/duty/detail?id=' + option.id).then(res => {
        if (res.data) {
          commit('getduty', res.data)
        }
      })
    },
    getPeriodInfo ({commit}, option = {}) {
      let that = option.vm
      that.$get('system/period').then(res => {
        if (res.data) {
          commit('getPeriodInfo', res.data)
        }
      })
    },
    getkqPeriodInfo ({commit}, option = {}) {
      let that = option.vm
      that.$get('system/kq/period').then(res => {
        if (res.data) {
          commit('getkqPeriodInfo', res.data)
        }
      })
    },
    setrszfw ({commit}) {
      commit('setRszfw', [])
    }
  }
}
