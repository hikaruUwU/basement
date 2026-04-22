<template>
  <section class="hero-shell">
    <div class="hero-copy">
      <span class="eyebrow">Neural Library Console</span>
      <h1>Book management for teams that curate faster than they shelve.</h1>
      <p>
        Coordinate acquisitions, monitor circulation, and surface high-value titles from one
        cinematic control layer built for modern collections.
      </p>

      <div class="hero-actions">
        <el-button type="primary" class="primary-action" @click="$emit('focus-shelf')">
          Sync Collection
        </el-button>
        <el-button plain class="secondary-action" @click="$emit('open-intake')">
          Review Intake
        </el-button>
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
</template>

<script setup lang="ts">
  defineProps<{
    heroSignals: Array<{ label: string; value: string }>;
    topMetrics: Array<{ label: string; value: string; detail: string }>;
  }>();

  defineEmits<{
    'focus-shelf': [];
    'open-intake': [];
  }>();
</script>

<style scoped lang="scss">
  .hero-shell {
    position: relative;
    z-index: 1;
    display: grid;
    grid-template-columns: minmax(0, 1.2fr) minmax(320px, 0.8fr);
    gap: 24px;
    margin-bottom: 24px;
  }

  .hero-copy,
  .hero-panel {
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
  .metric-card span {
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

  .hero-panel-head {
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    gap: 16px;
  }

  .hero-panel-head h2 {
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
  .metric-card p {
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

  .metric-card p {
    margin: 0;
    line-height: 1.6;
    font-size: 0.92rem;
  }

  @media (max-width: 1180px) {
    .hero-shell {
      grid-template-columns: 1fr;
    }
  }

  @media (max-width: 780px) {
    .hero-copy,
    .hero-panel {
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

    .hero-panel-head {
      flex-direction: column;
      align-items: flex-start;
    }
  }
</style>
