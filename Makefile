# Auto-detect ANTLR jar
ANTLR_JAR := $(firstword \
    $(wildcard antlr-4.*-complete.jar) \
    $(wildcard /usr/local/lib/antlr-4.*-complete.jar) \
    $(wildcard /usr/share/java/antlr-4.*-complete.jar) \
)

ifeq ($(ANTLR_JAR),)
$(error "ANTLR jar not found. Install it or place antlr-4.x-complete.jar in project root.")
endif

GRAMMAR = src/main/antlr/MOCP.g4
GEN_DIR = src/generated/java
SRC_DIR = src/main/java
BUILD_DIR = build
PACKAGE = mocp
MAIN_CLASS = mocp.Main

all: antlr compile

antlr:
	mkdir -p $(GEN_DIR)
	java -jar $(ANTLR_JAR) -Dlanguage=Java -package $(PACKAGE) -o $(GEN_DIR) $(GRAMMAR)

compile:
	mkdir -p $(BUILD_DIR)
	javac -cp $(ANTLR_JAR) -d $(BUILD_DIR) \
        $(shell find $(SRC_DIR) -name "*.java") \
        $(shell find $(GEN_DIR) -name "*.java")

run:
	java -cp $(BUILD_DIR):$(ANTLR_JAR) $(MAIN_CLASS) $(FILE)

clean:
	rm -rf $(BUILD_DIR) $(GEN_DIR)
