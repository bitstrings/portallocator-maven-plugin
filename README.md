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

            <portAllocators>

                <portAllocator>

                    <id>allocator-1</id>

                    <preferredPorts>8090</preferredPorts>

                    <strategy>
                        <order>ascending|descending|random</order>
                        <depletionAction>continue|fail</depletionAction>
                    </strategy>

                </portAllocator>

            </portAllocators>

        </configuration>
    </execution>
</executions>
```

Example - Port allocation
-------------------------
```
<configuration>
    <ports>
        <port>
            <poolRef>pool1</poolRef>
            <portName>tomcatPort</portName>
        </port>
    </ports>
</configuration>
```

```
<configuration>
    <ports>
        <port>
            <portName>tomcatPort</portName>
        </port>
    </ports>
</configuration>
```

Example - Port allocation with offset
-------------------------------------
```
<configuration>
    <ports>
        <port>
            <allocationRef>pool2</allocationRef>
            <portName>wildflyPort</portName>
            <setOffsetProperty>true|false</setOffsetProperty>
        </port>
    </ports>
</configuration>
```
