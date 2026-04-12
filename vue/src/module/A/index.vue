<template>A</template>
<script setup lang="ts">
import { $notify } from '@shared/service/notification/notify.ts';

$notify().call({
  ...$notify().preset.success,
  message: 'A',
});
</script>
