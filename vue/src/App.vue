<template>
  <el-button @click="execute"/>
  <router-view />
</template>
<script setup lang="ts">
import {$axios} from "./service/axios/enhanced.ts";
import {$api} from "./http/api.ts";

const { execute} = $axios({
  url : $api.module.user.login,
  method: 'post',
  data: {
    username: 'admin',
    password: '123456'
  }
})
</script>
