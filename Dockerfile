# 실행용 JRE 이미지 (멀티 아키텍처 지원)
FROM eclipse-temurin:21-jre

# 작업 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일 복사 (로컬에서 빌드된 JAR)
COPY build/libs/*.jar app.jar

# JAR 실행
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
