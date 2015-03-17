| **NOTE:** There's is also a 1.x version of this plugin but it's has a very different configuration. |
|-----------------------------------------------------------------------------------------------------|

portallocator-maven-plugin
==========================

### Description

The port allocator plugin finds an available port and makes sure they are unique for a Maven execution.

Goal `allocate`
---------------

### Attributes

* Requires a Maven project to be executed.
* The goal is thread-safe and supports parallel builds.
* Binds by default to the lifecycle phase: validate.

### Configuration

| name | type | Since | Description |
| ---- | ---- | ----- | ----------- |
| portAllocators | List | 2.0 | List of port allocators. |
| ports | List | 2.0 | List of ports. |
| nameSeparator | String | 2.0 | The name level separator.<br/>**Default:** `.` |
| portNameSuffix | String | 2.0 | The port name suffix.<br/>**Default:** `port` |
| offsetNameSuffix | String | 2.0 | The offset name suffix.<br/>**Default:** `port-offset` |
| quiet | Boolean | 1.0 | Set to `true` for no output. |

#### Tag `portAllocator`

| name | type | Since | Description |
| ---- | ---- | ----- | ----------- |
| id | String | 2.0 | The port allocator id then can be referenced. |
| preferredPorts | List | 2.0 | The preferred ports.<br/>**Default:** `8090` |
| depletionAction | String | 2.0 | The action to take if preferred ports are depleted.<br/>**Values:** `continue` or `fail`<br/>**Default:** `continue`  |

#### Tag `port`

| name | type | Since | Description |
| ---- | ---- | ----- | ----------- |
| name | String | 2.0 | The first level name of the property. |
| offsetBasePort | Integer | 2.0 | The base port for offset calculation. |


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
                <ports>
                    <port>
                        <name>tomcat</name>
                    </port>
                </ports>
            </configuration>
        </execution>
    <executions>
</plugin>
```


Example - Simplest way
----------------------
```xml
<configuration>
    <ports>
        <port>
            <name>tomcat</name>
        </port>
    </ports>
</configuration>
```

This is the simplest way you can assign a port. The default preferred port is 8090.

#### Result might be or any other port depending on availability:
```
tomcat.port = 8090
```
