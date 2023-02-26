FROM ubuntu:20.04

# Make bash the default shell for setup
SHELL ["/bin/bash", "-c"]

# Install all dependencies
RUN apt-get update && apt-get install --assume-yes \
  curl                       \
  g++                        \
  gcc                        \
  git                        \
  make                       \
  software-properties-common \
  zsh

# Make zsh the default shell for the users
RUN chsh -s $(which zsh)

# Install oh-my-zsh
RUN sh -c "$(curl -fsSL https://raw.github.com/ohmyzsh/ohmyzsh/master/tools/install.sh)"