#!/bin/sh
cd "$(dirname "${0}")"
jdk/bin/java -cp . --module-path mods:lib -m com.maknoon/com.maknoon.MaknoonIslamicEncyclopedia
