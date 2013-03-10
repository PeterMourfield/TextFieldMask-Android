package com.opens.components.mask;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Custom masked {@link TextWatcher} for android
 * support's multiple masks with an array as a parameter
 * the masks must be order by length (minor to mayor) first 
 * @author Leonardo Rossetto <leonardoxh@gmail.com>
 */
public class MaskedWatcher implements TextWatcher {

	/** The default mask used now */
	private String defMask;
	/** The masks to match */
	private ArrayList<String> masks;
	/** The formatter used to edit the masks */
	private MaskedFormatter mFormater;
	/** The current String begin edited by the user */
	private String result;
	
	/**
	 * Construct
	 * @param masks The masks to use
	 * @param hasOrdenate true if the masks has previously orded by length, if this 
	 * is passed as false the masks will be ordenated
	 * @throws ParseException if the masks are invalid / pattern not match
	 */
	public MaskedWatcher(ArrayList<String> masks, boolean hasOrdenate) throws ParseException {
		this.masks = masks;
		if(!hasOrdenate)
			this.order();
		this.defMask = masks.get(0);
		this.mFormater = new MaskedFormatter(this.defMask);
		this.result = "";
	}
	
	/**
	 * Return the default mask used now
	 * @return the default used mask
	 */
	public String getCurrentMask() {
		return this.defMask;
	}
	
	/**
	 * Get the current string editing right now by the user
	 * @return the current string edited right now
	 */
	public String inFieldRightNow() {
		return this.result;
	}
	
	/**
	 * Update the mask on the field if necessary
	 * @param state the current text to update
	 */
	protected void updateMask(String state) {
		for(String mask : this.masks) {
			if(MaskedWatcher.disponibleInMask(mask) >= MaskedWatcher.disponibleInMask(state)) {
				this.defMask = mask;
				break;
			}
		}
	}
	
	/**
	 * Return the disponible length of the respective mask
	 * @param mask the mask to check
	 * @return the disponible length in this mask
	 */
	protected static int disponibleInMask(String mask) {
		return mask.replaceAll("[^\\p{L}\\p{N}]", "").length();
	}
	
	/**
	 * Voodo - Remove the position of this string and replace to another
	 * @param s the string to manipulate
	 * @param pos the position to change
	 * @return the new string 
	 */
    protected static String removeCharAt(String s, int pos) {
        StringBuilder buffer = new StringBuilder(s.length() - 1);
        buffer.append(s.substring(0, pos)).append(s.substring(pos + 1));
        return buffer.toString();
    }
    
    /**
     * Order the array of masks
     */
    protected void order() {
    	this.masks = new ArrayList<String>(new HashSet<String>(this.masks));
    	Collections.sort(this.masks, new Ordenator());
    }
	
	@Override
	public void afterTextChanged(Editable text) {
        String editableText = text.toString();
        this.updateMask(text.toString());
        if(editableText.equals(this.result))
            return;
        try {
            this.mFormater.setMask(this.defMask);
            this.mFormater.setValueContainsLiteralCharacters(false);
            this.mFormater.setPlaceholderCharacter((char)1);
            editableText = this.mFormater.valueToString(editableText);
            try {
                editableText = editableText.substring(0, editableText.indexOf((char)1));
                if(editableText.charAt(editableText.length()-1) == 
                		this.defMask.charAt(editableText.length()-1))
                    editableText = editableText.substring(0, editableText.length() - 1);
            }
            catch(Exception e){ }
            this.result = editableText;
            text.replace(0, text.length(), editableText);
        }
        catch (ParseException e) {
            int offset = e.getErrorOffset();
            editableText = removeCharAt(editableText, offset);
            Editable replaced = text;
            if(!replaced.replace(0, text.length(), editableText).equals(text))
                text.replace(0, text.length(), editableText);
        }
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		
	}
	
}