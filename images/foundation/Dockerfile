FROM ubuntu:20.04
# FROM ghcr.io/mathieuroblin/ubuntu:latest

LABEL author="Mathieu Roblin"
LABEL version="1.0"
LABEL description="Base image with Babashka installed to run all the custom scripts"

SHELL ["/bin/bash", "-c"]

RUN apt-get update && apt-get install --assume-yes \
    apt-transport-https                            \
    ca-certificates                                \
    curl                                           \
    git                                            \
    wget

# Install Babashka so all the custom scripts can run
WORKDIR /tmp/install_babashka
RUN curl -sLO https://raw.githubusercontent.com/babashka/babashka/master/install
RUN chmod +x install
RUN ./install --version 1.1.173

# Set the default folder and make all scripts available
WORKDIR /root/workspace
COPY scripts ./scripts/

CMD tail -f /dev/null

# Make this the entrypoint once the configuration exposes the port etc...
# sh> bb nrepl-server 1667