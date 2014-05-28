/**
 * 
 */
package com.chester.android.choosemultiimage;

/**
 * @author CHazyhabiT
 *
 */
public class ImageEntity {

    private int imageUri;
    private boolean isSelected;
      
    public ImageEntity(int imageUri,boolean isSelected) {  
        this.imageUri = imageUri;  
        this.isSelected = isSelected;  
    }  
      
    public int getImageUri() {  
        return imageUri;  
    }  
    public void setImageUri(int imageUri) {  
        this.imageUri = imageUri;  
    }  
    public boolean isSelected() {  
        return isSelected;  
    }  
    public void setSelected(boolean isSelected) {  
        this.isSelected = isSelected;  
    }  
	
}
