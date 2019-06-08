package com.zsw.pojo.user;

import com.zsw.common.util.Uuid;
import com.zsw.pojo.role.Role;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class User {

    private String id;
    private String name;
    private String password;
    private String phone;
    private int age;
    private String address;
    private String email;
    private Date createTime;
    private byte[] photo;
    /**
     * 比如用户的roleid 是0，那么他拥有所有的123456的权限，以此类推，数字小的拥有数字大的权限。*/
//    private Integer roleId;
    /**
     * 用户拥有的角色列表 */
    private Set<Role> role;
    
    /**method */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        if (id == null) {
            id = createNewId();
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String createNewId() {
        this.id = Uuid.getUUID32();
        return this.id;
    }
  
    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        User user = (User) o;
        return age == user.age &&
                id.equals(user.id) &&
                name.equals(user.name) &&
                password.equals(user.password) &&
                Objects.equals(phone, user.phone) &&
                Objects.equals(address, user.address) &&
                email.equals(user.email) &&
                Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, password, phone, age, address, email, role);
    }

    public User(String id, String name, String password, String phone, int age, String address, String email, Integer roleId, Set<Role> role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.age = age;
        this.address = address;
        this.email = email;
        this.role = role;
    }

    public User() {
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }


}