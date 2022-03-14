# bachelor-thesis-sul
System under learning

# for local deployment
1. install microk8s
2. sudo docker build -t jsaev/bachelor-sul
3. sudo docker save jsaev/bachelor-sul > bachelor-sul.tar
4. microk8s ctr image import bachelor-sul.tar
5. microk8s ctr images ls | grep sul
6. if needed -> modify kubernetes/deployment.yaml
7. microk8s kubectl apply -f sul kubernetes/deployment.yaml
