<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <title>Printers</title>
</head>
<body>
    <h1>Printers</h1>
    <table border="1">
        <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Connection Status</th>
            <th>Submit...</th>
        </tr>
        <c:forEach var="printer" items="${it.printers}">
        <tr>
            <td><c:out value="${printer.displayName}" /></td>
            <td><c:out value="${printer.description}" /></td>
            <td><c:out value="${printer.connectionStatus}" /></td>
            <td>
                <c:url var="submitFormUrl" value="submit-form">
                    <c:param name="printer-id" value="${printer.id}" />
                </c:url>
                <a href="${submitFormUrl}">Submit...</a>
            </td>
        </tr>
        </c:forEach>
    </table>
</body>
</html>
