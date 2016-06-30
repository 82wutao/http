#!/usr/bin/env sh
git add -A
git commit -m "\"$1\""
expect <<EOF
    set timeout 1800
    spawn git push origin master
    expect {
        eof { exit 0 } 
        timeout { exit 4 } 
        "Connection refused" { exit 5 } 
        "Name or service not known" { exit 6 } 
        "yes/no" { send "yes\r" ;exp_continue } 
        "*ass*" { send "wutao123A\r" ;exp_continue } 
        "*name*" { send "82wutao@gmail.com\r" ;exp_continue } 
    } 
    interact 
EOF
exit $?
