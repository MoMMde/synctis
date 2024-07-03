# https://hub.docker.com/_/gradle#:~:text=specify%20a%20user.-,Java%2021%20support,-Java%2021%20does
# For now running on JDk 17 as KotlinDSL not supportet yet
FROM gradle:jdk17 as builder

WORKDIR /home/gradle/

COPY settings.gradle.kts settings.gradle.kts
COPY build.gradle.kts build.gradle.kts
COPY src src
COPY buildSrc buildSrc
COPY gradle.properties gradle.properties
COPY gradle gradle
COPY gradlew gradlew
# used by BuildConfig
COPY .git .git

RUN ["./gradlew", "installDist", "--no-daemon"]

FROM eclipse-temurin:17 as runner

WORKDIR /home/

COPY --from=builder /home/gradle/build/install .

EXPOSE 8080

ENTRYPOINT ["/home/synctis/bin/synctis"]