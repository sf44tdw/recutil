#!//bin/bash

source install_common.sh

LOGDIR=${THIS_DIR}/uninstall-log/

LOGFILE=${LOGDIR}`date +%Y%m%d%H%M%S`.log

make_dir_if_not_exist ${LOGDIR}

touch ${LOGFILE}
echo '***************************************************************************************************************************************'>> ${LOGFILE}
echo "削除開始。${DESTDIR}">> ${LOGFILE}
pwd >> ${LOGFILE} 2>&1
cd ${INSTALL_TARGET_DIR}
pwd >> ${LOGFILE} 2>&1
cat ${INSTALL_FILE_LIST} | xargs -t rm -rf >> ${LOGFILE} 2>&1
echo "削除完了。${DESTDIR}">> ${LOGFILE}
echo '***************************************************************************************************************************************' >> ${LOGFILE}



