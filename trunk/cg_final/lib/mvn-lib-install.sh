# Version numbers here must be updated to agree with the versions specified
# in the dependency section of our "pom.xml" file.

mvn install:install-file -Dfile=lib/lwjgl/lwjgl.jar -DgroupId=org.lwjgl -DartifactId=lwjgl -Dversion=2.1 -Dpackaging=jar
mvn install:install-file -Dfile=lib/lwjgl/lwjgl_util.jar -DgroupId=org.lwjgl -DartifactId=lwjgl_util -Dversion=2.1 -Dpackaging=jar
mvn install:install-file -Dfile=lib/lwjgl/lwjgl_util_applet.jar -DgroupId=org.lwjgl -DartifactId=lwjgl_util_applet -Dversion=2.1 -Dpackaging=jar
mvn install:install-file -Dfile=lib/lwjgl/jinput.jar -DgroupId=net.java.dev.jinput -DartifactId=jinput -Dversion=SNAPSHOT -Dpackaging=jar
mvn install:install-file -Dfile=lib/jorbis/jorbis-0.0.17.jar -DgroupId=jorbis -DartifactId=jorbis -Dversion=0.0.17 -Dpackaging=jar
mvn install:install-file -Dfile=lib/jogl/jogl.jar -DgroupId=net.java.dev.jogl -DartifactId=jogl -Dversion=1.1.1 -Dpackaging=jar
mvn install:install-file -Dfile=lib/jogl/gluegen-rt.jar -DgroupId=net.java.dev.gluegen -DartifactId=gluegen-rt -Dversion=1.0b06 -Dpackaging=jar
mvn install:install-file -Dfile=lib/ode/odejava-jni.jar -DgroupId=org.odejava -DartifactId=odejava -Dversion=1.0 -Dpackaging=jar

#ELEGIR SEGUN SISTEMA OPERATIVO
mvn install:install-file -Dfile=lib/swt/linux/swt.jar -DgroupId=org.eclipse.swt -DartifactId=swt -Dversion=3.448 -Dpackaging=jar

mvn install:install-file -Dfile=lib/jmephysics.jar -Dsources=lib/jmephysics-sources.jar -Djavadoc=lib/jmephysics-javadoc.jar -DgroupId=com.jmonkeyengine -DartifactId=jmephysics -Dversion=2.1.0rc1-SNAPSHOT -Dpackaging=jar

#JMONKEY JARS
mvn install:install-file -Dfile=lib/jme/jme.jar -Dsources=lib/jme-sources.jar -Djavadoc=lib/jme-javadoc.jar -DgroupId=com.jmonkeyengine -DartifactId=jme -Dversion=2.0.1 -Dpackaging=jar

mvn install:install-file -Dfile=lib/jme/jme-audio.jar -DgroupId=com.jmonkeyengine -DartifactId=jme-audio -Dversion=2.0.1 -Dpackaging=jar
mvn install:install-file -Dfile=lib/jme/jme-awt.jar -DgroupId=com.jmonkeyengine -DartifactId=jme-awt -Dversion=2.0.1 -Dpackaging=jar
mvn install:install-file -Dfile=lib/jme/jme-collada.jar -DgroupId=com.jmonkeyengine -DartifactId=jme-collada -Dversion=2.0.1 -Dpackaging=jar
mvn install:install-file -Dfile=lib/jme/jme-editors.jar -DgroupId=com.jmonkeyengine -DartifactId=jme-editors -Dversion=2.0.1 -Dpackaging=jar
mvn install:install-file -Dfile=lib/jme/jme-effects.jar -DgroupId=com.jmonkeyengine -DartifactId=jme-effects -Dversion=2.0.1 -Dpackaging=jar
mvn install:install-file -Dfile=lib/jme/jme-font.jar -DgroupId=com.jmonkeyengine -DartifactId=jme-font -Dversion=2.0.1 -Dpackaging=jar
mvn install:install-file -Dfile=lib/jme/jme-gamestates.jar -DgroupId=com.jmonkeyengine -DartifactId=jme-gamestates -Dversion=2.0.1 -Dpackaging=jar
mvn install:install-file -Dfile=lib/jme/jme-model.jar -DgroupId=com.jmonkeyengine -DartifactId=jme-model -Dversion=2.0.1 -Dpackaging=jar
mvn install:install-file -Dfile=lib/jme/jme-ogrexml.jar -DgroupId=com.jmonkeyengine -DartifactId=jme-ogrexml -Dversion=2.0.1 -Dpackaging=jar
mvn install:install-file -Dfile=lib/jme/jme-scene.jar -DgroupId=com.jmonkeyengine -DartifactId=jme-scene -Dversion=2.0.1 -Dpackaging=jar
mvn install:install-file -Dfile=lib/jme/jme-swt.jar -DgroupId=com.jmonkeyengine -DartifactId=jme-swt -Dversion=2.0.1 -Dpackaging=jar
mvn install:install-file -Dfile=lib/jme/jme-terrain.jar -DgroupId=com.jmonkeyengine -DartifactId=jme-terrain -Dversion=2.0.1 -Dpackaging=jar
