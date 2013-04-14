#!/bin/bash
JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0.jdk/Contents/Home/
echo java -version
java -Djavafx.verbose=true -cp ./target/airpad.jar com.airhacks.airpad.App
