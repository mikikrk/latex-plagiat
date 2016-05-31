package com.zpi.plagiarism_detector.commons.protocol;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Message implements Serializable {
    public static final Message POISON_PILL = new Message(ProtocolCode.POISON_PILL);
    private ProtocolCode code;
    private Object sendObject;

    public Message(ProtocolCode code) {
        this.code = code;
    }

    public Message(ProtocolCode code, Object obj) {
        this.code = code;
        sendObject = obj;
    }

    public ProtocolCode getCode() {
        return code;
    }

    public Object getSentObject() {
        return sendObject;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj instanceof Message) {
            Message msg = (Message) obj;
            boolean areCodesEqual = code == msg.getCode();
            boolean areSentObjectsEqual = sendObject == null || sendObject.equals(msg.getSentObject());

            return areCodesEqual && areSentObjectsEqual;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Msg code: ");
        String codeValue = code != null ? code.name() : "null";
        sb.append(codeValue);
        sb.append("\tvalue: ");
        Object sendObject1 = sendObject != null ? sendObject : "null";
        sb.append(sendObject1);
        return sb.toString();
    }
}