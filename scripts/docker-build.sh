#!/usr/bin/env bash

DOCKERFILE_PATH='./docker'
DOCKERFILE='Dockerfile'

docker build -t webui-user-v2 -f ${DOCKERFILE_PATH}/${DOCKERFILE} .
