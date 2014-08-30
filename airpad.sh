#!/bin/bash
JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0.jdk/Contents/Home/
echo java -version
java -Djavafx.verbose=true -Duser.home=/Users/abien/work/workspaces/airhacks/workspace/notes -cp ./target/airpad.jar com.airhacks.airpad.App
