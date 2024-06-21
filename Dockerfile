# 사용할 Java의 베이스 이미지
FROM eclipse-temurin:21-jdk-jammy

# 작업 디렉토리 설정
WORKDIR /home/gradle/project

# 환경 변수 설정
ENV GRADLE_USER_HOME=/cache

# 애플리케이션 파일을 컨테이너로 복사
COPY . /home/gradle/project

# Gradle wrapper 권한 부여
RUN chmod +x ./gradlew

# Gradle build 실행 (테스트 포함)
RUN ./gradlew clean build

# 포트 8080 노출
EXPOSE 8080

# 기본 실행 명령어 설정, 애플리케이션 실행을 위한 스크립트나 자바 실행 명령어를 사용
CMD ["java", "-jar", "build/libs/UniShop-0.0.1-SNAPSHOT.jar"]