#!/bin/bash
HOST=${1:-discovery-server}
PORT=${2:-8761}
TIMEOUT=${3:-60}

echo "Ожидание $HOST:$PORT..."

for i in $(seq 1 $TIMEOUT); do
  nc -z $HOST $PORT && echo "$HOST:$PORT доступен" && exit 0
  sleep 1
done

echo "Не удалось подключиться к $HOST:$PORT за $TIMEOUT секунд"
exit 1
