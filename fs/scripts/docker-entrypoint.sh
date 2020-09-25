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

# vars
FS_AUTH_BACK_URI=${FS_AUTH_BACK_URI:-http://keycloak:8080}
FS_AUTH_CLIENTID=${FS_AUTH_CLIENTID:-fsconsentmgr}
FS_AUTH_FRONT_URI=${FS_AUTH_FRONT_URI:-}
FS_AUTH_REALM=${FS_AUTH_REALM:-FairAndSmart}
FS_CONSENT_PROCESSOR=${FS_CONSENT_PROCESSOR:-https://www.fairandsmart.com}
FS_CONSENTMANAGER_BACKEND="${FS_CONSENTMANAGER_BACKEND:-h2}"
FS_CORS_AC_MAX_AGE=${FS_CORS_AC_MAX_AGE:-24H}
FS_CORS_EXPOSED_HEADERS=${FS_CORS_EXPOSED_HEADERS:-Content-Disposition}
FS_CORS_METHODS=${FS_CORS_METHODS:-OPTIONS,GET,PUT,POST,DELETE}
FS_CORS_ORIGINS=${FS_CORS_ORIGINS:-}
FS_DEBUG=${FS_DEBUG:-}
FS_JVM_ARGS=${FS_JVM_ARGS:-}
FS_KEYSTORE_PATH=${FS_KEYSTORE_PATH:-}
FS_MAXHEAPSIZE=${FS_MAXHEAPSIZE:-1g}
FS_SERIAL_PREFIX=${FS_SERIAL_PREFIX:-U}
FS_SERIAL_SLOT_CAPACITY=${FS_SERIAL_SLOT_CAPACITY:-100}
FS_SERIAL_SLOT_INITIAL=${FS_SERIAL_SLOT_INITIAL:-0}
FS_TOKEN_ISSUER=${FS_TOKEN_ISSUER:-admin}
FS_TOKEN_SECRET=${FS_TOKEN_SECRET:-ThisIsASuperSecret}
FS_TSA_URL=${FS_TSA_URL:-https://www.freetsa.org/tsr}
FS_UNAUTHENTICATED=${FS_UNAUTHENTICATED:-anonymous}

[[ -z "$FS_DEBUG" ]] || set -x

# sanity checks

[ -z "$FS_CORS_ORIGINS" ] && fs_error "Please set FS_CORS_ORIGINS up"
[ -z "$FS_AUTH_FRONT_URI" ] && fs_error "Please set FS_AUTH_FRONT_URI up"

cp -a /config/application-template.properties /config/application.properties

# LOG
mkdir -p /logs

# CORS configuration
sed -i "s|##FS_CORS_ORIGINS##|$FS_CORS_ORIGINS|" /config/application.properties
sed -i "s|##FS_CORS_METHODS##|$FS_CORS_METHODS|" /config/application.properties
sed -i "s|##FS_CORS_EXPOSED_HEADERS##|$FS_CORS_EXPOSED_HEADERS|" /config/application.properties
sed -i "s|##FS_CORS_AC_MAX_AGE##|$FS_CORS_AC_MAX_AGE|" /config/application.properties

# Keycloak configuration
sed -i "s|##FS_AUTH_BACK_URI##|$FS_AUTH_BACK_URI|" /config/application.properties
sed -i "s|##FS_AUTH_FRONT_URI##|$FS_AUTH_FRONT_URI|" /config/application.properties
sed -i "s|##FS_AUTH_REALM##|$FS_AUTH_REALM|" /config/application.properties
sed -i "s|##FS_AUTH_CLIENTID##|$FS_AUTH_CLIENTID|" /config/application.properties

# DB setup
if [[ ${FS_CONSENTMANAGER_BACKEND} == "h2" ]]; then
    FS_DATABASE_DRIVER="h2"
    FS_DATABASE_LOGIN="sa"
    FS_DATABASE_PASSWORD="sa"
    FS_DATABASE_URI="jdbc:h2:/data/database/consent-manager.h2;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
    mkdir -p /data/database
fi

sed -i "s|##FS_DATABASE_DRIVER##|${FS_DATABASE_DRIVER}|" /config/application.properties
sed -i "s|##FS_DATABASE_LOGIN##|${FS_DATABASE_LOGIN}|" /config/application.properties
sed -i "s|##FS_DATABASE_PASSWORD##|${FS_DATABASE_PASSWORD}|" /config/application.properties
sed -i "s|##FS_DATABASE_URI##|${FS_DATABASE_URI}|" /config/application.properties

# Storage configuration
mkdir -p /data/home

# Consent manager configuration
sed -i "s|##FS_CONSENT_PROCESSOR##|$FS_CONSENT_PROCESSOR|" /config/application.properties
sed -i "s|##FS_KEYSTORE_PATH##|$FS_KEYSTORE_PATH|" /config/application.properties
sed -i "s|##FS_SERIAL_PREFIX##|$FS_SERIAL_PREFIX|" /config/application.properties
sed -i "s|##FS_SERIAL_SLOT_CAPACITY##|$FS_SERIAL_SLOT_CAPACITY|" /config/application.properties
sed -i "s|##FS_SERIAL_SLOT_INITIAL##|$FS_SERIAL_SLOT_INITIAL|" /config/application.properties
sed -i "s|##FS_TOKEN_ISSUER##|$FS_TOKEN_ISSUER|" /config/application.properties
sed -i "s|##FS_TOKEN_SECRET##|$FS_TOKEN_SECRET|" /config/application.properties
sed -i "s|##FS_UNAUTHENTICATED##|$FS_UNAUTHENTICATED|" /config/application.properties

# Public links configuration
sed -i "s|##FS_TSA_URL##|$FS_TSA_URL|" /config/application.properties

[[ -z ${FS_DEBUG} ]] || (echo final configuration : ; cat /config/application.properties)

# shellcheck disable=SC2086
read -r -a FS_JVM_ARGS_A <<< ${FS_JVM_ARGS}

exec java "${FS_JVM_ARGS_A[@]}" "-Xmx$FS_MAXHEAPSIZE" -jar "/$FS_BACKEND_TYPE.jar"
