package dk.danthrane.twbs

import dk.danthrane.util.TagCaptureService
import dk.danthrane.util.TagContextService
import org.springframework.validation.Errors

import static dk.danthrane.TagLibUtils.*

class FormTagLib {
    static namespace = "twbs"

    TagCaptureService tagCaptureService
    TagContextService tagContextService

    private String findFieldFromName(String name) {
        int idx = name.lastIndexOf('.')
        if (idx != -1 && idx != name.length() - 1) {
            return name.substring(idx + 1)
        }
        return name
    }

    private String getValidationClass(Validation validation) {
        String validationClass = "has-${validation.baseName} has-feedback"
        if (validation == Validation.DEFAULT) {
            validationClass = ""
        }
        return validationClass
    }

    private Map prepareCommonInputAttributes(String tagName, Map attrs) {
        // Common attributes
        String name = attrs.remove("name") ?: fail(String, "name", "twbs:$tagName")
        String id = attrs.remove("id") ?: name

        boolean disabled = attrs.disabled ? Boolean.valueOf(attrs.disabled as String) : false
        String disabledAttr = disabled ? "disabled" : ""

        // Styling
        InputSize size = attrs.remove("size") as InputSize ?: InputSize.DEFAULT
        String sizeClass = ""
        String inputSizeClass = ""
        if (size != InputSize.DEFAULT) {
            sizeClass = "form-group-$size.className"
            inputSizeClass = "input-$size.className"
        }
        Map horizontalStyle = tagContextService.getContextAttributes("form-horizontal")
        boolean isHorizontal = horizontalStyle != null
        String clazz = attrs.class ?: ""
        String rawPlaceholder = attrs.placeholder
        String placeholder = expandOptionalAttribute("placeholder", attrs.remove("placeholder"))
        boolean showLabel = optionalBoolean(attrs.remove("showLabel"), true)

        String labelText = attrs.remove("labelText")
        if (labelText == null) {
            String labelCode = attrs.remove("labelCode")
            if (labelCode != null) {
                labelText = g.message(code: labelCode)
            } else {
                labelText = name
            }
        }

        // Validation
        Validation validation = attrs.remove("validation") ?: Validation.DEFAULT

        // Value
        String value = attrs.remove("value")

        // Value from object
        def bean = attrs.remove("bean")
        String beanField = attrs.remove("beanField") ?: findFieldFromName(name)
        if (value == null && bean != null) {
            if (bean.hasProperty(beanField)) {
                value = bean.properties[beanField]
            }

            // Validation from object
            if (bean.hasProperty("errors")) {
                Errors errors = bean.errors
                if (errors) {
                    if (errors.getFieldError(beanField)) {
                        validation = Validation.ERROR
                    }
                }
            }
        }

        [name: name, id: id, labelText: labelText, placeholder: placeholder, disabled: disabledAttr,
         clazz: clazz, validation: validation, validationClass: getValidationClass(validation), value: value,
         attrs: attrs, rawPlaceholder: rawPlaceholder, showLabel: showLabel, isHorizontal: isHorizontal,
         horizontalStyle: horizontalStyle, size: size, sizeClass: sizeClass, inputSizeClass: inputSizeClass]
    }

    /**
     * Displays a Bootstrap input.
     *
     * It uses form group, which sets the width of the element to 100%. It comes with a label.
     *
     * All core form attributes are accepted. Extra attributes are applied to the &lt;input&gt; element
     *
     * Body:        (Optional) Provides a help block for the input field.
     *
     * Additional attributes:
     * type:        (Optional) The 'type' attribute used on the input field. Defaults to "text"
     */
    def input = { attrs, body ->
        assistAutoComplete(attrs.name, attrs.id, attrs.labelText, attrs.labelCode, attrs.placeholder,
                attrs.placeholder, attrs.disabled, attrs.validation, attrs.value, attrs.bean, attrs.beanField,
                attrs.showLabel, attrs.size)

        Map model = prepareCommonInputAttributes("input", attrs)
        String type = attrs.remove("type") ?: "text"
        model.type = type

        String bodyContent = body()
        def leftAddon = tagCaptureService.hasTag("addon-left")
        def rightAddon = tagCaptureService.hasTag("addon-right")
        model.hasAddons = leftAddon || rightAddon

        out << render([plugin: "twbs3", template: "/twbs/form/input", model: model], { bodyContent })
    }

    def inputGroupAddon = { attrs, body ->
        out << render([plugin: "twbs3", template: "/twbs/form/addon"], body)
    }

    def inputGroupButton = { attrs, body ->
        out << render([plugin: "twbs3", template: "/twbs/form/buttonAddon"], body)
    }

    /**
     * Displays a Bootstrap text area.
     *
     * It uses form group, which sets the width of the element to 100%. It comes with a label.
     *
     * All core form attributes are accepted. Extra attributes are applied to the &lt;textarea&gt; element
     *
     * Body:        (Optional) Provides a help block for the field.
     */
    def textArea = { attrs, body ->
        assistAutoComplete(attrs.name, attrs.id, attrs.labelText, attrs.labelCode, attrs.placeholder,
                attrs.placeholder, attrs.disabled, attrs.validation, attrs.value, attrs.bean, attrs.beanField,
                attrs.showLabel, attrs.size)

        Map model = prepareCommonInputAttributes("textArea", attrs)

        out << render([plugin: "twbs3", template: "/twbs/form/textarea", model: model], body)
    }

