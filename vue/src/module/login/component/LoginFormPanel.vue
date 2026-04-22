<template>
  <section class="form-panel">
    <div class="form-shell">
      <div class="form-heading">
        <h2>Welcome back</h2>
        <p>Sign in to your account to continue.</p>
      </div>

      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-position="top"
        size="large"
        class="login-form"
      >
        <el-form-item label="Email Address" prop="email">
          <el-input v-model="form.email" type="email" placeholder="name@example.com" clearable />
        </el-form-item>

        <el-form-item label="Password" prop="password">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="Enter your password"
            show-password
            clearable
          />
        </el-form-item>

        <div class="form-options">
          <el-checkbox v-model="form.remember">Remember me</el-checkbox>
          <el-link type="primary" :underline="false">Forgot password?</el-link>
        </div>

        <el-button type="primary" class="submit-button" :loading="submitting" @click="$emit('submit')">
          Sign In
        </el-button>
      </el-form>

      <el-divider>OR CONTINUE WITH</el-divider>

      <div class="social-actions">
        <el-button plain size="large" class="social-button">
          <span class="social-icon google-icon" aria-hidden="true">
            <svg fill="currentColor" viewBox="0 0 24 24" height="18" width="18">
              <path
                d="M22.56 12.25c0-.78-.07-1.53-.2-2.25H12v4.26h5.92c-.26 1.37-1.04 2.53-2.21 3.31v2.77h3.57c2.08-1.92 3.28-4.74 3.28-8.09z"
                fill="#4285F4"
              />
              <path
                d="M12 23c2.97 0 5.46-.98 7.28-2.66l-3.57-2.77c-.98.66-2.23 1.06-3.71 1.06-2.86 0-5.29-1.93-6.16-4.53H2.18v2.84C3.99 20.53 7.7 23 12 23z"
                fill="#34A853"
              />
              <path
                d="M5.84 14.09c-.22-.66-.35-1.36-.35-2.09s.13-1.43.35-2.09V7.07H2.18C1.43 8.55 1 10.22 1 12s.43 3.45 1.18 4.93l2.85-2.22.81-.62z"
                fill="#FBBC05"
              />
              <path
                d="M12 5.38c1.62 0 3.06.56 4.21 1.64l3.15-3.15C17.45 2.09 14.97 1 12 1 7.7 1 3.99 3.47 2.18 7.07l3.66 2.84c.87-2.6 3.3-4.53 6.16-4.53z"
                fill="#EA4335"
              />
            </svg>
          </span>
          Google
        </el-button>

        <el-button plain size="large" class="social-button">
          <span class="social-icon apple-icon" aria-hidden="true">
            <svg fill="currentColor" viewBox="0 0 24 24" height="18" width="18">
              <path
                d="M16.365 21.41c-1.396 1.408-2.87 1.43-4.343.08-1.552-1.428-2.858-1.442-4.22-.045-3.327 3.326-6.61-4.062-6.61-4.062-2.146-6.177 1.258-8.775 3.197-8.775 1.767 0 2.225.862 3.655.862 1.34 0 2.278-.962 3.755-.962 2.76 0 3.774 1.353 3.774 1.353s-2.042.877-2.042 3.315c0 2.923 2.505 3.94 2.505 3.94s-1.87 3.633-3.67 5.294v-.002zM15.426 6.883c.758-1.042 1.056-2.16 1.056-3.21 0-.112-.01-.225-.03-.338-1.127.067-2.457.75-3.23 1.674-.6.713-1.11 1.83-1.11 2.898 0 .13.01.256.034.37.114.02.26.034.408.034 1.01 0 2.146-.43 2.872-1.426l.002-.002z"
              />
            </svg>
          </span>
          Apple
        </el-button>
      </div>

      <div class="signup-link">
        <span>Don't have an account?</span>
        <el-link type="primary" :underline="false">Sign Up</el-link>
      </div>
    </div>

    <div class="panel-footer">
      <el-link :underline="false">Privacy Policy</el-link>
      <el-link :underline="false">Terms of Service</el-link>
      <el-link :underline="false">Contact</el-link>
    </div>
  </section>
</template>

<script setup lang="ts">
  import type { FormInstance, FormRules } from 'element-plus';
  import { ref } from 'vue';

  defineProps<{
    form: Record<string, any>;
    rules: FormRules<Record<string, any>>;
    submitting: boolean;
  }>();

  defineEmits<{
    submit: [];
  }>();

  const formRef = ref<FormInstance>();

  const validate = async () => {
    if (!formRef.value) {
      return false;
    }
    return formRef.value.validate().catch(() => false);
  };

  defineExpose({
    validate,
    formRef,
  });
</script>

