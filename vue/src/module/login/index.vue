<template>
  <div class="login-container">
    <div class="left-panel" :style="{ backgroundImage: `url(${backgroundImage})` }">
      <div class="logo">
        <strong>Architect</strong>
      </div>

      <div class="hero-content">
        <h1 class="title">
          Design with<br />
          <span class="highlight-blue">Precision.</span>
        </h1>
        <p class="subtitle">
          Enter the Architect workspace. Where clarity meets<br />
          intent, and every interaction is meticulously<br />
          calibrated.
        </p>
      </div>

      <div class="footer-left"> © 2024 Precision Architect. All rights reserved. </div>
    </div>

    <div class="right-panel">
      <div class="login-box">
        <h2 class="form-title">Welcome back</h2>
        <p class="form-subtitle">Sign in to your account to continue.</p>

        <div class="form-group">
          <label>EMAIL ADDRESS</label>
          <input type="email" class="custom-input" />
        </div>

        <div class="form-group">
          <label>PASSWORD</label>
          <input type="password" class="custom-input" />
        </div>

        <div class="form-options">
          <label class="remember-me">
            <input type="checkbox" />
            <span>Remember me</span>
          </label>
          <a href="#" class="forgot-link">Forgot password?</a>
        </div>

        <button class="btn-primary">Sign In <span class="arrow">→</span></button>

        <div class="divider">
          <span>OR CONTINUE WITH</span>
        </div>

        <div class="social-login">
          <button class="btn-social">
            <svg
              class="icon icon-google"
              fill="currentColor"
              viewBox="0 0 24 24"
              height="18"
              width="18"
            >
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
            Google
          </button>
          <button class="btn-social">
            <svg
              class="icon icon-apple"
              fill="currentColor"
              viewBox="0 0 24 24"
              height="18"
              width="18"
            >
              <path
                d="M16.365 21.41c-1.396 1.408-2.87 1.43-4.343.08-1.552-1.428-2.858-1.442-4.22-.045-3.327 3.326-6.61-4.062-6.61-4.062-2.146-6.177 1.258-8.775 3.197-8.775 1.767 0 2.225.862 3.655.862 1.34 0 2.278-.962 3.755-.962 2.76 0 3.774 1.353 3.774 1.353s-2.042.877-2.042 3.315c0 2.923 2.505 3.94 2.505 3.94s-1.87 3.633-3.67 5.294v-.002zM15.426 6.883c.758-1.042 1.056-2.16 1.056-3.21 0-.112-.01-.225-.03-.338-1.127.067-2.457.75-3.23 1.674-.6.713-1.11 1.83-1.11 2.898 0 .13.01.256.034.37.114.02.26.034.408.034 1.01 0 2.146-.43 2.872-1.426l.002-.002z"
              />
            </svg>
            Apple
          </button>
        </div>

        <div class="signup-link"> Don't have an account? <a href="#">Sign Up</a> </div>
      </div>

      <div class="footer-right">
        <a href="#">Privacy Policy</a>
        <a href="#">Terms of Service</a>
        <a href="#">Contact</a>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import backgroundImage from '@/src/assets/background.png';
</script>

