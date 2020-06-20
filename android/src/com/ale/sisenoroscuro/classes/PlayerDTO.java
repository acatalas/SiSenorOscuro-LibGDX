package com.ale.sisenoroscuro.classes;

import com.google.firebase.firestore.DocumentId;

import org.jetbrains.annotations.NotNull;

public class PlayerDTO {
    @DocumentId
    private String id;
    private String name;

    public PlayerDTO(){}

    public PlayerDTO(String name){
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerDTO player = (PlayerDTO) o;
        return id.equals(player.id);
    }


    @NotNull
    @Override
    public String toString() {
        return "Player{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
