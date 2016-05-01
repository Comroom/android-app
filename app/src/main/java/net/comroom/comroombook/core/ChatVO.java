package net.comroom.comroombook.core;

/**
 * Created by InhoChoi on 2016. 5. 1..
 */
public class ChatVO {
    private String id;
    private String name;
    private String[] members;

    public ChatVO(String id, String name, String[] members){
        this.id = id;
        this.name = name;
        this.members = members;
    }

    String getId(){
        return this.id;
    }

    void setId(String id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    void setName(String name){
        this.name = name;
    }

    String[] getMembers(){
        return this.members;
    }

    void setMembers(String[] members){
        this.members = members;
    }
}
