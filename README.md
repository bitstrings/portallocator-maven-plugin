portallocator-maven-plugin
==========================

Goal `allocate`
---------------

### <portAllocation>

| name | type | Since | Description |
| ---- | ---- | ----- | ----------- |
| name | String | 1.0 | The first level name of the property. |
| preferredPorts | List | 1.0 | The preferred ports.<br/>**Default:** `8090` |
| depletionAction | String | 1.0 | The action to take if preferred ports are depleted.<br/>**Values:** `continue` or `fail`<br/>**Default:** `continue`  |
| offsetBasePort | Integer | 1.0 | The base port for offset calculation. |
| nameLevelSeparator | String | 1.0 | The name level separator.<br/>**Default:** `.` |
| portNameSuffix | String | 1.0 | The port name suffix.<br/>**Default:** `port` |
| offsetNameSuffix | String | 1.0 | The offset name suffix.<br/>**Default:** `offset` |

Example - Plugin execution
--------------------------
```xml
<plugin>
    <groupId>org.bitstrings.maven.plugins</groupId>
    <artifactId>portallocator-maven-plugin</artifactId>
    <executions>
        <execution>
            <id>port-allocation</id>
            <goals>
                <goal>allocate</goal>
            </goals>
            <configuration>
                ...
            </configuration>
        </execution>
    <executions>
</plugin>
```

Example - Simple port assignment
--------------------------------
```xml
<configuration>
    <portAllocations>
        <portAllocation>
            <name>tomcat</name>
        </portAllocation>
    </portAllocations>
</configuration>
```

#### Result:
```
tomcat.port = 8090
```

Example - Port Offset assignment
--------------------------------
```xml
<configuration>
    <portAllocations>
        <portAllocation>
            <name>tomcat</name>
            <offsetBasePort>8080</offsetBasePort>
        </portAllocation>
    </portAllocations>
</configuration>
```

#### Result:
```
tomcat.port = 8090
tomcat.offset = 10
```


Example - Port range
--------------------
```xml
<configuration>
    <portAllocations>
        <portAllocation>
            <name>tomcat</name>
            <preferredPorts>8080-8090</preferredPorts>
            <depletionAction>fail</depletionAction>
        </portAllocation>
    </portAllocations>
</configuration>
```

```xml
<configuration>
    <portAllocations>
        <portAllocation>
            <name>tomcat</name>
            <preferredPorts>
                <ports>8090</ports>
                <ports>9090</ports>
                <ports>10000</ports>
            </preferredPorts>
            <depletionAction>fail</depletionAction>
        </portAllocation>
    </portAllocations>
</configuration>
```