<style scoped lang="scss">
  * {
    box-sizing: border-box;
    user-select: none;
  }

  .login-container {
    display: flex;
    height: 100vh;
    width: 100%;
    overflow: hidden;
    font-family:
      'Inter',
      -apple-system,
      BlinkMacSystemFont,
      'Segoe UI',
      Roboto,
      Helvetica,
      Arial,
      sans-serif;
    background-color: #f8f9fc;
  }

  /* Left Panel */
  .left-panel {
    flex: 1;
    background-size: cover;
    background-position: center;
    position: relative;
    display: flex;
    flex-direction: column;
    justify-content: center;
    padding: 80px;
    color: #1a1a1a;
  }

  .logo {
    position: absolute;
    top: 40px;
    left: 80px;
    font-size: 1.2rem;
    letter-spacing: -0.5px;
  }

  .hero-content {
    max-width: 500px;
  }

  .title {
    font-size: 4rem;
    font-weight: 700;
    line-height: 1.1;
    margin-bottom: 20px;
    color: #111827;
  }

  .highlight-blue {
    color: #0b57d0;
  }

  .subtitle {
    font-size: 1.1rem;
    line-height: 1.6;
    color: #4b5563;
    margin-top: 1.5rem;
  }

  .footer-left {
    position: absolute;
    bottom: 40px;
    left: 80px;
    font-size: 0.85rem;
    color: #4b5563;
  }

  /* Right Panel */
  .right-panel {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    position: relative;
  }

  .login-box {
    background: white;
    width: 100%;
    max-width: 440px;
    padding: 40px;
    border-radius: 12px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  }

  .form-title {
    font-size: 1.8rem;
    font-weight: 600;
    color: #111827;
    margin-bottom: 8px;
  }

  .form-subtitle {
    color: #4b5563;
    font-size: 0.95rem;
    margin-bottom: 30px;
  }

  .form-group {
    margin-bottom: 20px;

    label {
      display: block;
      font-size: 0.75rem;
      font-weight: 600;
      color: #4b5563;
      margin-bottom: 8px;
      letter-spacing: 0.5px;
    }
  }

  .custom-input {
    width: 100%;
    padding: 14px 16px;
    background-color: #dbeafe; /* Light blue accent */
    border: none;
    border-radius: 6px;
    font-size: 1rem;
    outline: none;
    transition: background-color 0.2s;

    &:focus {
      background-color: #bfdbfe;
    }
  }

  .form-options {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;
    font-size: 0.9rem;

    .remember-me {
      display: flex;
      align-items: center;
      gap: 8px;
      color: #4b5563;
      cursor: pointer;

      input[type='checkbox'] {
        width: 16px;
        height: 16px;
        border-radius: 4px;
        border: 1px solid #d1d5db;
        accent-color: #0b57d0;
      }
    }

    .forgot-link {
      color: #0b57d0;
      text-decoration: none;
      font-weight: 500;

      &:hover {
        text-decoration: underline;
      }
    }
  }

  .btn-primary {
    width: 100%;
    padding: 14px;
    background-color: #0b57d0;
    color: white;
    border: none;
    border-radius: 6px;
    font-size: 1rem;
    font-weight: 500;
    cursor: pointer;
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 8px;
    transition: background-color 0.2s;

    &:hover {
      background-color: #0842a0;
    }

    .arrow {
      font-size: 1.1rem;
    }
  }

  .divider {
    display: flex;
    align-items: center;
    text-align: center;
    margin: 24px 0;
    color: #6b7280;
    font-size: 0.75rem;
    font-weight: 600;
    letter-spacing: 0.5px;

    &::before,
    &::after {
      content: '';
      flex: 1;
      border-bottom: 1px solid #e5e7eb;
    }

    span {
      padding: 0 10px;
    }
  }

  .social-login {
    display: flex;
    gap: 16px;
    margin-bottom: 30px;

    .btn-social {
      flex: 1;
      display: flex;
      justify-content: center;
      align-items: center;
      gap: 8px;
      padding: 12px;
      background-color: #f3f4f6;
      border: none;
      border-radius: 6px;
      font-size: 0.9rem;
      font-weight: 500;
      color: #374151;
      cursor: pointer;
      transition: background-color 0.2s;

      &:hover {
        background-color: #e5e7eb;
      }

      .icon {
        display: flex;
        align-items: center;
      }
    }
  }

  .signup-link {
    text-align: center;
    font-size: 0.9rem;
    color: #4b5563;

    a {
      color: #0b57d0;
      text-decoration: none;
      font-weight: 500;

      &:hover {
        text-decoration: underline;
      }
    }
  }

  .footer-right {
    position: absolute;
    bottom: 15px;
    right: 50px;
    display: flex;
    gap: 20px;

    a {
      font-size: 0.85rem;
      color: #4b5563;
      text-decoration: none;

      &:hover {
        color: #111827;
      }
    }
  }
</style>
