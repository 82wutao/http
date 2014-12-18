
#mysql -h192.168.1.14 -ugameboy -p123456 -Dgame_data </var/www/html/xx/EditorData/DataEditor/backend/server_msg.sql
mysql -h192.168.1.14 -ugameboy -p123456 -e "set names utf8; use game_data; source /var/www/html/xx/EditorData/DataEditor/backend/server_msg.sql;"
