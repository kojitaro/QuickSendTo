#!/bin/bash

ant clean
ant release
jarsigner -keystore  android.keystore  -verbose  bin/QuickSendTo-unsigned.apk kouji
zipalign -v 4 bin/QuickSendTo-unsigned.apk QuickSendTo.apk
