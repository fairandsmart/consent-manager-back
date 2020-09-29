## Building the docker image

Run "docker build . -t container-manager-back"

## Running the docker container

Environment variables (no default) :
* FS_AUTH_URI (default is http://127.0.0.1:8080)
* FS_AUTH_REALM (default is FairAndSmart)
* FS_AUTH_CLIENTID (default is fsconsentmgr)
* FS_CONSENT_PROCESSOR (default is https://www.fairandsmart.com)
* FS_UNAUTHENTICATED (default is anonymous)
* FS_TSA_URL (default is https://127.0.0.1:8580/tsr)
