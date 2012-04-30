-------------------------------
| PELLET (+ JENA) INTEGRATION |
-------------------------------

In order to use this maven project is mandatory to install the jars provided in this folder as Maven artifacts;
in fact these artifacts are not available in a public repository*, so they have been attached here.

Follow these steps:
1. Open a Prompt/Terminal
2. Navigate to this folder
3. Run the following (maven) commands

mvn install:install-file -Dpackaging=jar -DgroupId=org.mindswap.pellet -DartifactId=antlr -Dversion=2.7.5 -Dfile=antlr-2.7.5.jar
mvn install:install-file -Dpackaging=jar -DgroupId=org.mindswap.pellet -DartifactId=arq-extra -Dversion=2.6.4 -Dfile=arq-extra.jar
mvn install:install-file -Dpackaging=jar -DgroupId=org.mindswap.pellet -DartifactId=arq -Dversion=2.6.4 -Dfile=arq.jar
mvn install:install-file -Dpackaging=jar -DgroupId=org.mindswap.pellet -DartifactId=aterm-java -Dversion=1.6.0 -Dfile=aterm-java-1.6.jar
mvn install:install-file -Dpackaging=jar -DgroupId=org.mindswap.pellet -DartifactId=commons-logging -Dversion=1.1.1 -Dfile=commons-logging-1.1.1.jar
mvn install:install-file -Dpackaging=jar -DgroupId=org.mindswap.pellet -DartifactId=concurrent -Dversion=1.0.0 -Dfile=concurrent.jar
mvn install:install-file -Dpackaging=jar -DgroupId=org.mindswap.pellet -DartifactId=icu4j -Dversion=3.4.0 -Dfile=icu4j_3_4.jar 
mvn install:install-file -Dpackaging=jar -DgroupId=org.mindswap.pellet -DartifactId=iri -Dversion=2.6.4 -Dfile=iri.jar
mvn install:install-file -Dpackaging=jar -DgroupId=org.mindswap.pellet -DartifactId=jena -Dversion=2.6.4 -Dfile=jena.jar
mvn install:install-file -Dpackaging=jar -DgroupId=org.mindswap.pellet -DartifactId=pellet-full -Dversion=2.3.0 -Dfile=pellet.jar
mvn install:install-file -Dpackaging=jar -DgroupId=org.mindswap.pellet -DartifactId=relaxngDatatype -Dversion=1.0.0 -Dfile=relaxngDatatype.jar
mvn install:install-file -Dpackaging=jar -DgroupId=org.mindswap.pellet -DartifactId=xercesImpl -Dversion=1.0.0 -Dfile=xercesImpl.jar
mvn install:install-file -Dpackaging=jar -DgroupId=org.mindswap.pellet -DartifactId=xsdlib -Dversion=1.0.0 -Dfile=xsdlib.jar

After this (boring) procedure has been completed Maven will be able to retrieve declared dependencies and complete the build.


-----

* Actually someone has published some of this artifact on a public repository, but, for unknown reasons, they 
  does not work with this project; probably because of its OSGi nature (?!?).
