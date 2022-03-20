# bachelor-thesis-sul
System under learning

# for local deployment
1. install microk8s
2. sudo docker build -t coffee-sul
3. sudo docker save coffee-sul > coffee-sul.tar
4. microk8s ctr image import coffee-sul.tar
5. microk8s ctr images ls | grep sul
6. if needed -> modify kubernetes/deployment.yaml
7. microk8s kubectl apply -f sul kubernetes/deployment.yaml
