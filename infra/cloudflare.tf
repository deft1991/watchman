## Setup dns in CloudFlare for the domain deft.com , ip from aws_instance.web.public_ip
#
#data "cloudflare_zone" "deftfun" {
#  name = "deftfun.tech"
#}
#
## Use terraform plan -var "cloudflare_email=$EMAIL" -var "cloudflare_api_token=$API_TOKEN" to run this
#resource "cloudflare_record" "java_web" {
#  zone_id = data.cloudflare_zone.deftfun.id
#  name    = "hello"
#  value   = aws_instance.web.public_ip
#  type    = "A"
#  ttl     = "300"
#  proxied = true
#
#  comment = "Pointing to ec2 instance. Managed by Terraform."
#}
#
#resource "cloudflare_record" "java_web_www" {
#  zone_id = data.cloudflare_zone.deftfun.id
#  name    = "www.hello"
#  value   = aws_instance.web.public_ip
#  type    = "A"
#  ttl     = "300"
#  proxied = true
#
#  comment = "Pointing to ec2 instance. Managed by Terraform."
#}
