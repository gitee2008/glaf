package com.glaf.core.cache;

public class CacheItem implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	protected String region;
	protected String name;
	protected String key;
	protected long lastModified;
	protected long timeToLiveInSeconds;
	protected int size;

	public CacheItem() {

	}

	public CacheItem(String region, String key) {
		this.region = region;
		this.key = key;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CacheItem other = (CacheItem) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	public String getKey() {
		return key;
	}

	public long getLastModified() {
		return lastModified;
	}

	public String getName() {
		return name;
	}

	public String getRegion() {
		return region;
	}

	public int getSize() {
		return size;
	}

	public long getTimeToLiveInSeconds() {
		return timeToLiveInSeconds;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setTimeToLiveInSeconds(long timeToLiveInSeconds) {
		this.timeToLiveInSeconds = timeToLiveInSeconds;
	}

}
