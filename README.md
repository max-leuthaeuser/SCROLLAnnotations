SCROLLAnnotations
=================

This project contains annotations supporting [SCROLL][scroll].

**1. Edit, develop and build:**
  1. Clone this repo.
  2. Intellij IDE: use the built-in import SBT project functionality.
  3. You may want to use SBT and run ```eclipse``` if you are using the Eclipse Scala IDE (to config see [here][gen-eclipse]).
  4. Invoke ```sbt assembly```. The resulting ```SCROLLAnnotations.jar``` is stored under ```target/scala-2.12/```.

**2. Use the plugin:**

  1. Add ```"com.github.max-leuthaeuser" %% "scrollannotations" % "latest.integration"``` to your sbt configuration as dependecy.
  2. Or if you want to use it on the console directly with ```scalac```: add the ```-Xplugin:SCROLLAnnotations-assembly-versionNumber.jar``` switch.
  3. Add ```addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)``` to your sbt build configuration.

[gen-eclipse]: https://github.com/typesafehub/sbteclipse
[scroll]: https://github.com/max-leuthaeuser/SCROLL