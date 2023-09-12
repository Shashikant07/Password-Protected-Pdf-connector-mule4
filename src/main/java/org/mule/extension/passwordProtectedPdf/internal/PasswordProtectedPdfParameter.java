package org.mule.extension.passwordProtectedPdf.internal;

import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

public class PasswordProtectedPdfParameter {
	 @Parameter
	 @Expression(ExpressionSupport.SUPPORTED)
	 @Summary("Enter the Title for your pdf")
	 private String Title;
	 @Parameter
	 @Expression(ExpressionSupport.SUPPORTED)
	 @Summary("Enter the content for your pdf")
	 private String Content;
	 @Parameter
	@Summary("Enter password for your pdf")
	@Expression(ExpressionSupport.SUPPORTED)
	private String Password;
	 @Parameter
	 @Optional
	 @Expression(ExpressionSupport.SUPPORTED)
	 @Summary("Enter the Logo for your pdf")
	 private String Image;
	 @Parameter
	 @Optional()
	 @Expression(ExpressionSupport.SUPPORTED)
	 @Summary("set Image opacity. Default 0.5")
	 private int opacity;
	 @Parameter
	 @Optional
	 @Expression(ExpressionSupport.SUPPORTED)
	 @Summary("Move Image by X distance. Default value 200")
	 private int MoveImageByX;
	 @Parameter
	 @Optional
	 @Expression(ExpressionSupport.SUPPORTED)
	 @Summary("Move Image by Y distance. Default value 750")
	 private int MoveImageByY;
	
	 public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getContent() {
		return Content.replace("\r\n", "").replace("\\r\\n", "").replace("\\n", "").replace(" \n", "").replace(" \\n", "").replace(" \r\n", "").replace(" \\r\\n", "");
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getImage() {
		return Image.replace("\\", "/");
	}
	public void setImage(String image) {
		Image = image;
	}
	public int getMoveImageByX() {
		return MoveImageByX;
	}
	public void setMoveImageByX(int moveImageByX) {
		MoveImageByX = moveImageByX;
	}
	public int getMoveImageByY() {
		return MoveImageByY;
	}
	public void setMoveImageByY(int moveImageByY) {
		MoveImageByY = moveImageByY;
	}
	public int getOpacity() {
		return opacity;
	}
	public void setOpacity(int opacity) {
		this.opacity = opacity;
	}
	public String getPassword() {
		return Password;
	}

	public void setPassword(String Password) {
		this.Password = Password;
	}
	
	 
}
