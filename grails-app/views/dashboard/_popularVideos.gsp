<g:each in="${videos}" var="entry" status="i">
    <sdu:card class="popular-entry.video">
        <twbs:row>
            <twbs:column md="1">
                <h3>${i + 1}</h3>
            </twbs:column>
            <twbs:column md="11">
                <div>
                    <a href="#">${entry.video.name}</a>
                    fra
                    <sdu:linkToSubject subject="${entry.video.subject}">${entry.video.subject.name}</sdu:linkToSubject>
                    i
                    <sdu:linkToCourse course="${entry.video.subject.course}">
                        ${entry.video.subject.course.fullName} (${entry.video.subject.course.name})
                    </sdu:linkToCourse>
                <div>
                    I alt <i>${entry.views}</i> visninger med <i>${entry.answers}</i> svar, hvoraf
                    <i>${entry.correct} (${entry.correctPercentage}%)</i> var korrekte
                </div>
            </twbs:column>
        </twbs:row>
    </sdu:card>
</g:each>
