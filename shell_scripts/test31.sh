#!/bin/bash

a="abc"
b="efg"


if [[ -n ${a} ]]

then
   echo "-n $a : The string length is not 0"
else
   echo "-n $a : The string length is  0"
fi

if [[ ${a} ]]

then
   echo "$a : The string is not empty"
else
   echo "$a : The string is empty"
fi