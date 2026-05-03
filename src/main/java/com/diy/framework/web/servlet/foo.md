# 어노테이션 기반 예외 처리

지난 시간에 `HandlerExceptionResolver` 로 예외를 가로채는 hook 을 만들었으니 이제 그 위에 더 선언적인 API 를 얹을 차례이다. 지금 우리의 프레임워크는 예외 처리를 추가하려면 매번 `HandlerExceptionResolver` 를 직접 구현해야 한다. 이
과정에서 프레임워크 이용자는 `instanceof` 분기로 예외 타입을 골라내고, `ObjectMapper` 로 응답을 직접 직렬화하고, `ModelAndView(null)` 같은 framework 내부 마커까지 신경 써야 하므로 한 클래스에 여러 예외 처리가 뒤엉키고 보일러 플레이트가
누적된다.

이번 시간에선 `@ControllerAdvice` 와 `@ExceptionHandler` 어노테이션을 도입해 예외 타입과 처리 로직의 매핑을 선언적으로 분리하고, 응답 직렬화는 framework 에 맡겨보자.

## 요구사항

### 1. `@ExceptionHandler` / `@ControllerAdvice` 어노테이션 도입

프레임워크 이용자가 일반 클래스에 메서드를 정의하기만 하면 예외가 자동으로 처리되도록 다음 두 어노테이션을 추가하자.

- `@ExceptionHandler`: 메서드용. `value` 로 처리 대상 예외 클래스 배열을 받는다.
- `@ControllerAdvice`: 클래스용. 글로벌 예외 처리 클래스를 표시한다. `@Component` 메타 어노테이션으로 자동 빈 등록되도록 하자.

이 어노테이션을 해석하는 `HandlerExceptionResolver` 구현체 (`ExceptionHandlerExceptionResolver`) 를 만들고, `WebMvcConfigurationSupport` 에서 기본 빈으로 등록하자. resolver 동작은 다음 순서를 따른다.

- 컨텍스트 초기화 시점에 모든 `@ControllerAdvice` 빈을 훑어 `@ExceptionHandler` 메서드를 `예외 클래스 → 메서드` 형태로 인덱싱한다.
- 예외 발생 시 정확한 클래스부터 슈퍼클래스 순서로 매칭 메서드를 찾는다 (`instanceof` 의미).
- 매칭된 메서드를 invoke 한 뒤 반환값은 이미 등록된 `HttpMessageConverter` 로 JSON 직렬화한다. 이용자가 직접 응답 본문을 작성할 일은 없도록 한다.

`@ExceptionHandler` 메서드는 다음 파라미터를 골라 받을 수 있어야 한다 (단순화 버전 — 전체 `HandlerMethodArgumentResolver` 체인은 적용하지 않는다).

- 처리 대상 예외 객체 (선언 타입이 실제 예외와 호환될 때)
- `HttpServletRequest`
- `HttpServletResponse` (HTTP status 변경 등)

이 작업 역시 `HandlerExceptionResolver` 를 직접 구현한 기존 코드에는 영향을 주지 않아야 한다. `@ControllerAdvice` 가 한 개도 등록되지 않은 프로젝트도 기존과 동일하게 동작해야 한다.

### 2. 강의 등록 예외 처리를 `@ControllerAdvice` 로 전환

지난 시간에 만든 `HandlerExceptionResolver` 직접 구현체 (`LectureExceptionResolver`) 를 `@ControllerAdvice` 클래스로 전환하자.

- 새 `@ControllerAdvice` 클래스를 만들고 `@ExceptionHandler(IllegalArgumentException.class)` 메서드 안에 응답 데이터 (예: `Map`) 를 만들어 반환한다.
- HTTP 400 status 를 위해 메서드 파라미터로 `HttpServletResponse` 를 받아 `setStatus` 를 호출한다.
- 응답 직렬화는 framework 가 담당하므로 `ObjectMapper` 직접 사용은 더 이상 필요 없다.

전환 후 동작 결과는 이전과 동일해야 한다 (검증 실패 시 400 + JSON `{"error": "BAD_REQUEST", "message": ...}`). 기존 `LectureExceptionResolver` 는 비교 학습용으로 코드만 남겨두고 빈 등록에서만 빼자.