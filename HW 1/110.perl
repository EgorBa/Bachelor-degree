while (<>) {
print if  /^(\w+)\g1\W.*|.*\W(\w+)\g2\W.*|.*\W(\w+)\g3$|^(\w+)\g4$/ ;
}