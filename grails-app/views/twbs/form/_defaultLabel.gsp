<g:if test="${showLabel}">
    <g:if test="${isHorizontal}">
        <label for="${id}" class="${horizontalStyle.size.getClassName(horizontalStyle.columns)} control-label">
            ${labelText}
        </label>
    </g:if>
    <g:else>
        <label for="${id}">${labelText}</label>
    </g:else>
</g:if>