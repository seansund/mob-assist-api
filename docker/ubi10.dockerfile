# Check out https://hub.docker.com/_/node to select a new base image
FROM registry.access.redhat.com/ubi10/nodejs-22-minimal:10.0-1750857232 as base
FROM base as builder

USER 1001

# Install app dependencies
# A wildcard is used to ensure both package.json AND package-lock.json are copied
# where available (npm@5+)
COPY --chown=1001:0 package*.json ./

RUN npm ci

# Bundle app source code
COPY --chown=1001:0 .. .

RUN npm run build

FROM base as runtime

USER 1001

COPY --chown=1001:0 --from=builder /opt/app-root/src/dist dist
COPY --chown=1001:0 --from=builder /opt/app-root/src/package*.json ./

RUN npm ci --omit=dev

# Bind to all network interfaces so that it can be mapped to the host OS
ENV HOST=0.0.0.0 PORT=8080

EXPOSE ${PORT}
CMD [ "node", "." ]
