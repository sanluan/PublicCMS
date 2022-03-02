@bender-tags: feature, colorinput
@bender-ui: collapsed
@bender-ckeditor-plugins: colorinput,colordialog,toolbar,wysiwygarea,sourcearea,contextmenu

1. Click in the editor
1. Press the color input toolbar button
	* Color input preview dialog should appear.
1. Open the color dialog
1. Type a color value into the input field E.g. (`ABC123`).
1. Cancel it.
	* **Expected**: the color was not changed
1. Repeat above steps on other inputs.
