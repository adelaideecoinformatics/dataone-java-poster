FROM python:2.7-alpine3.8

ADD . /app/
WORKDIR /app/
RUN \
  apk add --no-cache --virtual .build_deps \
    gcc \
    py-cffi \
    musl-dev \
    python-dev \
    libffi-dev \
    openssl-dev \
    yaml-dev && \
  pip install -r ./docker/requirements.txt && \
  python setup.py install && \
  apk del .build_deps
