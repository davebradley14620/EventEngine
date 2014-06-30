#!/bin/sh
if [ $# -gt 0 ]; then
	url=$1;
fi
#	org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/jdk1.6.0_27
#	org.eclipse.jst.server.core.container/org.eclipse.jst.server.tomcat.runtimeTarget/Tomcat v6.0 Server at localhost
#	org.eclipse.jst.j2ee.internal.web.container
#	org.eclipse.jst.j2ee.internal.module.container
	lib=`pwd`"/WebContent/WEB-INF/lib"
#	CLASSPATH=`pwd`"/build/classes:$lib/asm-3.3.1.jar:$lib/axis.jar:$lib/axis-schema.jar:$lib/commons-lang3-3.1.jar:$lib/grizzly-framework-2.2.1.jar:$lib/grizzly-http-2.2.1.jar:$lib/grizzly-http-server-2.2.1.jar:$lib/jaxrpc.jar:$lib/jersey-bundle-1.12.jar:$lib/jersey-core-1.12.jar:$lib/jersey-grizzly2-1.12.jar:$lib/jersey-json-1.12.jar:$lib/jersey-server-1.12.jar:$lib/jsr311-api-1.1.1.jar:$lib/jstl.jar:$lib/log4j-1.2.8.jar:$lib/mailapi.jar:$lib/mail.jar:$lib/mysql-connector-java-5.1.13-bin.jar:$lib/standard.jar"
	CLASSPATH=`pwd`"/build/classes:$lib/asm-3.3.1.jar:$lib/axis.jar:$lib/axis-schema.jar:$lib/commons-lang3-3.1.jar:$lib/grizzly-framework-2.2.1.jar:$lib/grizzly-http-2.2.1.jar:$lib/grizzly-http-server-2.2.1.jar:$lib/jaxrpc.jar:$lib/jersey-client-1.18.jar:$lib/jersey-core-1.18.jar:$lib/jersey-json-1.18.jar:$lib/jersey-server-1.18.jar:$lib/jersey-servlet-1.18.jar:$lib/jsr311-api-1.1.1.jar:$lib/jstl.jar:$lib/log4j-1.2.8.jar:$lib/mailapi.jar:$lib/mail.jar:$lib/mysql-connector-java-5.1.13-bin.jar:$lib/standard.jar:$lib/jackson-core-asl-1.9.2.jar:$lib/jackson-jaxrs-1.9.2.jar:$lib/jackson-mapper-asl-1.9.2.jar:$lib/jackson-xc-1.9.2.jar"
	echo $CLASSPATH;
	export CLASSPATH;
	java -cp $CLASSPATH -enableassertions eventengine.server.Tester ${url}
