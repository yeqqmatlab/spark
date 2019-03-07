#!/bin/bash

if [ $# -ne 1 ];then

echo "Usage:basename $0 filename"
exit 1
fi


filename=$1
egrep -o "[a-zA-Z]+" $filename |
awk '{count[$0]++}
END{printf "%-14s %s\n","Word","Count"
for(i in count)printf "%-14s %s\n",i,count[i]|"sort -nrk 2"}'