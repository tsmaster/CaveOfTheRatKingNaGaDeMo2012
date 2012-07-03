
echo "----------------------------------------"
echo " Remember: you want to have tested the"
echo " HTML version before deploying, as that"
echo " builds the HTML target"
echo "----------------------------------------"
echo

echo "cleaning out old deploy"
rm -rf RpgWeb
echo "making new deploy dir"
mkdir RpgWeb
echo "archiving web dir"
cd html/target/rpg-html-1.0-SNAPSHOT
tar czf ../../../RpgWeb/rpghtml.tgz *
cd ../../..

echo "copying HTML to Mars"
cp RpgWeb/rpghtml.tgz ~/Mars

echo "copying APK to Mars"
cp android/target/rpg-android-*.apk ~/Mars/rpg.apk

echo "to complete, on Mars, run deploy.sh"

