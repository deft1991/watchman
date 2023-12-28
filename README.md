# watchman
Watchman telegram bot

docker logs -f --tail 10 701a253a3a46



### Manual CI/CD

1. Open terminal
2. From root: docker build -t deft1991/watchman:**v1.1.5** -t deft1991/watchman:latest -f docker/Dockerfile .
   and do not forget to change version **v1.1.5**
3. docker image push --all-tags deft1991/watchman
4. Optional(if there were changes in terraform) - Then call **terraform plan** and check your changes
5. Optional(if there were changes in terraform) Call **terraform apply**
6. Check ASW Console and wait till deploy
7. Call CICD
