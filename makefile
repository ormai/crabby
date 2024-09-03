.PHONY: clean run

BUILD_DIR=out/production/crabby

run: build
	java -cp $(BUILD_DIR) main.Game

build:
	mkdir -p $(BUILD_DIR)
	cp -r src/main/resources/* $(BUILD_DIR)
	javac -d $(BUILD_DIR) $$(find src -type f -name "*.java")

clean:
	rm -rf out
