FROM node:22-alpine AS builder

ENV CI=true

WORKDIR /app
RUN npm install -g pnpm

RUN pnpm config set ignore-scripts true

COPY package.json pnpm-workspace.yaml* pnpm-lock.yaml* ./
RUN pnpm install


COPY . .
RUN pnpm build

FROM fholzer/nginx-brotli:latest

COPY nginx.conf /etc/nginx/nginx.conf

RUN rm -f /etc/nginx/conf.d/default.conf

COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80

CMD ["-g", "daemon off;"]