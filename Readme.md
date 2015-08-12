AspectJ Load Time Weaving with Spring 4 (shared and webapp context) and Weblogic 12, proof of concept
=================================================

This demo shows by example how to use AspectJ Load Time Weaving with Spring 4 in Weblogic 12.  This demo is unique in that it load time weaves
the spring parent (shared) context in addition to the webapp contexts **without using a javaagent, custom classloaders, or ejbs**
This allows for weavable skinny wars and weavable library wars with zero system wide configuration changes.

These following requirements sum up the issue whose solution is the goal of this demo :
* AspectJ Load Time Weaving (LTW) across entire application
* No system-wide configuration (runs on standard WebLogic 12)
* Shared parent Spring context to avoid duplicate instantiation of expensive beans/resources
* Skinny WARs with no duplicate JARs in the EAR


Highlights
--------------------------------------------------

* weblogic-application.xml - Modifies classloader heirarchy
```xml
<classloader-structure>
...
</classloader-structure>
```

* web.xml - communal (shared) war loads spring parent context
```xml
<context-param>
    <param-name>locatorFactorySelector</param-name>
    <param-value>classpath:spring/shared-context.xml</param-value>
</context-param>
<context-param>
    <param-name>parentContextKey</param-name>
    <param-value>shared.context</param-value>
</context-param>
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath:spring/empty.xml</param-value>
</context-param>
```

* spring context in each classloader must create LTW for that classloader, therefore there should be declared one <context:load-time-weaver /> in the parent context and one per web application

* aspectjweaver is an EAR dependency so it will be available to all WARs

* skinny wars automatically generated using plugin at [Communal WAR Maven Plugin](https://github.com/asegner/communalwar-maven-plugin)


What was the problem again?
--------------------------------------------------
If we remove LTW from our requirements, the de facto solution for sharing resources involves moving the shared code into the EAR level. The application classloader would load these classes and then
they will be visible to the entire application. However, with LTW, the solution breaks. We are unable to use LTW with anything in the EAR; therefore the solution needs to be updated. In WebLogic 12, and
likely other containers, the classes loaded by the EAR level classloader are not available to AspectJ. This has the side effect of preventing the APP-INF/lib directory from being visible to a load
time weaver -- or any application scoped instrumentation.

The crux of the solution must address LTW across all spring contexts, most importantly, the shared context. We are still concerned with the skinny WAR level, but LTW for WARs should work out of the
box.  One viable solution is to use a javaagent at JVM startup to replace all the default classloaders with weavable implementations from the initial bootstrap classloader to the application level
classloaders. This will give AspectJ visibility to all classloaders. This is a system wide change and and will provide instrumentable classloaders to everything on the JVM, which may not be desirable.
Additionally, this solution eventually requires tasking for system admins to change server configuration.

A developer mindset may next think to write a custom classloader that will reload classes itself that are not loaded by a weavable class loader. Although it may appear to work,
this solution violates one of the main java classloader design principles - Uniqueness Principle. According to this principle, a class loaded by a parent classloader should never be loaded again
by a child classloader.  Though its completely possible to write a classloader which violates the Uniqueness principle and loads the class itself, it should not be practiced.
This solution breaks the established expectations and guarantees of a classloader and has the potential to introduce nondeterministic behavior into a system. A custom classloader must maintain
Java's classloader design principles.



How does this demo solve it?
--------------------------------------------------
As stated above, the difficult part of this problem is determining how to weave the shared code in the EAR. Since the EAR level is not weavable without system side changes, we need to move the
resources out of the EAR. This solution packages the shared resources into an additional WAR beneath the EAR. This will be referred to as the communal WAR. This communal WAR gives us LTW over the shared code.

At this point, however, the code is not yet shared, as the skinny WARs in the system cannot see the shared resources. Standard WAR classloaders are siblings and classloaders may only see resources in
their parent. To resolve this, the classloader heirarchy is modified. Weblogic's classloader heirarchy feature is leveraged to alter the classloader heirarcy inserting the communal WAR between the
application classloader and the individual skinny WAR's web application classloaders. Now from a classloader point of view, the communal WAR is the parent of the skinny WARs and provides a weavable,
shared library and spring context that is visible to all the WARS in the EAR without using a javaagent.

The next step of this solution is optional. [Communal WAR Maven Plugin](https://github.com/asegner/communalwar-maven-plugin) is used to automate the creation of skinny WARs from fat WARs. This plugin
attempts to determine what jars belong in the communal war without any additional configuration. This is one of the many ways to accomplish the packaging of skinny WARs. Maven shade plugin may be useful as well.
