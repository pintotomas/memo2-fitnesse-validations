
VERSION=$1
APP=guarabot
docker build -t nicopaez/$APP:$VERSION .
docker push nicopaez/$APP:$VERSION
docker tag nicopaez/$APP:$VERSION nicopaez/$APP:latest
docker push nicopaez/$APP:latest 
