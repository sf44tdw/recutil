#!/bin/bash

#cron等で定期実行するスクリプト。
#EPG取得のため、地上波物理チャンネル全てと、BSチャンネルから1つを選択し、90秒間録画する。
#BSch=101(NHK BS1)

#録画ファイル,EPGファイル,ログの保存先ディレクトリ(cron.dailyはrootで動くらしく、/varなどにしても場合によって動く)
PDIR=${HOME}/epg_update

LOGDIR=${PDIR}/epg_update_log

if [ ! -e ${LOGDIR} ]; then
 `mkdir -p ${LOGDIR}`
fi
echo "EPGファイルの取得ログ = ${LOGDIR}"

#日付取得
Date=`date "+%Y%m%d%H%M%S"`
#EPGファイルの取得ログファイル名生成
LOGFILENAME="D"${Date}"P"$$
LOGFILE=${LOGDIR}"/"${LOGFILENAME}".log"
echo ${LOGFILE} > ${LOGFILE}

separator () {
    echo "`date "+%Y-%m-%d %H:%M:%S"`***************************************************************************************************************************************" >> ${LOGFILE}
}

#多重起動防止機講
# 同じ名前のプロセスが起動していたら起動しない。
_lockfile="/tmp/`basename $0`.lock"
ln -s /dummy $_lockfile 2> /dev/null || { echo 'Cannot run multiple instance.'  >>${LOGFILE}; exit 9; }
trap "rm $_lockfile; exit" 1 2 3 15

separator
echo "ファイル更新日時が10日を越えたログファイルを削除" >> ${LOGFILE}
PARAM_DATE_NUM=10
find ${LogDir_epgDump} -name "*.log" -type f -mtime +${PARAM_DATE_NUM} -exec rm -f {} \;
separator

separator
echo "受信" >> ${LOGFILE}
#tsファイル保存先ディレクトリ
tsdir=${PDIR}/tsDir
if [ ! -e ${tsdir} ]; then
`mkdir ${tsdir}`
fi

#EPG XMLファイル保存先ディレクトリ
epgdir=${PDIR}/epg_xml
if [ ! -e ${epgdir} ]; then
`mkdir ${epgdir}`
fi

#前回のファイルが残っているかも知れないので、念のため削除
rm -f ${tsdir}/*.ts
rm -f ${epgdir}/*.xml

#放送局種別
btype=0

#in以降にチャンネル番号をスペースで区切って記入する。
for channel in 16 21 22 23 24 25 26 27 28 30 32 101
do
separator
echo "受信チャンネル:"${channel} >> ${LOGFILE}
/usr/local/bin/recpt1 --strip --b25 ${channel} 90 ${tsdir}/${channel}.ts 1>>${LOGFILE} 2>&1
case ${channel} in

#BS放送
 101 )
 btype="BS";;

#地上波
 *   )
 btype=${channel};;

esac
/usr/local/bin/epgdump ${btype} ${tsdir}/${channel}.ts ${epgdir}/${channel}.xml
separator
done
rm -f ${tsdir}/*.ts
separator

separator
echo "EPGDB更新" >> ${LOGFILE}

/usr/local/bin/updatedb -c "UTF-8" -d ${epgdir} 1>>${LOGFILE} 2>&1
separator


rm $_lockfile

