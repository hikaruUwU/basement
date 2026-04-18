<template></template>
<script setup lang="ts">
  import { $notify } from '@shared/service/notification/notify.ts';

  $notify.call({
    message: 'root',
    ...$notify.preset.success,
  });
</script>
