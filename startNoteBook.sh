#!/bin/bash
HAS_NET=`docker network ls | grep 'mynbnetwork' | wc -l`
if [ "X${HAS_NET}" != "X1" ]; then
	echo "Create Docker network"
	sudo docker network create mynbnetwork
fi

echo "Start Postgres"
sudo docker run -d --name notebpostgres --rm -p 5432:5432 --network mynbnetwork iitdbgroup/cs480
echo "Start notebook server"
sudo docker run --user root --rm --name=mynotebook -v "$(pwd)":/home/jovyan/ -p 0.0.0.0:8888:8888/tcp --network mynbnetwork -d iitdbgroup/sql-notebook start-notebook.sh --NotebookApp.token=''
