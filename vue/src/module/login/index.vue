<template>
  <el-card v-loading="loading" style="width: 50vw; margin: 0 auto">
    <el-input v-model="payload.user.username" clearable placeholder="用户名" />
    <br /><br />
    <el-input v-model="payload.user.password" type="password" clearable placeholder="密码" />
    <br /><br />
    <el-button @click="authenticate">auth</el-button>
  </el-card>
</template>
<script setup lang="ts">
  import { reactive } from 'vue';
  import { $axios } from '@shared/axios/enhanced.ts';
  import { API } from '@/src/api/api.ts';
  import { $message } from '@shared/message/messaging.ts';
  import { $router } from '@/src/router/router.tsx';
  import type { UserLogin } from '@/src/type/User.ts';

  const payload = reactive({
    user: { username: '', password: '' } as Required<UserLogin>,
  });

  const { execute: authenticate, loading } = $axios.call(
    () => API.authenticate({ ...payload.user }),
    {
      onFinally: async ([res, _err]) => {
        if (res) {
          $message.preset('success')({
            message: 'Authenticated successfully.',
          });
          await $router.push('/workbench');
        }
      },
    },
  );
</script>
