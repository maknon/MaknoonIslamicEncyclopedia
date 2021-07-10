#!/bin/bash

if [ "$(uname)" != "Linux" ]; then
	echo "This version of Maknoon Islamic Encyclopedia is not for this platform $(uname)"
	exit 1
fi


if [ "$(id -u)" = "0" ]; then
	echo "You cannot install Maknoon Islamic Encyclopedia as a root user"
	exit 1
fi

ARCHIVE=`awk '/^__ARCHIVE_BELOW__/ {print NR + 1; exit 0; }' $0`

mkdir MaknoonIslamicEncyclopedia
cd MaknoonIslamicEncyclopedia
CDIR=`pwd`
tail -n+$ARCHIVE ../$0 | tar -xzmo

echo "[Desktop Entry]
Comment=برنامج حساب المواريث والزكاة
Exec=\"$CDIR/startup.sh\"
Name=المواريث والزكاة
Icon=$CDIR/icon.png
Terminal=false
Type=Application
Path=$CDIR" > maknoon-maknoonislamicencyclopedia.desktop

xdg-desktop-menu install maknoon-maknoonislamicencyclopedia.desktop
xdg-desktop-icon install maknoon-maknoonislamicencyclopedia.desktop
xdg-icon-resource install --context apps --size 256 icon.png application-maknoonislamicencyclopedia

chmod -R u+rwx ../MaknoonIslamicEncyclopedia
chmod u+rwx ~/Desktop/maknoon-maknoonislamicencyclopedia.desktop

echo "Installation is completed. Please right click on Desktop file and select 'Allow Launching'"
exit 0
__ARCHIVE_BELOW__
