#!/bin/bash

MINPARAMS=10

echo

echo The name this script is $0

echo "The name this script is \" `basename $0` \"."

if [ -n "$1" ]; then
echo "Parameter #1 is $1"
fi

if [ -n "${10}" ]; then
echo "Parameter #10 is ${10}"

fi

echo "---------------------------------------"

echo the number of this command-line parameters are: $#
echo All the command-line parameters are: $*
echo the proess of this script: $$
echo the last proess of this script: $!
echo the code of status: $?

