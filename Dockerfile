FROM openjdk:11

RUN apt-get update && apt-get install -y \
  build-essential
