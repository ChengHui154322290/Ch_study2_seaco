package com.tp.redis.test.domain;

import java.io.Serializable;

public class PersonDO implements Serializable {

	private static final long serialVersionUID = -2807595057656887643L;

	private int id;

	private String name;
	
	public PersonDO(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public PersonDO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		PersonDO other = (PersonDO) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PersonDO [id=" + id + ", name=" + name + "]";
	}

}
