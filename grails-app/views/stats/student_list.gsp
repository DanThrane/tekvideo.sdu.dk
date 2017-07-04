<%--suppress HtmlUnknownTag --%>
<g:applyLayout name="stats_layout">
    <html xmlns="">
    <head>
        <title>Studerende i ${node.name}</title>
    </head>

    <body>
    <p>Antal studerende: ${students.size()}</p>

    <twbs:row>
        <twbs:column>
            <twbs:table responsive="true" striped="true" hover="true">
                <thead>
                <twbs:tr>
                    <twbs:th>Bruger type</twbs:th>
                    <twbs:th>Brugernavn</twbs:th>
                    <twbs:th>E-mail</twbs:th>
                    <twbs:th>E-Learn</twbs:th>
                </twbs:tr>
                </thead>
                <tbody>
                <g:each in="${students}" var="student">
                    <twbs:tr>
                        <twbs:td>${student.isCas ? "SDU" : "TekVideo"}</twbs:td>
                        <twbs:td>
                            <g:if test="${student.realName != null}">
                                <span title="Fulde navn">${student.realName}</span>
                                <span title="Brugernavn">(${student.username})</span>
                            </g:if>
                            <g:else>
                                <span title="Brugernavn">${student.username}</span>
                            </g:else>
                        </twbs:td>
                        <twbs:td>${student.email}</twbs:td>
                        <twbs:td>${student.elearnId}</twbs:td>
                    </twbs:tr>
                </g:each>
                </tbody>
            </twbs:table>
        </twbs:column>
    </twbs:row>

    </body>
    </html>
</g:applyLayout>
