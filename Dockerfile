FROM openjdk:17-slim

ADD target/DailyPeTask.jar DailyPeTask.jar

VOLUME /tmp

ENTRYPOINT ["java", "-jar", "DailyPeTask.jar"]