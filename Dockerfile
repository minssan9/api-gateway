#FROM 10.20.101.172:5000/gradle as builder
#WORKDIR /app
#COPY --chown=gradle:gradle . /app
## Command gradle build
#RUN gradle :clean :build -x test --no-daemon


FROM 10.20.101.172:5000/openjdk11
LABEL maintainer="sanghun.min@halla.com"
VOLUME /hallahds/gateway
EXPOSE 31000

#RUN mkdir /app
#COPY --from=builder /app/build/libs/*.jar /app/hds_api_gateway.jar
COPY /build/libs/*.jar /app/hds_api_gateway.jar

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

