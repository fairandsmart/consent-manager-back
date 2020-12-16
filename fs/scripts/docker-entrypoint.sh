#!/usr/bin/env bash

fs_error() {
  echo "$*"
  exit 1
}

# security checks
FS_PROD=${FS_PROD:-true}

# container setup
FS_ENVIRONMENT_NAME=${FS_ENVIRONMENT_NAME:-dev}
FS_BACKEND_TYPE=consent-manager-back
FS_DEBUG=${FS_DEBUG:-}
FS_JVM_ARGS=${FS_JVM_ARGS:-}
FS_MAXHEAPSIZE=${FS_MAXHEAPSIZE:-1g}

# HTTP config
export FS_HTTP_PORT=${FS_HTTP_PORT:-8087}
export FS_CORS_ORIGINS=${FS_CORS_ORIGINS:-}

# OIDC config
export FS_AUTH_BACK_URI=${FS_AUTH_BACK_URI:-http://keycloak:8080}
export FS_AUTH_CLIENTID=${FS_AUTH_CLIENTID:-fsconsentmgr}
export FS_AUTH_FRONT_URI=${FS_AUTH_FRONT_URI:-}
export FS_AUTH_REALM=${FS_AUTH_REALM:-FairAndSmart}

# DB setup
FS_CONSENTMANAGER_BACKEND="${FS_CONSENTMANAGER_BACKEND:-h2}"
if [[ ${FS_CONSENTMANAGER_BACKEND} == "h2" ]]; then
    export FS_DATABASE_DRIVER="h2"
    export FS_DATABASE_LOGIN="sa"
    export FS_DATABASE_PASSWORD="sa"
    export FS_DATABASE_URI="jdbc:h2:/data/database/consent-manager.h2;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
    mkdir -p /data/database
fi

# Mailer setup
export FS_MAILER_FROM=${FS_MAILER_FROM:-consent-manager@fairandsmart.io}
export FS_MAILER_HOST=${FS_MAILER_HOST:-mail}
export FS_MAILER_PORT=${FS_MAILER_PORT:-25}

# Logs setup
export FS_LOGLEVEL_MAIN=${FS_LOGLEVEL_MAIN:-INFO}
export FS_LOGLEVEL_FS=${FS_LOGLEVEL_FS:-DEBUG}
mkdir -p /logs

# MainConfig
export FS_INSTANCE_NAME=${FS_INSTANCE_NAME:-$FS_ENVIRONMENT_NAME}
export FS_INSTANCE_OWNER=${FS_INSTANCE_OWNER:-demo}
export FS_PUBLIC_URL=${FS_PUBLIC_URL:-}
export FS_TOKEN_SECRET=${FS_TOKEN_SECRET:-eozaireeghie1aeD2phu}
mkdir -p /data/home

# SupportServiceConfig
export FS_CORE_URL=${FS_CORE_URL:-https://core.fairandsmart.com/api}
export FS_TSA_URL=${FS_TSA_URL:-https://freetsa.org/tsr}

# SerialConfig

# KeystoreConfig
export FS_KEYSTORE_PATH=${FS_KEYSTORE_PATH:-/data/keystore.jks}

# CLientConfig
export FS_GUI_URI=${FS_GUI_URI:-http://localhost}

[[ -z "$FS_DEBUG" ]] || set -x

# sanity checks

[[ -z "$FS_CORS_ORIGINS" ]] && fs_error "Please set FS_CORS_ORIGINS up"
[[ -z "$FS_AUTH_FRONT_URI" ]] && fs_error "Please set FS_AUTH_FRONT_URI up"
[[ -z "$FS_CORE_URL" ]] && fs_error "Please set FS_CORE_URL up"
[[ -z "$FS_PUBLIC_URL" ]] && fs_error "Please set FS_PUBLIC_URL up"

envsubst < /config/application-template.properties > /config/application.properties

[[ -z ${FS_DEBUG} ]] || (echo final configuration : ; cat /config/application.properties)

# shellcheck disable=SC2086
read -r -a FS_JVM_ARGS_A <<< ${FS_JVM_ARGS}

exec java "${FS_JVM_ARGS_A[@]}" "-Xmx$FS_MAXHEAPSIZE" -jar "/$FS_BACKEND_TYPE.jar"
