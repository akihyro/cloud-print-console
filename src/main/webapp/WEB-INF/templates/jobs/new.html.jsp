<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <title>Submit Job</title>
</head>
<body>
    <h1>Submit Job</h1>
    <form action="${fn:escapeXml(uriInfo.resolve('jobs.html'))}" method="post" enctype="multipart/form-data">
        <table border="1">
            <tr>
                <th><label for="printer-id">Printer ID</label></th>
                <td><input id="printer-id" type="text" name="printer-id" value="${fn:escapeXml(it.printerId)}" /></td>
            </tr>
            <tr>
                <th><label for="form-title">Title</label></th>
                <td><input id="form-title" type="text" name="title" /></td>
            </tr>
            <tr>
                <th><label for="form-content-type">Type</label></th>
                <td>
                    <select id="form-content-type" name="content-type">
                        <option value="text/html">HTML</option>
                    </select>
                </td>
            </tr>
            <tr>
                <th><label for="form-content">File</label></th>
                <td><input id="form-content" type="file" name="content" /></td>
            </tr>
        </table>
        <p>
            <input type="submit" value="Submit" />
        </p>
    </form>
    <p>
        <a href="${fn:escapeXml(uriInfo.resolve('printers.html'))}">Back</a>
    </p>
</body>
</html>
