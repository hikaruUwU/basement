<template>
  <div class="library-page">
    <div class="background-orb orb-one" />
    <div class="background-orb orb-two" />

    <WorkbenchHeroSection
      :hero-signals="heroSignals"
      :top-metrics="topMetrics"
      @focus-shelf="focusShelf"
      @open-intake="openIntake"
    />

    <WorkbenchBoardGrid
      :intake-queue="intakeQueue"
      :spotlight-tags="spotlightTags"
      :activity-feed="activityFeed"
    />

    <WorkbenchCatalogSection
      :views="views"
      :active-view="activeView"
      :books="filteredBooks"
      @update:active-view="activeView = $event"
    />
  </div>
</template>

<script setup lang="ts">
  import { computed, ref } from 'vue';
  import { $message } from '@shared/message/messaging.ts';
  import WorkbenchBoardGrid from './component/WorkbenchBoardGrid.vue';
  import WorkbenchCatalogSection from './component/WorkbenchCatalogSection.vue';
  import WorkbenchHeroSection from './component/WorkbenchHeroSection.vue';

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

  @media (max-width: 780px) {
    .library-page {
      padding: 18px;
    }
  }
</style>
