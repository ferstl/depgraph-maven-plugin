graph [
node [
id "com.github.ferstl:sub-parent:pom:"
label "sub-parent"
]

node [
id "com.github.ferstl:module-2:jar:"
label "module-2"
]

node [
id "com.google.guava:guava:jar:"
label "guava"
]

edge [
source "com.github.ferstl:sub-parent:pom:"
target "com.github.ferstl:module-2:jar:"
]

edge [
source "com.github.ferstl:module-2:jar:"
target "com.google.guava:guava:jar:"
]

]