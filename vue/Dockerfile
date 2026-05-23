FROM node:22-alpine AS builder

ENV CI=true

WORKDIR /app

RUN --mount=type=cache,target=/root/.npm \
    npm install -g pnpm

RUN pnpm config set ignore-scripts true

COPY package.json pnpm-workspace.yaml* pnpm-lock.yaml* ./

RUN --mount=type=cache,target=/root/.local/share/pnpm/store \
    pnpm install

COPY . .

RUN --mount=type=cache,target=/root/.local/share/pnpm/store \
    pnpm build:production

FROM fholzer/nginx-brotli:v1.31

COPY nginx.conf /etc/nginx/nginx.conf

RUN rm -f /etc/nginx/conf.d/default.conf

RUN rm -f /usr/share/nginx/html/*

COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80

ENTRYPOINT ["nginx", "-g", "daemon off;"]