FROM isuper/java-oracle
MAINTAINER Ilya Obshadko <xfyre@xfyre.com>
COPY target/tour-site.jar /home/tour-site.jar
ENV JAVA_OPTIONS=""
ENTRYPOINT exec java ${JAVA_OPTIONS} -Dport=9037 -jar /home/tour-site.jar
EXPOSE 9037
