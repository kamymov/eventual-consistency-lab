package com.store.post.infra.dto;

import java.util.Map;

public class DebeziumEvent {

    Payload payload;

    public static class Payload {
        Map<String, Object> before;
        Map<String, Object> after;
        String op;

        public Map<String, Object> getBefore() {
            return before;
        }

        public void setBefore(Map<String, Object> before) {
            this.before = before;
        }

        public Map<String, Object> getAfter() {
            return after;
        }

        public void setAfter(Map<String, Object> after) {
            this.after = after;
        }

        public String getOp() {
            return op;
        }

        public void setOp(String op) {
            this.op = op;
        }

        public Payload(Map<String, Object> before, Map<String, Object> after, String op) {
            this.before = before;
            this.after = after;
            this.op = op;
        }
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public DebeziumEvent(Payload payload) {
        this.payload = payload;
    }
}
