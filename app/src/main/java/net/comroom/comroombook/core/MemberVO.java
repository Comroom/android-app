package net.comroom.comroombook.core;

/**
 * Created by InhoChoi on 2016. 4. 4..
 */
public class MemberVO {
    private String name;
    private String email;
    private String id;

    MemberVO(String name,String email,String id){
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
