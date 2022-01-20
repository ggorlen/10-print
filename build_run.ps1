$package_name = "com.example.tenprint"
.\gradlew assembleDebug
adb -e install -r app/build/outputs/apk/debug/app-debug.apk
adb shell monkey -p "$package_name" -c android.intent.category.LAUNCHER 1
$app_pid = adb shell ps | grep "$package_name" | awk '{ print $2 }'
adb logcat --pid=$app_pid tenprint:E *:S
