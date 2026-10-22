
# ============================================================
# Versions
# ============================================================

ARG NODE_VERSION=26
ARG NGINX_VERSION=1.31.3
ARG PNPM_VERSION=12.3.4

ARG BROTLI_REPO=https://github.com/google/ngx_brotli.git
ARG VTS_REPO=https://github.com/vozlt/nginx-module-vts.git

# ============================================================
# Frontend builder
# ============================================================

FROM node:${NODE_VERSION}-bookworm-slim AS builder

ENV CI=true

WORKDIR /app

RUN npm config set registry https://registry.npmmirror.com

RUN --mount=type=cache,target=/root/.npm \
    npm install -g --allow-scripts=pnpm pnpm@${PNPM_VERSION}

RUN pnpm config set registry https://registry.npmmirror.com

COPY package.json pnpm-workspace.yaml* pnpm-lock.yaml* ./

RUN --mount=type=cache,target=/root/.local/share/pnpm/store \
    pnpm -r install

COPY . .

RUN --mount=type=cache,target=/root/.local/share/pnpm/store \
    pnpm build:production

# ============================================================
# Build nginx Brotli + VTS dynamic modules
# ============================================================

FROM nginx:${NGINX_VERSION}-alpine AS nginx-builder

ARG NGINX_VERSION
ARG BROTLI_REPO
ARG VTS_REPO

RUN sed -i \
    's/dl-cdn.alpinelinux.org/mirrors.tuna.tsinghua.edu.cn/g' \
    /etc/apk/repositories

RUN apk add --no-cache \
    git \
    gcc \
    g++ \
    make \
    libc-dev \
    pcre2-dev \
    zlib-dev \
    openssl-dev \
    linux-headers \
    brotli-dev \
    wget \
    tar

WORKDIR /tmp

# Download the exact Nginx source version
RUN wget -O nginx.tar.gz \
        https://nginx.org/download/nginx-${NGINX_VERSION}.tar.gz \
    && tar -xzf nginx.tar.gz

# Build dependency: ngx_brotli
RUN git clone --depth 1 ${BROTLI_REPO} ngx_brotli \
    && cd ngx_brotli \
    && git submodule update --init --recursive

# Build dependency: nginx-module-vts
RUN git clone --depth 1 ${VTS_REPO} nginx-module-vts

WORKDIR /tmp/nginx-${NGINX_VERSION}

# Compile both as dynamic modules
RUN ./configure \
        --with-compat \
        --add-dynamic-module=/tmp/ngx_brotli \
        --add-dynamic-module=/tmp/nginx-module-vts \
    && make modules

# ============================================================
# Runtime
# ============================================================

FROM nginx:${NGINX_VERSION}-alpine AS runtime

# Timezone
RUN apk add --no-cache tzdata \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo "Asia/Shanghai" > /etc/timezone

# Copy Brotli modules
COPY --from=nginx-builder \
    /tmp/nginx-${NGINX_VERSION}/objs/ngx_http_brotli_filter_module.so \
    /usr/lib/nginx/modules/

COPY --from=nginx-builder \
    /tmp/nginx-${NGINX_VERSION}/objs/ngx_http_brotli_static_module.so \
    /usr/lib/nginx/modules/

# Copy VTS module
COPY --from=nginx-builder \
    /tmp/nginx-${NGINX_VERSION}/objs/ngx_http_vhost_traffic_status_module.so \
    /usr/lib/nginx/modules/

# Copy Nginx configuration
COPY nginx* /etc/nginx/

# Clean default page and configuration
RUN rm -f /etc/nginx/conf.d/default.conf \
    && rm -f /usr/share/nginx/html/*

# Copy frontend build output
COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80

ENTRYPOINT ["nginx", "-g", "daemon off;"]