<template>
  <el-text @click="activate">root</el-text>
</template>
<script setup lang="ts">
  import { onMounted } from 'vue';
  import { $notify } from '@shared/notification/notify.ts';

  const activate = () => {
    $notify.preset('success')({
      message: 'root',
    });
  };

  onMounted(() => {
    activate();
  });
</script>
