<li class="${dropdownClass} ${clazz}">
    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="${expanded}">
        ${raw(body())}
        <g:if test="${!hideCaret}">
            <twbs:caret />
        </g:if>
    </a>
    <g:selectContent key="dropdown-menu"/>
</li>
