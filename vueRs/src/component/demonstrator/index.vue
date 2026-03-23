<template>
  <div class="container">
    <el-text size="large">Data</el-text>
    <el-divider/>
    <Searcher :schema="searchSchema" :model-value="formData">
      <template #action>
        <el-button @click="resetForm(); execute()" :loading="isLoading" :disabled="isLoading">Reset</el-button>
        <el-button type="primary" @click="execute()" :loading="isLoading" :disabled="isLoading">Search</el-button>
      </template>
    </Searcher>

    <el-skeleton :loading="isLoading" animated :rows="11">
      <template #default>
        <Table :schema="schema" @buildup="onBuildup">
          <template #operation="scope">
            <el-button link type="primary" size="small" @click="notifyPlain(scope.row.body)">Detail</el-button>
            <el-button link type="primary" size="small" @click="notifyPlain(scope.row.body)">Edit</el-button>
          </template>
        </Table>
      </template>
    </el-skeleton>
    <el-divider/>
    <el-pagination :disabled="isLoading"
                   :total="source.length"
                   v-model:current-page="currentPage"
                   :pageSize="pageSize"
    />
  </div>
</template>

<script setup lang="ts">
import {usePresetPaginator} from '../../util/Paginator.ts';
import {useTable} from '../../widget/table/TableBuildup.tsx';
import {ref} from 'vue';
import Table from '../../widget/table/Table.vue';

import {definedTableOptions, definedTableSearchOptions} from './type.ts';
import {fetchData} from "../../service/http.ts";
import Searcher from "../../widget/searcher/Searcher.vue";
import {useSearch} from "../../widget/searcher/SearchBuildup.tsx";
import {notifyPlain} from "../../util/noticePopup.ts";

const source = ref<any[]>([]);

const {schema: searchSchema, formData, resetForm} = useSearch({schema: definedTableSearchOptions()})

const {isLoading, execute} = fetchData({parameter: formData}, source)

const {filteredData, currentPage, pageSize} = usePresetPaginator(source, {initialPageSize: 4});

const {schema, onBuildup} = useTable().register(definedTableOptions(filteredData));
</script>
<style scoped>
.container {
  padding: 20px;
}
</style>