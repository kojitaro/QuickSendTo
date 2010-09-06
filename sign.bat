call ant clean
call ant release
jarsigner -keystore  android.keystore  -verbose  bin\QuickSendTo-unsigned.apk kouji
