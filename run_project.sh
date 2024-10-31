#!/bin/bash

# 빌드된 JAR 파일 경로 설정 (변경 가능)
JAR_PATH=$(find ./build/libs -name "*.jar" | head -n 1)

# 빌드된 JAR 파일이 있는지 확인
if [ ! -f "$JAR_PATH" ]; then
  echo "JAR file not found. Exiting..."
  exit 1
fi

# 기본 포트 설정 
DEFAULT_PORT=8080

# JAR 파일 실행
echo "Running the application on port $PORT..."
java -jar "$JAR_PATH" --server.port="$PORT"
