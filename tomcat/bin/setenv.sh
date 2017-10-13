JAVA_OPTS="-Djava.awt.headless=true -XX:+UseG1GC -Dfile.encoding=UTF-8 $JAVA_OPTS "
JAVA_OPTS="-XX:MaxPermSize=512M -Xms512M -Xmx2048M $JAVA_OPTS " # java-memory-settings
JAVA_OPTS="$JAVA_OPTS -Drebel.packages_include=com.glaf -Drebel.aspectj_plugin=true -Drebel.spring_plugin=true -Drebel.resteasy_plugin=true -Drebel.mybatis_plugin=true -Drebel.jackson_plugin=true -noverify -agentpath:$CATALINA_HOME/bin/libjrebel64.so "

export JAVA_OPTS
			    