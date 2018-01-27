<%@ page import="dk.sdu.tekvideo.FaIcon" %>
<div class="card-stack">
    <div class="card-item default active">
        ${raw(body())}
    </div>
    <div class="card-item loading">
        <fa:icon icon="${FaIcon.SPINNER}" spin="true" />
        Arbejder...
    </div>
    <div class="card-item success">
        <fa:icon icon="${FaIcon.CHECK}" />
        Succes!
    </div>
    <div class="card-item error">
        <fa:icon icon="${FaIcon.TIMES}" />
        Fejl - Prøv igen
    </div>
</div>