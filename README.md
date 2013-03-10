TextFieldMask-Android
=====================

A simple telephone mask for andorid EditText to use with N masks wath you want, for example in the brazilians telephones we have to masks (##) ####-#### and (##) #####-#### this will handle that.

Eliminate repetitive masks, order the masks for you if you need, and update the edittext if needed with a new mask.

Simple example:

EditText editText = new EditText(this);
ArrayList<String> masks = new ArrayList<String>();
masks.add("(##) ####-####");
masks.add("(##) #####-####");
try {
	MaskedWatcher watcher = new MaskedWatcher(masks, false);
	editText.addTextChangeListener(watcher);
}
catch(ParseException e) {
}
