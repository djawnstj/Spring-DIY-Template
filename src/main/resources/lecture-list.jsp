<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>강의 목록</title>
</head>
<body>
<a href="/lecture-registration">등록</a>
<c:forEach var="lecture" items="${lectures}">
    <li>No: ${lecture.id}</li>
    <li>강의 이름: ${lecture.name}</li>
    <li>강의 가격: ${lecture.price}</li>
    <button onclick="deleteLecture(${lecture.id})">삭제</button>
    <br>
</c:forEach>

<script>
    function deleteLecture(id) {
        if (!confirm("정말 삭제하시겠습니까?")) return;

        fetch("/lectures/" + id, {
            method: "DELETE"
        }).then(response => {
            if (response.ok) {
                alert("삭제되었습니다.");
                window.location.reload();
            } else {
                alert("삭제를 실패했습니다.");
            }
        });
    }
</script>
</body>
</html>
