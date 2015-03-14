portallocator-maven-plugin
==========================

Goal -> allocate
----------------
| name | type | Since | Description |
| ---- | ---- | ----- | ----------- |
| name | String | 1.0 | The first level name of the property. |
| preferredPorts | List | 1.0 | The preferred ports. |
| offsetBasePort | Integer | 1.0 | The base port for offset calculation. |


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
