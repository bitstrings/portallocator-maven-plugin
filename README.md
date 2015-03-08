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
            <portPools>
                <portPool>
                    <id>pool1</id>
                    <selectionStrategy>ascendant</selectionStrategy>
                    <ports>8080-</ports>
                </portPool>
                <portPool>
                    <id>pool2</id>
                    <selectionStrategy>random|lowestFirst|highestFirst</selectionStrategy>
                    <depletionStrategy>fail|random</depletionStrategy>
                    <ports>8080,8082,8085</ports>
                </portPool>
                <portPool>
                    <id>pool3</id>
                    <depletionStrategy>
                        <usePool></usePool>
                        <fail/>
                        <random/>
                    </depletionStrategy>
                    <ports>9090-9099</ports>
                </portPool>
            </portPools>
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
