graph [
node [
id "com.github.ferstl:module-1:jar"
label "com.github.ferstl
module-1
1.0.0-SNAPSHOT"
]

node [
id "commons-codec:commons-codec:jar"
label "commons-codec
commons-codec
1.16.0"
]

node [
id "org.apache.commons:commons-lang3:jar"
label "org.apache.commons
commons-lang3
3.14.0"
]

node [
id "com.github.ferstl:module-3:jar"
label "com.github.ferstl
module-3
1.0.0-SNAPSHOT"
]

node [
id "com.github.ferstl:module-2:jar"
label "com.github.ferstl
module-2
1.0.0-SNAPSHOT"
]

node [
id "com.google.guava:guava:jar"
label "com.google.guava
guava
33.0.0-jre"
]

node [
id "com.querydsl:querydsl-core:jar"
label "com.querydsl
querydsl-core
5.0.0"
]

node [
id "com.mysema.commons:mysema-commons-lang:jar"
label "com.mysema.commons
mysema-commons-lang
0.2.4"
]

edge [
source "com.github.ferstl:module-1:jar"
target "commons-codec:commons-codec:jar"
]

edge [
source "com.github.ferstl:module-1:jar"
target "org.apache.commons:commons-lang3:jar"
]

edge [
source "com.github.ferstl:module-3:jar"
target "com.github.ferstl:module-1:jar"
]

edge [
source "com.github.ferstl:module-2:jar"
target "com.google.guava:guava:jar"
]

edge [
source "com.github.ferstl:module-3:jar"
target "com.github.ferstl:module-2:jar"
]

edge [
source "com.querydsl:querydsl-core:jar"
target "com.mysema.commons:mysema-commons-lang:jar"
]

edge [
source "com.github.ferstl:module-3:jar"
target "com.querydsl:querydsl-core:jar"
]

]
