package Content;

import java.io.Serializable;

public class Mail implements Serializable {
	byte[] message;
	Header header;
	Attachment attachment;
	boolean ifAttachment = false;

	public boolean isIfAttachment() {
		return ifAttachment;
	}

	public void setIfAttachment(boolean ifAttachment) {
		this.ifAttachment = ifAttachment;
	}

	public Attachment getAttachment() {
		return attachment;
	}

	public void setAttachment(Attachment attachment) {
		this.attachment = attachment;
	}

	public byte[] getMessage() {
		return message;
	}

	public void setMessage(byte[] message) {
		this.message = message;
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

}
