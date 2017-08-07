
#指定されたディレクトリが無いなら作る
make_dir_if_not_exist () {
    if [ ! -e $1 ]; then
       `mkdir -p $1`
    fi
}

PATH=/usr/bin:/bin

THIS_DIR=$(cd $(dirname $0); pwd)

DESTDIR=${THIS_DIR}/recutil_bin
