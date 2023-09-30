

## ssl
``` shell
mkdir ca && cd ca
# 创建密钥仓库，存储证书文件
keytool -keystore server.keystore.jks -alias shafishkafka -validity 100000 -genkey
# 创建ca
openssl req -new -x509 -keyout ca-key -out ca-cert -days 100000
# 将ca添加到客户信任库
keytool -keystore client.truststore.jks -alias CARoot -import -file ca-cert
# 
keytool -keystore server.truststore.jks -alias CARoot -import -file ca-cert
# 证书生成
## 从密钥仓库导出证书
keytool -keystore server.keystore.jks -alias shafishkafka -certreq -file cert-file
## 用ca签名
openssl x509 -req -CA ca-cert -CAkey ca-key -in cert-file -out cert-signed -days 10000 -CAcreateserial -passin pass:shafish
## 导入ca证书和已签的证书到密钥仓库
keytool -keystore server.keystore.jsk -alias CARoot -import -file ca-cert
keytool -keystore server.keystore.jsk -alias shafish -import -file cert-signed
```

``` shell
# server.propertis
listeners=PLAINTEXT://192.168.220.128:9092,SSL://192.168.220.128:8989
advertised.listeners=PLAINTEXT://192.168.220.128:9092,SSL://192.168.220.128:8989
ssl.keystore.location=/opt/ca/server.keystore.jks
ssl.keystore.password=shafish
ssl.key.password=shafish
ssl.truststore.location=/opt/ca/server.truststore.jks
ssl.truststore.password=shafish
```

``` java
props.put("bootstrap.servers", "ip:8989")
props.put("security.protocol","SSL");
props.put("ssl.endpoint.identification. algorithm","");
props.put("ssl.truststore.location","client.truststore.jks")
props.put("ssl.truststore.password","shafish");
```