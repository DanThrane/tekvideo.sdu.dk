<input type="hidden" name="isEditing" value="${isEditing}" />
<g:if test="${isEditing}">
    <input type="hidden" name="domain.id" value="${command?.domain?.id}" />
</g:if>