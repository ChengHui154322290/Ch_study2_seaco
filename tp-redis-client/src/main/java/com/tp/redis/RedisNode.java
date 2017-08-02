package com.tp.redis;

import java.io.Serializable;

/**
 * @author szy
 *
 */
public class RedisNode implements Serializable {

	private static final long serialVersionUID = -8410959643418470980L;

	private String host;

	private int port;

	private boolean alive;

	public RedisNode() {

	}

	public RedisNode(String host, int port, boolean alive) {
		super();
		this.host = host;
		this.port = port;
		this.alive = alive;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (alive ? 1231 : 1237);
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + port;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RedisNode other = (RedisNode) obj;
		if (alive != other.alive)
			return false;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (port != other.port)
			return false;
		return true;
	}

}
