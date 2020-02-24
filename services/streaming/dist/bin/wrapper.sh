#!/usr/bin/env bash

APPNAME="streaming"
INSTALLDIR="/opt/docker"

${INSTALLDIR}/bin/${APPNAME} -Dlogger.file=${INSTALLDIR}/conf/logback.xml
