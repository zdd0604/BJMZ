package com.hjnerp.net;

import java.io.Serializable;


/**
 * data packet which carry chat message or announcement
 * 
 * @author John Kenrinus Lee
 */
public class Msg implements DataPacket
{
	private static final long serialVersionUID = 1L;
	/** the id of the message*/
	public String id;
	/** the ID of the user who send the message */
	public String from;
	/** the ID of the user who will receive the message */
	public String to;
	/** message type，see the constants which start with ChatConstants.msg.TYPE_ */
	public String type;
	/** the timestamp of send message，a millisecond */
	public String time;
	/** message body */
	public String body;
	/** if this message from a group, the value is group id, otherwise is "" */
	public String group;
	/** prompt encode of response, like ChatConstants.ENCODE_*/
	public String encode;
	/** media file info*/
	public File file = new File();
	/** if msg need beep*/
	public String isplay = "Y";
	
	
	/** media file info*/
	public static final class File implements Serializable
	{
		private static final long serialVersionUID = 1L;
		/** file id*/
		public String id;
		/** file type*/
		public String scene;
		
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			result = prime * result + ((scene == null) ? 0 : scene.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			File other = (File) obj;
			if (id == null)
			{
				if (other.id != null)
					return false;
			}
			else if (!id.equals(other.id))
				return false;
			if (scene == null)
			{
				if (other.scene != null)
					return false;
			}
			else if (!scene.equals(other.scene))
				return false;
			return true;
		}

		@Override
		public String toString()
		{
			return "File [id=" + id + ", scene=" + scene + "]";
		}
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((body == null) ? 0 : body.hashCode());
		result = prime * result + ((encode == null) ? 0 : encode.hashCode());
		result = prime * result + ((file == null) ? 0 : file.hashCode());
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Msg other = (Msg) obj;
		if (body == null)
		{
			if (other.body != null)
				return false;
		}
		else if (!body.equals(other.body))
			return false;
		if (encode == null)
		{
			if (other.encode != null)
				return false;
		}
		else if (!encode.equals(other.encode))
			return false;
		if (file == null)
		{
			if (other.file != null)
				return false;
		}
		else if (!file.equals(other.file))
			return false;
		if (from == null)
		{
			if (other.from != null)
				return false;
		}
		else if (!from.equals(other.from))
			return false;
		if (group == null)
		{
			if (other.group != null)
				return false;
		}
		else if (!group.equals(other.group))
			return false;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		if (time == null)
		{
			if (other.time != null)
				return false;
		}
		else if (!time.equals(other.time))
			return false;
		if (to == null)
		{
			if (other.to != null)
				return false;
		}
		else if (!to.equals(other.to))
			return false;
		if (type == null)
		{
			if (other.type != null)
				return false;
		}
		else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Msg [id=" + id + ", from=" + from + ", to=" + to + ", type=" + type + ", time="
				+ time + ", body=" + body + ", group=" + group + ", encode=" + encode + ", file="
				+ file + "]";
	}
}
