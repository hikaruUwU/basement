<template>
  <el-text>{{ new Date() }}</el-text>
  <el-divider/>
  <el-button plain @click="execute" :loading="loading">request</el-button>
</template>
<script setup lang="ts">
import {$axios} from '@frontier/shared/axios'
import {$api} from '@frontier/api/api'

const {execute, loading} = $axios.call(
    () => $api.demo('username'), {}
)

</script>
