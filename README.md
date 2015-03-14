portallocator-maven-plugin
==========================

### Description

The port allocator plugin finds an available port.

Goal `allocate`
---------------

### Attributes

* Requires a Maven project to be executed.
* The goal is thread-safe and supports parallel builds.
* Binds by default to the lifecycle phase: validate.

#### Tag `portAllocation`

| name | type | Since | Description |
| ---- | ---- | ----- | ----------- |
| name | String | 1.0 | The first level name of the property. |
| preferredPorts | List | 1.0 | The preferred ports.<br/>**Default:** `8090` |
| depletionAction | String | 1.0 | The action to take if preferred ports are depleted.<br/>**Values:** `continue` or `fail`<br/>**Default:** `continue`  |
| offsetBasePort | Integer | 1.0 | The base port for offset calculation. |
| nameLevelSeparator | String | 1.0 | The name level separator.<br/>**Default:** `.` |
| portNameSuffix | String | 1.0 | The port name suffix.<br/>**Default:** `port` |
| offsetNameSuffix | String | 1.0 | The offset name suffix.<br/>**Default:** `offset` |
| relativePorts | list | 1.0 | Extra ports relative to the preferred allocated port. |


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
                <portAllocations>
                    <portAllocation>
                        <name>tomcat</name>
                    </portAllocation>
                </portAllocations>
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

This is the simplest way you can assign a port. The default preferred port is 8090.

#### Result might be or any other port depending on availability:
```
tomcat.port = 8090
```


Example - Port offset assignment
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

When you set `offsetBasePort` a new "offset" property will be assigned the offset between the allocated port and the base port.

#### Result might be:
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

Setting `depletionAction` to `fail` makes sure only the preferred ports can be allocated.


Example - Relative ports
------------------------
```xml
<configuration>
    <portAllocations>
        <portAllocation>
            <name>tomcat</name>
            <relativePort>
                <name>jndi</name>
                <offset>5</offset>
            </relativePort>
        </portAllocation>
    </portAllocations>
</configuration>
```

Note that all relative ports are checked for availability.

#### Result might be:
```
tomcat.port = 8090
tomcat.jndi.port = 8095
```

If the port `8095` is not available.

#### If the port `8095` is not available, result might be:
```
tomcat.port = 8091
tomcat.jndi.port = 8096
```
