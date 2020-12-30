## Building the docker image using maven (Dev style)

`mvn clean install -DbuildDockerImage`

## Building the docker image using docker (Ops style)

`docker build . -f Dockerfile.dockerhub -t container-manager-back`

## Running the docker container

`docker run container-manager-back`

## Environment variables

see fs/scripts/docker-entrypoint.sh
