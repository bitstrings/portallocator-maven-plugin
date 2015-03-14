portallocator-maven-plugin
==========================

Goal -> allocate
----------------
| name | type | Since | Description |
| ---- | ---- | ----- | ----------- |
| name | String | 1.0 | The first level name of the property. |
| preferredPorts | List | 1.0 |
```
<PreferredPorts>8080</PreferredPorts>

<PreferredPorts>8080-8090</PreferredPorts>

<PreferredPorts>
    <ports>8080-8090</ports>
    <ports>9090-</ports>
</PreferredPorts>
``` |


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
