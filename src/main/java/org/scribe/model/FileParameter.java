/**
 * 
 */
package org.scribe.model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import org.scribe.utils.OAuthEncoder;

/**
 * @author Jermaine
 * 
 */
public class FileParameter extends Parameter {

	public static final String DEFAULT_MIME_TYPE = "application/octet-stream";
	
	private String mimeType;
	private File srcFile;

	public FileParameter(final String key, final File file) {
		super(key, (String) null);
		this.srcFile = file;
	}

	/* (non-Javadoc)
	 * @see org.scribe.model.Parameter#getValue()
	 */
	@Override
	protected String getValue() {
		String value = super.getValue();
		FileInputStream fInStr = null;;
		try {
			// Load the file to a stream
			fInStr = new FileInputStream(srcFile);
			//Creat a byte buffer the size of the file
			byte readBuf[] = new byte[(int) fInStr.getChannel().size()];
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			int readCnt = fInStr.read(readBuf);
			while (0 < readCnt) {
				bout.write(readBuf, 0, readCnt);
				readCnt = fInStr.read(readBuf);
			}
			fInStr.close();
			byte[] byteArray = bout.toByteArray();
			value = new String(byteArray);
			bout.close();
		} catch (Throwable error) {
		} finally {
			if( fInStr != null ) {
				try {
					fInStr.close();
				} catch (Throwable e) {	}
			}
		}
		return value;
	}

	/**
	 * Gets the name which should be sent as the filenam
	 * in the request.
	 * @return The filename to be sent.
	 */
	public String getFileName() {
		return this.srcFile.getName();
	}
	
	/**
	 * Gets the MIME-Type for this parameter.
	 * @return The mime type specified for this parameter.
	 */
	public String getMimeType() {
		if( this.mimeType == null ) {
			this.mimeType = DEFAULT_MIME_TYPE;
		}
		return mimeType;
	}

	/**
	 * Sets the mime type to be used for this parameter.
	 * @param newMimeType The new mime type to be used.
	 * 
	 */
	public void setMimeType(final String newMimeType) {
		this.mimeType = newMimeType;
	}

	/* (non-Javadoc)
	 * @see org.scribe.model.Parameter#usedInBaseString()
	 */
	@Override
	public boolean usedInBaseString() {
		return false;
	}
	/**
	 * Encodes this parameter in the multi part format.
	 * @return The string encoded using the multi part
	 * format.
	 */
	public String asMultiPartEncodedString() {
		final StringBuilder strBldr = new StringBuilder();
		strBldr.append( String.format("Content-Disposition: %1$s; ", this.getDisposition()));
		strBldr.append( String.format("name=\"%1$s\"; ", OAuthEncoder.encode(this.getKey())));
		strBldr.append( String.format("filename=\"%1$s\"", this.getFileName()));
		strBldr.append( SEQUENCE_NEW_LINE );
		strBldr.append( String.format("Content-Type: %1$s", this.getMimeType()) );
		strBldr.append( SEQUENCE_NEW_LINE );
		strBldr.append( this.getValue() );
		strBldr.append( SEQUENCE_NEW_LINE );
		return strBldr.toString();
	}
}
