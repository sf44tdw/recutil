#!//bin/bash

source install_common.sh

LOGDIR=${THIS_DIR}/install-log/

LOGFILE=${LOGDIR}`date +%Y%m%d%H%M%S`.log

make_dir_if_not_exist ${LOGDIR}

cd ${THIS_DIR}
touch ${LOGFILE}
echo '***************************************************************************************************************************************'>> ${LOGFILE}
echo "コピー開始。${DESTDIR} -> ${INSTALL_TARGET_DIR}">> ${LOGFILE}
make_dir_if_not_exist ${INSTALL_TARGET_DIR}
\cp -f -p ./regularly/updateEPG.sh ${DESTDIR}
chmod -R a+x ${DESTDIR}
cd ${DESTDIR}
find > ${INSTALL_FILE_LIST} 2>>${LOGFILE}
\cp -r -v -f -p * ${INSTALL_TARGET_DIR} >> ${LOGFILE} 2>&1
echo "コピー完了。${DESTDIR} -> ${INSTALL_TARGET_DIR}">> ${LOGFILE}
echo '***************************************************************************************************************************************' >> ${LOGFILE}

