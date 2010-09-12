#!/bin/bash
ant clean
ant release
jarsigner -keystore  android.keystore  -verbose  bin/QuickSendTo-unsigned.apk kouji
