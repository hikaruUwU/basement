#!/bin/bash

if [ "$EUID" -ne 0 ]; then
  echo "Not root env"
  exit 1
fi

mkdir -p /etc/docker

cat <<EOF > /etc/docker/daemon.json
{
  "registry-mirrors": [
    "https://hub.rat.dev",
    "https://dockerpull.pw",
    "https://dockerproxy.cn"
  ]
}
EOF

systemctl daemon-reload
systemctl restart docker

if docker info | grep -q "https://hub.rat.dev"; then
    echo "OK"
    docker info | grep -A 4 "Registry Mirrors"
else
    echo "FAIL"
fi