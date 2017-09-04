package com.hjnerp.net;

/**
 * data packet which carry user status
 * 
 * @author John Kenrinus Lee
 */
public class Presence implements DataPacket
{
	private static final long serialVersionUID = 1L;
	/** the ID of the user who send user status */
	public String from;
	/** the ID of the user who will receive user status */
	public String to;
	/** the user status which will be sended */
	public String status;
	
	public Presence()
	{
	}
	
	public Presence(String from, String to, String status)
	{
		this.from = from;
		this.to = to;
		this.status = status;
	}
	
	public Presence(Presence presence)
	{
		this.from = presence.from;
		this.to = presence.to;
		this.status = presence.status;
	}
	
	@Override
	protected Presence clone()
	{
		return new Presence(this);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((to == null) ? 0 : to.hashCode());
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
		Presence other = (Presence) obj;
		if (from == null)
		{
			if (other.from != null)
				return false;
		}
		else if (!from.equals(other.from))
			return false;
		if (status == null)
		{
			if (other.status != null)
				return false;
		}
		else if (!status.equals(other.status))
			return false;
		if (to == null)
		{
			if (other.to != null)
				return false;
		}
		else if (!to.equals(other.to))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "Presence [from=" + from + ", to=" + to + ", status=" + status + "]";
	}
}
