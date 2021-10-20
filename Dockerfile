FROM gradle:7-jdk11 as builder
WORKDIR /app
COPY --chown=gradle:gradle . /app
# Command gradle build
RUN ./gradlew :clean :build -x test


FROM java:8
LABEL maintainer="sanghun.min@halla.com"
VOLUME /hallahds/gateway
EXPOSE 31000

RUN mkdir /app
COPY --from=builder /app/build/libs/*.jar /app/hds_api_gateway.jar

ENV SPRING_PROFILES_ACTIVE develop
RUN echo 'spring profile active='$SPRING_PROFILES_ACTIVE
#ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILE}", "-jar", "/hds_api_gateway.jar"]
ENTRYPOINT ["java", "-jar", "/app/hds_api_gateway.jar"]

# docker command
# docker build -t hds_api_gateway -f Dockerfile .
# docker tag hds_api_gateway asia.gcr.io/vaulted-timing-325008/hds_api_gateway
# docker push asia.gcr.io/vaulted-timing-325008/hds_api_gateway
# docker pull asia.gcr.io/vaulted-timing-325008/hds_api_gateway
# docker tag asia.gcr.io/vaulted-timing-325008/hds_api_gateway hds_api_gateway
# docker run -it -d -p 31000:31000 -v /var/log:/var/log -e SPRING_PROFILES_ACTIVE=develop hds_api_gateway

