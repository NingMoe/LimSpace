#!/bin/bash

SCRIPT_NAME=`basename "$0"`
PWD=`cd "$(dirname $0)" && pwd`
WAR_DIR="/home/ec/war"
API_WAR="ec.war"
DATE=`date "+%Y-%m-%d"`
i=0
while true; do
    ((i++))
    NEW_WAR="$DATE.$i.war"
    if ! [[ -f $WAR_DIR/$NEW_WAR ]]; then
        break
    fi
done

mvn package
#if [[ $? -eq 0 ]]; then
#    exit 1
#fi

cp $PWD/target/api.war $WAR_DIR/$NEW_WAR
echo "rm $WAR_DIR/$API_WAR"
rm $WAR_DIR/$API_WAR
echo "ln -sf $WAR_DIR/$NEW_WAR $WAR_DIR/$API_WAR"
ln -sf $WAR_DIR/$NEW_WAR $WAR_DIR/$API_WAR
