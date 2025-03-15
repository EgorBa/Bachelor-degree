while (<>) {
print if /(^cat(\W)(.*))|((.*)(\W)cat(\W)(.*))|((.*)(\W)cat$)|(^cat$)/ ;
}
