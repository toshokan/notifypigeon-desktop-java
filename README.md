# notifypigeon-desktop-java

NotifyPigeon-Desktop-java is a simple Java application that listens for notification information on a TCP/IP socket. It expects data in the format sent by NotifyPigeon-android, and is designed to be used together with it. It currently supports displaying notifications through libnotify and recalling notifications that have been dismissed on the Android device. *It also requires gnome-java to build.* You might want to change the path to gnome-java in compile.sh if it is located somewhere else. 

It is still very primitive and only works with data sent over your network in plain text. 
