FROM maven:3.9.11-eclipse-temurin-21 AS builder

WORKDIR /app

RUN mkdir -p ~/.m2 && \
    echo '<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" \
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
    </settings>' > ~/.m2/settings.xml

COPY pom.xml .

RUN --mount=type=cache,target=/root/.m2 \
    mvn dependency:go-offline -T 1C

COPY src ./src

RUN --mount=type=cache,target=/root/.m2 \
    mvn clean package -DskipTests -T 1C

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENV JAVA_OPTS="-Xms1024m -Xmx1024m \
    -XX:+UseG1GC \
    -XX:MaxGCPauseMillis=200 \
    -XX:+ExitOnOutOfMemoryError \
    -XX:+HeapDumpOnOutOfMemoryError \
    -XX:HeapDumpPath=/app/logs/heapdump.hprof \
    -Djava.security.egd=file:/dev/./urandom \
    -Dfile.encoding=UTF-8 \
    -XX:+PrintCommandLineFlags"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]