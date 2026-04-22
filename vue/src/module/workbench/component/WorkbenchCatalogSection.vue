<template>
  <section class="catalog-shell">
    <div class="catalog-head">
      <div>
        <span class="panel-kicker">Discovery Matrix</span>
        <h3>Catalog Overview</h3>
      </div>
      <div class="filter-pills">
        <button
          v-for="view in views"
          :key="view"
          type="button"
          class="filter-pill"
          :class="{ active: activeView === view }"
          @click="$emit('update:active-view', view)"
        >
          {{ view }}
        </button>
      </div>
    </div>

    <div class="table-wrap">
      <table class="catalog-table">
        <thead>
          <tr>
            <th>Title</th>
            <th>Author</th>
            <th>Status</th>
            <th>Format</th>
            <th>Demand</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="book in books" :key="book.title">
            <td>
              <div class="title-cell">
                <strong>{{ book.title }}</strong>
                <span>{{ book.genre }}</span>
              </div>
            </td>
            <td>{{ book.author }}</td>
            <td>
              <span class="status-badge" :class="book.statusClass">{{ book.status }}</span>
            </td>
            <td>{{ book.format }}</td>
            <td>{{ book.demand }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup lang="ts">
  defineProps<{
    views: string[];
    activeView: string;
    books: Array<{
      title: string;
      author: string;
      status: string;
      statusClass: string;
      format: string;
      demand: string;
      genre: string;
    }>;
  }>();

  defineEmits<{
    'update:active-view': [value: string];
  }>();
</script>

<style scoped lang="scss">
  .catalog-shell {
    position: relative;
    z-index: 1;
    padding: 26px 26px 18px;
    border: 1px solid var(--page-border);
    border-radius: 28px;
    background: linear-gradient(180deg, rgba(255, 255, 255, 0.94), rgba(249, 251, 255, 0.84));
    box-shadow:
      0 24px 60px rgba(32, 60, 94, 0.08),
      inset 0 1px 0 rgba(255, 255, 255, 0.85);
    backdrop-filter: blur(18px);
  }

  .catalog-head {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    gap: 16px;
  }

  .panel-kicker {
    display: inline-flex;
    align-items: center;
    gap: 8px;
    color: var(--accent);
    font-size: 0.78rem;
    font-weight: 700;
    letter-spacing: 0.16em;
    text-transform: uppercase;
  }

  .panel-kicker::before {
    content: '';
    width: 28px;
    height: 1px;
    background: currentColor;
  }

  .catalog-head h3 {
    margin: 10px 0 0;
    font-size: 1.6rem;
    letter-spacing: -0.04em;
  }

  .filter-pills {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
  }

  .filter-pill {
    padding: 10px 14px;
    border: 1px solid rgba(159, 178, 203, 0.16);
    border-radius: 999px;
    background: rgba(255, 255, 255, 0.76);
    color: var(--text-secondary);
    font: inherit;
    cursor: pointer;
    transition:
      border-color 0.25s ease,
      background-color 0.25s ease,
      color 0.25s ease,
      transform 0.25s ease;
  }

  .filter-pill:hover,
  .filter-pill.active {
    border-color: rgba(14, 165, 233, 0.28);
    background: rgba(14, 165, 233, 0.08);
    color: var(--text-primary);
    transform: translateY(-1px);
  }

  .table-wrap {
    overflow-x: auto;
    margin-top: 20px;
  }

  .catalog-table {
    width: 100%;
    min-width: 720px;
    border-collapse: collapse;
  }

  .catalog-table th,
  .catalog-table td {
    padding: 18px 12px;
    border-bottom: 1px solid rgba(116, 143, 181, 0.12);
    text-align: left;
  }

  .catalog-table th {
    color: var(--text-muted);
    font-size: 0.78rem;
    font-weight: 700;
    letter-spacing: 0.12em;
    text-transform: uppercase;
  }

  .title-cell {
    display: grid;
    gap: 6px;
  }

  .title-cell strong {
    font-size: 1rem;
  }

  .title-cell span {
    display: block;
    color: var(--text-muted);
    font-size: 0.84rem;
  }

  .status-badge {
    display: inline-flex;
    align-items: center;
    padding: 8px 12px;
    border-radius: 999px;
    font-size: 0.78rem;
    font-weight: 700;
  }

  .status-badge.available {
    background: rgba(94, 240, 179, 0.12);
    color: var(--accent-green);
  }

  .status-badge.trending {
    background: rgba(101, 217, 255, 0.12);
    color: var(--accent);
  }

  .status-badge.reserved {
    background: rgba(255, 207, 102, 0.14);
    color: var(--accent-amber);
  }

  .status-badge.archived {
    background: rgba(159, 178, 203, 0.14);
    color: #c7d2e3;
  }

  @media (max-width: 780px) {
    .catalog-shell {
      padding: 20px 20px 18px;
      border-radius: 24px;
    }

    .catalog-head {
      flex-direction: column;
      align-items: flex-start;
    }
  }
</style>
