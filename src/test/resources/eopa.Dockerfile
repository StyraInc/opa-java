FROM alpine:latest

RUN apk add nginx bash

ADD nginx.conf /etc/nginx/nginx.conf

ADD entrypoint.sh /entrypoint.sh

RUN chmod +x /entrypoint.sh

# We name the eopa binary opa so that the entrypoint script does not need to be
# modified.
COPY --from=ghcr.io/styrainc/enterprise-opa:latest /ko-app/enterprise-opa-private /usr/bin/opa

ENTRYPOINT ["/entrypoint.sh"]

