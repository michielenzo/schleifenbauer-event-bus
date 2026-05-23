FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn -DskipTests package dependency:copy-dependencies

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /build/target/classes ./classes
COPY --from=build /build/target/dependency ./libs

CMD ["java", "-cp", "classes:libs/*", "schleifenbauer.Main"]
