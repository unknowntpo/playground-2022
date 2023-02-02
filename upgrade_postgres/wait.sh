#!/bin/bash

status=$(pg_isready -q)
while [[ "$status" -ne 0 ]]; do
    echo $status
    sleep 1
    status=$(pg_isready -q)
done

exit 0
