# =========================
# 1. Maven Build
# =========================

FROM maven:3.9.16-eclipse-temurin-25-noble AS builder

WORKDIR /app

RUN echo '<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" \
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" \
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd"> \
  <mirrors> \
    <mirror> \
      <id>aliyunmaven</id> \
      <mirrorOf>central</mirrorOf> \
      <name>Aliyun Maven</name> \
      <url>https://maven.aliyun.com/repository/public</url> \
    </mirror> \
  </mirrors> \
  </settings>' > /usr/share/maven/conf/settings.xml

# Copy Maven manifests
COPY backend/pom.xml backend/lombok.config ./
COPY backend/app/pom.xml app/

# Resolve dependencies
RUN --mount=type=cache,target=/app/.cache/mvn \
    mvn dependency:resolve -T 1C -Dmaven.repo.local=/app/.cache/mvn

# Copy application source
COPY backend/app/src ./app/src

# Build application
RUN --mount=type=cache,target=/app/.cache/mvn \
    mvn clean package -q -DskipTests -T 1C -Dmaven.repo.local=/app/.cache/mvn


# =========================
# 2. AOT Cache Training
# =========================

FROM eclipse-temurin:25-jdk-noble AS trainer

WORKDIR /app

COPY --from=builder /app/app/target/*.jar app.jar

# Extract Spring Boot application
RUN java -Djarmode=tools -jar app.jar extract --destination application

# Generate AOT Cache
RUN java \
    -XX:AOTCacheOutput=application/app.aot \
    -Dspring.context.exit=onRefresh \
    -jar application/*.jar


# =========================
# 3. Production Runtime
# =========================

FROM eclipse-temurin:25-jdk-noble

WORKDIR /app

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/* \
    && groupadd -r appgroup \
    && useradd -r -g appgroup -m -d /app appuser \
    && mkdir -p /app/logs \
    && chown -R appuser:appgroup /app

COPY --from=trainer --chown=appuser:appgroup \
    /app/application ./application

USER appuser

ENV JAVA_OPTS="-XX:InitialRAMPercentage=25.0 \
  -XX:MaxRAMPercentage=60.0 \
  -XX:MetaspaceSize=128m \
  -XX:MaxMetaspaceSize=384m \
  -XX:ReservedCodeCacheSize=256m \
  -XX:+UseG1GC \
  -Xlog:arguments=info \
  -XX:+ExitOnOutOfMemoryError \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/app/logs/heapdump-%p.hprof \
  -Xlog:gc*,gc+heap=debug,safepoint:file=/app/logs/gc.log:time,uptime,level,tags:filecount=10,filesize=50M \
  -Djava.security.egd=file:/dev/./urandom \
  -Dfile.encoding=UTF-8"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -XX:AOTCache=/app/application/app.aot -jar /app/application/*.jar"]