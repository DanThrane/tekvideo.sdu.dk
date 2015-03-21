package dk.sdu.tekvideo.twbs

class FormTagLib {
    static namespace = "twbs"

    /**
     * Displays a Bootstrap input. It uses form group, which sets the width of the element to 100%. It comes with
     * a label.
     *
     * Body:        (Optional) Provides a help block for the input field.
     *
     * Attributes
     * name:        The 'name' attribute used on the input field.
     * labelText:   (Optional) The text for the label. Defaults to the name attribute
     * type:        (Optional) The 'type' attribute used on the input field. Defaults to "text"
     * id:          (Optional) The 'id' attribute used for the label and input. Will default to the name attribute
     * placeholder: (Optional) The 'placeholder' attribute used for the input. Will default to an empty string.
     * disabled:    (Optional) If the field should be disabled. Defaults to false.
     */
    def input = { attrs, body ->
        String name = attrs.name
        String id = attrs.id ? attrs.id : name
        String labelText = attrs.labelText ? attrs.labelText : name
        String placeholder = attrs.placeholder ? "placeholder=\"$attrs.placeholder\"" : ""
        String type = attrs.type ? attrs.type : "text"
        boolean disabled = attrs.disabled ? Boolean.valueOf(attrs.disabled as String) : false
        String disabledAttr = disabled ? "disabled" : ""
        out << """
        <div class="form-group">
            <label for="$id">$labelText</label>
            <input type="$type" name="$name" class="form-control" id="$id" $placeholder $disabledAttr>
            <p class="help-block">
                ${body()}
            </p>
        </div>
        """
    }

    def checkbox = { attrs, body ->
        String name = attrs.name
        out << """
        <div class="checkbox">
            <label>
                <input type="checkbox" name="$name">${body}</input>
            </label>
        </div>
        """
    }

}
