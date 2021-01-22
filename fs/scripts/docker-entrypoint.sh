#!/usr/bin/env bash

fs_error() {
  echo "$*"
  exit 1
}

# security checks
FS_PROD=${FS_PROD:-true}

# container setup
FS_ENVIRONMENT_NAME=${FS_ENVIRONMENT_NAME:-DEV}
FS_ENVIRONMENT_NAME=${FS_ENVIRONMENT_NAME^^}
FS_BACKEND_TYPE=consent-manager-back
FS_DEBUG=${FS_DEBUG:-}
FS_JVM_ARGS=${FS_JVM_ARGS:-}
FS_MAXHEAPSIZE=${FS_MAXHEAPSIZE:-1g}

# HTTP config
export FS_HTTP_PORT=${FS_HTTP_PORT:-8087}

# OIDC config
export FS_AUTH_BACK_URL=${FS_AUTH_BACK_URL:-http://keycloak:8080}
export FS_AUTH_CLIENTID=${FS_AUTH_CLIENTID:-cmclient}
export FS_AUTH_FRONT_URL=${FS_AUTH_FRONT_URL:-}
export FS_AUTH_REALM=${FS_AUTH_REALM:-RightConsents}

# DB setup
FS_DATABASE_KIND="${FS_DATABASE_KIND:-h2}"
if [[ ${FS_DATABASE_KIND} == "pg" ]]; then
    export FS_DATABASE_DRIVER="postgresql"
    export FS_DATABASE_USER=${FS_DATABASE_USER:-user}
    export FS_DATABASE_PASSWORD=${FS_DATABASE_PASSWORD:-password}
    export FS_DATABASE_SERVERS=${FS_DATABASE_SERVERS:-server}
    export FS_DATABASE_DB=${FS_DATABASE_DB:-db}
else
    export FS_DATABASE_DRIVER="h2"
    export FS_DATABASE_USER="sa"
    export FS_DATABASE_PASSWORD="sa"
    export FS_DATABASE_SERVERS="data/database"
    export FS_DATABASE_DB="consent-manager.h2"
    mkdir -p /data/database
fi

export FS_DATABASE_URL="jdbc:${FS_DATABASE_DRIVER}://${FS_DATABASE_SERVERS}/${FS_DATABASE_DB}"

# Mailer setup
export FS_MAILER_FROM=${FS_MAILER_FROM:-demo@demo.com}
export FS_MAILER_HOST=${FS_MAILER_HOST:-mail}
export FS_MAILER_PORT=${FS_MAILER_PORT:-25}

# Logs setup
export FS_LOGLEVEL_MAIN=${FS_LOGLEVEL_MAIN:-INFO}
export FS_LOGLEVEL_FS=${FS_LOGLEVEL_FS:-DEBUG}
mkdir -p /logs

# MainConfig
export FS_INSTANCE_NAME=${FS_INSTANCE_NAME:-$FS_ENVIRONMENT_NAME}
export FS_INSTANCE_LANG=${FS_INSTANCE_LANG:-en}
export FS_INSTANCE_OWNER=${FS_INSTANCE_OWNER:-demo}
export FS_PUBLIC_URL=${FS_PUBLIC_URL:-}
export FS_TOKEN_SECRET=${FS_TOKEN_SECRET:-eozaireeghie1aeD2phu}
mkdir -p /data/home

# ClientConfig
export FS_GUI_URL=${FS_GUI_URL:-http://localhost}

[[ -z "$FS_DEBUG" ]] || set -x

# sanity checks

[[ -z "$FS_AUTH_FRONT_URL" ]] && fs_error "Please set FS_AUTH_FRONT_URL up"
[[ -z "$FS_PUBLIC_URL" ]] && fs_error "Please set FS_PUBLIC_URL up"

envsubst < /config/application-template.properties > /config/application.properties

[[ -z ${FS_DEBUG} ]] || (echo final configuration : ; cat /config/application.properties)

# shellcheck disable=SC2086
read -r -a FS_JVM_ARGS_A <<< ${FS_JVM_ARGS}

exec java "${FS_JVM_ARGS_A[@]}" "-Xmx$FS_MAXHEAPSIZE" -jar "/$FS_BACKEND_TYPE-$FS_DATABASE_KIND.jar"
