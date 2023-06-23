#!/usr/bin/env bash

export TWILIO_PHONE="$1"
export TWILIO_ACCOUNT_SID="$2"

usage () {
  echo "twilio-env.sh TWILIO_PHONE TWILIO_ACCOUNT_SID" >&2
}

if [[ -z "${TWILIO_PHONE}" ]] || [[ -z "${TWILIO_ACCOUNT_SID}" ]]; then
  usage
fi

if [[ -z "${TWILIO_AUTH_TOKEN}" ]]; then
  echo "  TWILIO_AUTH_TOKEN must be provided as an environment variable" >&2
fi
