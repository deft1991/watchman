output "ec2_public_dns" {
  description = "The public DNS name assigned to the instance. Use it to connect to your instance or app."
  value       = aws_instance.web.public_dns
}

output "ec2_public_ip" {
  description = "The public IP assigned to the instance."
  value       = aws_instance.web.public_ip
}

#output "cloudflare_java_web_id" {
#  description = "The ID of the Cloudflare record"
#  value       = cloudflare_record.java_web.id
#}

#output "cloudflare_java_web_hostname" {
#  description = "The hostname of the Cloudflare record"
#  value       = cloudflare_record.java_web.hostname
#}
