## IAC for AWS java application

### Quick start

1. Setup credentials for AWS provider. You can use `~/.aws/credentials` file or environment variables. Read [more](https://registry.terraform.io/providers/hashicorp/aws/latest/docs#authentication).

```shell
vi ~/.aws/credentials
```

```shell
[deft]
aws_access_key_id=<your access key>
aws_secret_access_key=<your secret key>
```

```shell
export AWS_PROFILE=deft
```

2. Run terraform

```shell
cd infra/aws
terraform init # initialize terraform providers and dependencies
terraform plan # show what will be changed
terraform apply # apply changes
``` 

### Requirements

- Terraform version 1.4.6 or newer
