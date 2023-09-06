package com.peaga.todolist.enums;

public enum Status {
    PENDING(1),ONGOING(2),FINISHED(3);
    private int id;
    private Status(int id) {
        this.id=id;
    }
    public static Status valueOf(int num){
        for (Status value : Status.values()) { //verificar se o valor passado eh valido
            if(num==value.getId())
                return value;
        }
        throw new IllegalArgumentException("Invalid status id");
    }
    public int getId() {
        return id;
    }
}
