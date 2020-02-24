#!/usr/bin/env bash

APPNAME="metrics"
INSTALLDIR="/opt/docker"

${INSTALLDIR}/bin/${APPNAME} -Dlogger.file=${INSTALLDIR}/conf/logback.xml
