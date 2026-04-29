<template>
  <el-table></el-table>

  <el-card style="width: 50vw; margin: 0 auto">
    {{ load.date }}
  </el-card>
</template>
<script setup lang="ts">
  import { onMounted, onUnmounted, shallowReactive } from 'vue';

  const load = shallowReactive({
    date: new Date() as Date,
    watcher: undefined as ReturnType<typeof setTimeout> | undefined,
  });

  onMounted(() => {
    load.watcher = setInterval(() => {
      load.date = new Date();
    }, 1000);
  });

  onUnmounted(() => {
    clearTimeout(load.watcher);
  });
</script>
