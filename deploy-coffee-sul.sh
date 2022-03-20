#!/bin/bash
echo "Cleanup..."
sudo docker image rm coffee-sul:latest;
microk8s ctr image remove docker.io/library/coffee-sul:latest;
microk8s kubectl delete deployment -n sul coffee-sul
echo "Cleanup complete!"
echo "Building image..."
sudo docker build -t coffee-sul . --no-cache
echo "Image built!"
echo "Exporting image..."
sudo docker save coffee-sul > coffee-sul.tar
echo "Image exported!"
echo "Adding image to microk8s local repository..."
microk8s ctr image import coffee-sul.tar > /dev/null
microk8s ctr images ls | grep coffee-sul
echo "Granted access to image in microk8s deployments!"
echo "Deploying..."
microk8s kubectl apply -f ./kubernetes/deployment.yaml -n sul
echo "Deployed - have fun!"
microk8s kubectl get deployments,pods -n sul
