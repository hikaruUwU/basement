<template>
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
</template>

<script setup lang="ts">
  defineProps<{
    intakeQueue: Array<{ stage: string; summary: string; progress: number }>;
    spotlightTags: string[];
    activityFeed: Array<{ title: string; detail: string; time: string }>;
  }>();
</script>

<style scoped lang="scss">
  .board-grid {
    position: relative;
    z-index: 1;
    display: grid;
    grid-template-columns: 1.05fr 0.95fr 0.9fr;
    gap: 24px;
    margin-bottom: 24px;
  }

  .glass-card {
    padding: 26px;
    border: 1px solid var(--page-border);
    border-radius: 28px;
    background: linear-gradient(180deg, rgba(255, 255, 255, 0.94), rgba(249, 251, 255, 0.84));
    box-shadow:
      0 24px 60px rgba(32, 60, 94, 0.08),
      inset 0 1px 0 rgba(255, 255, 255, 0.85);
    backdrop-filter: blur(18px);
  }

  .section-head {
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

  .section-head h3 {
    margin: 10px 0 0;
    font-size: 1.6rem;
    letter-spacing: -0.04em;
  }

  .section-note {
    display: block;
    color: var(--text-muted);
    font-size: 0.84rem;
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
  .activity-item strong {
    font-size: 1rem;
  }

  .pipeline-copy p,
  .spotlight-copy p,
  .activity-item p {
    margin: 0;
    color: var(--text-secondary);
    line-height: 1.6;
    font-size: 0.92rem;
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

  .tag-row span {
    display: inline-flex;
    align-items: center;
    padding: 8px 12px;
    border-radius: 999px;
    background: rgba(247, 250, 255, 0.9);
    color: var(--text-secondary);
    font-size: 0.78rem;
    font-weight: 700;
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

  @media (max-width: 1180px) {
    .board-grid {
      grid-template-columns: 1fr;
    }
  }

  @media (max-width: 780px) {
    .glass-card {
      padding: 20px;
      border-radius: 24px;
    }

    .section-head,
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
