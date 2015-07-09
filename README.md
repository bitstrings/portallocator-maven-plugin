[![Build Status](https://travis-ci.org/bitstrings/portallocator-maven-plugin.svg?branch=master)](https://travis-ci.org/bitstrings/portallocator-maven-plugin)

| **NOTE:** There's is also a 1.x version of this plugin but it has a very different configuration. |
|---------------------------------------------------------------------------------------------------|

portallocator-maven-plugin
==========================

### Description

The port allocator plugin finds an available port and makes sure it is unique for the Maven execution.

### Usage
--------------------------

```xml
<plugin>
    <groupId>org.bitstrings.maven.plugins</groupId>
    <artifactId>portallocator-maven-plugin</artifactId>
    <configuration>
        <execution>
            <id>allocate</id>
            <goals>
                <goal>allocate</goal>
            </goals>
            <configuration>
                <ports>portName</ports>
            </configuration>
        </execution>
    </configuration>
</plugin>
```

Goal `allocate`
---------------

### Attributes

* Requires a Maven project to be executed.
* The goal is thread-safe and supports parallel builds.
* Binds by default to the lifecycle phase: generate-test-resources.

### Configuration

| name | type | Since | Description |
| ---- | ---- | ----- | ----------- |
| portAllocators | List | 2.0 | List of port allocators.<br/>**Short form:** `<portAllocators>shortForm1,shortForm2,...</portAllocators>` |
| ports | Ports | 2.0 | List of ports.<br/>**Short form:** `<ports>shortForm1,shortForm2,...</ports>`|
| nameSeparator | String | 2.0 | The name level separator.<br/>**Default:** `.` |
| portNameSuffix | String | 2.0 | The port name suffix.<br/>**Default:** `port`<br/>**Example:** `name.port` |
| offsetNameSuffix | String | 2.0 | The offset name suffix.<br/>**Default:** `port-offset`<br/>**Example:** `name.port-offset` |
| writePropertiesFile | File | 2.0 | Write ports properties file. |
| quiet | Boolean | 1.0 | Set to `true` for no output.<br/>**Default:** `false` |

#### Structure `portAllocators`

| name | type | Since | Description |
| ---- | ---- | ----- | ----------- |
| portAllocator | PortAllocator | 2.0 | Port allocator.<br/>**Short form:** `<portAllocator>shortForm</portAllocator>` |

#### Structure `portAllocator`

| name | type | Since | Description |
| ---- | ---- | ----- | ----------- |
| id | String | 2.0 | The port allocator id.<br/>**Default:** `default` when not set. |
| preferredPorts | List | 2.0 | The preferred ports.<br/>**Default:** `8090` |
| depletedAction | String | 2.0 | The action to take if preferred ports are depleted.<br/>**Values:** `continue` or `fail`<br/>**Default:** `continue`  |
| permitOverride | Boolean | 2.0 | By default allocators can not be overridden.<br/>This is useful for multi-module projects.<br/>**Default:** `false` |

##### `preferredPorts`
| name | type | Since | Description |
| ---- | ---- | ----- | ----------- |
| ports | String | 2.0 | Allowed ports. |

#### Structure `ports`

| name | type | Since | Description |
| ---- | ---- | ----- | ----------- |
| portAllocatorRef | String | 2.0 | The port allocator `id` to use.<br/>**Default:** `default`<br/>*You may only use `portAllocatorRef` or `portAllocator` but not both.* |
| portAllocator | PortAllocator | 2.0 | Inner port allocator.<br/>*You may only use `portAllocatorRef` or `portAllocator` but not both.* |
| port | Port | 2.0 | The port to assign.<br/>**Short form:** `<port>shortForm</port>` |

#### Structure `port`

| name | type | Since | Description |
| ---- | ---- | ----- | ----------- |
| name | String | 2.0 | The first level name of the property. |
| preferredPort | Integer | 2.0 | The preferred port if available. |
| offsetFrom | String | 2.0 | Use the offset from the specified port. |
| setOffsetProperty | Boolean | 2.0 | If `true` this will set the port offset property.<br/>**Default:** `false` |

#### Short form `port`

```
{name}:{preferredPort}:{offsetFrom}:{setOffsetProperty}
```

#### Short form `portAllocator`

```
{preferredPorts}:{id}:{depletedAction}
```

If `id` is omitted, then `default` is used.

Example - Simplest way
----------------------

This is the simplest way you can assign a port. The default preferred port is 8090.

#### Short form

```xml
<configuration>
    <ports>tomcat</ports>
</configuration>
```

```xml
<configuration>
    <ports>
        <port>tomcat</port>
    </ports>
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

#### Result might be or any other port depending on availability:
```
tomcat.port = 8090
```


Example - Preferred port
------------------------

#### Short form

```xml
<configuration>
    <ports>
        <port>wildfly:8080</port>
    </ports>
</configuration>
```

```xml
<configuration>
    <ports>wildfly:8080</ports>
</configuration>
```


#### Long form

```xml
<configuration>
    <ports>
        <port>
            <name>wildfly</name>
            <preferredPort>8080</preferredPort>
        </port>
    </ports>
</configuration>
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

#### Short form

```xml
<configuration>
    <portAllocators>9090</portAllocators>
</configuration>
```

```xml
<configuration>
    <portAllocators>
        <portAllocator>9090</portAllocators>
    </portAllocators>
</configuration>
```

#### Long form

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


Examples
-----------------------------------

```xml
<configuration>
    <ports>
        wildfly-http:8080::true,
        wildfly-https:8443:wildfly-http,
        wildfly-jndi:8888:wildfly-http,
        hsqldb
    </ports>
</configuration>
```

```xml
<configuration>
    <ports>
        <portAllocator>9090</portAllocator>
        <port>wildfly-http</port>
        <port>wildfly-https</port>
        <port>wildfly-jndi</port>
        <port>hsqldb</port>
    </ports>
</configuration>
```

```xml
<configuration>
    <portAllocators>8080;8085;9090:custom</portAllocators>
    <ports>
        <portAllocatorRef>custom</portAllocatorRef>
        <port>http</port>
        <port>https</port>
        <port>hsqldb</port>
    </ports>
</configuration>
```

```xml
<executions>
    <execution>
        <id>allocators</id>
        <goals>
            <goal>allocate</goal>
        </goals>
        <configuration>
            <portAllocators>9000-9100</portAllocators>
        </configuration>
    </execution>
</executions>

<executions>
    <execution>
        <id>allocate-ports</id>
        <goals>
            <goal>allocate</goal>
        </goals>
        <configuration>
            <ports>portA,portB,portC</ports>
        </configuration>
    </execution>
</executions>
```
