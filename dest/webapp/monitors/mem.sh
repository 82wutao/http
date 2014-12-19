#!/bin/sh
/usr/bin/free -tmo | /usr/bin/awk 'BEGIN {OFS=","} {print $1,$2,$3-$6-$7,$4+$6+$7}'