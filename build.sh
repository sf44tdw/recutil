#!//bin/bash

source common.sh

separator () {
    echo '***************************************************************************************************************************************' >> ${LOGFILE}
}

maven_install(){
	#テスト用ダミーコマンドに実行権限付加
	chmod u+x ${THIS_DIR}/recutil/recutil/executable/needdb/executerecordcommand/recpt1
    separator
    cd recutil
    mvn install >> ${LOGFILE} 2>&1
    separator
}

prepare_deploy(){
separator
cd ${THIS_DIR}
#依存ファイルをすべて組み込んだ実行可能jarを1か所に集める。既にある場合は上書きする。

make_dir_if_not_exist ${JARDIR}

separator
echo "ファイル移動開始" >> ${LOGFILE}
pwd >> ${LOGFILE} 2>&1

cd ${THIS_DIR}
pwd >> ${LOGFILE} 2>&1

find . -type f -name "*jar-with-dependencies.jar" | xargs -t -I{} mv -f -v {} ${JARDIR} >> ${LOGFILE} 2>&1

echo "ファイル移動完了" >> ${LOGFILE}
pwd >> ${LOGFILE} 2>&1
separator

separator
echo "キッカー作成開始" >> ${LOGFILE}
pwd >> ${LOGFILE} 2>&1

#コマンド用キッカー配置

KICKER_DIR=${THIS_DIR}/kicker

SOURCE_SCRIPT=${KICKER_DIR}/executor

cd ${JARDIR}
pwd >> ${LOGFILE} 2>&1

LIST=`find . -type f -name "*jar-with-dependencies.jar" | sed 's!^.*/!!' | sed -e 's/-.*\.jar$//' `

cd ${THIS_DIR}
pwd >> ${LOGFILE} 2>&1

for L in ${LIST}
do
  cat ${SOURCE_SCRIPT} > ${DESTDIR}/${L}
  chmod 711 ${DESTDIR}/${L}
done

echo "キッカー作成完了" >> ${LOGFILE}
pwd >> ${LOGFILE} 2>&1
separator
}

LOGDIR=${THIS_DIR}/build-log/

LOGFILE=${LOGDIR}`date +%Y%m%d%H%M%S`.log

make_dir_if_not_exist ${LOGDIR}

touch ${LOGFILE}

#多重起動防止機講
# 同じ名前のプロセスが起動していたら起動しない。
_lockfile="/tmp/`basename $0`.lock"
ln -s /dummy $_lockfile 2> /dev/null || { echo 'Cannot run multiple instance.'  >>${LOGFILE}; exit 9; }
trap "rm -f $_lockfile; exit" 1 2 3 15


# ファイル更新日時が10日を越えたログファイルを削除
PARAM_DATE_NUM=10
find ${LOGDIR} -name "*.log" -type f -mtime +${PARAM_DATE_NUM} -exec rm -f {} \;

maven_install && prepare_deploy

echo "" >> ${LOGFILE}

rm -f $_lockfile





