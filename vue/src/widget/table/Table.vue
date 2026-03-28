<template>
  <div class="pro-table-wrapper">
    <!--header-->
    <div v-if="$slots.header" class="table-header">
      <slot name="header"></slot>
    </div>
    <!---------->
    <el-table ref="tableRef" v-bind="schema.props" :data="computedData" >
      <el-table-column type="selection" v-if="schema.selector" width="55"  />

      <template #empty>
        <transition name="el-fade-in">
          <div class="empty-box">No Data</div>
        </transition>
      </template>


      <!--body-->
      <template v-for="col in visibleColumns" :key="col.field">
        <el-table-column
          :prop="col.field"
          :label="col.column"
          :width="col.width"
          :fixed="col.fixed"
          :align="col.align || 'center'"
          v-bind="col.props"
        >
          <!--custom render H5-->
          <template #default="scope">
            <template v-if="$slots[col.field]">
              <slot :name="col.field" v-bind="scope"></slot>
            </template>
            <template v-else>
              {{ scope.row[col.field] }}
            </template>
          </template>
        </el-table-column>
      </template>
      <el-table-column
          v-if="$slots.operation"
          fixed="right"
          align="center"
          label="操作"
      >
        <template #default="scope">
          <slot name="operation" v-bind="scope"></slot>
        </template>
      </el-table-column>
      <!--------->
    </el-table>
    <!--footer-->
    <div v-if="$slots.footer" class="table-footer">
      <slot name="footer"></slot>
    </div>
    <!---------->
  </div>
</template>

<script setup lang="ts" generic="T extends Record<string, any>">
import { computed, onMounted, ref, unref } from 'vue';
import type { TableAction, TableOptions } from './TableBuildup.tsx';
import type { TableInstance } from 'element-plus';
const props = defineProps<{
  schema: TableOptions<T>;
}>();
const emit = defineEmits<{
  (e: 'buildup', actions: TableAction): void;
}>();

//----------------------------------------------------------------------------------------------------------------------
const tableRef = ref<TableInstance | null>(null);
//----------------------------------------------------------------------------------------------------------------------

const computedData = computed(() => {
  const propsData = props.schema?.props?.data;
  return unref(propsData) || [];
});

const visibleColumns = computed(() => {
  return props.schema.columns.filter((col: any) => !col.hidden);
});

//----------------------------------------------------------------------------------------------------------------------

const instanceActions: TableAction = {
  reload: () => {},
  destroy: () => {},
  getElTableExpose: () => tableRef.value,
  getSelected: () => tableRef.value?.getSelectionRows() || [],
};

onMounted(() => {
  emit('buildup', instanceActions);
});
</script>

<style scoped>
.table-header {
  margin-bottom: 12px;
}

.table-footer {
  margin-top: 12px;
}
</style>
