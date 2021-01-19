# Right Consents - Consent Manager Backend Component

This is the backend of the Right Consents Community Edition product. 

You should find more information on the dedicated product website https://fairandsmart.github.io/right-consents/

## Building the docker image using maven (Dev style)

`mvn clean install -DbuildDockerImage`

## Building the docker image using docker (Ops style)

`docker build . -f Dockerfile.dockerhub -t container-manager-back`

## Running the docker container

`docker run container-manager-back`

## Environment variables

see fs/scripts/docker-entrypoint.sh
