name := "kinesis-tutorial"

version := "0.1"

scalaVersion := "2.13.0"
// https://mvnrepository.com/artifact/com.amazonaws/amazon-kinesis-client
libraryDependencies += "com.amazonaws" % "amazon-kinesis-client" % "1.10.0"
// https://mvnrepository.com/artifact/com.amazonaws/aws-java-sdk
libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.11.595"
// https://mvnrepository.com/artifact/org.apache.httpcomponents/httpcore
libraryDependencies += "org.apache.httpcomponents" % "httpcore" % "4.4.11"
// https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient
libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.5.9"
// https://mvnrepository.com/artifact/commons-lang/commons-lang
libraryDependencies += "commons-lang" % "commons-lang" % "2.6"
// https://mvnrepository.com/artifact/commons-logging/commons-logging
libraryDependencies += "commons-logging" % "commons-logging" % "1.1.1"
// https://mvnrepository.com/artifact/com.google.guava/guava
libraryDependencies += "com.google.guava" % "guava" % "28.0-jre"
// https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-annotations
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-annotations" % "2.9.9"
// https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-core
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-core" % "2.9.9"
// https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.9.9"
// https://mvnrepository.com/artifact/com.fasterxml.jackson.dataformat/jackson-dataformat-cbor
libraryDependencies += "com.fasterxml.jackson.dataformat" % "jackson-dataformat-cbor" % "2.9.9"
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.9"
// https://mvnrepository.com/artifact/joda-time/joda-time
libraryDependencies += "joda-time" % "joda-time" % "2.10.3"
