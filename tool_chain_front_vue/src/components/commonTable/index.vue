<template>
  <div class="common-table-wrapper">
    <!-- 查询条件插槽 -->
    <div class="table-query-bar">
      <slot name="query"></slot>
    </div>
    <!-- 表格主体 -->
    <el-table
      :data="data"
      :loading="loading"
      style="width: 100%"
      v-bind="tableProps"
    >
      <el-table-column
        v-for="col in visibleColumns"
        :key="col.prop"
        :prop="col.prop"
        :label="col.label"
        :sortable="col.sortable || false"
        :width="col.width"
        :align="col.align || 'left'"
      >
        <template slot-scope="scope">
          <template v-if="$scopedSlots[col.slot]">
            <slot :name="col.slot" v-bind="scope"></slot>
          </template>
          <template v-else>
            {{ scope.row[col.prop] }}
          </template>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <div class="pager-bar" v-if="pagination && pagination.total > 0">
      <el-pagination
        background
        @size-change="onSizeChange"
        @current-change="onCurrentChange"
        :current-page="queryParam.currentPage"
        :page-sizes="pagination.page_sizes"
        :page-size="queryParam.pageSize"
        :layout="pagination.layout"
        :total="pagination.total"
      ></el-pagination>
    </div>
  </div>
</template>

<script>
    export default {
        name: 'common-table',
        props: {
            data: { type: Array, default: () => [] },
            loading: { type: Boolean, default: false },
            columns: { type: Array, required: true }, // [{prop, label, slot, sortable, width, align, visible}]
            pagination: { type: Object, default: null },
            queryParam: { type: Object, default: null },
            tableProps: { type: Object, default: () => ({}) }
        },
        computed: {
            visibleColumns() {
                // 支持动态显示/隐藏列
                return this.columns.filter(col => col.visible !== false);
            }
        },
        methods: {
            onSizeChange(val) {
                this.$emit('size-change', val);
            },
            onCurrentChange(val) {
                this.$emit('current-change', val);
            }
        }
    };
</script>

<style scoped>
.common-table-wrapper {
  width: 100%;
}
.table-query-bar {
  margin-bottom: 16px;
}
.pager-bar {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style> 