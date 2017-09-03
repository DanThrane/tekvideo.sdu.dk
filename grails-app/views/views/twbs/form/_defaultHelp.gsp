<g:if test="${isHorizontal}">
    <div class="${horizontalStyle.size.getClassName(12 - horizontalStyle.columns)} ${horizontalStyle.size.getOffsetName(horizontalStyle.columns)}">
</g:if>
<p class="help-block">
    ${raw(body())}
</p>
<g:if test="${isHorizontal}">
    </div>
</g:if>