javac OSproj.java pcb.java
cat test.sh
java OSproj << EOF
4000000000
5
A 1 1000000000
A 5 2
A 1 12
d 1 a.txt
A 2 2
A 1 1
A 1 3
S m
t
t
A 1 1
S r
S i
D 1
S i
S r
t
t
t
A 1 1
A 10 10
A 1 1
t
A 1 3
S r
exit
EOF
