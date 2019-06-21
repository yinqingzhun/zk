<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>demo</title>
    <script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.js"></script>
    <style type="text/css">
        table {
            table-layout: fixed;
            width: 100%;
            border-color: #2D93CA;
        }

        thead th {
            text-align: left;
        }
    </style>
</head>
<body>

<div>welcome to freemarker world!</div>

<h1>slow sql</h1>
<table>
    <thead>
    <th>TYPE</th>
    <th>COMMAND</th>
    <th>DURATION</th>
    <th>TIMESTAMP</th>
    </thead>
    <tbody>
        <#list logs as log>
        <tr>
            <td>${log.getLogType()}</td>

            <td>${log.getCommand()}</td>

            <td>${log.getDuration()}</td>
            <td>${log.getTimestamp()}</td>
        </tr>
        <#else>
        <tr>
            <td colspan="4">No Records</td>
        </tr>
        </#list>
    </tbody>
</table>

<script type="text/javascript">
    $(function () {

    })

</script>
</body>
</html>