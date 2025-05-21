FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copy Maven wrapper và file cấu hình
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Cấp quyền cho mvnw
RUN chmod +x mvnw

# Copy toàn bộ source code
COPY src ./src

# Build package jar
RUN ./mvnw clean package -DskipTests

# Stage chạy app
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy jar từ stage build
COPY --from=build /app/target/RoomieHub-0.0.1-SNAPSHOT.jar app.jar

# Chạy jar
ENTRYPOINT ["java", "-jar", "app.jar"]
