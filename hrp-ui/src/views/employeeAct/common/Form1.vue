<template>
    <div>
        <div class="actionBtn">
            <a-button v-if="$route.query.id" type="primary" class="editable-add-btn editable-margin-right" @click="onSave('addBtn')">
                保存
            </a-button>
        </div>
        <a-form-model
            ref="ruleForm"
            :model="formData"
        >
            <a-form-model-item label="" lebal-width="0">
                <a-textarea
                    v-model="formData.description"
                    :auto-size="{ minRows: 10 }"
                    allowClear
                />
            </a-form-model-item>
        </a-form-model>
    </div>
</template>
<script>
import { mapGetters, mapActions } from 'vuex'
export default {
  data () {
    return {
      formData: {
        description: '',
        employeeid: this.$route.query.id
      },
      rules: {}
    }
  },
  computed: {
    ...mapGetters(
      ['workExperiences']
    )
  },
  watch: {
    workExperiences: {
      handler (v) {
        if (v && v.length > 0) {
          this.formData.description = v[0].description
        }
      },
      immediate: true,
    }
  },
  mounted () {
  },
  methods: {
    ...mapActions(
      ['getSearch']
    ),
    onSave () {
      this.$post('system/work-experience', this.formData).then(res => {
        this.$message.success('保存成功!')
        this.$store.dispatch('getSearch', {vm: this, id: this.$route.query.id})
      }).catch(() => {
        this.$message.warning('保存失败!')
      })
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
