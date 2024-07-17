#!/bin/sh

VERSION="3.1.1"

if [[ "${VERSION}" =~ 3.* ]]; then
	echo "3.x.x"
else
	echo "not 3.x.x"
fi
