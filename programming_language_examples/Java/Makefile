MAINCLASS=edu.iit.MyJDBCExample
BINDIR=./bin
SRCDIR=./src
JAVA_FILES=$(SRCDIR)/$(subst .,/,$(MAINCLASS)).java
CLASS_FILES=$(subst $(SRCDIR),$(BINDIR),$(subst .java,.class,$(JAVA_FILES)))
MYCLASSPATH=postgresql.jar:$(BINDIR)
JAVAC=javac
JAVA=java

all: $(CLASS_FILES)

$(CLASS_FILES): $(JAVA_FILES)
	$(JAVAC) -d $(BINDIR) $(JAVA_FILES)

run: $(CLASS_FILES)
	$(JAVA) -cp $(MYCLASSPATH) $(MAINCLASS)

clean:
	rm -rf $(CLASS_FILES)

.PHONY = clean
