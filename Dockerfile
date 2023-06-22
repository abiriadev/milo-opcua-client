FROM gradle:8.1.1-jdk17-jammy AS builder

WORKDIR /usr/app

# setup gradle build
COPY ./settings.gradle .

# setup gradle wrapper
COPY ./gradle/wrapper/gradle-wrapper.properties ./gradle/wrapper/
RUN ["gradle", "wrapper"]
# this is necessary to download gradle before copying source code
RUN ["./gradlew", "--version"]

# copy source and build
COPY . .
RUN ["./gradlew", "build"]

FROM eclipse-temurin:17.0.7_7-jdk-jammy AS runner

WORKDIR /usr/app

# copy fat jar file from the previous container
COPY --from=builder /usr/app/app/build/libs/app-all.jar app-all.jar

CMD ["java", "-jar", "./app-all.jar"]
