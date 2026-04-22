<template>
  <section class="hero-panel">
    <el-image class="hero-background" lazy :src="backgroundImage" fit="cover" />
    <div class="hero-mask" />

    <div class="brand">
      <strong>Architect</strong>
    </div>

    <div class="hero-content">
      <span class="hero-kicker">Workspace Access</span>
      <h1 class="hero-title">
        Design with<br />
        <span>Precision.</span>
      </h1>
      <p class="hero-subtitle">
        Enter the Architect workspace. Where clarity meets intent, and every interaction is
        meticulously calibrated.
      </p>
    </div>

    <div class="hero-footer">(c) 2026 Precision Architect. All rights reserved.</div>
  </section>
</template>

<script setup lang="ts">
  defineProps<{
    backgroundImage: string;
  }>();
</script>

<style scoped lang="scss">
  .hero-panel {
    position: relative;
    display: flex;
    flex-direction: column;
    justify-content: center;
    padding: 56px 72px;
    isolation: isolate;
    overflow: hidden;
  }

  .hero-background {
    position: absolute;
    inset: 0;
    z-index: -2;
  }

  .hero-mask {
    position: absolute;
    inset: 0;
    background:
      linear-gradient(180deg, rgba(255, 255, 255, 0.24) 0%, rgba(255, 255, 255, 0.08) 100%),
      linear-gradient(120deg, rgba(240, 247, 255, 0.92) 0%, rgba(226, 237, 255, 0.72) 100%);
    backdrop-filter: blur(2px);
    z-index: -1;
  }

  .hero-background :deep(.el-image__inner) {
    width: 100%;
    height: 100%;
    object-position: center;
  }

  .brand,
  .hero-content,
  .hero-footer {
    position: relative;
    z-index: 1;
  }

  .brand {
    position: absolute;
    top: 48px;
    left: 72px;
    font-size: 1.15rem;
    letter-spacing: 0.04em;
  }

  .hero-content {
    max-width: 560px;
  }

  .hero-kicker {
    display: inline-flex;
    align-items: center;
    padding: 8px 14px;
    border-radius: 999px;
    margin-bottom: 20px;
    background: rgba(255, 255, 255, 0.72);
    color: var(--brand-primary-deep);
    font-size: 0.78rem;
    font-weight: 700;
    letter-spacing: 0.14em;
    text-transform: uppercase;
    box-shadow: 0 10px 30px rgba(37, 99, 235, 0.08);
  }

  .hero-title {
    margin: 0;
    font-size: clamp(3rem, 5vw, 4.6rem);
    line-height: 1.02;
    letter-spacing: -0.05em;

    span {
      color: var(--brand-primary);
    }
  }

  .hero-subtitle {
    max-width: 480px;
    margin: 24px 0 0;
    font-size: 1.03rem;
    line-height: 1.8;
    color: var(--text-secondary);
  }

  .hero-footer {
    position: absolute;
    left: 72px;
    bottom: 42px;
    color: var(--text-secondary);
    font-size: 0.88rem;
  }

  @media (max-width: 1080px) {
    .hero-panel {
      min-height: 360px;
      padding: 92px 32px 56px;
    }

    .brand {
      left: 32px;
      top: 32px;
    }

    .hero-footer {
      left: 32px;
      bottom: 24px;
    }
  }

  @media (max-width: 640px) {
    .hero-panel {
      min-height: auto;
      padding: 84px 20px 76px;
    }

    .hero-footer {
      position: static;
      margin-top: 28px;
    }
  }
</style>
