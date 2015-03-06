portallocator-maven-plugin
==========================

Example - Port allocation
-------------------------
```
<configuration>
    <portAllocation>
        <name>pool1</name>
        <strategy>random</strategy>
        <basePort>8080</basePort>
        <minPort>8080</minPort>
        <maxPort>8090</maxPort>
    </portAllocation>
    <portAllocation>
        <name>pool2</name>
        <ports>8080,8082,8085</ports>
    </portAllocation>
</configuration>
```

Example - Port Use
------------------
```
<configuration>
    <port>
        <allocationRef>pool1</allocationRef>
        <portName>tomcat</portName>
    </port>
</configuration>
```

Example - Port Use with offset
------------------------------
```
<configuration>
    <port>
        <allocationRef>pool2</allocationRef>
        <portName>wildfly</portName>
        <setOffset/>
    </port>
</configuration>
```
