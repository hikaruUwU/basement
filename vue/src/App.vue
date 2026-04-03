<template>
  <router-view />
</template>
<script setup lang="ts">
import {$copy} from "./util/copy.ts";

$copy(new Date().toString());
</script>
