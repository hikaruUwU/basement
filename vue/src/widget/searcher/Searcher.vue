<template>
  <div class="search-bar-container">
    <el-form :inline="true" :model="modelValue" class="search-form">
      <el-form-item
        v-for="item in schema"
        :key="item.field"
        :label="item.label"
        :style="{ margin: item.margin, width: item.width }"
      >
        <el-input
          v-if="item.type === 'input'"
          v-model="modelValue[item.field]"
          :placeholder="item.placeholder"
          v-bind="item.attr || {}"
        />

        <el-select
          v-else-if="item.type === 'select'"
          v-model="modelValue[item.field]"
          placeholder="请选择"
          clearable
        >
          <el-option
            v-for="opt in item.options"
            :key="opt.value"
            :label="opt.label"
            :value="opt.value"
          />
        </el-select>

        <el-radio-group
          v-else-if="item.type === 'radio'"
          v-model="modelValue[item.field]"
        >
          <el-radio
            v-for="opt in item.options"
            :key="opt.value"
            :label="opt.label"
            :value="opt.value"
          />
        </el-radio-group>
      </el-form-item>

      <el-form-item>
        <slot name="action"></slot>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import type { SearchFieldSchema } from './SearchBuildup';

defineProps<{
  schema: SearchFieldSchema[];
  modelValue: Record<string, any>;
}>();
</script>
