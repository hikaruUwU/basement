<template>
  <div class="library-page">
    <div class="background-orb orb-one" />
    <div class="background-orb orb-two" />

    <section class="hero-shell">
      <div class="hero-copy">
        <span class="eyebrow">Neural Library Console</span>
        <h1>Book management for teams that curate faster than they shelve.</h1>
        <p>
          Coordinate acquisitions, monitor circulation, and surface high-value titles from one
          cinematic control layer built for modern collections.
        </p>

        <div class="hero-actions">
          <el-button type="primary" class="primary-action" @click="focusShelf">
            Sync Collection
          </el-button>
          <el-button plain class="secondary-action" @click="openIntake"> Review Intake </el-button>
        </div>

        <div class="signal-row">
          <div v-for="signal in heroSignals" :key="signal.label" class="signal-chip">
            <span>{{ signal.label }}</span>
            <strong>{{ signal.value }}</strong>
          </div>
        </div>
      </div>

      <div class="hero-panel">
        <div class="hero-panel-head">
          <div>
            <span class="panel-kicker">Realtime Pulse</span>
            <h2>Collection Health</h2>
          </div>
          <span class="panel-status">Stable + Growing</span>
        </div>

        <div class="score-ring">
          <div>
            <strong>94%</strong>
            <span>catalog integrity</span>
          </div>
        </div>

        <div class="metric-grid">
          <article v-for="metric in topMetrics" :key="metric.label" class="metric-card">
            <span>{{ metric.label }}</span>
            <strong>{{ metric.value }}</strong>
            <p>{{ metric.detail }}</p>
          </article>
        </div>
      </div>
    </section>

    <section class="board-grid">
      <article class="glass-card stack-card">
        <div class="section-head">
          <div>
            <span class="panel-kicker">Flow Control</span>
            <h3>Acquisition Pipeline</h3>
          </div>
          <span class="section-note">{{ intakeQueue.length }} active lanes</span>
        </div>

        <div class="pipeline-list">
          <div v-for="item in intakeQueue" :key="item.stage" class="pipeline-item">
            <div class="pipeline-track">
              <div class="pipeline-fill" :style="{ width: `${item.progress}%` }" />
            </div>
            <div class="pipeline-copy">
              <div>
                <strong>{{ item.stage }}</strong>
                <p>{{ item.summary }}</p>
              </div>
              <span>{{ item.progress }}%</span>
            </div>
          </div>
        </div>
      </article>

      <article class="glass-card spotlight-card">
        <div class="section-head">
          <div>
            <span class="panel-kicker">AI Picks</span>
            <h3>Curator Spotlight</h3>
          </div>
          <span class="section-note">This week</span>
        </div>

        <div class="spotlight-panel">
          <div class="spotlight-cover">
            <span>Featured</span>
            <strong>Atlas of Quiet Cities</strong>
          </div>

          <div class="spotlight-copy">
            <p>
              Demand telemetry suggests a 27% lift among design, urbanism, and travel segments.
            </p>
            <div class="tag-row">
              <span v-for="tag in spotlightTags" :key="tag">{{ tag }}</span>
            </div>
          </div>
        </div>
      </article>

      <article class="glass-card activity-card">
        <div class="section-head">
          <div>
            <span class="panel-kicker">Live Feed</span>
            <h3>Recent Activity</h3>
          </div>
          <span class="section-note">Last 24h</span>
        </div>

        <div class="activity-list">
          <div v-for="entry in activityFeed" :key="entry.title" class="activity-item">
            <div class="activity-dot" />
            <div>
              <strong>{{ entry.title }}</strong>
              <p>{{ entry.detail }}</p>
            </div>
            <span>{{ entry.time }}</span>
          </div>
        </div>
      </article>
    </section>

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
            @click="activeView = view"
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
            <tr v-for="book in filteredBooks" :key="book.title">
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
  </div>
</template>