<style scoped lang="scss">
  .form-panel {
    position: relative;
    display: flex;
    flex-direction: column;
    justify-content: center;
    padding: 40px 32px;
  }

  .form-shell {
    width: min(100%, 460px);
    margin: 0 auto;
    padding: 38px 34px 28px;
    border: 1px solid var(--panel-border);
    border-radius: 28px;
    background: var(--panel-bg);
    box-shadow:
      0 24px 80px rgba(15, 23, 42, 0.08),
      inset 0 1px 0 rgba(255, 255, 255, 0.55);
    backdrop-filter: blur(18px);
    transition:
      transform 0.35s ease,
      box-shadow 0.35s ease,
      border-color 0.35s ease;
  }

  .form-shell:hover {
    transform: translateY(-4px);
    border-color: rgba(37, 99, 235, 0.24);
    box-shadow:
      0 30px 90px rgba(15, 23, 42, 0.12),
      inset 0 1px 0 rgba(255, 255, 255, 0.65);
  }

  .form-heading {
    margin-bottom: 24px;

    h2 {
      margin: 0;
      font-size: 2rem;
      line-height: 1.1;
    }

    p {
      margin: 10px 0 0;
      color: var(--text-secondary);
      font-size: 0.96rem;
    }
  }

  .login-form {
    :deep(.el-form-item) {
      margin-bottom: 22px;
    }

    :deep(.el-form-item__label) {
      padding-bottom: 8px;
      color: var(--text-primary);
      font-size: 0.78rem;
      font-weight: 700;
      letter-spacing: 0.08em;
      text-transform: uppercase;
    }

    :deep(.el-input__wrapper) {
      min-height: 48px;
      border-radius: 14px;
      box-shadow: 0 0 0 1px rgba(148, 163, 184, 0.18) inset;
      background: rgba(247, 250, 255, 0.96);
      transition:
        transform 0.25s ease,
        box-shadow 0.25s ease,
        background-color 0.25s ease;
    }

    :deep(.el-input__wrapper:hover) {
      transform: translateY(-1px);
      box-shadow: 0 0 0 1px rgba(37, 99, 235, 0.35) inset;
      background: rgba(255, 255, 255, 0.98);
    }

    :deep(.el-input__wrapper.is-focus) {
      box-shadow: 0 0 0 1px var(--brand-primary) inset;
    }
  }

  .form-options {
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 16px;
    margin: -4px 0 22px;

    :deep(.el-checkbox__label) {
      color: var(--text-secondary);
    }
  }

  .submit-button {
    position: relative;
    overflow: hidden;
    width: 100%;
    height: 48px;
    border: none;
    border-radius: 14px;
    background: linear-gradient(135deg, #2563eb 0%, #1746b7 100%);
    box-shadow: 0 18px 30px rgba(37, 99, 235, 0.22);
    font-size: 1rem;
    font-weight: 700;
    letter-spacing: 0.02em;
    transition:
      transform 0.28s ease,
      box-shadow 0.28s ease,
      filter 0.28s ease;
  }

  .submit-button::before {
    content: '';
    position: absolute;
    inset: 0;
    background: linear-gradient(
      120deg,
      transparent 20%,
      rgba(255, 255, 255, 0.28) 50%,
      transparent 80%
    );
    transform: translateX(-130%);
    transition: transform 0.55s ease;
  }

  .submit-button:hover {
    transform: translateY(-2px);
    box-shadow: 0 24px 40px rgba(37, 99, 235, 0.3);
    filter: saturate(1.08);
  }

  .submit-button:hover::before {
    transform: translateX(130%);
  }

  .submit-button:active {
    transform: translateY(0);
  }

  :deep(.el-divider__text) {
    color: var(--text-secondary);
    font-size: 0.78rem;
    font-weight: 700;
    letter-spacing: 0.12em;
  }

  .social-actions {
    display: grid;
    grid-template-columns: repeat(2, minmax(0, 1fr));
    gap: 14px;
  }

  .social-button {
    width: 100%;
    height: 46px;
    margin: 0;
    border-radius: 14px;
    border-color: rgba(148, 163, 184, 0.28);
    background: rgba(255, 255, 255, 0.8);
    color: var(--text-primary);
    font-weight: 600;
    transition:
      transform 0.24s ease,
      box-shadow 0.24s ease,
      border-color 0.24s ease,
      background-color 0.24s ease;
  }

  .social-button:hover {
    transform: translateY(-2px);
    border-color: rgba(37, 99, 235, 0.32);
    background: rgba(255, 255, 255, 0.96);
    box-shadow: 0 14px 28px rgba(15, 23, 42, 0.08);
  }

  .social-button:hover .social-icon {
    transform: scale(1.08) rotate(-4deg);
  }

  .social-icon {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 18px;
    height: 18px;
    margin-right: 8px;
    transition: transform 0.24s ease;
  }

  .apple-icon {
    color: #111827;
  }

  .signup-link {
    display: flex;
    justify-content: center;
    gap: 6px;
    margin-top: 24px;
    color: var(--text-secondary);
    font-size: 0.95rem;
  }

  .panel-footer {
    position: absolute;
    right: 36px;
    bottom: 18px;
    display: flex;
    gap: 18px;
  }

  :deep(.el-link) {
    transition:
      color 0.22s ease,
      transform 0.22s ease,
      opacity 0.22s ease;
  }

  :deep(.el-link:hover) {
    transform: translateY(-1px);
    opacity: 0.92;
  }

  @media (max-width: 1080px) {
    .form-panel {
      padding-top: 0;
      padding-bottom: 78px;
    }

    .panel-footer {
      left: 50%;
      right: auto;
      transform: translateX(-50%);
      white-space: nowrap;
    }
  }

  @media (max-width: 640px) {
    .form-panel {
      padding: 16px 16px 78px;
    }

    .form-shell {
      padding: 28px 20px 24px;
      border-radius: 22px;
    }

    .social-actions {
      grid-template-columns: 1fr;
    }

    .form-options,
    .signup-link,
    .panel-footer {
      flex-direction: column;
      align-items: flex-start;
    }

    .panel-footer {
      left: 16px;
      right: 16px;
      bottom: 20px;
      transform: none;
      gap: 10px;
    }
  }
</style>
