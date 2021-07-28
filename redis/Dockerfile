FROM redis:3.0
MAINTAINER Sanghun Min <sanghun.min@halla.com>

RUN mkdir /var/log/redis

RUN mkdir /usr/local/etc/redis

COPY conf/redis.conf /usr/local/etc/redis/redis.conf
CMD [ "redis-server", "/usr/local/etc/redis/redis.conf" ]


# docker command
docker pull redis
docker network create redis-net
sudo usermod -aG docker root
# redis 실행
# linux
docker run --rm --name redis -p 6379:6379 --network redis-net  -v /hds_redis/redis/redis.conf:/usr/local/etc/redis/redis.conf -d redis:latest redis-server /usr/local/etc/redis/redis.conf --appendonly yes
# windows
docker run --rm --name redis -p 6379:6379 --network redis-net -d redis:latest redis-server  --appendonly yes
# redis log 확인
docker logs redis
# redis container shell
docker run -it --network redis-net --rm redis redis-cli -h redis
