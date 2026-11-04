# ============================================================
# Versions
# ============================================================

ARG NODE_VERSION=26
ARG NGINX_VERSION=1.31.3
ARG PNPM_VERSION=12.3.4


# ============================================================
# 1. Frontend Builder
# ============================================================

FROM node:${NODE_VERSION}-bookworm-slim AS builder

ARG PNPM_VERSION

ENV CI=true

WORKDIR /app

# NPM registry
RUN npm config set registry https://registry.npmmirror.com

# Install pnpm
RUN --mount=type=cache,target=/root/.npm \
    npm install -g --allow-scripts=pnpm pnpm@${PNPM_VERSION}

RUN pnpm config set registry https://registry.npmmirror.com

# Copy frontend manifests
COPY frontend/package.json \
     frontend/pnpm-workspace.yaml* \
     frontend/pnpm-lock.yaml* \
     ./

# Install dependencies
RUN --mount=type=cache,target=/root/.local/share/pnpm/store \
    pnpm -r install

# Copy frontend source
COPY frontend/ ./

# Build frontend
RUN --mount=type=cache,target=/root/.local/share/pnpm/store \
    pnpm build:production


# ============================================================
# 2. Nginx Builder
# ============================================================

FROM nginx:${NGINX_VERSION}-alpine AS nginx-builder

ARG NGINX_VERSION=1.31.3

# ------------------------------------------------------------
# Install build dependencies
# ------------------------------------------------------------

RUN sed -i \
    's/dl-cdn.alpinelinux.org/mirrors.tuna.tsinghua.edu.cn/g' \
    /etc/apk/repositories \
    && apk add --no-cache \
        git \
        wget \
        tar \
        cmake \
        gcc \
        g++ \
        make \
        libc-dev \
        pcre2-dev \
        zlib-dev \
        openssl-dev \
        linux-headers

WORKDIR /tmp


# ============================================================
# 3. Download Nginx Source
# ============================================================

RUN wget -O nginx.tar.gz \
        https://nginx.org/download/nginx-${NGINX_VERSION}.tar.gz \
    && tar -xzf nginx.tar.gz \
    && mv nginx-${NGINX_VERSION} nginx \
    && rm nginx.tar.gz


# ============================================================
# 4. Clone Third-party Modules
# ============================================================

ARG NGX_BROTLI_REPO=https://github.com/google/ngx_brotli.git
ARG NGX_BROTLI_BRANCH=master

ARG NGINX_VTS_REPO=https://github.com/vozlt/nginx-module-vts.git
ARG NGINX_VTS_BRANCH=master

# Clone ngx_brotli with Brotli submodule
RUN git clone --depth 1 \
        --branch ${NGX_BROTLI_BRANCH} \
        --recurse-submodules \
        ${NGX_BROTLI_REPO} \
        /tmp/ngx_brotli

# Clone nginx-module-vts
RUN git clone --depth 1 \
        --branch ${NGINX_VTS_BRANCH} \
        ${NGINX_VTS_REPO} \
        /tmp/nginx-module-vts


# ============================================================
# 5. Build Brotli Libraries
# ============================================================

# ngx_brotli expects the Brotli libraries in:
# /tmp/ngx_brotli/deps/brotli/out

RUN cd /tmp/ngx_brotli/deps/brotli \
    && cmake -S . -B out \
        -DCMAKE_BUILD_TYPE=Release \
        -DCMAKE_POSITION_INDEPENDENT_CODE=ON \
        -DBUILD_SHARED_LIBS=OFF \
    && cmake --build out \
        --config Release \
        --parallel \
    && ls -lh out/libbrotlienc.a out/libbrotlicommon.a


# ============================================================
# 6. Compile Nginx Dynamic Modules
# ============================================================

WORKDIR /tmp/nginx

RUN ./configure \
        --with-compat \
        --add-dynamic-module=/tmp/ngx_brotli \
        --add-dynamic-module=/tmp/nginx-module-vts \
    && make modules


# ============================================================
# 7. Production Runtime
# ============================================================

FROM nginx:${NGINX_VERSION}-alpine AS runtime


# ------------------------------------------------------------
# Timezone
# ------------------------------------------------------------

RUN apk add --no-cache tzdata \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone


# ============================================================
# 8. Copy Dynamic Modules
# ============================================================

# Brotli filter module
COPY --from=nginx-builder \
    /tmp/nginx/objs/ngx_http_brotli_filter_module.so \
    /usr/lib/nginx/modules/

# Brotli static module
COPY --from=nginx-builder \
    /tmp/nginx/objs/ngx_http_brotli_static_module.so \
    /usr/lib/nginx/modules/

# VTS module
COPY --from=nginx-builder \
    /tmp/nginx/objs/ngx_http_vhost_traffic_status_module.so \
    /usr/lib/nginx/modules/


# ============================================================
# 9. Nginx Configuration
# ============================================================

COPY infra/nginx/conf/ /etc/nginx/

# Remove default configuration and default web files
RUN rm -f /etc/nginx/conf.d/default.conf \
    && rm -f /usr/share/nginx/html/*


# ============================================================
# 10. Frontend Static Files
# ============================================================

COPY --from=builder \
    /app/dist/ \
    /usr/share/nginx/html/


# ============================================================
# 11. Runtime
# ============================================================

EXPOSE 80

ENTRYPOINT ["nginx", "-g", "daemon off;"]