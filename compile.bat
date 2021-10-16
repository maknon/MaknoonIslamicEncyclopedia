javac -encoding UTF-8 --module-source-path src --module-path lib -d mods ^
        src/com.maknoon/module-info.java ^
        src/com.maknoon/com/maknoon/*

jlink --no-header-files --no-man-pages --module-path mods;lib;"E:/Support/jdk-17/jmods" --add-modules com.maknoon --output jdk

jdk\bin\java -cp . --module-path mods;lib -m com.maknoon/com.maknoon.MaknoonIslamicEncyclopedia

pause