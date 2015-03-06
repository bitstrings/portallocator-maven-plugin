portallocator-maven-plugin
==========================

Example - Port pools
--------------------
```
<groupId>org.bitstrings.maven.plugins</groupId>
<artifactId>portallocator-maven-plugin</artifactId>
<executions>
    <execution>
        <id>pools</id>
        <goals>
            <goal>allocate</goal>
        </goals>
        <configuration>
            <portPool>
                <name>pool1</name>
                <strategy>random</strategy>
                <basePort>8080</basePort>
                <minPort>8080</minPort>
                <maxPort>8090</maxPort>
            </portPool>
            <portPool>
                <name>pool2</name>
                <ports>8080,8082,8085</ports>
            </portPool>
        </configuration>
    </execution>
</executions>
```

Example - Port allocation
-------------------------
```
<configuration>
    <port>
        <poolRef>pool1</poolRef>
        <portName>tomcat</portName>
    </port>
</configuration>
```

```
<configuration>
    <port>
        <portName>tomcat</portName>
    </port>
</configuration>
```

Example - Port allocation with offset
-------------------------------------
```
<configuration>
    <port>
        <allocationRef>pool2</allocationRef>
        <portName>wildfly</portName>
        <setOffset/>
    </port>
</configuration>
```
