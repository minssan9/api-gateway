FROM redis:3.0
MAINTAINER Sanghun Min <sanghun.min@halla.com>

RUN mkdir /var/log/redis

RUN mkdir /usr/local/etc/redis

COPY conf/redis.conf /usr/local/etc/redis/redis.conf
CMD [ "redis-server", "/usr/local/etc/redis/redis.conf" ]


# docker command
sudo usermod -aG docker root
docker run --rm --name redis -p 6379:6379 --network redis-net  -v /hds_redis/redis/redis.conf:/usr/local/etc/redis/redis.conf -d redis:latest redis-server /usr/local/etc/redis/redis.conf --appendonly yes
docker run -it --network redis-net --rm redis redis-cli -h redis

