graph [
node [
id "com.github.ferstl:module-2:jar"
label "module-2"
]

node [
id "com.google.guava:guava:jar"
label "guava"
]

node [
id "com.github.ferstl:module-3:jar"
label "module-3"
]

node [
id "com.mysema.querydsl:querydsl-core:jar"
label "querydsl-core"
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
source "com.mysema.querydsl:querydsl-core:jar"
target "com.google.guava:guava:jar"

graphics
[
style "dotted"
targetArrow "standard"
]
]

edge [
source "com.github.ferstl:module-3:jar"
target "com.mysema.querydsl:querydsl-core:jar"
]

]
