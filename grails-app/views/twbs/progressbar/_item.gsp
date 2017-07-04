<div class="progress-bar ${barClasses} ${clazz}" role="progressbar" aria-valuenow="${value}" aria-valuemin="${minValue}"
     aria-valuemax="${maxValue}" style="${style}" id="${id}">
    <g:if test="${showLabel}">
        ${label}
    </g:if>
    <g:else>
        <span class="sr-only">${label}</span>
    </g:else>
</div>
