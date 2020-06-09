#!/bin/bash

# solution from https://stackoverflow.com/questions/59895/getting-the-source-directory-of-a-bash-script-from-within
SOURCE="${BASH_SOURCE[0]}"
while [ -h "$SOURCE" ]; do # resolve $SOURCE until the file is no longer a symlink
  DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"
  SOURCE="$(readlink "$SOURCE")"
  [[ $SOURCE != /* ]] && SOURCE="$DIR/$SOURCE" # if $SOURCE was a relative symlink, we need to resolve it relative to the path where the symlink file was located
done
DIR="$( cd -P "$( dirname "$SOURCE" )" && pwd )"

mvn clean compile package -f "$DIR/pom.xml"
heroku container:push worker -a quiet-scrubland-05864
heroku container:release worker -a quiet-scrubland-05864


#Picked up JAVA_TOOL_OPTIONS: -XX:+UseContainerSupport -Xmx300m -Xss512k -XX:CICompilerCount=2 -Dfile.encoding=UTF-8
#: Starting process with command `java git -jar target/realestate_email_aggregator-1.0-SNAPSHOT.jar`
#2020-06-08T11:12:22.956247+00:00 app[web.1]: Create a Procfile to customize the command used to run this process: https://devcenter.heroku.com/articles/procfile
#2020-06-08T11:12:23.080227+00:00 app[web.1]: Setting JAVA_TOOL_OPTIONS defaults based on dyno size. Custom settings will override them.
#2020-06-08T11:12:23.086547+00:00 app[web.1]: Picked up JAVA_TOOL_OPTIONS: -XX:+UseContainerSupport -Xmx300m -Xss512k -XX:CICompilerCount=2 -Dfile.encoding=UTF-8
