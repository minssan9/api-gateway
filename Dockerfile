FROM java:8
# Add Author info
LABEL maintainer="sanghun.min@halla.com"

# Add a volume to /tmp
VOLUME /hallahds/gateway

# Make port 8080 available to the world outside this container
EXPOSE 31000

# The application's jar file
ARG JAR_FILE=build/libs/*.jar

# Add the application's jar to the container
ADD ${JAR_FILE} gateway.jar

ARG PROFILE
RUN echo $PROFILE
ENV SPRING_PROFILES_ACTIVE=$PROFILE
# Run the jar file
#ENTRYPOINT ["java", "-Dspring.profiles.active=release", "-jar", "/auth.jar"]
ENTRYPOINT ["java", "-jar", "/gateway.jar"]



# docker command
# docker build -t hds_api_gateway ./api --build-arg PROFILE=release
# docker tag hds_api_gateway 10.20.101.172:5000/hds_api_gateway
# docker push 10.20.101.172:5000/hds_api_gateway
# docker pull 10.20.101.172:5000/hds_api_gateway
# docker tag 10.20.101.172:5000/hds_api_gateway hds_api_gateway
# docker run -it -d -p 34009:34009 -v /var/log:/var/log   hds_api_gateway



