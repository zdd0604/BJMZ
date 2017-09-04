package com.hjnerp.net;

/**
 * data packet which carry control command
 * 
 * @author John Kenrinus Lee
 */
public class IQ implements DataPacket
{
	private static final long serialVersionUID = 1L;
	/**
	 * the ID of the user who send the control command, if the value equals
	 * ChatConstants.iq.USERID_SYSTEM that means this control command is from
	 * server
	 */
	public String from;
	/**
	 * the ID of the user who will receive the control command,
	 * ChatConstants.iq.USERID_SYSTEM is the user ID that we can send to server
	 */
	public String to;
	/**
	 * the ID of the control command, it is a random UUID, and the IQ which
	 * response from server has the same ID value
	 */
	public String id;
	/**
	 * the category of the control command function, see the constants which
	 * start with ChatConstants.iq.FEATURE_
	 */
	public String feature;
	/**
	 * the type of the control command, see the constants which start with
	 * ChatConstants.iq.TYPE_
	 */
	public String type;
	/**
	 * extra data, a child instance of java.util.Map, for the key, see the
	 * constants which start with ChatConstants.iq.DATA_KEY_
	 */
	public Object data;
	/** prompt encode of response, like ChatConstants.ENCODE_*/
	public String encode;
	
	public IQ()
	{
	}
	
	public IQ(String from, String to, String id, String feature, String type, Object data, String encode)
	{
		this.from = from;
		this.to = to;
		this.id = id;
		this.feature = feature;
		this.type = type;
		this.data = data;
		this.encode = encode;
	}
	
	public IQ(IQ iq)
	{
		this.from = iq.from;
		this.to = iq.to;
		this.id = iq.id;
		this.feature = iq.feature;
		this.type = iq.type;
		this.data = iq.data;
		this.encode = iq.encode;
	}

	@Override
	protected IQ clone()
	{//warning: this is light copy
		return new IQ(this);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((feature == null) ? 0 : feature.hashCode());
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((encode == null) ? 0 : encode.hashCode());
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
		IQ other = (IQ) obj;
		if (data == null)
		{
			if (other.data != null)
				return false;
		}
		else if (!data.equals(other.data))
			return false;
		if (feature == null)
		{
			if (other.feature != null)
				return false;
		}
		else if (!feature.equals(other.feature))
			return false;
		if (from == null)
		{
			if (other.from != null)
				return false;
		}
		else if (!from.equals(other.from))
			return false;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
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
		if (encode == null)
		{
			if (other.encode != null)
				return false;
		}
		else if (!encode.equals(other.encode))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "IQ [from=" + from + ", to=" + to + ", id=" + id + ", feature=" + feature
				+ ", type=" + type + ", data=" + data + ", encode=" + encode + "]";
	}
}
