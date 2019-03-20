#!/bin/bash

a=10
b=20

if [ $a -eq $b ]; then
    echo "a = b"
else
    echo " a != b"
fi

if [ $a -gt $b ]; then
    echo " $a 大于 $b "
else
    echo " $a 小于 $b "
fi