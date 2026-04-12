<template>B</template>
<script setup lang="ts">
import { $notify } from '@shared/service/notification/notify.ts';

$notify().call({
  ...$notify().preset.success,
  message: 'B',
});
</script>
