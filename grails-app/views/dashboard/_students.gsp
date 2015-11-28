<%@ page import="dk.sdu.tekvideo.DashboardPeriod; dk.danthrane.twbs.NavStyle; dk.sdu.tekvideo.NodeStatus; dk.danthrane.twbs.ButtonSize; dk.danthrane.twbs.ButtonStyle; dk.sdu.tekvideo.FaIcon" contentType="text/html;charset=UTF-8" %>
<div id="students" class="card-item">
    <twbs:column sm="12">
        <p>
            <b>8 studerende</b> har til faget <b>"Et fag 2"</b> har set videoer over de sidste
            <b>14 dage</b>. Fordelingen ser ud som følger:
        </p>

        <twbs:table condensed="true" striped="true" responsive="true">
            <thead>
            <th>Navn</th>
            <th>Antal unikke videoer i faget</th>
            <th>Antal svar</th>
            <th>Detaljer</th>
            </thead>
            <tbody>
            <g:each in="${0..10}" var="i">
                <tr>
                    <td>Studerende ${i}</td>
                    <td>${(int) Math.floor(Math.random() * i)}</td>
                    <td>10 (8 <span class="text-success"><fa:icon
                            icon="${FaIcon.CHECK}"/></span>/2 <span
                            class="text-danger"><fa:icon icon="${FaIcon.TIMES}"/></span> [80%])</td>
                    <td><twbs:button style="${ButtonStyle.INFO}">&gt;</twbs:button></td>
                </tr>
            </g:each>
            </tbody>
        </twbs:table>
    </twbs:column>
</div>