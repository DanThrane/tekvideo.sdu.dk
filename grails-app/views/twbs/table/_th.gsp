<%@ page import="dk.danthrane.TagLibUtils" %>
<th class="${context.baseName} ${clazz}" ${raw(TagLibUtils.expandAttributes(attrs))}>${raw(body())}</th>