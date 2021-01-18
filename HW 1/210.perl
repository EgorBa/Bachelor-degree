while (<>) {
s/a([0-9a-zA-Z\W]*?)aa([0-9a-zA-Z\W]*?)aa([0-9a-zA-Z\W]*?)a/bad/g ;
print ;
}