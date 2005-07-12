#!/bin/sh

# You May Need To Change this to your Quartz installation root
QUARTZ=../..

# Change this to your JDK installation root
#
#JAVA_HOME=/usr/java/j2sdk1.4.0_01

JRE=$JAVA_HOME/jre
JAVA=$JRE/bin/java

QUARTZ_CP=""
for jarfile in $QUARTZ/lib/*.jar; do
  QUARTZ_CP=$QUARTZ_CP:$jarfile
done

echo "Classpath: " $QUARTZ_CP

# Uncomment the following line if you would like to set log4j 
# logging properties
#
#LOGGING_PROPS="-Dlog4j.configuration=log4j.properties"

# Set the name and location of the quartz.properties file
QUARTZ_PROPS="-Dorg.quartz.properties=quartz.properties"

$JAVA -classpath $QUARTZ_CP $QUARTZ_PROPS $LOGGING_PROPS org.quartz.examples.example11.LoadExample 500