<script setup lang="ts">
  import { computed, ref } from 'vue';
  import { $message } from '@shared/message/messaging.ts';

  const views = ['All Books', 'Trending', 'Archived'];
  const activeView = ref('All Books');

  const heroSignals = [
    { label: 'Active Members', value: '18.4k' },
    { label: 'Smart Holds', value: '2,184' },
    { label: 'Auto-Restocks', value: '126' },
  ];

  const topMetrics = [
    { label: 'Books Indexed', value: '248,920', detail: '+3.8% this month' },
    { label: 'Loans Today', value: '4,312', detail: 'Peak at 14:00' },
    { label: 'Returns Due', value: '389', detail: '67 flagged high priority' },
    { label: 'Shelf Accuracy', value: '99.2%', detail: 'Sensors fully synced' },
  ];

  const intakeQueue = [
    { stage: 'Metadata enrichment', summary: '312 titles awaiting semantic tags', progress: 82 },
    { stage: 'Vendor confirmations', summary: '48 orders pending contract approval', progress: 61 },
    {
      stage: 'Archival migration',
      summary: 'Rare collection batch 07 in validation',
      progress: 44,
    },
  ];

  const spotlightTags = ['Urban futures', 'High circulation', 'Cross-campus demand'];

  const activityFeed = [
    {
      title: 'Acquisition batch approved',
      detail: 'North Hall added 84 architecture titles.',
      time: '08m ago',
    },
    {
      title: 'Hold surge detected',
      detail: 'Speculative fiction queue exceeded forecast by 13%.',
      time: '19m ago',
    },
    {
      title: 'Inventory drift resolved',
      detail: 'Shelf cluster C-12 reconciled after scan sync.',
      time: '43m ago',
    },
  ];

  const books = [
    {
      title: 'Signal & Stacks',
      author: 'Mira Chen',
      status: 'Available',
      statusClass: 'available',
      format: 'Print + eBook',
      demand: 'High',
      genre: 'Systems Design',
    },
    {
      title: 'After the Last Archive',
      author: 'Jonas Vale',
      status: 'Trending',
      statusClass: 'trending',
      format: 'Audiobook',
      demand: 'Very High',
      genre: 'Speculative Fiction',
    },
    {
      title: 'Atlas of Quiet Cities',
      author: 'Leonie Hart',
      status: 'Reserved',
      statusClass: 'reserved',
      format: 'Collector Print',
      demand: 'Rising',
      genre: 'Urbanism',
    },
    {
      title: 'Paper Memory',
      author: 'Asha Moreno',
      status: 'Archived',
      statusClass: 'archived',
      format: 'Digital Vault',
      demand: 'Steady',
      genre: 'History',
    },
  ];

  const filteredBooks = computed(() => {
    if (activeView.value === 'Trending') {
      return books.filter((book) => book.status === 'Trending' || book.demand === 'Very High');
    }
    if (activeView.value === 'Archived') {
      return books.filter((book) => book.status === 'Archived');
    }
    return books;
  });

  const focusShelf = () => {
    $message.preset('success')({
      message: 'Collection sync queued across all shelves.',
    });
  };

  const openIntake = () => {
    $message.preset('info')({
      message: 'Acquisition intake review opened.',
    });
  };
</script>

