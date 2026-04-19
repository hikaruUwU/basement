<template>
  <el-text>workbench</el-text>
</template>
<script setup lang="ts">
  import { $notify } from '@shared/service/notification/notify.ts';

  $notify.call({
    message: 'workbench',
    ...$notify.preset.success,
  });
</script>
