#!/bin/bash

javac -cp /usr/share/java-gnome-4.1/lib/gtk.jar NotifyPigeonListener.java
jar cfe NotifyPigeon-Desktop.jar NotifyPigeon-Desktop NotifyPigeonListener.class
