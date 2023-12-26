#!/bin/bash

DOCKER_DIR="${DOCKER_DIR:-docker}"

# Define the name of the new Docker image for the 'flask' service
new_docker_image="${1:-deft1991/watchman:latest}"

# Export the new image name as an environment variable
export API_IMAGE="$new_docker_image"

# Use envsubst to replace the placeholder with the new image name in the template
envsubst < "$DOCKER_DIR/compose.template.yaml" > "$DOCKER_DIR/compose.yaml"

echo "Docker image for 'api' service has been updated to '$new_docker_image'."
