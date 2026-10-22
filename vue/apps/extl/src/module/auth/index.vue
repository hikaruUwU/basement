<template>
  <div>TEST</div>

  <input type="text" v-model="data.val" />

  <button type="button" @click="execute">submit</button>
</template>
<script setup lang="ts">
import { reactive } from 'vue';
import { $axios } from '@/core/axios/$axios';
import { $api } from '@/static/api/$api';

const data = reactive({
  val: 'test',
});

const { execute } = $axios($api.checkHealthy).call(() => [data.val, 114514]);
</script>
