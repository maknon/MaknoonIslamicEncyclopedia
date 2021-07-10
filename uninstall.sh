#!/bin/sh
cd "$(dirname "${0}")"

xdg-desktop-menu uninstall maknoon-maknoonislamicencyclopedia.desktop
xdg-desktop-icon uninstall maknoon-maknoonislamicencyclopedia.desktop
xdg-icon-resource uninstall --context apps --size 256 maknoon-maknoonislamicencyclopedia

cd ..; rm -rf MaknoonIslamicEncyclopedia