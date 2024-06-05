FROM alpine:latest

RUN apk add go git nginx bash

ADD nginx.conf /etc/nginx/nginx.conf

ADD entrypoint.sh /entrypoint.sh

# Install the latest version of OPA from source, since we need nginx in the
# container, OPA isn't packaged by Alpine, and the upstream OPA package
# contains only OPA and no userland.
RUN mkdir -p /src && \
    cd /src && \
    git clone https://github.com/open-policy-agent/opa.git && \
    cd opa && \
    git checkout "$(git tag | grep -v 'rc' | grep '^v' | sort -V | tail -n 1)" && \
    go build -o /usr/bin/opa -ldflags="-s -w" ./ && \
    cd /src && \
    rm -rf ./opa && \
    chmod +x /entrypoint.sh

ENTRYPOINT ["/entrypoint.sh"]

