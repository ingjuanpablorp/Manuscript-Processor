FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY ../../applications/app-service/build/libs/manuscript-processor.jar ./app.jar
ENV JAVA_OPTS=" -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Dcom.sun.management.jmxremote=false"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]

