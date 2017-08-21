package model.identity;

import java.util.Comparator;

public class ID<T extends Comparable> {

	/* The actual id value, immutable */
	private final T id;

	/**
	 * C'tor for ID class
	 *
	 * @param id
	 */
	public ID(final T id) {
		this.id = id;
	}

	/**
	 * Getter for value
	 *
	 * @return
	 */
	public T getId() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof ID) {
			ID other = (ID) obj;
			result = this.id.equals(other.id);
		}
		return result;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return id.toString();
	}

	public int compareTo(ID id1) {
		return intCompare((String)this.id, (String)id1.id);
	}

	private int intCompare(String id1, String id2) {
		int i = Integer.parseInt(id1);
		int j = Integer.parseInt(id2);
		return Integer.compare(i,j);
	}
}
