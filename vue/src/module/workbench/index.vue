<template>
  <el-button @click="activate">workbench</el-button>
</template>
<script setup lang="ts">
  import { $notify } from '@shared/service/notification/notify.ts';
  import { onMounted } from 'vue';

  const activate = () => {
    $notify.call({
      message: 'workbench',
      ...$notify.preset.success,
    });
  };

  onMounted(() => {
    activate();
  });
</script>
