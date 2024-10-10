FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY *.jar app.jar
ENV JAVA_OPTS=" -Xshareclasses:name=cacheapp,cacheDir=/cache,nonfatal -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"
#ENTRYPOINT ["java", "-jar", "app.jar"]
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]