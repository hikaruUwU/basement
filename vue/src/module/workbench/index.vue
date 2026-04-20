<template>
  <el-text @click="activate">workbench</el-text>
</template>
<script setup lang="ts">
  import { onMounted } from 'vue';
  import {$notify} from "@shared/notification/notify.ts";

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
