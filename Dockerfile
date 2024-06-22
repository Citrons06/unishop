# 사용할 Java의 베이스 이미지
FROM eclipse-temurin:21-jdk-jammy

# 작업 디렉토리 설정
WORKDIR /home/gradle/project

# Gradle 설치
RUN apt-get update && apt-get install -y wget unzip \
    && wget https://services.gradle.org/distributions/gradle-8.8-bin.zip -P /tmp \
    && unzip -d /opt/gradle /tmp/gradle-8.8-bin.zip \
    && ln -s /opt/gradle/gradle-8.8/bin/gradle /usr/bin/gradle \
    && rm /tmp/gradle-8.8-bin.zip \
    && apt-get clean

# 환경 변수 설정
ENV GRADLE_HOME=/opt/gradle/gradle-8.8
ENV PATH=$GRADLE_HOME/bin:$PATH

# 애플리케이션 파일을 컨테이너로 복사
COPY . /home/gradle/project

EXPOSE 8080

# Gradle build 실행, 테스트 프로파일 사용
RUN gradle clean build

# 기본 실행 명령어
CMD ["gradle", "build"]