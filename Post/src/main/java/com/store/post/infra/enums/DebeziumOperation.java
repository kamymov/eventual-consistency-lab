package com.store.post.infra.enums;

public enum DebeziumOperation {
    CREATE,
    UPDATE,
    DELETE;

    public static DebeziumOperation getOperation(String op) {
        return switch (op) {
            case "c" -> CREATE;
            case "d" -> DELETE;
            case "u" -> UPDATE;
            default -> null;
        };
    }
}
