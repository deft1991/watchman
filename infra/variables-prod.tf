#variable "stack_name-prod" {
#  description = "Name of the stack, used to prefix all resources"
#  default     = "watchman-app-prod"
#}
#
#variable "tags-prod" {
#  description = "Tags to apply to all resources"
#  type        = map(string)
#  default = {
#    "Environment" = "dev"
#    "ManagedBy"   = "Terraform"
#    "Name"        = "watchman-app-prod"
#    "Repository"  = "https://github.com/deft1991/watchman"
#  }
#}
#
#variable "vpc_cidr-prod" {
#  description = "CIDR block for the VPC"
#  default     = "10.0.0.0/16"
#  type        = string
#}
#
#variable "azs-prod" {
#  description = "A list of availability zones in the region"
#  default     = ["us-east-2a"]
#  type        = list(string)
#}
#
