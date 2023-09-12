package org.mule.extension.passwordProtectedPdf.internal;


import org.mule.runtime.extension.api.annotation.Configurations;
import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;

@Xml(prefix="PasswordProtectedPdf")
@Extension(name="PasswordProtectedPdf")
@Configurations(PasswordProtectedPdfConfiguration.class)
public class PasswordProtectedPdfExtension{
	
}
