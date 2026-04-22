<template>
  <div class="login-page">
    <LoginHeroPanel :background-image="backgroundImage" />
    <LoginFormPanel
      ref="formPanelRef"
      :form="form"
      :rules="rules"
      :submitting="submitting"
      @submit="handleSubmit"
    />
  </div>
</template>

<script setup lang="ts">
  import type { FormRules } from 'element-plus';
  import { sleep } from 'radash';
  import backgroundImage from '@/src/assets/background.png';
  import { $message } from '@shared/message/messaging.ts';
  import { reactive, ref } from 'vue';
  import LoginFormPanel from './component/LoginFormPanel.vue';
  import LoginHeroPanel from './component/LoginHeroPanel.vue';

  const formPanelRef = ref<InstanceType<typeof LoginFormPanel>>();
  const submitting = ref(false);

  const form = reactive<Record<string, any>>({
    email: '',
    password: '',
    remember: true,
  });

  const rules: FormRules<Record<string, any>> = {
    email: [
      { required: true, message: 'Please enter your email address', trigger: 'blur' },
      { type: 'email', message: 'Please enter a valid email address', trigger: ['blur', 'change'] },
    ],
    password: [
      { required: true, message: 'Please enter your password', trigger: 'blur' },
      { min: 6, message: 'Password must be at least 6 characters', trigger: 'blur' },
    ],
  };

  const handleSubmit = async () => {
    const valid = await formPanelRef.value?.validate();
    if (!valid) {
      return;
    }
    submitting.value = true;
    try {
      await sleep(1000);
      $message.preset('success')({
        message: 'Login success, redirecting',
      });
    } finally {
      submitting.value = false;
    }
  };
</script>

<style scoped lang="scss">
  * {
    box-sizing: border-box;
    user-select: none;
  }

  .login-page {
    --page-bg: #eef3fb;
    --panel-bg: rgba(255, 255, 255, 0.88);
    --panel-border: rgba(148, 163, 184, 0.2);
    --text-primary: #122033;
    --text-secondary: #5b6b82;
    --brand-primary: #2563eb;
    --brand-primary-deep: #173ea8;

    display: grid;
    grid-template-columns: minmax(0, 1.15fr) minmax(420px, 0.85fr);
    min-height: 100vh;
    background:
      radial-gradient(circle at top left, rgba(37, 99, 235, 0.14), transparent 34%),
      linear-gradient(135deg, #eef4ff 0%, #f8fbff 40%, #edf2fb 100%);
    color: var(--text-primary);
    overflow: hidden;
  }

  @media (max-width: 1080px) {
    .login-page {
      grid-template-columns: 1fr;
    }
  }
</style>
