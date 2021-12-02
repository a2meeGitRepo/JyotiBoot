FROM openjdk:8
ADD target/Boot-1.7.jar Boot-1.7.jar
EXPOSE 8091
ENTRYPOINT ["java", "-jar", "Boot-1.7.jar"]