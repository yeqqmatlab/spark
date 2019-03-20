#!/bin/bash

# 判读文件是否存在，是否可读
file="/Users/yqq/IdeaProjects/Spark2Test/shell_scripts/test.sh"

if [[ -e ${file} ]]; then
    echo "the file is exists"
else
    echo "the file is not exists"
fi

if [[ -r ${file} ]]; then
    echo "the file is readable"
else
    echo "the file is not readable"
fi