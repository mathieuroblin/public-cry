# public-cry
Container registry for public images

## Authentication
```sh
export CR_PAT=<Personal_Token>
echo $CR_PAT | docker login ghcr.io -u mathieuroblin --password-stdin
```

## Pushing an image
```sh
docker build -t ghcr.io/mathieuroblin/ubuntu:latest
docker push ghcr.io/mathieuroblin/ubuntu:latest
```

## Hacking away
**Build the latest image from the Dockerfile**
docker build -t ghcr.io/mathieuroblin/ubuntu:latest .
**Start the container**
docker run -d --name dev-box ghcr.io/mathieuroblin/ubuntu:latest tail -f /dev/null
**Stop the container and delete it**
docker stop dev-box && docker rm dev-box