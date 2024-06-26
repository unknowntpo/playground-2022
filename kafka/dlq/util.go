package main

import (
	"bufio"
	"encoding/json"
	"fmt"
	"os"
	"strings"

	"github.com/confluentinc/confluent-kafka-go/kafka"
)

func ReadConfig(configFile string) kafka.ConfigMap {
	m := make(map[string]kafka.ConfigValue)

	file, err := os.Open(configFile)
	if err != nil {
		fmt.Fprintf(os.Stderr, "Failed to open file: %s", err)
		os.Exit(1)
	}
	defer file.Close()

	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		line := strings.TrimSpace(scanner.Text())
		if !strings.HasPrefix(line, "#") && len(line) != 0 {
			kv := strings.Split(line, "=")
			parameter := strings.TrimSpace(kv[0])
			value := strings.TrimSpace(kv[1])
			m[parameter] = value
		}
	}

	if err := scanner.Err(); err != nil {
		fmt.Printf("Failed to read file: %s", err)
		os.Exit(1)
	}

	return m
}

func Marshal(v any) []byte {
	b, err := json.Marshal(v)
	must(err)
	return b
}

func must(err error) {
	if err != nil {
		panic(err)
	}
}

func debug(v any) string {
	return string(Marshal(v))
}

func Unmarshal(b []byte, v any) {
	must(json.Unmarshal(b, v))
}
