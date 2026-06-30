# 1 වෙනි පියවර: Java 25 image එකක් අරන් Maven ඉන්ස්ටෝල් කිරීම
FROM eclipse-temurin:25-jdk AS build

# Maven ඉන්ස්ටෝල් කරගන්නා ආකාරය
RUN apt-get update && apt-get install -y maven

WORKDIR /app
COPY . .

# ප්‍රොජෙක්ට් එක බිල්ඩ් කිරීම
RUN mvn -f TravelApp/pom.xml clean package -DskipTests

# 2 වෙනි පියවර: ඇප් එක රන් කිරීම සඳහා කුඩා JRE image එකක් ගැනීම
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/TravelApp/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]