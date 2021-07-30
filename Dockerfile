FROM java:8
# Add Author info
LABEL maintainer="sanghun.min@halla.com"
o
# Add a volume to /tmp
VOLUME /hallahds/gateway

# Make port 8080 available to the world outside this container
EXPOSE 34009

# The application's jar file
ARG JAR_FILE=build/libs/*.jar

# Add the application's jar to the container
ADD ${JAR_FILE} hds_api_gateway.jar

ENV SPRING_PROFILES_ACTIVE develop
RUN echo 'spring profile active='$SPRING_PROFILES_ACTIVE
# Run the jar file
#ENTRYPOINT ["java", "-Dspring.profiles.active=release", "-jar", "/hds_api_gateway.jar"]
ENTRYPOINT ["java", "-jar", "/hds_api_gateway.jar"]



# docker command
# docker build -t hds_api_gateway ./api --build-arg PROFILE=release
# docker tag hds_api_gateway 10.20.101.172:5000/hds_api_gateway
# docker push 10.20.101.172:5000/hds_api_gateway
# docker pull 10.20.101.172:5000/hds_api_gateway
# docker tag 10.20.101.172:5000/hds_api_gateway hds_api_gateway
# docker run -it -d -p 34009:34009 -v /var/log:/var/log -e SPRING_PROFILES_ACTIVE=develop hds_api_gateway


