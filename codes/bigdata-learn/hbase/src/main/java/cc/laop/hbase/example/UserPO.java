package cc.laop.hbase.example;

/**
 * @Auther: Administrator
 * @Date: create in 2021/1/4 11:32
 * @Description:
 */
public class UserPO {

    private String username;

    private String roles;

    public String getRoles() {
        return roles;
    }

    public UserPO setRoles(String roles) {
        this.roles = roles;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserPO setUsername(String username) {
        this.username = username;
        return this;
    }
}
