# Stage 1: build the SPA (workspace packages are consumed as TS source,
# so the whole monorepo is needed for the app build).
ARG NODE_VERSION=26
ARG PNPM_VERSION=12.3.4
FROM node:${NODE_VERSION}-bookworm-slim AS web-builder
ARG PNPM_VERSION
ENV CI=true
WORKDIR /src
# Install pnpm
RUN npm config set registry https://registry.npmmirror.com
RUN --mount=type=cache,target=/root/.npm \
    npm install -g --allow-scripts=pnpm pnpm@${PNPM_VERSION}
# Copy dependency manifests
COPY package.json pnpm-lock.yaml pnpm-workspace.yaml ./
# Fetch dependencies from the lockfile alone
RUN pnpm config set registry https://registry.npmmirror.com
# Copy source code
COPY . .
# Install dependencies
RUN pnpm install
# Build application
RUN pnpm --filter dashglass build
# Stage 2: build the Go binary with the SPA embedded.
FROM golang:1.26-alpine AS go-builder
ARG VERSION=dev
WORKDIR /src/backend
COPY backend/go.mod backend/go.sum ./
ENV GOPROXY=https://goproxy.cn,https://proxy.golang.org,direct
RUN go mod download
COPY backend/ ./
# Replace the tracked placeholder with the real build output.
RUN rm -rf internal/ui/dist
COPY --from=web-builder /src/apps/dashglass/dist internal/ui/dist
RUN CGO_ENABLED=0 go build -trimpath -ldflags="-s -w -X main.version=${VERSION}" -o /dashglass .

# Stage 3: minimal runtime image. git + ssh power the dashboard history
# and remote sync features (absent binaries just disable them).
FROM alpine:3.22
RUN apk add --no-cache git openssh-client
RUN adduser -D -H -u 1000 dashglass
COPY --from=go-builder /dashglass /usr/local/bin/dashglass
# /data holds server-managed state (auth.yaml); owned by the non-root user
# and its own volume so it persists and stays writable.
RUN mkdir -p /data && chown dashglass /data
USER dashglass
EXPOSE 8080
VOLUME /dashboards
VOLUME /data
ENV DASHGLASS_AUTH_FILE=/data/auth.yaml
ENTRYPOINT ["dashglass", "-dashboards-dir", "/dashboards"]
