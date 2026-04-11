<template></template>
<script setup lang="ts">
import { $notify } from '@shared/service/notification/notify.ts';

$notify($notify().preset.success);
</script>
