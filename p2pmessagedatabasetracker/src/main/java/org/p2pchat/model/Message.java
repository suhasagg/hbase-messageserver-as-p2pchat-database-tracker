package org.p2pchat.model;

import java.util.Objects;

public class Message {

	private int index;
    private long timestamp;
    private String peerId;
	private String message;

    public Message(int index, long timestamp, String peerId, String message) {
        this.index = index;
        this.timestamp = timestamp;
        this.peerId = peerId;
        this.message = message;
    }
	
	
    public int getIndex() {
		return index;
	}



	public void setIndex(int index) {
		this.index = index;
	}



	public long getTimestamp() {
		return timestamp;
	}



	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}



	public String getMessage() {
		return message;
	}



	public void setMessage(String message) {
		this.message = message;
	}

	
    public String getPeerId() {
		return peerId;
	}

	public void setPeerId(String peerId) {
		this.peerId = peerId;
	}



    @Override
    public String toString() {
        return String.format("%d, [%d] %s: %s", index, timestamp, peerId, message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return index == message1.index &&
                timestamp == message1.timestamp &&
                Objects.equals(peerId, message1.peerId) &&
                Objects.equals(message, message1.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, timestamp, peerId, message);
    }
}
