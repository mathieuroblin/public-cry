# public-cry
Container registry for public images

## Authentication
```sh
export CR_PAT=<Personal_Token>
echo $CR_PAT | docker login ghcr.io -u mathieuroblin --password-stdin
```

## Pushing an image
```sh
docker build -t ghcr.io/mathieuroblin/bullseye:latest
docker push ghcr.io/mathieuroblin/bullseye:latest
```
