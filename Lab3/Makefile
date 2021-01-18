SOURCES = $(shell find src -type f -name "*.java")
CLASSES = $(patsubst src/%.java,out/%.class,$(SOURCES))

all: $(CLASSES)

run:
	java  -Xms256m -Xmx1024m -cp out Main

pack:
	zip Parser2.zip -r Makefile src

out/%.class: src/%.java out
	javac -encoding UTF8 -cp src $< -d out

out:
	mkdir -p out