    private Map prepareCheckboxAndRadio(name, attrs) {
        Map model = prepareCommonInputAttributes(name, attrs)
        String checkedAttribute = ""
        if (model.value) {
            model.value = Boolean.parseBoolean(model.value)
            if (model.value) checkedAttribute = true
        }
        model.checked = checkedAttribute
        return model
    }

    /**
     * Displays a checkbox.
     *
     * All core form attributes are accepted, except for placeholder. Extra attributes are applied to the
     * &lt;input&gt; element
     *
     * Body:        This tag doesn't take any body.
     */
    def checkbox = { attrs, body ->
        assistAutoComplete(attrs.name, attrs.id, attrs.labelText, attrs.labelCode, attrs.disabled,
                attrs.validation, attrs.value, attrs.bean, attrs.beanField, attrs.showLabel)

        Map model = prepareCheckboxAndRadio("checkbox", attrs)
        out << render([plugin: "twbs3", template: "/twbs/form/checkbox", model: model], body)
    }

    /**
     * Displays a radio.
     *
     * All core form attributes are accepted, except for placeholder. Extra attributes are applied to the
     * &lt;input&gt; element
     *
     * Body:        This tag doesn't take any body.
     */
    def radio = { attrs, body ->
        assistAutoComplete(attrs.name, attrs.id, attrs.labelText, attrs.labelCode, attrs.disabled,
                attrs.validation, attrs.value, attrs.bean, attrs.beanField, attrs.showLabel)

        Map model = prepareCheckboxAndRadio("radio", attrs)
        out << render([plugin: "twbs3", template: "/twbs/form/radio", model: model], body)
    }

    /**
     * Displays a select.
     *
     * All core form attributes are accepted. Extra attributes are applied to the
     * &lt;select&gt; element
     *
     * Note that the placeholder attribute is used for the "empty" option. Thus it will only be shown if the
     * "allowEmpty" attribute is set to true.
     *
     * Body:        (Optional) Provides a help block for the input field.
     * list:        A list of items that will be shown in the select box.
     * multiple:    (Optional) If the select should allow multiple selection, defaults to false.
     * name:        (Optional) The name attribute used for the select, defaults to an empty string.
     * property:    (Optional) If the object passed in the list is a non primitive, then the value of that property
     *              will be passed into the 'value' attribute of the option tag. This will default to "id" (that is the
     *              tag will work out of the box for domain models).
     */
    def select = { attrs, body ->
        assistAutoComplete(attrs.name, attrs.id, attrs.labelText, attrs.labelCode, attrs.disabled,
                attrs.validation, attrs.value, attrs.bean, attrs.beanField, attrs.placeholder, attrs.showLabel,
                attrs.size)

        Map model = prepareCommonInputAttributes("select", attrs)

        String property = attrs.remove("property") ?: "id"
        boolean allowEmpty = attrs.remove("allowEmpty") ?: false
        String multiple = attrs.remove("multiple")
        String multipleAttr = ""
        List listForTemplate = []
        List list = attrs.remove("list")
        if (list == null) {
            fail("list", "twbs:select")
        }

        if (multiple != null && Boolean.valueOf(multiple)) {
            multipleAttr = "multiple"
        }

        if (allowEmpty) {
            listForTemplate.add([value: "null", isSelected: false, selected: "", message: model.rawPlaceholder ?: "-"])
        }

        list.each {
            def item = [:]
            if (it.hasProperty(property)) {
                item.value = it.properties[property]
                item.message = it.toString()
                item.isSelected = model.value != null && model.value == item.message
            } else {
                item.value = it.toString()
                item.message = item.value
                item.isSelected = model.value != null && it as String == model.value
            }
            item.selected = item.isSelected ? "selected" : ""
            listForTemplate.add(item)
        }

        model.list = listForTemplate
        model.multiple = multipleAttr
        out << render([plugin: "twbs3", template: "/twbs/form/select", model: model], body)
    }

    def form = { attrs, body ->
        boolean inline = optionalBoolean(attrs.remove("inline"))
        boolean horizontal = optionalBoolean(attrs.remove("horizontal"))
        String clazz = attrs.remove("class") ?: ""
        List classes = [clazz]
        if (inline) {
            classes += "form-inline"
        }
        if (horizontal) {
            classes += "form-horizontal"
        }
        Map model = [classes: classes.join(" "), attrs: attrs]

        if (horizontal) {
            GridSize size = attrs.remove("label-grid-size") as GridSize ?: GridSize.MD
            Integer columns = attrs.remove("label-cols") as Integer ?: 3
            Map gridAttributes = [size: size, columns: columns]

            tagContextService.contextWithAttributes("form-horizontal", gridAttributes) {
                out << render([plugin: "twbs3", template: "/twbs/form/form", model: model], body)
            }
        } else {
            out << render([plugin: "twbs3", template: "/twbs/form/form", model: model], body)
        }
    }

}
