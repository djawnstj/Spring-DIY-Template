# 커밋 (Conventional Commit)

변경사항을 분석하여 Conventional Commits 규칙에 맞게 커밋합니다.

## 사용법

- `/commit` — 모든 변경사항을 커밋
- `/commit <파일명>` — 특정 파일만 커밋 (예: `/commit src/main/java/com/diy/app/DispatcherServlet.java`)

## 실행 순서

1. `git status` 와 `git diff HEAD` 로 변경사항 파악
2. `$ARGUMENTS` 처리:
   - **있으면**: `git commit --only $ARGUMENTS` 사용 (이미 스테이징된 다른 파일에 영향 없이 해당 파일만 커밋)
   - **없으면**: 관련 파일 전체를 `git add` 후 `git commit`
   - 제외 대상: `.env`, 인증 정보, 대용량 바이너리
3. 변경 내용에 맞는 커밋 타입 결정:
   - `feat`: 새 기능 추가
   - `fix`: 버그 수정
   - `docs`: 문서 변경
   - `style`: 코드 포맷 변경 (로직 변경 없음)
   - `refactor`: 기능 변경 없는 코드 구조 개선
   - `test`: 테스트 추가 또는 수정
   - `chore`: 빌드 설정, 의존성, IDE 설정 변경
4. 아래 형식으로 커밋 메시지 작성 후 커밋

## 커밋 메시지 형식

```
<타입>(<선택적 범위>): <명령형으로 작성한 요약>

- <주요 변경사항>
- <주요 변경사항>
```

## 작성 규칙

- 요약 줄: 최대 72자, 콜론 뒤 소문자, 마침표 없음
- 명령형 사용: "추가" (O) / "추가했음" (X), "수정" (O) / "수정함" (X)
- 본문 bullet은 내용이 복잡할 때만 작성
- 변경사항이 여러 관심사에 걸치면 주된 타입 사용, 나머지는 본문에 언급
- Co-Authored-By 줄을 절대 추가하지 않는다

이제 현재 변경사항을 분석하고 커밋을 생성하세요.
