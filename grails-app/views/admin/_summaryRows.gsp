<%@ page import="dk.sdu.tekvideo.twbs.Icon" %>
<g:each in="${summary}" var="entry">
<tr>
    <td>${entry.key.name}</td>
    <td><date:oldDtFormatter date="${entry.key.dateCreated}"/></td>
    <td>
        ${entry.value.totalViews}
        <twbs:linkButton id="viewTrigger${entry.key.id}" class="mousePanelTrigger">
            <twbs:icon icon="${Icon.INFO_SIGN}"/>
        </twbs:linkButton>
        <sdu:mousePanel id="viewPanel${entry.key.id}" title="${g.message(code: "admin.video.summary.thead.views")}">
            <table>
                <tbody>
                <tr>
                    <td class="view-table-name"><g:message code="admin.video.summary.thead.views"/></td>
                    <td>${entry.value.totalViews}</td>
                </tr>
                <tr>
                    <td class="view-table-name"><g:message code="admin.video.summary.viewsByUsers"/></td>
                    <td>${entry.value.viewsByUsers} (${entry.value.viewsByUsersPercentage}%)</td>
                </tr>
                <tr>
                    <td class="view-table-name"><g:message code="admin.video.summary.viewsByStudents"/></td>
                    <td>${entry.value.viewsByStudents} (${entry.value.viewsByStudentsPercentage}%)</td>
                </tr>
                </tbody>
            </table>
        </sdu:mousePanel>
    </td>
    <td>
        ${entry.value.answersGiven} (${entry.value.correctPercentage}%)
        <twbs:linkButton id="answerTrigger${entry.key.id}" class="mousePanelTrigger">
            <twbs:icon icon="${Icon.INFO_SIGN}"/>
        </twbs:linkButton>
        <sdu:mousePanel id="answerPanel${entry.key.id}" title="${g.message(code: "admin.video.summary.thead.answers")}">
            <table>
                <tbody>
                <tr>
                    <td class="view-table-name"><g:message code="admin.video.summary.thead.answers"/></td>
                    <td>
                        ${entry.value.answersGiven}
                        [<span class="correct-answer">${entry.value.correctAnswers}</span> |
                        <span class="wrong-answer">${entry.value.wrongAnswers}</span>]
                    (${entry.value.correctPercentage}%)
                    </td>
                </tr>
                <tr>
                    <td class="view-table-name"><g:message code="admin.video.summary.answersByUsers"/></td>
                    <td>
                        ${entry.value.answersByUsers}
                        [<span class="correct-answer">${entry.value.correctByUsers}</span> |
                        <span class="wrong-answer">${entry.value.wrongAnswersByUsers}</span>]
                    (${entry.value.correctPercentageByUsers}%)
                    </td>
                </tr>
                <tr>
                    <td class="view-table-name"><g:message code="admin.video.summary.answersByStudents"/></td>
                    <td>
                        ${entry.value.answersByStudents}
                        [<span class="correct-answer">${entry.value.correctByStudents}</span> |
                        <span class="wrong-answer">${entry.value.wrongAnswersByStudents}</span>]
                    (${entry.value.correctPercentageByStudents}%)
                    </td>
                </tr>
                </tbody>
            </table>
        </sdu:mousePanel>
    </td>
    <td>${entry.value.mostCommonIncorrect}</td>
    <td>
        <twbs:linkButton action="videoDetailed" btnstyle="action">
            <twbs:icon icon="${Icon.SEARCH}"/>
        </twbs:linkButton>
    </td>
</tr>
</g:each>