| **NOTE:** There's is also a 1.x version of this plugin but it has a very different configuration. |
|---------------------------------------------------------------------------------------------------|

portallocator-maven-plugin
==========================

### Description

The port allocator plugin finds an available port and makes sure it is unique for the Maven execution.

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
| ports | Ports | 2.0 | List of ports.<br/>**Short form:** `<ports>name1,name2,...</ports>`|
| nameSeparator | String | 2.0 | The name level separator.<br/>**Default:** `.` |
| portNameSuffix | String | 2.0 | The port name suffix.<br/>**Default:** `port` |
| offsetNameSuffix | String | 2.0 | The offset name suffix.<br/>**Default:** `port-offset` |
| writePropertiesFile | File | 2.0 | The properties file. |
| quiet | Boolean | 1.0 | Set to `true` for no output. |

#### Structure `portAllocator`

| name | type | Since | Description |
| ---- | ---- | ----- | ----------- |
| id | String | 2.0 | The port allocator id then can be referenced. |
| preferredPorts | List | 2.0 | The preferred ports.<br/>**Default:** `8090` |
| depletedAction | String | 2.0 | The action to take if preferred ports are depleted.<br/>**Values:** `continue` or `fail`<br/>**Default:** `continue`  |
| permitOverride | Boolean | 2.0 | By default allocators can not be overridden.<br/>This is useful for multi-module projects.<br/>**Default:** `false` |

#### Structure `ports`

| name | type | Since | Description |
| ---- | ---- | ----- | ----------- |
| portAllocatorRef | String | 2.0 | The port allocator `id` to use.<br/>**Default:** `default`<br/>*You may only use `portAllocatorRef` or `portAllocator` but not both.* |
| portAllocator | PortAllocator | 2.0 | Inner port allocator.<br/>*You may only use `portAllocatorRef` or `portAllocator` but not both.* |
| port | Port | 2.0 | The port to assign.<br/>**Short form:** `<port>name</port>` |

#### Structure `port`

| name | type | Since | Description |
| ---- | ---- | ----- | ----------- |
| name | String | 2.0 | The first level name of the property. |
| offsetBasePort | Integer | 2.0 | The base port for offset calculation.<br/>This will set a second property with the suffix `offsetNameSuffix`. |


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
                <ports>http,https,jndi</ports>
            </configuration>
        </execution>
    <executions>
</plugin>
```


Example - Simplest way
----------------------

#### Short form

```xml
<configuration>
    <ports>tomcat</ports>
</configuration>
```

#### Long form

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


Example - Get offset relative to a base port
--------------------------------------------
```xml
<configuration>
    <ports>
        <port>
            <name>wildfly</name>
            <offsetBasePort>8080</offsetBasePort>
        </port>
    </ports>
</configuration>
```

#### Result might be or any other port depending on availability:
```
wildfly.port = 8090
wildfly.port-offset = 10
```


Example - Write the ports to a properties file
----------------------------------------------

#### Short form

```xml
<configuration>
    <writePropertiesFile>${project.build.directory}/ports.properties</writePropertiesFile>
    <ports>tomcat,hsqldb</ports>
</configuration>
```

```xml
<configuration>
    <writePropertiesFile>${project.build.directory}/ports.properties</writePropertiesFile>
    <ports>
        tomcat,
        hsqldb
    </ports>
</configuration>
```

#### Somewhat short form

```xml
<configuration>
    <writePropertiesFile>${project.build.directory}/ports.properties</writePropertiesFile>
    <ports>
        <port>tomcat</port>
        <port>hsqldb</port>
    </ports>
</configuration>
```


#### Long form

```xml
<configuration>
    <writePropertiesFile>${project.build.directory}/ports.properties</writePropertiesFile>
    <ports>
        <port>
            <name>tomcat</name>
        </port>
        <port>
            <name>hsqldb</name>
        </port>
    </ports>
</configuration>
```


Example - Register a port allocator
-----------------------------------
```xml
<configuration>
    <portAllocators>
        <portAllocator>
            <preferredPorts>9090</preferredPorts>
        </portAllocator>
    </portAllocators>
</configuration>
```

Two things:

1. Unless you set `permitOverride` to `true`, you can register port allocators in the parent without worrying about re-registering in child modules.
2. The can override the default allocator by using `<id>default</id>` or omit the id.

```xml
<configuration>
    <portAllocators>
        <portAllocator>
            <id>pool-1</id>
            <preferredPorts>
                <ports>8080-9090</ports>
                <depletedAction>fail</depletedAction>
            </preferredPorts>
        </portAllocator>
    </portAllocators>
</configuration>
```

This allocator will try to assign ports from `8080` to `9090`, otherwise fail.

```xml
<configuration>
    <ports>
        <portAllocatorRef>pool-1</portAllocatorRef>
        <port>
            <name>wildfly</name>
            <offsetBasePort>8080</offsetBasePort>
        </port>
        <port>hsqldb</port>
    </ports>
</configuration>
```

Use the `pool-1` port allocator.
