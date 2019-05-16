# cas-server
cas5.3的服务端程序

# 支持https的话需要修改tomcat的service.xml文件

```
<!--cas的服务注册管理端口-->
	<Connector port="8081" protocol="org.apache.coyote.http11.Http11NioProtocol"
             minSpareThreads="5" maxSpareThreads="75"
             enableLookups="true" disableUploadTimeout="true"
             acceptCount="100"  maxThreads="200"
             scheme="https" secure="true" SSLEnabled="true"
             clientAuth="false" sslProtocol="TLS"
             keystoreFile="D:keys/keystore2"
             keystorePass="123456"/>
```