<style scoped lang="scss">
  * {
    box-sizing: border-box;
  }

  .library-page {
    --page-bg: #f4f7fb;
    --page-surface: rgba(255, 255, 255, 0.82);
    --page-surface-strong: rgba(255, 255, 255, 0.96);
    --page-border: rgba(116, 143, 181, 0.18);
    --text-primary: #112033;
    --text-secondary: #5a6d85;
    --text-muted: #7f91a8;
    --accent: #0ea5e9;
    --accent-deep: #2563eb;
    --accent-green: #17b26a;
    --accent-amber: #c98512;

    position: relative;
    min-height: 100vh;
    padding: 36px;
    overflow: hidden;
    background:
      radial-gradient(circle at 15% 20%, rgba(14, 165, 233, 0.14), transparent 28%),
      radial-gradient(circle at 82% 14%, rgba(37, 99, 235, 0.16), transparent 24%),
      linear-gradient(135deg, #f7fbff 0%, #f3f7fc 46%, #edf3fa 100%);
    color: var(--text-primary);
    font-family: 'Space Grotesk', 'Segoe UI', sans-serif;
  }

  .background-orb {
    position: absolute;
    border-radius: 999px;
    filter: blur(30px);
    opacity: 0.42;
    pointer-events: none;
  }

  .orb-one {
    top: 160px;
    right: -80px;
    width: 280px;
    height: 280px;
    background: rgba(23, 178, 106, 0.14);
  }

  .orb-two {
    bottom: 120px;
    left: -70px;
    width: 220px;
    height: 220px;
    background: rgba(14, 165, 233, 0.14);
  }

  .hero-shell,
  .board-grid,
  .catalog-shell {
    position: relative;
    z-index: 1;
  }

  .hero-shell {
    display: grid;
    grid-template-columns: minmax(0, 1.2fr) minmax(320px, 0.8fr);
    gap: 24px;
    margin-bottom: 24px;
  }

  .hero-copy,
  .hero-panel,
  .glass-card,
  .catalog-shell {
    border: 1px solid var(--page-border);
    background: linear-gradient(180deg, rgba(255, 255, 255, 0.94), rgba(249, 251, 255, 0.84));
    box-shadow:
      0 24px 60px rgba(32, 60, 94, 0.08),
      inset 0 1px 0 rgba(255, 255, 255, 0.85);
    backdrop-filter: blur(18px);
  }

  .hero-copy {
    padding: 42px;
    border-radius: 32px;
  }

  .eyebrow,
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

  .eyebrow::before,
  .panel-kicker::before {
    content: '';
    width: 28px;
    height: 1px;
    background: currentColor;
  }

  .hero-copy h1 {
    max-width: 760px;
    margin: 18px 0 18px;
    font-size: clamp(3rem, 5vw, 5.8rem);
    line-height: 0.95;
    letter-spacing: -0.06em;
  }

  .hero-copy p {
    max-width: 620px;
    margin: 0;
    color: var(--text-secondary);
    font-size: 1.02rem;
    line-height: 1.8;
  }

  .hero-actions {
    display: flex;
    flex-wrap: wrap;
    gap: 14px;
    margin: 30px 0 34px;
  }

  .primary-action,
  .secondary-action {
    height: 48px;
    padding: 0 22px;
    border-radius: 999px;
    font-weight: 700;
    letter-spacing: 0.02em;
  }

  .primary-action {
    border: none;
    background: linear-gradient(135deg, #38bdf8 0%, #2563eb 100%);
    box-shadow: 0 18px 34px rgba(37, 99, 235, 0.2);
  }

  .secondary-action {
    border-color: rgba(159, 178, 203, 0.28);
    background: rgba(255, 255, 255, 0.72);
    color: var(--text-primary);
  }

  .signal-row {
    display: grid;
    grid-template-columns: repeat(3, minmax(0, 1fr));
    gap: 14px;
  }

  .signal-chip {
    padding: 18px 20px;
    border: 1px solid rgba(116, 143, 181, 0.14);
    border-radius: 22px;
    background: rgba(255, 255, 255, 0.72);
  }

  .signal-chip span,
  .metric-card span,
  .section-note,
  .title-cell span {
    display: block;
    color: var(--text-muted);
    font-size: 0.84rem;
  }

  .signal-chip strong {
    display: block;
    margin-top: 8px;
    font-size: 1.5rem;
    letter-spacing: -0.04em;
  }

  .hero-panel {
    display: flex;
    flex-direction: column;
    gap: 24px;
    padding: 28px;
    border-radius: 32px;
  }

  .hero-panel-head,
  .section-head,
  .catalog-head {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    gap: 16px;
  }

  .hero-panel-head h2,
  .section-head h3,
  .catalog-head h3 {
    margin: 10px 0 0;
    font-size: 1.6rem;
    letter-spacing: -0.04em;
  }

  .panel-status {
    padding: 10px 14px;
    border-radius: 999px;
    background: rgba(23, 178, 106, 0.1);
    color: var(--accent-green);
    font-size: 0.84rem;
    font-weight: 700;
  }

  .score-ring {
    display: grid;
    place-items: center;
    min-height: 220px;
    border-radius: 28px;
    background:
      radial-gradient(circle at center, rgba(247, 250, 255, 0.98) 44%, transparent 45%),
      conic-gradient(
        from 210deg,
        rgba(56, 189, 248, 0.24),
        #38bdf8 20%,
        #2563eb 58%,
        rgba(148, 163, 184, 0.16) 0
      );
  }

  .score-ring div {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 6px;
  }

  .score-ring strong {
    font-size: 3.4rem;
    line-height: 1;
    letter-spacing: -0.06em;
  }

  .score-ring span,
  .metric-card p,
  .pipeline-copy p,
  .spotlight-copy p,
  .activity-item p {
    color: var(--text-secondary);
  }

  .metric-grid {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 14px;
  }

  .metric-card {
    padding: 18px;
    border-radius: 22px;
    background: rgba(247, 250, 255, 0.86);
  }

  .metric-card strong {
    display: block;
    margin: 10px 0 8px;
    font-size: 1.6rem;
    letter-spacing: -0.04em;
  }

  .metric-card p,
  .pipeline-copy p,
  .spotlight-copy p,
  .activity-item p {
    margin: 0;
    line-height: 1.6;
    font-size: 0.92rem;
  }

  .board-grid {
    display: grid;
    grid-template-columns: 1.05fr 0.95fr 0.9fr;
    gap: 24px;
    margin-bottom: 24px;
  }

  .glass-card,
  .catalog-shell {
    border-radius: 28px;
    padding: 26px;
  }

  .pipeline-list,
  .activity-list {
    display: grid;
    gap: 16px;
    margin-top: 22px;
  }

  .pipeline-item {
    padding: 16px;
    border-radius: 20px;
    background: rgba(247, 250, 255, 0.84);
  }

  .pipeline-track {
    height: 10px;
    overflow: hidden;
    border-radius: 999px;
    background: rgba(148, 163, 184, 0.16);
  }

  .pipeline-fill {
    height: 100%;
    border-radius: inherit;
    background: linear-gradient(90deg, #38bdf8 0%, #2563eb 100%);
  }

  .pipeline-copy {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    gap: 12px;
    margin-top: 14px;
  }

  .pipeline-copy strong,
  .activity-item strong,
  .title-cell strong {
    font-size: 1rem;
  }

  .pipeline-copy span {
    color: var(--accent);
    font-weight: 700;
  }

  .spotlight-panel {
    display: grid;
    gap: 18px;
    margin-top: 22px;
  }

  .spotlight-cover {
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
    min-height: 220px;
    padding: 22px;
    border-radius: 24px;
    background:
      linear-gradient(180deg, rgba(255, 255, 255, 0.08), rgba(17, 32, 51, 0.82)),
      linear-gradient(135deg, rgba(56, 189, 248, 0.38), rgba(37, 99, 235, 0.22)),
      url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='240' height='240' viewBox='0 0 240 240'%3E%3Cg fill='none' stroke='rgba(255,255,255,0.22)' stroke-width='1'%3E%3Cpath d='M24 34h92v172H24z'/%3E%3Cpath d='M124 52h92v154h-92z'/%3E%3Cpath d='M42 60h56M42 78h56M42 96h40M142 76h56M142 94h52M142 112h44'/%3E%3C/g%3E%3C/svg%3E")
        center / cover;
  }

  .spotlight-cover span {
    color: rgba(255, 255, 255, 0.72);
    font-size: 0.82rem;
    letter-spacing: 0.14em;
    text-transform: uppercase;
  }

  .spotlight-cover strong {
    max-width: 220px;
    margin-top: 12px;
    font-size: 2rem;
    line-height: 1;
    letter-spacing: -0.05em;
  }

  .tag-row {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    margin-top: 16px;
  }

  .tag-row span,
  .status-badge {
    display: inline-flex;
    align-items: center;
    padding: 8px 12px;
    border-radius: 999px;
    font-size: 0.78rem;
    font-weight: 700;
  }

  .tag-row span {
    background: rgba(247, 250, 255, 0.9);
    color: var(--text-secondary);
  }

  .activity-item {
    display: grid;
    grid-template-columns: auto 1fr auto;
    gap: 14px;
    align-items: start;
    padding: 14px 0;
    border-bottom: 1px solid rgba(116, 143, 181, 0.12);
  }

  .activity-item:last-child {
    padding-bottom: 0;
    border-bottom: none;
  }

  .activity-dot {
    width: 10px;
    height: 10px;
    margin-top: 8px;
    border-radius: 999px;
    background: var(--accent);
    box-shadow: 0 0 16px rgba(14, 165, 233, 0.32);
  }

  .activity-item span {
    color: var(--text-muted);
    font-size: 0.82rem;
    white-space: nowrap;
  }

  .catalog-shell {
    padding-bottom: 18px;
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

  @media (max-width: 1180px) {
    .hero-shell,
    .board-grid {
      grid-template-columns: 1fr;
    }
  }

  @media (max-width: 780px) {
    .library-page {
      padding: 18px;
    }

    .hero-copy,
    .hero-panel,
    .glass-card,
    .catalog-shell {
      padding: 20px;
      border-radius: 24px;
    }

    .hero-copy h1 {
      font-size: clamp(2.4rem, 10vw, 4rem);
    }

    .signal-row,
    .metric-grid {
      grid-template-columns: 1fr;
    }

    .hero-panel-head,
    .section-head,
    .catalog-head,
    .pipeline-copy {
      flex-direction: column;
      align-items: flex-start;
    }

    .activity-item {
      grid-template-columns: 1fr;
      gap: 8px;
    }
  }
</style>
