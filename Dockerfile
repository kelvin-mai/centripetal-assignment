FROM clojure:temurin-21-tools-deps-alpine AS builder
ENV CLOJURE_VERSION=1.12.0
ARG APP_PORT
ENV PORT=$APP_PORT

WORKDIR /app
COPY deps.edn build.clj ./
RUN clj -P -X:build
COPY src ./src
COPY resources ./resources
RUN clj -T:build uberjar

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /app/target/indicators-api.jar .

CMD ["java", "-jar", "indicators-api.jar"]
