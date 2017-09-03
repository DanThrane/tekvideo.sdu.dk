<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title>500</title>
</head>

<body>

<twbs:row>
    <twbs:column>
        <twbs:pageHeader><h3>500 - Intern server fejl</h3></twbs:pageHeader>
        <twbs:row>
            <twbs:column>
                Der er sket en fejl. Prøv igen senere.

                <g:if test="${subject != null && body != null}">
                    <h4>${subject}</h4>
                    <pre>${body}</pre>
                </g:if>
            </twbs:column>
        </twbs:row>
    </twbs:column>
</twbs:row>

</body>
</html>