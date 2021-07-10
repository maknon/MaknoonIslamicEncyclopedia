#!/bin/sh

cd "$(dirname "${0}")" || exit 1

../PlugIns/jdk/bin/java \
	-Dapple.laf.useScreenMenuBar=true \
	-Xdock:name="المواريث والزكاة" \
	-Xdock:icon=../Resources/icon.icns \
	-cp . --module-path mods:lib -m com.maknoon/com.maknoon.MaknoonIslamicEncyclopedia
