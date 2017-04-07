# tutuk-performance
This measures the Gattling performance for tuktuk repo

#Pre-requisites to run the test
1. scala needs to be installed on the machine. The version being referred in build.sbt is 2.11.7
   The same can be downloaded from https://www.scala-lang.org/download/2.11.7.html
2. sbt (scala build tool) needs to be installed on machine. The same can be downloaded from http://www.scala-sbt.org/download.html 


#Command to run the perf tests
sbt 'testOnly com.tuktuk.TukTukApiLoadTest'

The following just depitcs the stats presented by Gattling. Once run, the results will be published to html and the same can be seen below
![Performace tests run on laptop](/design/images/laptoprun.png?raw=true "laptop run")


#Stack used for performing tests
http://gatling.io/
The actual jar dependencies can be referenced in build.sbt file in this repo

# Performance characterization
![Performance Characterization](/design/images/1.png?raw=true "Performance Characterization")
![Performance Characterization](/design/images/2.png?raw=true "Performance Characterization")
![Performance Characterization](/design/images/3.png?raw=true "Performance Characterization")
