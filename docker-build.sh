#!/usr/bin/env bash
name=tripstore-shopping-carts-service
docker rmi "$name"
docker build . -t "$name"