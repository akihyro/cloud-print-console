<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <title>Job Posted !!</title>
</head>
<body>
    <h1>Job Posted !!</h1>
    <p>
        <c:out value="${it}" />
    </p>
    <p>
        <a href="${fn:escapeXml(uriInfo.resolve('printers.html'))}">Back</a>
    </p>
</body>
</html>
