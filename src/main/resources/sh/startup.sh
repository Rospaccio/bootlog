#!/bin/bash
CURRENT_DIR=${PWD}
CLASSPATH="${CURRENT_DIR}/conf:${CURRENT_DIR}/lib/${project.artifactId}-${project.version}.jar"
MAINCLASS="xyz.codevomit.bootlog.BootlogApplication"
LOG_DIR=${CURRENT_DIR}/logs
if [ ! -d "${LOG_DIR}" ]; then
mkdir ${LOG_DIR}
chmod -R ugoa+rwx ${LOG_DIR}
fi
java -Dapplication.base="${CURRENT_DIR}"  -Dspring.profiles.active=dev -cp "${CLASSPATH}" ${MAINCLASS} >> ${LOG_DIR}/applicationscript.log &
