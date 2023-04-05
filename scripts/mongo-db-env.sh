#!/usr/bin/env bash

NAME="$1"

if ! command -v jq 1> /dev/null 2> /dev/null; then
  echo "jq not found"
  exit 1
fi

if [[ -z "${NAME}" ]]; then
  echo "IBM Cloud MongoDB resource name must be provided as the first argument"
  exit 1
fi

RESOURCE_ID=$(ibmcloud resource service-instance "${NAME}" --output json | jq -r '.[0] | .id // empty')

if [[ -z "${RESOURCE_ID}" ]]; then
  echo "MongoDB instance not found with name: $NAME"
  exit 1
fi

SERVICE_KEY=$(ibmcloud resource service-keys --output json | jq -c --arg ID "${RESOURCE_ID}" '.[] | select(.source_crn == $ID)')

if [[ -z "${SERVICE_KEY}" ]]; then
  echo "Service key not found for resource: ${RESOURCE_ID}"
  exit 1
fi

export MONGODB_URI=$(echo "${SERVICE_KEY}" | jq -r '.credentials.connection.mongodb.composed[]')
export MONGODB_DATABASE=$(echo "${SERVICE_KEY}" | jq -r '.credentials.connection.mongodb.database')
export MONGODB_USERNAME=$(echo "${SERVICE_KEY}" | jq -r '.credentials.connection.mongodb.authentication.username')
export MONGODB_PASSWORD=$(echo "${SERVICE_KEY}" | jq -r '.credentials.connection.mongodb.authentication.password')
export MONGODB_CACERT_BASE64=$(echo "${SERVICE_KEY}" | jq -r '.credentials.connection.mongodb.certificate.certificate_base64')
